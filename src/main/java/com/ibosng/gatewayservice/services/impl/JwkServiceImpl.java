package com.ibosng.gatewayservice.services.impl;

import com.ibosng.gatewayservice.dtos.JwkResponseDto;
import com.ibosng.gatewayservice.services.JwkService;
import org.springframework.stereotype.Service;

@Service
public class JwkServiceImpl implements JwkService {
    private final GatewayRestServiceImpl gatewayRestServiceImpl;

    public JwkServiceImpl(GatewayRestServiceImpl gatewayRestServiceImpl) {
        this.gatewayRestServiceImpl = gatewayRestServiceImpl;
    }

    @Override
    public JwkResponseDto getJwk(String uri) {
        // Get JWK Public Key from Azure public endpoint
        return gatewayRestServiceImpl.getForEntity(uri, JwkResponseDto.class);
    }
}