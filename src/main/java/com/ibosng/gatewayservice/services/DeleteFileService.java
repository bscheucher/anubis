package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.enums.FileUploadTypes;

public interface DeleteFileService {
    void deleteFile(String identifier, String additionalIdentifier, FileUploadTypes type);
}
