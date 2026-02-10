package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.enums.FileUploadTypes;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DownloadService {
    ResponseEntity<byte[]> downloadFileWithResponse(String identifier, String additionalIdentifier, FileUploadTypes type);

    ResponseEntity<byte[]> downloadFileWithFilenameAndDirectory(String fileName, String directoryPath);

    ResponseEntity<byte[]> downloadFileWithID(String fileID);

    ResponseEntity<byte[]> downloadTeilnehmersCsv(List<Integer> ids);
}
