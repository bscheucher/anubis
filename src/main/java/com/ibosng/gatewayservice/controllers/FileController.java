package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.DeleteFileService;
import com.ibosng.gatewayservice.services.DownloadService;
import com.ibosng.gatewayservice.services.FolderStructureService;
import com.ibosng.gatewayservice.services.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.FN_MA_UEBERPRUEFEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_TN_UPLOAD_PROFIL;
import static com.ibosng.gatewayservice.utils.Constants.FN_UNTERLAGEN_LESEN;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/file")
@Tag(name = "Upload controller")
@RequiredArgsConstructor
public class FileController {

    private final BenutzerDetailsService benutzerDetailsService;

    private final UploadService uploadService;
    private final DownloadService downloadService;
    private final DeleteFileService deleteFileService;
    private final FolderStructureService folderStructureService;

    @PostMapping(value = "/upload")
    @Operation(
            summary = "Upload a file.",
            description = "This endpoint allows users to upload a file with specified type and identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PayloadResponse> uploadFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestParam String type,
            @RequestParam String identifier,
            @RequestParam(required = false) String additionalIdentifier) {
        if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(identifier, authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(uploadService.uploadFile(file, type, identifier, additionalIdentifier));
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @RequestParam String type,
                                               @RequestParam String identifier,
                                               @RequestParam(required = false) String additionalIdentifier) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_UEBERPRUEFEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return downloadService.downloadFileWithResponse(identifier, additionalIdentifier, FileUploadTypes.fromValue(type));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<byte[]> deleteFile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                             @RequestParam String type,
                                             @RequestParam String identifier,
                                             @RequestParam(required = false) String additionalIdentifier) {
        if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(identifier, authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        deleteFileService.deleteFile(identifier, additionalIdentifier, FileUploadTypes.fromValue(type));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/folder-structure")
    public ResponseEntity<PayloadResponse> getFolderStructure(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                              @RequestParam String identifier) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_UNTERLAGEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(folderStructureService.fetchFolderStructure(identifier));
    }

    @GetMapping("/own-folder-structure")
    public ResponseEntity<PayloadResponse> getFolderStructure(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_UNTERLAGEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(folderStructureService.fetchOwnFolderStructure(token));
    }

    @GetMapping("/download-filename")
    public ResponseEntity<byte[]> downloadFile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @RequestParam String filename,
                                               @RequestParam String directoryPath) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_UNTERLAGEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return downloadService.downloadFileWithFilenameAndDirectory(filename, directoryPath);
    }

    @GetMapping("/download-id")
    public ResponseEntity<byte[]> downloadFile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @RequestParam String fileID) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_UNTERLAGEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return downloadService.downloadFileWithID(fileID);
    }

    @PostMapping("/downloadTeilnehmers")
    public ResponseEntity<byte[]> downloadTeilnehmer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                     @RequestBody List<Integer> ids) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Arrays.asList(FN_TN_UPLOAD_PROFIL))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return downloadService.downloadTeilnehmersCsv(ids);
    }
}
