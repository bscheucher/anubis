package com.ibosng.teilnehmerportal.controller;

import com.ibosng.teilnehmerportal.dto.TeilnehmerAbwesenheitsbestaetigungDto;
import com.ibosng.teilnehmerportal.exception.NatifApiException;
import com.ibosng.teilnehmerportal.mapper.TeilnehmerAbwesenheitsbestaetigungMapper;
import com.ibosng.teilnehmerportal.repository.TeilnehmerAbwesenheitsbestaetigungRepository;
import com.ibosng.teilnehmerportal.service.TeilnehmerAbwesenheitsbestaetigungService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/tn-document")
@RequiredArgsConstructor
@Tag(name = "TN Absence Document Upload controller")
@ConditionalOnProperty(
        name = "features.tn-document-upload.enabled",
        havingValue = "true"
)
public class TeilnehmerAbwesenheitsbestaetigungController {

    private final TeilnehmerAbwesenheitsbestaetigungService teilnehmerAbwesenheitsBestaetigungService;
    private final TeilnehmerAbwesenheitsbestaetigungRepository teilnehmerAbwesenheitsBestaetigungRepository;
    private final TeilnehmerAbwesenheitsbestaetigungMapper teilnehmerAbwesenheitsBestaetigungMapper;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException() {
        log.warn("File upload rejected: size limit exceeded");
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of(
                        "error", "File size exceeds " + maxFileSize + " limit",
                        "type", "VALIDATION_ERROR"
                ));
    }

    @PostMapping(value = "/upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> uploadFile(
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam String identifier
    ) {

        SseEmitter emitter = new SseEmitter(180_000L);

        if (file.isEmpty()) {
            sendErrorAndComplete(emitter, "File must not be empty", "VALIDATION_ERROR", identifier);
            return ResponseEntity.badRequest().body(emitter);
        }

        String contentType = file.getContentType();
        boolean isImage = contentType != null && contentType.startsWith("image/");
        boolean isPdf = "application/pdf".equals(contentType);

        if (!isImage && !isPdf) {
            sendErrorAndComplete(emitter, "Invalid file type. Only images and PDF files are allowed.",
                    "VALIDATION_ERROR", identifier);
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(emitter);
        }

        final byte[] fileBytes;
        final String originalFilename = file.getOriginalFilename();
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            log.error("Failed to read file bytes for identifier {}", identifier, e);
            sendErrorAndComplete(emitter, "Failed to read file", "INTERNAL_ERROR", identifier);
            return ResponseEntity.internalServerError().body(emitter);
        }

        log.info("Processing upload for identifier: {} via SSE", identifier);

        executorService.execute(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("status")
                        .data(Map.of(
                                "message", "Uploading to Natif API...",
                                "identifier", identifier
                        )));

                teilnehmerAbwesenheitsBestaetigungService.uploadDocument(fileBytes, originalFilename, emitter);

            } catch (NatifApiException e) {
                log.error("Natif API error for identifier {}: {}", identifier, e.getMessage());
                sendErrorAndComplete(emitter, e.getMessage(), "API_ERROR", identifier,
                        Map.of("statusCode", e.getStatusCode()));

            } catch (IOException e) {
                log.error("IO error during upload for identifier {}: {}", identifier, e.getMessage());
                emitter.completeWithError(e);

            } catch (Exception e) {
                log.error("Unexpected error during upload for identifier {}: ", identifier, e);
                sendErrorAndComplete(emitter, "An unexpected error occurred", "INTERNAL_ERROR", identifier);
            }
        });

        emitter.onTimeout(() -> {
            log.warn("SSE connection timeout for identifier: {}", identifier);
            emitter.complete();
        });

        emitter.onError((Throwable ex) ->
                log.error("SSE connection error for identifier: {}", identifier, ex)
        );

        return ResponseEntity.ok(emitter);
    }

    @PostMapping(value = "/confirm")
    public ResponseEntity<Map<String, String>> confirmData(
            @RequestBody Map<String, String> data
    ) {
        log.info("Data confirmed: {}", data);
        return ResponseEntity.ok(data);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<TeilnehmerAbwesenheitsbestaetigungDto> saveData(
            @Valid @RequestBody TeilnehmerAbwesenheitsbestaetigungDto dto
    ) {
        log.info("Data saved: {}", dto);
        var saved = teilnehmerAbwesenheitsBestaetigungRepository.save(teilnehmerAbwesenheitsBestaetigungMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(teilnehmerAbwesenheitsBestaetigungMapper.toDto(saved));
    }

    private void sendErrorAndComplete(SseEmitter emitter, String message, String type,
                                      String identifier, Map<String, Object> extras) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("error", message);
            data.put("type", type);
            data.put("identifier", identifier);
            if (extras != null) data.putAll(extras);

            emitter.send(SseEmitter.event().name("error").data(data));
            emitter.complete();
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }

    private void sendErrorAndComplete(SseEmitter emitter, String message, String type, String identifier) {
        sendErrorAndComplete(emitter, message, type, identifier, null);
    }
}