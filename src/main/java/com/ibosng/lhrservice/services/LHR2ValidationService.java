package com.ibosng.lhrservice.services;

import org.springframework.http.ResponseEntity;

public interface LHR2ValidationService {

    ResponseEntity<String> validateSyncMitarbeiterWithUPN(String upn);
}
