package com.ibosng.teilnehmerportal.controller;

import com.ibosng.teilnehmerportal.service.TnDocumentUploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tn-document")
@RequiredArgsConstructor
@Tag(name = "TN Absence Document Upload controller")
public class TnAbsenceDocumentUploadController {

    private final TnDocumentUploadService documentService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam String type,
            @RequestParam String identifier
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File must not be empty"));
        }

        log.info("Processing upload for identifier: {}", identifier);

        try {
            String rawJsonResponse = documentService.uploadDocument(file);
            return ResponseEntity.ok(rawJsonResponse);

        } catch (WebClientResponseException e) {
            // Directly use the WebClient exception
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error processing document"));
        }
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<Map<String, String>> confirmData(
            @RequestBody Map<String, String> data
    ) {
        log.info("Data confirmed: {}", data);
        return ResponseEntity.ok(data);
    }
}