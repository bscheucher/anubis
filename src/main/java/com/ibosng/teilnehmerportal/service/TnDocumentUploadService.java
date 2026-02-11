package com.ibosng.teilnehmerportal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
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

    public String uploadDocument(MultipartFile file) {
        log.info("Uploading document to Natif API: {}", file.getOriginalFilename());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("include", "extractions")
                            .queryParam("wait_for", 120)
                            .build())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException e) {
            log.error("Natif API error: Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e; // Just re-throw it
        } catch (Exception e) {
            log.error("Error sending document to Natif: ", e);
            throw new RuntimeException("Error processing document", e);
        }
    }
}