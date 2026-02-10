package com.ibosng.lhrservice.services;

import com.ibosng.lhrservice.dtos.zeitdaten.AnfrageSuccessDto;
import org.springframework.http.ResponseEntity;

public interface LHRAuszahlungsService {
    ResponseEntity<AnfrageSuccessDto> postAuszahlungsanfrage(String personalnummer, String day, String zspNr, String minutes);
    ResponseEntity<AnfrageSuccessDto> getAuszahlungsanfrage(String personalnummer, Integer anfrageNr);
}
