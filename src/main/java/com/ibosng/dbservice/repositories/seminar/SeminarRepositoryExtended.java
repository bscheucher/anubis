package com.ibosng.dbservice.repositories.seminar;

import com.ibosng.dbservice.dtos.SeminarAnAbwesenheitDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction;

@Transactional("postgresTransactionManager")
public interface SeminarRepositoryExtended {
    List<Seminar> findByBezeichnungIsActiveAndBenutzer(String projektBezeichnung, Boolean isActive, Benutzer benutzer);

    List<Seminar> findSeminarByBezeichnung1InAndDatumVonAndDatumBisAndZeitVon(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam);

    Page<SeminarAnAbwesenheitDto> findSeminarFiltered(
            boolean isAdmin,
            Integer benutzerId,
            Boolean isActive,
            String projectName,
            String seminarName,
            LocalDate kursEndeSpaeterAls,
            LocalDate kursEndeFrueherAls,
            Boolean verzoegerung,
            String sortProperty,
            Direction sortDirection,
            int page,
            int size);
}
