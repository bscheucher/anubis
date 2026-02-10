package com.ibosng.microsoftgraphservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibosng.microsoftgraphservice.config.properties.OneDriveProperties;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.OneDriveDocumentService;
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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Profile("!(localdev | test)")
@Service
@Slf4j
public class OneDriveDocumentServiceImpl implements OneDriveDocumentService {

    private final GraphServiceClient<Request> oneDriveGraphClient;
    private final OneDriveProperties oneDriveProperties;

    public OneDriveDocumentServiceImpl(@Qualifier("oneDriveGraphClient") GraphServiceClient<Request> oneDriveGraphClient,
                                       OneDriveProperties oneDriveProperties) {
        this.oneDriveGraphClient = oneDriveGraphClient;
        this.oneDriveProperties = oneDriveProperties;
    }

    @Override
    public File downloadFile(String fileId, String filename) throws MSGraphServiceException {
        InputStream inputStream = oneDriveGraphClient
                .sites(getSiteIdByUrl(oneDriveProperties.getSiteUrl()))
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
        return oneDriveGraphClient.sites(
                        getSiteIdByUrl(oneDriveProperties.getSiteUrl()))
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
        // Upload to the root of the OneDrive
        return oneDriveGraphClient
                .sites(getSiteIdByUrl(oneDriveProperties.getSiteUrl()))
                .drive()
                .root()
                .itemWithPath(folderPath + "/" + fileName)
                .content()
                .buildRequest()
                .put(byteArray);
    }

    @Override
    public DriveItem moveFile(String fileId, String filename, String targetFolder) {
        String targetFolderId = getFolderIdByName(targetFolder);

        // Create a ParentReference object with the target folder ID
        ItemReference parentRef = new ItemReference();
        parentRef.id = targetFolderId;

        // Create a DriveItem object with the updated parent reference
        DriveItem updatedItem = new DriveItem();
        updatedItem.parentReference = parentRef;
        updatedItem.name = filename;

        // Send the PATCH request to update the item
        DriveItem movedItem = oneDriveGraphClient
                .sites(getSiteIdByUrl(oneDriveProperties.getSiteUrl()))
                .drive()
                .items(fileId)
                .buildRequest()
                .patch(updatedItem);
        log.info("File {} was successfully moved to folder {}", filename, targetFolder);
        return movedItem;
    }

    @Override
    public DriveItem moveFile(FileDetails fileDetails, String targetFolder) {
        String targetFolderId = getFolderIdByName(targetFolder);

        // Create a ParentReference object with the target folder ID
        ItemReference parentRef = new ItemReference();
        parentRef.id = targetFolderId;

        // Create a DriveItem object with the updated parent reference
        DriveItem updatedItem = new DriveItem();
        updatedItem.parentReference = parentRef;
        updatedItem.name = fileDetails.getFilename();

        // Send the PATCH request to update the item
        DriveItem movedItem = oneDriveGraphClient
                .sites(getSiteIdByUrl(oneDriveProperties.getSiteUrl()))
                .drive()
                .items(fileDetails.getOneDriveId())
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
    public String getFolderIdByName(String folderPath) {
        String[] pathComponents = folderPath.split("/");

        // Start the search from the root, with the first component
        return getFolderIdRecursive("root", pathComponents, 0);
    }

    @Override
    public DriveItemCollectionPage getContentsOfFolder(Optional<String> folderPath) {
        DriveItemCollectionRequestBuilder childrenBuilder;
        childrenBuilder = folderPath.map(s -> oneDriveGraphClient.sites(getSiteIdByUrl(oneDriveProperties.getSiteUrl())).drive().root().itemWithPath(s).children()).orElseGet(() -> oneDriveGraphClient.sites(getSiteIdByUrl(oneDriveProperties.getSiteUrl())).drive().root().children());
        return childrenBuilder
                .buildRequest()
                .get();
    }

    private String getFolderIdRecursive(String currentFolderId, String[] pathComponents, int index) {
        DriveItemCollectionPage items = oneDriveGraphClient.sites(getSiteIdByUrl(oneDriveProperties.getSiteUrl())).drive().items(currentFolderId).children()
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

        Site site = oneDriveGraphClient
                .sites(hostName + ":" + sitePath)
                .buildRequest()
                .get();
        return site.id;
    }

}
