package com.ibosng.gatewayservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibosng.gatewayservice.dtos.JwkResponseDto;

public interface JwkService {
    JwkResponseDto getJwk(String uri) throws JsonProcessingException;
}
