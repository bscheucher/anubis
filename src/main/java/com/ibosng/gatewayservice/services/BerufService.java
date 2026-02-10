package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;

public interface BerufService {

    PayloadResponse fetchBerufe(String searchTerm);
}
