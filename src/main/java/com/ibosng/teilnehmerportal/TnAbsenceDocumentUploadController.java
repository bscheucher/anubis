package com.ibosng.teilnehmerportal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tn-document")
@Tag(name = "TN Absence Document Upload controller")
public class TnAbsenceDocumentUploadController {

    @Value("${tnDocumentNatifEndpoint}")
    private String natifEndpoint;

    @Value("${tnDocumentNatifApiKey}")
    private String natifApiKey;

    @Operation(
            summary = "Upload absence document",
            description = "Upload an absence verification document and extract data as raw JSON",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Extraction successful"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
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

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        try {
            // Returns the raw JSON string from the external API directly to the client
            String rawJsonResponse = WebClient.builder()
                    .baseUrl(natifEndpoint)
                    .defaultHeader("Authorization", natifApiKey)
                    .build()
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("include", "extractions")
                            .queryParam("wait_for", 120)
                            .build())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.ok(rawJsonResponse);

        } catch (WebClientResponseException e) {
            log.error("Natif API error: Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Error sending document to Natif: ", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Error processing document"));
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