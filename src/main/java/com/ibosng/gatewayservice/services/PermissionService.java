package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;

public interface PermissionService {

    boolean canCreateVereinbarung(Benutzer benutzer, VereinbarungDto vereinbarungDto);
    boolean canCreateVereinbarung(Benutzer benutzer, ReportRequestDto reportRequestDto);

    boolean canReadVereinbarung(Benutzer benutzer, Integer vereinbarungId);
}
