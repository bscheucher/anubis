package com.ibosng.microsoftgraphservice.services.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.ibosng.microsoftgraphservice.services.BlobStorageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
@RequiredArgsConstructor
public class BlobStorageServiceImpl implements BlobStorageService {
    @Value("${storageContainerJasperReports:#{null}}")
    private String jasperReportsContainer;

    private final BlobClientBuilder blobClientBuilder;
    private final BlobContainerClientBuilder blobContainerClientBuilder;


    @Override
    public void uploadOrReplaceJasperReport(String blobName, InputStream data, long length) throws IOException {
        BlobClient blobClient = blobClientBuilder
                .containerName(getJasperReportsContainer())
                .blobName(blobName)
                .buildClient();

        blobClient.deleteIfExists();
        blobClient.upload(data, length);
    }

    @Override
    public void uploadOrReplaceFile(String container, String blobName, InputStream data, long length) throws IOException {
        BlobClient blobClient = blobClientBuilder
                .containerName(container)
                .blobName(blobName)
                .buildClient();

        blobClient.deleteIfExists();
        // Delete existing blob regardless of extension
        BlobItem foundBlob = getMatchingBlob(blobName, container);
        if (foundBlob != null) {
            blobClientBuilder.blobName(foundBlob.getName());
            BlobClient blobClientDelete = blobClientBuilder.buildClient();
            blobClientDelete.deleteIfExists();
        }
        blobClient.upload(data, length);
    }

    @Override
    public InputStream downloadBlob(String blobName, String containerName) {
        BlobClient blobClient = blobClientBuilder
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();

        return blobClient.openInputStream();
    }

    @Override
    public BlobItem getMatchingBlob(String identifier, String containerName) {
        // Use the injected blobContainerClientBuilder to set the container name dynamically
        BlobContainerClient blobContainerClient = blobContainerClientBuilder
                .containerName(containerName)
                .buildClient();

        // List blobs and find the matching one, including the extension
        PagedIterable<BlobItem> blobList = blobContainerClient.listBlobs();
        String trimmedIdentifier = FilenameUtils.removeExtension(identifier);
        List<BlobItem> matchingBlob = blobList.stream()
                .filter(blobItem -> blobItem.getName().startsWith(trimmedIdentifier))
                .toList();

        if (matchingBlob.isEmpty()) {
            log.warn("No Matching Blob found for identifier: " + identifier);
            return null;
        }
        return matchingBlob.get(0);
    }

    @Override
    public void deleteFile(String identifier, String containerName) {
        BlobItem fileToBeDeleted = getMatchingBlob(identifier, containerName);
        if (fileToBeDeleted != null) {
            BlobClient blobClient = blobClientBuilder
                    .containerName(containerName)
                    .blobName(fileToBeDeleted.getName())
                    .buildClient();

            blobClient.deleteIfExists();
        } else {
            log.warn("No matching file found for identifier: {}", identifier);
        }
    }
}
