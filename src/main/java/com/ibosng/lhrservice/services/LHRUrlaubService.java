package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import org.springframework.http.ResponseEntity;

public interface LHRUrlaubService {
    ResponseEntity<?> getAllUrlaubstand(Integer personalnummerId, String effectiveDate);

    ResponseEntity<?> getAllUrlaubs(Integer personalnummerId, String fromDate, String endDate);

    ResponseEntity<?> getUrlaubstand(Integer personalnummerId, String date);

    ResponseEntity<?> sendLeistungsdatumToLhr(Integer personalnummerId, String leistungDatum);

    ResponseEntity<?> deleteLeistungsdatumToLhr(Integer personalnummerId, String leistungDatum);

    ResponseEntity<?> createUrlaub(String modify, String ignore, AbwesenheitDto abwesenheitDto);

    ResponseEntity<?> deleteUrlaub(AbwesenheitDto abwesenheitDto, String modify, String ignore);

    void manageAbwesenheitenVonZeitbuchungen(Integer personalnummerId, String from, String to);

    ResponseEntity<?> syncUrlaubDetails(Integer personalnummerId, String startDate, String endDate);

    void calculateAbwesenheiten();

    ResponseEntity<?> deleteZeitausgelich(Integer personalnummerId, String date, boolean forceDelete);

    ResponseEntity<?> deleteZeitausgelich(Integer personalnummerId, String date);

    boolean sendZeitausgleichToLhr(Integer personalnummerId, Integer firmaNr, String firma, String buchungType,
                                   String dateTimeVon, String dateTimeBis);

    void compareAndUpdateErroneousUrlaube();
}
