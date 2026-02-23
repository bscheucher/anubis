package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.SeminarAnAbwesenheitDto;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarType;
import com.ibosng.dbservice.repositories.seminar.SeminarRepository;
import com.ibosng.dbservice.repositories.seminar.SeminarRepositoryExtended;
import com.ibosng.dbservice.services.SeminarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class SeminarServiceImpl implements SeminarService {
    private static final String UEBA_WIEN = "ÜBA Wien";
    private static final String UEBA_NOE = "ÜBA NÖ";
    private static final String UEBA_TIROL = "ÜBA Tirol";
    private static final String UEBA_OOE = "ÜBA OÖ";
    private static final String UEBA_SALZBURG = "ÜBA Salzburg";

    private final SeminarRepository seminarRepository;
    private final SeminarRepositoryExtended seminarRepositoryExtendedImpl;
    private final Collator germanCollator = Collator.getInstance(Locale.GERMAN);

    @Override
    public List<Seminar> findAll() {
        return seminarRepository.findAll();
    }

    @Override
    public Optional<Seminar> findById(Integer id) {
        return seminarRepository.findById(id);
    }

    @Override
    public Seminar save(Seminar object) {
        return seminarRepository.save(object);
    }

    @Override
    public List<Seminar> saveAll(List<Seminar> objects) {
        return seminarRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarRepository.deleteById(id);
    }

    @Override
    public List<Seminar> findAllByIdentifier(String identifier) {
        return seminarRepository.findAllByIdentifier(identifier);
    }

    @Override
    public Seminar findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTimeAndEndTime(String identifier, SeminarType seminarType, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return seminarRepository.findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTimeAndEndTime(identifier, seminarType, startDate, endDate, startTime, endTime);
    }

    @Override
    public Seminar findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTime(String identifier, SeminarType seminarType, LocalDate startDate, LocalDate endDate, LocalTime startTime) {
        return seminarRepository.findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTime(identifier, seminarType, startDate, endDate, startTime);
    }

    @Override
    public List<Seminar> findByBezeichnung(String bezeichnung) {
        return seminarRepository.findByBezeichnung(bezeichnung);
    }

    @Override
    public List<Seminar> findSeminarByDetails(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam) {
        return seminarRepository.findSeminarByBezeichnung1InAndDatumVonAndDatumBisAndZeitVon(identifiers, datumVonParam, datumBisParam, zeitVonParam);
    }

    @Override
    public Page<SeminarAnAbwesenheitDto> findAllSeminarAnUndAbwesenheit(boolean isAdmin,
                                                                        Integer benutzerId,
                                                                        Boolean isActive, String projectName,
                                                                        String seminarName,
                                                                        LocalDate kursEndeSpaeterAls,
                                                                        LocalDate kursEndeFrueherAls,
                                                                        Boolean verzoegerung,
                                                                        String sortProperty,
                                                                        Direction sortDirection,
                                                                        int page, int size) {
        return seminarRepository.findSeminarFiltered(isAdmin, benutzerId, isActive, projectName,
                seminarName, kursEndeSpaeterAls, kursEndeFrueherAls, verzoegerung, sortProperty, sortDirection, page, size);
    }

    @Override
    public List<Seminar> findAllBySeminarNummer(Integer seminarNummer) {
        return seminarRepository.findAllBySeminarNummer(seminarNummer);
    }

    @Override
    public List<String> findAllByStatusAndProjectName(String projectName, Boolean isActive, Boolean isKorrigieren, Benutzer benutzer) {
        return seminarRepository.findAllSeminarsFromProjectAndActive(isNullOrBlank(projectName) ? null : projectName, isActive, isKorrigieren, benutzer != null ? benutzer.getId() : null);
    }

    @Override
    public List<BasicSeminarDto> findAllByStatusAndProjectNameBasicDto(String projectName, Boolean isActive, Benutzer benutzer) {
        return findAllSeminarsFromProjectAndActive(projectName, isActive, benutzer)
                .stream().map(seminar -> new BasicSeminarDto(seminar.getId(), seminar.getSeminarNummer(), seminar.getBezeichnung())).sorted(germanCollator).toList();
    }

    @Override
    public Page<BasicSeminarDto> getAllSeminarsPageable(Boolean isUeba, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("bezeichnung")));
        Page<BasicSeminarDto> seminarPage;
        if (isUeba != null && isUeba) {
            List<String> projektTypeNames = new ArrayList<>();
            projektTypeNames.add(UEBA_WIEN);
            projektTypeNames.add(UEBA_NOE);
            projektTypeNames.add(UEBA_OOE);
            projektTypeNames.add(UEBA_TIROL);
            projektTypeNames.add(UEBA_SALZBURG);
            seminarPage = seminarRepository.findAllSeminarsFromProjectAndIsUeba(pageable, projektTypeNames);
        } else {
            seminarPage = seminarRepository.findAllBasicSeminarDtos(pageable);
        }
        return seminarPage;
    }

    @Override
    public List<Seminar> findSeminarsWithoutTrainers() {
        return seminarRepository.findSeminarsWithoutTrainers();
    }

    @Override
    public List<Seminar> findAllActive(LocalDate currentDate) {
        return seminarRepository.findAllActive(currentDate);
    }

    private List<Seminar> findAllSeminarsFromProjectAndActive(String projectName, Boolean isActive, Benutzer benutzer) {
        return seminarRepositoryExtendedImpl.findByBezeichnungIsActiveAndBenutzer(projectName, isActive, benutzer);
    }
}
