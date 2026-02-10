package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.entities.seminar.SeminarGesamtbeurteilung;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.services.Teilnehmer2SeminarService;
import com.ibosng.dbservice.services.masterdata.SeminarAustrittsgrundService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungErgebnisTypeService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungGegenstandService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.Teilnehmer2SeminarValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.manual.AbstractValidator;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class Teilnehmer2SeminarValidatorServiceImpl extends AbstractValidator<Teilnehmer2Seminar, SeminarDto>
        implements Teilnehmer2SeminarValidatorService {
    private final SeminarAustrittsgrundService seminarAustrittsgrundService;
    private final Teilnehmer2SeminarService teilnehmer2SeminarService;
    private final List<FieldValidator<SeminarDto>> fieldValidators;
    private final SeminarPruefungGegenstandService seminarPruefungGegenstandService;
    private final SeminarPruefungErgebnisTypeService seminarPruefungErgebnisTypeService;

    @Override
    public SeminarDto validateTeilnehmerSeminar(SeminarDto dto, String changedBy, String teilnehmerId) {
        //for updating EXISTING Teilnehmer2Seminar
        log.info("Validating Teilnehmer2Seminar for Teilnehmer {} with SeminarId {}", teilnehmerId, dto.getId());
        return validateDto(dto, teilnehmerId, changedBy, null);
    }

    @Override
    protected Teilnehmer2Seminar validateEntity(SeminarDto dto, String parentId, String changedBy, String secondParentId) {
        Integer tnId = Parsers.parseStringToInteger(parentId);
        if (tnId == null) return null;

        Teilnehmer2Seminar teilnehmer2Seminar = null;
        if (dto.getId() != 0) {
            teilnehmer2Seminar = loadExistingEntity(tnId, dto.getId());
        }

        if (teilnehmer2Seminar == null) return null;

        fieldValidators.forEach(v -> v.validate(dto, null));
        setFields(teilnehmer2Seminar, dto, changedBy);
        return saveIfValid(teilnehmer2Seminar, dto, changedBy);
    }

    @Override
    protected SeminarDto mapToDto(Teilnehmer2Seminar entity, SeminarDto dto) {
        if (entity == null) return null;
        if (entity.getAustritt() != null) {
            dto.setAustritt(entity.getAustritt().toString());
        }
        if (entity.getAustrittsgrund() != null) {
            dto.setAustrittsgrund(entity.getAustrittsgrund().getName());
        }
        if (entity.getBegehrenBis() != null) {
            dto.setBegehrenBis(entity.getBegehrenBis().toString());
        }
        dto.setZusaetzlicheUnterstuetzung(entity.getZusaetzlicheUnterstuetzung());
        dto.setErrors(dto.getErrorsMap() != null
                ? new ArrayList<>(dto.getErrorsMap().keySet())
                : new ArrayList<>());
        return dto;
    }

    private Teilnehmer2Seminar loadExistingEntity(Integer teilnehmerId, int seminarId) {
        return teilnehmer2SeminarService.findByTeilnehmerIdAndSeminarId(teilnehmerId, seminarId).orElse(null);
    }

    private void setFields(Teilnehmer2Seminar entity, SeminarDto dto, String changedBy) {
        if (isFieldValid(dto, "austrittsgrund")) {
            entity.setAustrittsgrund(seminarAustrittsgrundService.findByName(dto.getAustrittsgrund()));
        }
        if (isFieldValid(dto, "begehrenBis")) {
            entity.setBegehrenBis(parseDate(dto.getBegehrenBis()));
        }
        if (isFieldValid(dto, "austritt")) {
            entity.setAustritt(parseDate(dto.getAustritt()));
        }
        if (isFieldValid(dto, "fruehwarnung")) {
            entity.setFruehwarnung(parseDate(dto.getFruehwarnung()));
        }
        boolean validType = isFieldValid(dto, "gesamtbeurteilungTyp");
        boolean validErgebnis = isFieldValid(dto, "gesamtbeurteilungErgebnis");
        if (entity.getSeminarGesamtbeurteilung() != null) {
            if (validType) {
                entity.getSeminarGesamtbeurteilung().setType(seminarPruefungGegenstandService.findByName(dto.getGesamtbeurteilungTyp()));
            }

            if (validErgebnis) {
                entity.getSeminarGesamtbeurteilung().setErgebnis(seminarPruefungErgebnisTypeService.findByName(dto.getGesamtbeurteilungErgebnis()));
            }

            if (validType || validErgebnis) {
                entity.getSeminarGesamtbeurteilung().setChangedOn(getLocalDateNow());
                entity.getSeminarGesamtbeurteilung().setChangedBy(changedBy);
            }
        } else {
            SeminarGesamtbeurteilung seminarGesamtbeurteilung = new SeminarGesamtbeurteilung();
            if (validType) {
                seminarGesamtbeurteilung.setType(seminarPruefungGegenstandService.findByName(dto.getGesamtbeurteilungTyp()));
            }

            if (validErgebnis) {
                seminarGesamtbeurteilung.setErgebnis(seminarPruefungErgebnisTypeService.findByName(dto.getGesamtbeurteilungErgebnis()));
            }

            if (validType && validErgebnis) {
                seminarGesamtbeurteilung.setCreatedOn(getLocalDateNow());
                seminarGesamtbeurteilung.setCreatedBy(changedBy);
                entity.setSeminarGesamtbeurteilung(seminarGesamtbeurteilung);
            }
        }

        entity.setZusaetzlicheUnterstuetzung(dto.getZusaetzlicheUnterstuetzung());
        entity.setLernfortschritt(dto.getLernfortschritt());
        entity.setAnmerkung(dto.getAnmerkung());
    }

    private boolean isFieldValid(SeminarDto dto, String fieldName) {
        return !dto.getErrorsMap().containsKey(fieldName);
    }

    private Teilnehmer2Seminar saveIfValid(Teilnehmer2Seminar entity, SeminarDto dto, String changedBy) {
        if (dto.getErrorsMap().isEmpty()) {
            entity.setChangedBy(changedBy);
            entity.setChangedOn(getLocalDateNow());
            return teilnehmer2SeminarService.save(entity);
        }
        return entity;
    }
}
