package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.SeminarAnAbwesenheitDto;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface SeminarService extends BaseService<Seminar> {
    Seminar findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTimeAndEndTime(String identifier, SeminarType seminarType, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);

    Seminar findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTime(String identifier, SeminarType seminarType, LocalDate startDate, LocalDate endDate, LocalTime startTime);

    List<Seminar> findByBezeichnung(String bezeichnung);

    List<Seminar> findSeminarByDetails(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam);

    Page<SeminarAnAbwesenheitDto> findAllSeminarAnUndAbwesenheit(boolean isAdmin,
                                                                 Integer benutzerId,
                                                                 Boolean isActive, String projectName,
                                                                 String seminarName,
                                                                 LocalDate kursEndeSpaeterAls,
                                                                 LocalDate kursEndeFrueherAls,
                                                                 Boolean verzoegerung,
                                                                 String sortProperty,
                                                                 Direction sortDirection,
                                                                 int page, int size);

    List<Seminar> findAllBySeminarNummer(Integer seminarNummer);

    List<String> findAllByStatusAndProjectName(String projectName, Boolean isActive, Boolean isKorrigieren, @Nullable Benutzer benutzer);

    List<BasicSeminarDto> findAllByStatusAndProjectNameBasicDto(String projectName, Boolean isActive, Benutzer benutzer);

    Page<BasicSeminarDto> getAllSeminarsPageable(Boolean isUeba, int page, int size);

    List<Seminar> findSeminarsWithoutTrainers();

    List<Seminar> findAllActive(@Param("currentDate") LocalDate currentDate);
}
