package com.ibosng.teilnehmerportal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class TeilnehmerAbwesenheitsbestaetigungService {

    private final WebClient webClient;

    public TeilnehmerAbwesenheitsbestaetigungService(
            @Value("${natifBaseEndpoint}") String natifEndpoint,
            @Value("${natifApiKey}") String natifApiKey,
            @Value("${natifProcessDefinitionKey}") String natifProcessDefinitionKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(natifEndpoint + natifProcessDefinitionKey)
                .defaultHeader("Authorization", natifApiKey)
                .build();
    }

    public void uploadDocument(byte[] fileBytes, String filename, SseEmitter emitter)
            throws IOException {

        log.info("Uploading document to Natif API: {}", filename);


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
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
                    .subscribe();

        } catch (IOException e) {
            log.error("Error sending initial progress event", e);
            throw e;
        }
    }


}