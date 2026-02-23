package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.dtos.masterdata.FileUpload;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    PayloadResponse uploadFile(MultipartFile file, String type, String identifier, String additionalIdentifier);

    void updateEntity(FileUpload fileUpload);
}
