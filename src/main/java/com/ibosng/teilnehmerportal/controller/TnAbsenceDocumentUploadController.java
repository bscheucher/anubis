package com.ibosng.teilnehmerportal.controller;

import com.ibosng.teilnehmerportal.exception.DocumentValidationException;
import com.ibosng.teilnehmerportal.exception.NatifApiException;
import com.ibosng.teilnehmerportal.service.TnDocumentUploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/tn-document")
@RequiredArgsConstructor
@Tag(name = "TN Absence Document Upload controller")
public class TnAbsenceDocumentUploadController {

    private final TnDocumentUploadService documentService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @PostMapping(value = "/upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter uploadFile(
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam String type,
            @RequestParam String identifier
    ) {

        SseEmitter emitter = new SseEmitter(180_000L);

        if (file.isEmpty()) {
            try {
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data(Map.of("error", "File must not be empty", "type", "VALIDATION_ERROR")));
                emitter.complete();

            } catch (IOException e) {
                emitter.completeWithError(e);
            }
            return emitter;
        }

        log.info("Processing upload for identifier: {} via SSE", identifier);

        executorService.execute(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("status")
                        .data(Map.of("message", "Starting upload...", "identifier", identifier)));

                emitter.send(SseEmitter.event()
                        .name("progress")
                        .data(Map.of("message", "Uploading to Natif API...")));

                documentService.uploadDocument(file, emitter);

            } catch (DocumentValidationException e) {
                log.warn("Validation error for identifier {}: {}", identifier, e.getMessage());
                try {
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(Map.of(
                                    "error", e.getMessage(),
                                    "type", "VALIDATION_ERROR",
                                    "identifier", identifier
                            )));
                    emitter.complete();

                } catch (IOException ioException) {
                    emitter.completeWithError(ioException);
                }

            } catch (NatifApiException e) {
                log.error("Natif API error for identifier {}: {}", identifier, e.getMessage());
                try {
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(Map.of(
                                    "error", e.getMessage(),
                                    "type", "API_ERROR",
                                    "statusCode", e.getStatusCode(),
                                    "identifier", identifier
                            )));
                    emitter.complete();

                } catch (IOException ioException) {
                    emitter.completeWithError(ioException);
                }

            } catch (IOException e) {
                log.error("IO error during upload for identifier {}: {}", identifier, e.getMessage());
                emitter.completeWithError(e);

            } catch (Exception e) {
                log.error("Unexpected error during upload for identifier {}: ", identifier, e);
                try {
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(Map.of(
                                    "error", "An unexpected error occurred",
                                    "type", "INTERNAL_ERROR",
                                    "identifier", identifier
                            )));
                    emitter.complete();
                } catch (IOException ioException) {
                    emitter.completeWithError(ioException);
                }
            }
        });

        emitter.onTimeout(() -> {
            log.warn("SSE connection timeout for identifier: {}", identifier);
            emitter.complete();
        });

        emitter.onError((Throwable ex) ->
                log.error("SSE connection error for identifier: {}", identifier, ex)
        );

        return emitter;
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<Map<String, String>> confirmData(
            @RequestBody Map<String, String> data
    ) {
        log.info("Data confirmed: {}", data);
        return ResponseEntity.ok(data);
    }
}