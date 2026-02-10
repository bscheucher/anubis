package com.ibosng.validationservice.zeitbuchung.validations.impl;

import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import com.ibosng.dbmapperservice.services.SeminarProjektMapperService;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenType;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.SeminarService;
import com.ibosng.validationservice.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeitbuchungSeminarValidation implements Validation<ZeitbuchungenDto, Zeitbuchung> {
    private final SeminarService seminarService;
    private final SeminarIbosService seminarIbosService;
    private final SeminarProjektMapperService seminarProjektMapperService;

    @Override
    public boolean executeValidation(ZeitbuchungenDto objectT, Zeitbuchung objectV) {
        if (!ZeitbuchungenType.ANWESENHEIT.equals(objectT.getAnAbwesenheit())) {
            return true;
        }
        Seminar seminar = getSeminar(objectT.getSeminar());
        if (seminar == null) {
            log.info("No seminar with id {} found", objectT.getSeminar());
            return false;
        }
        objectT.getSeminar().setId(seminar.getId());
        objectT.getSeminar().setSeminarNumber(seminar.getSeminarNummer());
        objectT.getSeminar().setSeminarBezeichnung(seminar.getBezeichnung());
        objectV.setSeminar(seminar);
        return true;
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
                seminar.setStatus(Status.ACTIVE);
                seminar.setCreatedBy(VALIDATION_SERVICE);
                seminar = seminarService.save(seminar);
            }
        }
        return seminar;
    }
}
