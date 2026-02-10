package com.ibosng.gatewayservice.services.impl;

import com.ibosng.gatewayservice.dtos.OpenIdConfigurationDto;
import com.ibosng.gatewayservice.services.OpenIdConfigurationService;
import org.springframework.stereotype.Service;

@Service
public class OpenIdConfigurationServiceImpl implements OpenIdConfigurationService {

    private final GatewayRestServiceImpl gatewayRestServiceImpl;

    public OpenIdConfigurationServiceImpl(GatewayRestServiceImpl gatewayRestServiceImpl) {
        this.gatewayRestServiceImpl = gatewayRestServiceImpl;
    }

    @Override
    public OpenIdConfigurationDto getOpenIdConfiguration(String url) {
        return gatewayRestServiceImpl.getForEntity(url, OpenIdConfigurationDto.class);
    }
}
