package com.ibosng.gatewayservice.services.impl;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.gatewayservice.services.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static com.ibosng.gatewayservice.utils.Helpers.createErrorResponse;
import static com.ibosng.gatewayservice.utils.Helpers.extractErrorMessage;

@Service
@Slf4j
public class GatewayRestServiceImpl implements RestService {

    private final static String USER_HEADER = "auth-user-id";
    private final GlobalUserHolder globalUserHolder;
    private final RestTemplate restTemplate;

    public GatewayRestServiceImpl(GlobalUserHolder globalUserHolder, RestTemplate restTemplate) {
        this.globalUserHolder = globalUserHolder;
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> sendRequest(HttpMethod httpMethod, String endpoint, Object dto, Class<T> classDto, MediaType mediaType, Map<String, String> queryParams) {
        try {
            String urlWithParams = buildUriWithQueryParams(endpoint, queryParams);
            ResponseEntity<T> response;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType != null ? mediaType : MediaType.APPLICATION_JSON);
            headers.add(USER_HEADER, String.valueOf(globalUserHolder.getUserId()));
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

    public <T> ResponseEntity<T> sendRequestParametrized(HttpMethod httpMethod, String endpoint, Object dto, ParameterizedTypeReference<T> responseType, MediaType mediaType, Map<String, String> queryParams) {
        try {
            String urlWithParams = buildUriWithQueryParams(endpoint, queryParams);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType != null ? mediaType : MediaType.APPLICATION_JSON);
            headers.add(USER_HEADER, String.valueOf(globalUserHolder.getUserId()));
            // Create HttpEntity with the DTO and headers
            HttpEntity<Object> entity = dto != null ? new HttpEntity<>(dto, headers) : new HttpEntity<>(headers);
            ResponseEntity<T> response = restTemplate.exchange(urlWithParams, httpMethod, entity, responseType);
            // Log and return the response regardless of the status
            log.info("Service returned response with status: " + response.getStatusCode());
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            log.error("An HTTP error occurred: Status - {}, Body - {}", e.getStatusCode(), errorBody);
            try {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAs(responseType));
            } catch (Exception parseException) {
                log.error("Failed to parse error body: {}", parseException.getMessage());
            }
            HttpStatus statusCode = HttpStatus.valueOf(e.getStatusCode().value());
            return createErrorResponse(statusCode, errorBody);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public <T> T getForEntity(String endpoint, Class<T> classDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(USER_HEADER, String.valueOf(globalUserHolder.getUserId()));
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<T> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, classDto);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Successful response from service");
                return response.getBody();
            }
            log.warn("Сervice returned response with status: " + response.getStatusCode());
        } catch (org.springframework.web.client.HttpClientErrorException.UnprocessableEntity e) {
            log.error("An HTTP 422 Unprocessable Entity error occurred: " + e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
        }
        return null;
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
