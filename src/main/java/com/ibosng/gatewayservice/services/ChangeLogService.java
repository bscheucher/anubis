package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;

public interface ChangeLogService {
    PayloadResponse getMAChangeLog(String personalnummer);

    PayloadResponse getVertragsaenderungenChangeLog(String personalnummer);
}
