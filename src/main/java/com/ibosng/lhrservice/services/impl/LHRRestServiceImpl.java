package com.ibosng.lhrservice.services.impl;

import com.ibosng.lhrservice.services.LHRRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static com.ibosng.lhrservice.utils.Helpers.createErrorResponse;
import static com.ibosng.lhrservice.utils.Helpers.extractErrorMessage;

@Service
@Slf4j
public class LHRRestServiceImpl implements LHRRestService {

    private final RestTemplate restTemplate;

    public LHRRestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> ResponseEntity<T> sendRequest(HttpMethod httpMethod, String endpoint, Object dto, Class<T> classDto, MediaType mediaType, Map<String, String> queryParams) {
        try {
            String urlWithParams = buildUriWithQueryParams(endpoint, queryParams);
            ResponseEntity<T> response;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType != null ? mediaType : MediaType.APPLICATION_JSON);
            // Create HttpEntity with the DTO and headers
            HttpEntity<Object> entity;
            if (dto != null) {
                // Create HttpEntity with the DTO and headers
                entity = new HttpEntity<>(dto, headers);
            } else {
                // Create HttpEntity with only headers
                entity = new HttpEntity<>(headers);
            }
            response = restTemplate.exchange(urlWithParams, httpMethod, entity, classDto);
            // Log and return the response regardless of the status
            log.info("Service returned response with status: " + response.getStatusCode());
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("An HTTP error occurred: " + e.getMessage());
            String errorMessage = extractErrorMessage(e.getResponseBodyAsString());
            HttpStatus statusCode = HttpStatus.valueOf(e.getStatusCode().value());
            return createErrorResponse(statusCode, errorMessage);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String buildUriWithQueryParams(String endpoint, Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return endpoint;
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint);
        queryParams.forEach(builder::queryParam);
        return builder.toUriString();
    }

}
