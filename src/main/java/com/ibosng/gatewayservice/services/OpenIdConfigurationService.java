package com.ibosng.gatewayservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibosng.gatewayservice.dtos.OpenIdConfigurationDto;

public interface OpenIdConfigurationService {
    OpenIdConfigurationDto getOpenIdConfiguration(String url) throws JsonProcessingException;
}
