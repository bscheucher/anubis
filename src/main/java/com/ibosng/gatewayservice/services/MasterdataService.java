package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;

public interface MasterdataService {

    PayloadResponse getMitarbeiterPayload(String type);

    PayloadResponse getKostenstelleFromPersonalnummer(String personalnummer);

}
