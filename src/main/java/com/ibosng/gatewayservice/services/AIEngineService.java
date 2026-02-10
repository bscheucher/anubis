package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import org.springframework.http.ResponseEntity;


public interface AIEngineService {

    ResponseEntity<PayloadResponse> getSeminarVertretungRequest(String trainerId, String von, String bis);

    ResponseEntity<String> sendSingleStringRequest(String request);

    ResponseEntity<Void> setAnweisung(String assistantName, String anweisung, String user);

    ResponseEntity<String> getAnweisung(String assistantName);
}
