package com.ibosng.microsoftgraphservice.services;

import com.azure.storage.blob.models.BlobItem;

import java.io.IOException;
import java.io.InputStream;

public interface BlobStorageService {
    void uploadOrReplaceJasperReport(String blobName, InputStream data, long length) throws IOException;
    void uploadOrReplaceFile(String container, String blobName, InputStream data, long length) throws IOException;
    InputStream downloadBlob(String blobName, String containerName);
    BlobItem getMatchingBlob(String identifier, String containerName);
    void deleteFile(String identifier, String containerName);
}
