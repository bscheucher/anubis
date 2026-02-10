package com.ibosng.validationservice.zeiterfassung.validations.impl;

import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import com.ibosng.dbmapperservice.services.SeminarProjektMapperService;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.SeminarService;
import com.ibosng.validationservice.Validation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ZeiterfassungSeminarValidation implements Validation<ZeiterfassungTransferDto, ZeiterfassungTransfer> {

    private final SeminarService seminarService;
    private final SeminarIbosService seminarIbosService;
    private final SeminarProjektMapperService seminarProjektMapperService;

    @Getter
    @Setter
    private boolean isValid = true;

    @Override
    public boolean executeValidation(ZeiterfassungTransferDto zeiterfassungDto, ZeiterfassungTransfer zeiterfassung) {
        setValid(true);
        for (BasicSeminarDto seminarDto : zeiterfassungDto.getSeminars()) {
            Seminar seminar = getSeminar(seminarDto);
            if (seminar != null) {
                zeiterfassung.getSeminars().add(seminar);
                seminarDto.setSeminarBezeichnung(seminar.getBezeichnung());
                seminarDto.setSeminarNumber(seminar.getSeminarNummer());
                seminarDto.setId(seminar.getId());
            }
        }
        return isValid();
    }

    private Seminar getSeminar(BasicSeminarDto seminarDto) {
        List<Seminar> seminars = new ArrayList<>();
        Seminar seminar;
        if (seminarDto.getSeminarNumber() != null) { // Try to find by seminar number
            seminars = seminarService.findAllBySeminarNummer(seminarDto.getSeminarNumber());
        } else if (!isNullOrBlank(seminarDto.getSeminarBezeichnung())) {
            seminars = seminarService.findByBezeichnung(seminarDto.getSeminarBezeichnung());
        }
        seminar = findFirstObject(seminars,
                Set.of(seminarDto.getSeminarBezeichnung() != null ? seminarDto.getSeminarBezeichnung() : ""),
                "seminar");


        //The seminar does not exist in the ibosNG DB, try and find it in the ibos DB
        if (seminar == null) {
            SeminarIbos seminarIbos = null;
            if (seminarDto.getSeminarNumber() != null) {
                Optional<SeminarIbos> optionalSeminarIbos = seminarIbosService.findById(seminarDto.getSeminarNumber());
                if (optionalSeminarIbos.isPresent()) {
                    seminarIbos = optionalSeminarIbos.get();
                }
            }
            if (seminarIbos != null) {
                seminar = seminarProjektMapperService.mapSeminarIbosToSeminar(seminarIbos);
                Set<Seminar> seminarSet = new HashSet<>(seminarService.findAll());
                if (!seminarSet.contains(seminar)) {
                    seminar.setStatus(Status.ACTIVE);
                    seminar.setCreatedBy(VALIDATION_SERVICE);
                    seminar = seminarService.save(seminar);
                } else {
                    for (Seminar s : seminarSet) {
                        if (s.equals(seminar)) {
                            seminar = s;
                            break;
                        }
                    }
                }
            }
        }
        return seminar;
    }
}
