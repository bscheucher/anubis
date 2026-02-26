package com.ibosng.teilnehmerportal.controller;

import com.ibosng.teilnehmerportal.dto.AbwesenheitEntryDto;
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
import java.util.List;
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

    @GetMapping(value = "/abwesenheiten")
    public ResponseEntity<List<AbwesenheitEntryDto>> getAbwesenheiten(
            @RequestParam String azureId
    ) {
        log.info("Fetching abwesenheiten for azureId: {}", azureId);
        var dummyData = List.of(
                new AbwesenheitEntryDto(1, "Anna", "Müller", "1234010185", "dummy-files/Steiner.pdf", "2025-01-06", "2025-01-10", "2025-01-05T08:23:11"),
                new AbwesenheitEntryDto(2, "Thomas", "Bauer", "2345150390", "dummy-files/Steiner.pdf", "2025-01-13", "2025-01-17", "2025-01-12T09:45:00"),
                new AbwesenheitEntryDto(3, "Maria", "Gruber", "3456221292", "dummy-files/Steiner.pdf", "2025-02-03", "2025-02-07", "2025-02-01T14:10:33"),
                new AbwesenheitEntryDto(4, "Stefan", "Huber", "4567080978", "dummy-files/Steiner.pdf", "2025-02-10", "2025-02-14", "2025-02-08T11:05:22"),
                new AbwesenheitEntryDto(5, "Laura", "Wagner", "5678301195", "dummy-files/Steiner.pdf", "2025-03-03", "2025-03-05", "2025-03-01T07:58:44"),
                new AbwesenheitEntryDto(6, "Markus", "Schneider", "6789190287", "dummy-files/Steiner.pdf", "2025-03-17", "2025-03-21", "2025-03-14T16:30:01"),
                new AbwesenheitEntryDto(7, "Julia", "Fischer", "7890251188", "dummy-files/Steiner.pdf", "2025-04-07", "2025-04-11", "2025-04-04T10:12:55"),
                new AbwesenheitEntryDto(8, "Klaus", "Weber", "8901140593", "dummy-files/Steiner.pdf", "2025-04-22", "2025-04-24", "2025-04-20T13:47:19"),
                new AbwesenheitEntryDto(9, "Sandra", "Pichler", "9012070891", "dummy-files/Steiner.pdf", "2025-05-05", "2025-05-09", "2025-05-02T08:00:00"),
                new AbwesenheitEntryDto(10, "Michael", "Hofer", "0123301286", "dummy-files/Steiner.pdf", "2025-05-19", "2025-05-23", "2025-05-16T15:22:38"),
                new AbwesenheitEntryDto(11, "Elisabeth", "Maier", "1234180994", "dummy-files/Steiner.pdf", "2025-06-02", "2025-06-06", "2025-05-30T09:33:50"),
                new AbwesenheitEntryDto(12, "David", "Reiter", "2345260190", "dummy-files/Steiner.pdf", "2025-06-16", "2025-06-20", "2025-06-13T11:11:11"),
                new AbwesenheitEntryDto(13, "Katharina", "Schwarz", "3456090389", "dummy-files/Steiner.pdf", "2025-07-07", "2025-07-18", "2025-07-04T07:00:00"),
                new AbwesenheitEntryDto(14, "Andreas", "Brandstätter", "4567221191", "dummy-files/Steiner.pdf", "2025-08-04", "2025-08-08", "2025-08-01T12:44:05"),
                new AbwesenheitEntryDto(15, "Sabine", "Leitner", "5678150688", "dummy-files/Steiner.pdf", "2025-09-01", "2025-09-05", "2025-08-29T10:05:30")
        );
        return ResponseEntity.ok(dummyData);
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