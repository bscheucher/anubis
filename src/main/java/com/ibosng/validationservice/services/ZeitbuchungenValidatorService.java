package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.ZeitbuchungSyncRequestDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ZeitbuchungenValidatorService {
    List<ZeitbuchungenDto> syncZeitbuchungenForMitarbeiter(Personalnummer personalnummer, Integer adresseIbosId, LocalDate startDate, LocalDate endDate);

    ResponseEntity<List<ZeitbuchungenDto>> getZeitbuchungen(ZeitbuchungSyncRequestDto requestDto);

    ResponseEntity<List<ZeitbuchungenDto>> updateZeitbuchungen(ZeitbuchungSyncRequestDto requestDto, String changedBy);
}
