package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.ZeitausgleichDto;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface Validation2LHRService {
    void deleteLeistungserfassung(Integer personalnummerId, String leistungDatum);

    ResponseEntity syncLeistungerfassung(Integer personalnummerId, String date);

    void manageAbsencesFromZeitbuchungen(Integer personalnummerId, String von, String bis);

    ResponseEntity<?> sendUrlaub(String modify, String ignore, AbwesenheitDto abwesenheitDto);

    ResponseEntity<List<ZeitausgleichDto>> sendZeitausgleichPeriod(AbwesenheitDto abwesenheitDto);

    ResponseEntity syncUrlaubeForMA(Integer personalnummer, String fromDate, String toDate);

    ResponseEntity deleteAbwesenheit(AbwesenheitDto dto);

    ResponseEntity deleteZeitausgleich(Integer personalnummerId, String date, boolean forceDelete);
}
