package com.ibosng.gatewayservice.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface RestService {
    <T> ResponseEntity<T> sendRequest(HttpMethod httpMethod, String endpoint, Object dto, Class<T> classDto, MediaType mediaType, Map<String, String> queryParams);
    <T> T getForEntity(String endpoint, Class<T> classDto);
    <T> ResponseEntity<T> sendRequestParametrized(HttpMethod httpMethod, String endpoint, Object dto, ParameterizedTypeReference<T> responseType, MediaType mediaType, Map<String, String> queryParams);
}
