package com.ibosng.aiservice.services;

import org.springframework.http.ResponseEntity;

public interface AIRoutingService {

    <T> ResponseEntity<String> sendRequest(T request);
}
