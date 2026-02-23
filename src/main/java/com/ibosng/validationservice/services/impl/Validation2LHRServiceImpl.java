package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.ZeitausgleichDto;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.lhrservice.services.LHRUrlaubService;
import com.ibosng.lhrservice.services.LHRZeiterfassungService;
import com.ibosng.validationservice.services.Validation2LHRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class Validation2LHRServiceImpl implements Validation2LHRService {

    private final LHRUrlaubService lhrUrlaubService;
    private final LHRZeiterfassungService lhrZeiterfassungService;

    @Override
    public void deleteLeistungserfassung(Integer personalnummerId, String leistungDatum) {
        ResponseEntity<?> response = lhrUrlaubService.deleteLeistungsdatumToLhr(personalnummerId, leistungDatum);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Successfully sent request to the lhr service to delete pn:{} and datum:{}", personalnummerId, leistungDatum);
        } else {
            log.error("Lhr service responded with: {} for pn:{} and datum:{}", response.getStatusCode(), personalnummerId, leistungDatum);
        }
    }

    @Override
    public ResponseEntity<?> syncLeistungerfassung(Integer personalnummerId, String date) {
        return lhrUrlaubService.sendLeistungsdatumToLhr(personalnummerId, date);
    }

    @Override
    public void manageAbsencesFromZeitbuchungen(Integer personalnummerId, String von, String bis) {
        lhrUrlaubService.manageAbwesenheitenVonZeitbuchungen(personalnummerId, von, bis);
    }

    @Override
    public ResponseEntity<?> sendUrlaub(String modify, String ignore, AbwesenheitDto abwesenheitDto) {
        return lhrUrlaubService.createUrlaub(modify, ignore, abwesenheitDto);
    }

    @Override
    public ResponseEntity<List<ZeitausgleichDto>> sendZeitausgleichPeriod(AbwesenheitDto abwesenheitDto) {
        return lhrZeiterfassungService.submitZeitausgleichForPeriod(abwesenheitDto);
    }

    @Override
    public ResponseEntity<?> syncUrlaubeForMA(Integer personalnummerId, String fromDate, String toDate) {
        return lhrUrlaubService.syncUrlaubDetails(personalnummerId, fromDate, toDate);
    }

    @Override
    public ResponseEntity<?> deleteAbwesenheit(AbwesenheitDto dto) {
        return lhrUrlaubService.deleteUrlaub(dto, null, null);
    }

    @Override
    public ResponseEntity<?> deleteZeitausgleich(Integer personalnummerId, String date, boolean forceDelete) {
        return lhrUrlaubService.deleteZeitausgelich(personalnummerId, date, forceDelete);
    }
}
