package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.*;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import org.springframework.http.ResponseEntity;

public interface MAOnboardingService {
    PayloadResponse saveStammdaten(StammdatenDto stammdatenDto, Integer workflowId, String token);

    PayloadResponse saveVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String token);

    PayloadResponse saveUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String token);

    PayloadResponse saveVertragsdaten(VertragsdatenDto vertragsDatenDto, Integer workflowId, String token);

    PayloadResponse informLohnverrechnung(String token, String personalnummerString);

    PayloadResponse getAllMitarbeiterList(String sortProperty, String sortDirection, int page, int size, String mitarbeiterType);
    PayloadResponse getEigeneMitarbeiterList(String sortProperty, String sortDirection, int page, int size, String mitarbeiterType, Benutzer benutzer);

    PayloadResponse saveLvAcceptance(LvAcceptanceDto lvAcceptanceDto, String token);

    PayloadResponse getLvAcceptance(String personalnummer, String token);

    PayloadResponse generatePersonalnummerPayloadResponse(Integer teilnehmerID, String firmenName, String createdBy);

    PayloadResponse getWorkflowgroup(String personalNummer);

    PayloadResponse saveNewTeilnehmerStammdaten(Personalnummer personalnummer, Integer teilnehmerId, String changedBy);

    ResponseEntity<byte[]> getZeitnachweisFile(String token, String date);
}
