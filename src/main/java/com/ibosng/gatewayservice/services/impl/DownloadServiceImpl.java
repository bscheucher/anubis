package com.ibosng.gatewayservice.services.impl;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.ibosng.dbmapperservice.services.TeilnehmerMapperService;
import com.ibosng.dbservice.dtos.TeilnehmerCsvDto;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.utils.Mappers;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import com.ibosng.gatewayservice.services.DownloadService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.gatewayservice.utils.Helpers.getFileName;
import static com.ibosng.gatewayservice.utils.Helpers.getMimeType;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {
    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    private final FileShareService fileShareService;
    private final PersonalnummerService personalnummerService;
    private final TeilnehmerService teilnehmerService;
    private final TeilnehmerMapperService teilnehmerMapperService;

    @Override
    public ResponseEntity<byte[]> downloadFileWithResponse(String identifier, String additionalIdentifier, FileUploadTypes type) {
        String fileNamePart = getFileName(identifier, additionalIdentifier, type, false);
        String firma = personalnummerService.findByPersonalnummer(identifier).getFirma().getName();
        String subdirectory = Mappers.getSubdirectoryForDocument(type.getValue());
        String tempDirectoryPath = firma + "/" + identifier + "/" + subdirectory;

        try {
            String fullFileName;
            String activeDirectoryPath;
            String activeStorage;

            // Check in temp storage first
            fullFileName = fileShareService.getFullFileName(getFileShareTemp(), firma, identifier, tempDirectoryPath, fileNamePart, fileNamePart);
            activeDirectoryPath = tempDirectoryPath;
            activeStorage = getFileShareTemp();

            // If not found in temp storage, check in personalunterlagen
            if (fullFileName == null) {
                log.warn("File with prefix '{}' not found in temp directory '{}'. Checking personalunterlagen storage.", fileNamePart, tempDirectoryPath);

                String personalunterlagenDirectoryPath = firma + "/" + identifier + "/" + subdirectory;
                fullFileName = fileShareService.getFullFileName(getFileSharePersonalunterlagen(), firma, identifier, personalunterlagenDirectoryPath, fileNamePart, fileNamePart);
                if (fullFileName != null) {
                    activeDirectoryPath = personalunterlagenDirectoryPath;
                    activeStorage = getFileSharePersonalunterlagen();
                }
            }

            try (InputStream inputStream = fileShareService.downloadFromFileShare(activeStorage, activeDirectoryPath, fullFileName)) {
                if (inputStream == null) {
                    log.error("File '{}' not found in directory '{}'.", fullFileName, activeDirectoryPath);
                    return ResponseEntity.noContent().build();
                }
                return returnResponse(inputStream, fullFileName);
            }
        } catch (IOException e) {
            log.error("Error while downloading file with prefix '{}': {}", fileNamePart, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFileWithFilenameAndDirectory(String fileName, String directoryPath) {
        String decodedDirectoryPath = URLDecoder.decode(directoryPath, StandardCharsets.UTF_8);
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        InputStream inputStream = fileShareService.downloadFromFileShare(getFileSharePersonalunterlagen(), decodedDirectoryPath, decodedFileName);
        if (inputStream == null) {
            log.error("File '{}' not found in directory '{}'.", decodedFileName, decodedDirectoryPath);
            return ResponseEntity.noContent().build();
        }
        return returnResponse(inputStream, decodedFileName);
    }

    @Override
    public ResponseEntity<byte[]> downloadFileWithID(String fileID) {
        try (InputStream inputStream = fileShareService.downloadFileById(getFileSharePersonalunterlagen(), fileID)) {
            if (inputStream == null) {
                log.error("File '{}' not found.", fileID);
                return ResponseEntity.noContent().build();
            }
            return returnResponse(inputStream, fileID);
        } catch (IOException e) {
            log.error("Error while downloading file with ID '{}': {}", fileID, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<byte[]> returnResponse(InputStream inputStream, String filename) {
        try {
            byte[] fileBytes = inputStream.readAllBytes();
            InputStream mimeTypeStream = new ByteArrayInputStream(fileBytes);
            String mimeType = getMimeType(mimeTypeStream, filename);

            // Prepare response headers
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            responseHeaders.set(HttpHeaders.CONTENT_TYPE, mimeType);

            // Return the response with file bytes and headers
            return ResponseEntity.ok().headers(responseHeaders).body(fileBytes);
        } catch (IOException e) {
            log.error("Error while downloading file with ID '{}': {}", filename, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadTeilnehmersCsv(List<Integer> ids) {
        final String timestamp = getLocalDateNow().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));

        final CsvMapper csvMapper = new CsvMapper().enable(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING);
        final CsvSchema schema = csvMapper.schemaFor(TeilnehmerCsvDto.class).withColumnSeparator(';')
                .withHeader();

        List<TeilnehmerCsvDto> teilnehmerCsvDtos = teilnehmerService.findTeilnehmersWithIds(ids)
                .stream().map(teilnehmerMapperService::mapToCsv).toList();

        if (teilnehmerCsvDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        File tempFile = new File(System.getProperty("java.io.tmpdir"), "request_%s.csv".formatted(timestamp));
        try (SequenceWriter writer = csvMapper.writerFor(TeilnehmerCsvDto.class).with(schema).writeValues(tempFile)) {
            for (TeilnehmerCsvDto teilnehmerCsvDto : teilnehmerCsvDtos) {
                writer.write(teilnehmerCsvDto);
            }
            InputStream inputStream = new DataInputStream(new FileInputStream(tempFile));
            return returnResponse(inputStream, "teilnehmer_%s.csv".formatted(timestamp));
        } catch (IOException e) {
            log.error("Error happens during exporting teilnehmer: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}