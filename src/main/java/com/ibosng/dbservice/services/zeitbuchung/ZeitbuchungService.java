package com.ibosng.dbservice.services.zeitbuchung;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungsort;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ZeitbuchungService extends BaseService<Zeitbuchung> {
    List<Zeitbuchung> getZeitbuchungenByListungserfassen(Leistungserfassung leistungserfassung);

    ZeitbuchungenDto mapToZeitbuchungenDto(Abwesenheit abwesenheit, LocalDate date);

    boolean isZeitbuchungExists(LocalTime von, LocalTime bis, Boolean anAbwesenheit, Leistungsort leistungsort,
                                Integer seminarId, Integer zeitbuchuntypId, Integer leistungserfassungId, Integer kostenstelleId);

    List<Zeitbuchung> findZeitbuchungenInPeriodAndAnAbwesenheit(Integer personalnummerId, LocalDate startDate, LocalDate endDate, Boolean isAnAbwesenheit);
}
