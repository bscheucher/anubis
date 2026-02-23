package com.ibosng.natifservice.client;

import com.ibosng.natifservice.dtos.ProcessingDto;
import com.ibosng.natifservice.dtos.extractions.ExtractionsDto;
import com.ibosng.natifservice.services.NatifMapperService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.ibosng.natifservice.utils.Constants.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;


@Service
@Profile("!(localdev | test)")
@Slf4j
public class NatifClientImpl implements NatifClient {

    private final WebClient webClient;
    private final NatifMapperService natifMapperService;

    @Getter
    @Value("${natifProcessDefinitionKey:#{null}}")
    private String processDefinitionKey;

    public NatifClientImpl(@Qualifier("natifservicewebclient") WebClient webClient, NatifMapperService natifMapperService) {
        this.webClient = webClient;
        this.natifMapperService = natifMapperService;
    }


    // TODO side effects - except get extraction results, saves the results in the db
    public ResponseEntity<ExtractionsDto> getExtractionResults(MultipartFile file, Integer teilnehmerId) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(INCLUDE, EXTRACTIONS);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        if (file != null) {
            body.add(FILE, file.getResource());
        }

        log.info("Sending PDF to Natif...");
        ResponseEntity<ProcessingDto> processingDto = buildResponse(PROCESSING + processDefinitionKey, POST, body,
                null, queryParams, ProcessingDto.class, MULTIPART_FORM_DATA);


        if (processingDto.getStatusCode().is2xxSuccessful()) {
            log.info("Extraction from Natif successful; adding them to Database of teilnehmer with ID: {}", teilnehmerId);

            natifMapperService.saveKompetenz(Objects.requireNonNull(processingDto.getBody()).getExtractionsDto(),
                    teilnehmerId);

            log.info("Returning response with status {} to gateway-service", processingDto.getStatusCode());
            return ResponseEntity.status(processingDto.getStatusCode()).build();
        }

        log.error("getting extraction returned: {}", processingDto.getStatusCode());
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(processingDto.getStatusCode(),
                "error getting extraction results")).build();

    }

    public <T> ResponseEntity<T> buildResponse(String path, HttpMethod method, MultiValueMap<String, Object> body,
                                               Integer uriParameter, Map<String, String> queryParams, Class<T> classDto,
                                               MediaType mediaType) {
        try {
            return webClient
                    .method(method)
                    .uri(uriBuilder -> {
                        if (queryParams != null) {
                            queryParams.forEach(uriBuilder::queryParam);
                        }
                        if (uriParameter != null) {
                            return uriBuilder
                                    .path(path)
                                    .build(uriParameter);
                        } else {
                            return uriBuilder.path(path)
                                    .build();
                        }
                    })
                    .contentType(mediaType)
                    .bodyValue(body != null ? body : BodyInserters.empty())
                    .retrieve()
                    .toEntity(classDto)
                    .blockOptional()
                    .orElse(ResponseEntity.notFound().build());
        } catch (WebClientResponseException e) {
            log.error("Natif API Error: Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Natif Client Internal Error: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
