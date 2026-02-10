package com.ibosng.lhrservice.services.impl;

import com.ibosng.lhrservice.services.LHR2ValidationService;
import com.ibosng.lhrservice.services.LHRRestService;
import com.ibosng.validationservice.services.MitarbeiterSyncService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LHR2ValidationServiceImpl implements LHR2ValidationService {

    private final MitarbeiterSyncService mitarbeiterSyncService;

    @Override
    public ResponseEntity<String> validateSyncMitarbeiterWithUPN(String upn) {
        log.info("Calling validation service to validate and sync mitarbeiter with upn: {}", upn);
        return mitarbeiterSyncService.syncMitarbeiterFromIbisacamWithUPN(upn, null, null);
    }

}
