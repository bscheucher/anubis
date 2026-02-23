package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.lhrservice.dtos.DnStammStandaloneDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LHRDienstnehmerService {

    ResponseEntity<DnStammStandaloneDto> getDienstnehmerstamm(Integer personalnummerId);

    ResponseEntity<?> createOrUpdateLHRDienstnehmerstamm(Integer personalnummer) throws LHRException;

    ResponseEntity<List<DnStammStandaloneDto>> findAllDienstnehmers(String firma, Integer minDnNr, Integer maxDnNr,
                                                                    String effectiveDate, String activeSince);

    ResponseEntity<?> mapAndSendUebaAbmeldung(AbmeldungDto abmeldungDto);
}
