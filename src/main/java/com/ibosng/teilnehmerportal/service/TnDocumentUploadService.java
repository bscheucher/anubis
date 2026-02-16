package com.ibosng.teilnehmerportal.service;

import com.ibosng.teilnehmerportal.exception.DocumentValidationException;
import com.ibosng.teilnehmerportal.exception.NatifApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class TnDocumentUploadService {

    private final WebClient webClient;

    public TnDocumentUploadService(
            @Value("${tnDocumentNatifEndpoint}") String natifEndpoint,
            @Value("${tnDocumentNatifApiKey}") String natifApiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(natifEndpoint)
                .defaultHeader("Authorization", natifApiKey)
                .build();
    }

    public void uploadDocument(MultipartFile file, SseEmitter emitter)
            throws IOException, NatifApiException, DocumentValidationException {

        log.info("Uploading document to Natif API: {}", file.getOriginalFilename());
        validateFile(file);

        // Read file into memory BEFORE reactive chain
        byte[] fileBytes = file.getBytes();
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // Create a ByteArrayResource instead of using file.getResource()
        body.add("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        });

        try {
            emitter.send(SseEmitter.event()
                    .name("progress")
                    .data(Map.of("message", "Processing with Natif API...")));

            webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("include", "extractions")
                            .queryParam("wait_for", 120)
                            .build())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(response -> {
                        try {
                            log.info("Document processed successfully");
                            emitter.send(SseEmitter.event()
                                    .name("progress")
                                    .data(Map.of("message", "Document processed successfully")));
                            emitter.send(SseEmitter.event()
                                    .name("success")
                                    .data(Map.of("message", "Upload completed", "result", response)));
                            emitter.complete();
                        } catch (IOException e) {
                            log.error("Error sending SSE events", e);
                            emitter.completeWithError(e);
                        }
                    })
                    // ... rest of error handlers
                    .subscribe();

        } catch (IOException e) {
            log.error("Error sending initial progress event", e);
            throw e;
        }
    }

    private void validateFile(MultipartFile file) throws DocumentValidationException {
        if (file.getSize() > 10_000_000) {
            throw new DocumentValidationException("File size exceeds 10MB limit");
        }

        String contentType = file.getContentType();
        boolean isImage = contentType != null && contentType.startsWith("image/");
        boolean isPdf = "application/pdf".equals(contentType);

        if (!isImage && !isPdf) {
            throw new DocumentValidationException(
                    "Invalid file type. Only images and PDF files are allowed."
            );
        }
    }
}