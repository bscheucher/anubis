package com.ibosng.moxisservice.clients;

import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisReducedJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisUserDto;
import com.ibosng.moxisservice.exceptions.MoxisWebClientException;
import com.ibosng.moxisservice.services.MoxisEnvironmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.moxisservice.utils.Helpers.createFile;
import static com.ibosng.moxisservice.utils.Helpers.extractFilename;

@Slf4j
@Service
@Conditional(MoxisCondition.class)
public class MoxisClientImpl implements MoxisClient {

    private final WebClient webClient;
    private final MoxisEnvironmentService environmentService;

    public MoxisClientImpl(@Qualifier("moxisservicewebclient") WebClient webClient,
                           MoxisEnvironmentService environmentService) {
        this.webClient = webClient;
        this.environmentService = environmentService;
    }

    @Override
    public ResponseEntity<String> startProcess(MultiValueMap<String, HttpEntity<?>> multiValueMap, boolean isExternalWithHandySignatur) {
        log.info("Sending to the moxis, isExternalWithHandySignatur: {}", isExternalWithHandySignatur);
        try {
            String response = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/startProcess/{processId}")
                            .build(environmentService.getProcessId(isExternalWithHandySignatur)))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(multiValueMap))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return ResponseEntity.ok(response);
        } catch (WebClientResponseException ex) {
            log.error("WebClientResponseException: Status code: {}, Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new MoxisWebClientException(ex);
        } catch (WebClientRequestException ex) {
            log.error("WebClientRequestException: Message: {}", ex.getMessage());
            throw new MoxisWebClientException(ex);
        }

    }

    @Override
    public ResponseEntity<String> cancelJob(String processInstanceId, MoxisUserDto user) {
        try {
            String response = webClient
                    .put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cancelJob/{processInstanceId}")
                            .build(processInstanceId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(user))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return ResponseEntity.ok(response);
        } catch (WebClientResponseException ex) {
            throw new MoxisWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<List<MoxisJobStateDto>> testConnection() {
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/lookupJobStates").build())
                    .retrieve()
                    .toEntityList(MoxisJobStateDto.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new MoxisWebClientException(ex);
        }
    }


    @Override
    public ResponseEntity<MoxisJobStateDto> getJobState(String processInstanceId, String nameClassifier) {
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/getJobState/{processInstanceId}/{nameClassifier}")
                            .build(processInstanceId, nameClassifier))
                    .retrieve()
                    .toEntity(MoxisJobStateDto.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new MoxisWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<List<MoxisReducedJobStateDto>> getJobStates(List<Integer> jobIds) {
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/getJobState/{processInstanceId}/{nameClassifier}")
                            .build(StringUtils.join(jobIds, ",")))
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<MoxisReducedJobStateDto>>() {
                    })
                    .block();
        } catch (WebClientResponseException ex) {
            throw new MoxisWebClientException(ex);
        }
    }

    @Override
    public Mono<File> getDocument(String processInstanceId) {
        return webClient.get()
                .uri("/getDocument/{processInstanceId}", processInstanceId)
                .retrieve()
                .toEntityFlux(DataBuffer.class)
                .flatMap(responseEntity -> {
                    try {
                        String contentDisposition = responseEntity.getHeaders().getContentDisposition().toString();
                        String fileName = extractFilename(contentDisposition);
                        Flux<DataBuffer> dataBufferFlux = responseEntity.getBody();
                        if (dataBufferFlux == null) {
                            return Mono.error(new RuntimeException("No data found"));
                        }
                        return DataBufferUtils.join(dataBufferFlux)
                                .flatMap(dataBuffer -> {
                                    try {
                                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(bytes);
                                        DataBufferUtils.release(dataBuffer);
                                        return Mono.just(createFile(getLocalDateNow().truncatedTo(ChronoUnit.SECONDS) + "_" + processInstanceId + "-" + fileName, bytes));
                                    } catch (WebClientResponseException e) {
                                        log.error("Error creating file: " + e.getMessage());
                                        throw new MoxisWebClientException(e);
                                    }
                                });
                    } catch (Exception e) {
                        log.error("Error processing response: " + e.getMessage());
                        return Mono.error(new RuntimeException("Error processing response: " + e.getMessage(), e));
                    }
                })
                .onErrorMap(WebClientResponseException.class, MoxisWebClientException::new)
                .onErrorResume(e -> {
                    // Log the error or handle it
                    log.error("Error occurred: " + e.getMessage());
                    return Mono.empty();
                });
    }

}
