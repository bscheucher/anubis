package com.ibosng.microsoftgraphservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointCondition;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.ItemReference;
import com.microsoft.graph.models.Site;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import com.microsoft.graph.requests.DriveItemCollectionRequestBuilder;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Conditional(SharePointCondition.class)
@Service
@Slf4j
public class SharePointServiceImpl implements SharePointService {

    public static final String ROOT = "root";
    private final GraphServiceClient<Request> sharePointGraphClient;
    private final SharePointProperties sharePointProperties;

    public SharePointServiceImpl(@Qualifier("sharePointGraphClient") GraphServiceClient<Request> sharePointGraphClient,
                                 SharePointProperties sharePointProperties) {
        this.sharePointGraphClient = sharePointGraphClient;
        this.sharePointProperties = sharePointProperties;
    }

    @Override
    public File downloadFile(String fileId, String filename) throws MSGraphServiceException {
        InputStream inputStream = sharePointGraphClient
                .sites(getSiteIdByUrl(sharePointProperties.getSiteUrl()))
                .drive()
                .items(fileId)
                .content()
                .buildRequest()
                .get();

        File file = new File(filename);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            if (inputStream != null) {
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
            } else {
                return null;
            }
        } catch (NullPointerException | IOException e) {
            throw new MSGraphServiceException("Error while downloading file from one-drive");
        }
        return file;
    }

    @Override
    public DriveItemCollectionPage getUploadedFiles(String folder) {
        return sharePointGraphClient.sites(
                        getSiteIdByUrl(sharePointProperties.getSiteUrl()))
                .drive()
                .root()
                .itemWithPath(folder)
                .children().buildRequest()
                .get();
    }

    @Override
    public DriveItem uploadFile(String filePath, String fileName, String folderPath) throws MSGraphServiceException {
        byte[] byteArray;
        try {
            byteArray = FileUtils.readFileToByteArray(new File(filePath));
        } catch (IOException e) {
            throw new MSGraphServiceException("File could not be converted to byte array");
        }

        return sharePointGraphClient
                .sites(getSiteIdByUrl(sharePointProperties.getSiteUrl()))
                .drive()
                .root()
                .itemWithPath(folderPath + "/" + fileName)
                .content()
                .buildRequest()
                .put(byteArray);
    }

    @Override
    public DriveItem moveFile(String fileId, String filename, String targetFolder) {
        String targetFolderId = getFolderIdByPath(targetFolder);

        // Create a ParentReference object with the target folder ID
        ItemReference parentRef = new ItemReference();
        parentRef.id = targetFolderId;

        // Create a DriveItem object with the updated parent reference
        DriveItem updatedItem = new DriveItem();
        updatedItem.parentReference = parentRef;
        updatedItem.name = filename;

        // Send the PATCH request to update the item
        DriveItem movedItem = sharePointGraphClient
                .sites(getSiteIdByUrl(sharePointProperties.getSiteUrl()))
                .drive()
                .items(fileId)
                .buildRequest()
                .patch(updatedItem);
        log.info("File {} was successfully moved to folder {}", filename, targetFolder);
        return movedItem;
    }

    @Override
    public DriveItem moveFile(FileDetails fileDetails, String targetFolder) {
        String targetFolderId = getFolderIdByPath(targetFolder);

        // Create a ParentReference object with the target folder ID
        ItemReference parentRef = new ItemReference();
        parentRef.id = targetFolderId;

        // Create a DriveItem object with the updated parent reference
        DriveItem updatedItem = new DriveItem();
        updatedItem.parentReference = parentRef;
        updatedItem.name = fileDetails.getFilename();

        // Send the PATCH request to update the item
        DriveItem movedItem = sharePointGraphClient
                .sites(getSiteIdByUrl(sharePointProperties.getSiteUrl()))
                .drive()
                .items(fileDetails.getSharePointId())
                .buildRequest()
                .patch(updatedItem);
        log.info("File {} was successfully moved to folder {}", fileDetails.getFilename(), targetFolder);
        return movedItem;
    }

    @Override
    public File createJsonFileFromDto(Object dto, String filename) throws MSGraphServiceException {
        File tempFile;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            tempFile = File.createTempFile(filename, ".json");
            try (FileWriter fileWriter = new FileWriter(tempFile)) {
                objectMapper.writeValue(fileWriter, dto);
            }
            return tempFile;
        } catch (Exception e) {
            throw new MSGraphServiceException("Unexpected error during JSON file creation", e);
        }
    }

    @Override
    public DriveItem createFolder(String folderPath, String folderName) {
        DriveItem folder = new DriveItem();
        folder.name = folderName;
        folder.folder = new com.microsoft.graph.models.Folder();

        return sharePointGraphClient
                .sites(getSiteIdByUrl(sharePointProperties.getSiteUrl()))
                .drive()
                .root()
                .itemWithPath(folderPath)
                .children()
                .buildRequest()
                .post(folder);
    }

    @Override
    public String getFolderIdByPath(String folderPath) {
        String[] pathComponents = folderPath.split("/");
        return getFolderIdRecursive(ROOT, pathComponents, 0);
    }

    @Override
    public DriveItemCollectionPage getContentsOfFolder(Optional<String> folderPath) {
        DriveItemCollectionRequestBuilder childrenBuilder;
        String siteIdByUrl = getSiteIdByUrl(sharePointProperties.getSiteUrl());
        childrenBuilder = folderPath.map(s -> sharePointGraphClient.sites(siteIdByUrl)
                        .drive()
                        .root()
                        .itemWithPath(s)
                        .children())
                .orElseGet(() -> sharePointGraphClient.sites(siteIdByUrl).drive().root().children());
        return childrenBuilder
                .buildRequest()
                .get();
    }

    @Override
    public DriveItem getItemByPath(String fullPath) {
        return sharePointGraphClient.sites(getSiteIdByUrl(sharePointProperties.getSiteUrl()))
                .drive()
                .root()
                .itemWithPath(fullPath)
                .buildRequest()
                .get();
    }

    private String getFolderIdRecursive(String currentFolderId, String[] pathComponents, int index) {
        DriveItemCollectionPage items = sharePointGraphClient.sites(getSiteIdByUrl(sharePointProperties.getSiteUrl())).drive().items(currentFolderId).children()
                .buildRequest()
                .get();

        if (items != null) {
            for (DriveItem item : items.getCurrentPage()) {
                if (item.folder != null && pathComponents[index].equals(item.name)) {
                    if (index == pathComponents.length - 1) {
                        return item.id; // Folder found
                    } else {
                        return getFolderIdRecursive(item.id, pathComponents, index + 1); // Recursive call for next level
                    }
                }
            }
        }
        return null;
    }

    private String getSiteIdByUrl(String siteUrl) {
        String[] urlParts = siteUrl.replace("https://", "").split("/", 2);
        String hostName = urlParts[0];
        String sitePath = "/" + urlParts[1];

        Site site = sharePointGraphClient
                .sites(hostName + ":" + sitePath)
                .buildRequest()
                .get();
        return site.id;
    }

}
