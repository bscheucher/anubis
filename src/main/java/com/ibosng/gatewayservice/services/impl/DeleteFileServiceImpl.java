package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import com.ibosng.gatewayservice.services.DeleteFileService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Mappers.getSubdirectoryForDocument;
import static com.ibosng.gatewayservice.utils.Helpers.getFileName;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteFileServiceImpl implements DeleteFileService {
    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    private final FileShareService fileShareService;
    private final PersonalnummerService personalnummerService;

    @Override
    public void deleteFile(String identifier, String additionalIdentifier, FileUploadTypes type) {
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(identifier);

        String firma = personalnummerEntity.getFirma().getName();
        String subdirectory = getSubdirectoryForDocument(type.getValue());
        String directoryPath = String.format("%s/%s/%s", firma, identifier, subdirectory);
        String fileNamePrefix = getFileName(identifier, additionalIdentifier, type, false);

        log.info("Attempting to delete file(s) with prefix '{}' in directory '{}'.", fileNamePrefix, directoryPath);

        try {
            if (!fileShareService.fileExists(getFileShareTemp(), directoryPath)) {
                log.warn("Directory '{}' does not exist. No files deleted.", directoryPath);
                return;
            }

            fileShareService.deleteFromFileShare(getFileShareTemp(), identifier, directoryPath, fileNamePrefix);
            log.info("File(s) with prefix '{}' deleted successfully from directory '{}'.", fileNamePrefix, directoryPath);

        } catch (Exception e) {
            log.error("Error deleting file(s) with prefix '{}' in directory '{}': {}", fileNamePrefix, directoryPath, e.getMessage());
        }
    }
}