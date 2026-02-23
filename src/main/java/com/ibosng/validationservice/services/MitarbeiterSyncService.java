package com.ibosng.validationservice.services;

import org.springframework.http.ResponseEntity;

public interface MitarbeiterSyncService {
    ResponseEntity<String> syncMitarbeiterFromIbisacam(String email, String personalnummerString, Integer ibisFirmaIbos);

    ResponseEntity<String> syncMitarbeiterFromIbisacamWithUPN(String upn, String personalnummerString, Integer ibisFirmaIbos);
}
