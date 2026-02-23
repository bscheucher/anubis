package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import org.springframework.http.ResponseEntity;

public interface MitarbeiterService {
    ResponseEntity<PayloadResponse> getStammdaten(String personalnummer, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> saveStammdaten(StammdatenDto stammdatenDto, Integer workflowId, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> getVertragsdaten(String personalnummer, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> saveVertragsdaten(VertragsdatenDto vertragsDatenDto, Integer workflowId, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> getVordienstzeiten(String personalnummer, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> saveVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> deleteVordienstzeiten(Integer vordienstzeitenId, String authorizationHeader);

    ResponseEntity<PayloadResponse> getUnterhaltsberechtigte(String personalnummer, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> saveUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, Boolean isOnboarding, String authorizationHeader);

    ResponseEntity<PayloadResponse> deleteUnterhaltsberechtigte(Integer unterhaltsberechtigteId, String authorizationHeader);

    ResponseEntity<PayloadResponse> sendMoxisSigningRequest(String personalnummer, String authorizationHeader);

    ResponseEntity<PayloadResponse> cancelMoxisSigningRequest(String personalnummer, String authorizationHeader);

    ResponseEntity<PayloadResponse> sendVereinbarungSigningRequest(String authorizationHeader, Integer vereinbarungId);

    PayloadResponse getSeminars(String personalnummer);

    PayloadResponse getProjekts(String personalnummer);
}
