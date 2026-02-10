package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;

public interface ProjektResponseService {
    PayloadResponse getProjektByStatus(Boolean isActive, Boolean isKorrigieren, Benutzer benutzer);
}
