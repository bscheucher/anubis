package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;

public interface EnvironmentService {
    boolean isProduction();

    Personalnummer checkLoggedInUserAndGetPersonalnummer();
}
