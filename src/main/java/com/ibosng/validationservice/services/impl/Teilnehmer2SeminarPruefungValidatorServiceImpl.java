package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.entities.seminar.SeminarPruefung;
import com.ibosng.dbservice.services.SeminarPruefungService;
import com.ibosng.dbservice.services.Teilnehmer2SeminarService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungArtService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungBegruendungService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungBezeichnungService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungErgebnisTypeService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungGegenstandService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungInstitutService;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungNiveauService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.Teilnehmer2SeminarPruefungValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.manual.AbstractValidator;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class Teilnehmer2SeminarPruefungValidatorServiceImpl extends AbstractValidator<SeminarPruefung, SeminarPruefungDto>
        implements Teilnehmer2SeminarPruefungValidatorService {
    private final SeminarPruefungService seminarPruefungService;
    private final SeminarPruefungBezeichnungService seminarPruefungBezeichnungService;
    private final SeminarPruefungArtService seminarPruefungArtService;
    private final SeminarPruefungGegenstandService seminarPruefungGegenstandService;
    private final SeminarPruefungNiveauService seminarPruefungNiveauService;
    private final SeminarPruefungInstitutService seminarPruefungInstitutService;
    private final SeminarPruefungBegruendungService seminarPruefungBegruendungService;
    private final SeminarPruefungErgebnisTypeService seminarPruefungErgebnisTypeService;
    private final Teilnehmer2SeminarService teilnehmer2SeminarService;
    private final List<FieldValidator<SeminarPruefungDto>> fieldValidators;

    @Override
    public SeminarPruefungDto validateTeilnehmerSeminarPruefung(SeminarPruefungDto dto, String changedBy, String teilnehmerId, String seminarId) {
        log.info("Validating SeminarPruefung for Teilnehmer {} and Seminar {}", teilnehmerId, seminarId);
        return validateDto(dto, teilnehmerId, changedBy, seminarId);
    }

    @Override
    protected SeminarPruefung validateEntity(SeminarPruefungDto dto, String parentId, String changedBy, String secondParentId) {
        Integer tnId = Parsers.parseStringToInteger(parentId);
        Integer smId = parseStringToInteger(secondParentId);
        if (tnId == null || smId == null) return null;

        SeminarPruefung seminarPruefung = dto.getId() == null
                ? createNewEntity(tnId, smId)
                : loadExistingEntity(dto.getId());

        if (seminarPruefung == null) return null;

        fieldValidators.forEach(v -> v.validate(dto, null));
        setFields(seminarPruefung, dto);
        return saveIfValid(seminarPruefung, dto, changedBy);
    }

    @Override
    protected SeminarPruefungDto mapToDto(SeminarPruefung entity, SeminarPruefungDto dto) {
        if (entity == null) return null;
        dto.setId(entity.getId());
        if (entity.getBezeichnung() != null) {
            dto.setBezeichnung(entity.getBezeichnung().getName());
        }
        if (entity.getPruefungArt() != null) {
            dto.setPruefungArt(entity.getPruefungArt().getName());
        }
        if (entity.getGegenstand() != null) {
            dto.setGegenstand(entity.getGegenstand().getName());
        }
        if (entity.getNiveau() != null) {
            dto.setNiveau(entity.getNiveau().getName());
        }
        if (entity.getInstitut() != null) {
            dto.setInstitut(entity.getInstitut().getName());
        }
        dto.setAntritt(entity.getAntritt());
        if (entity.getBegruendung() != null) {
            dto.setBegruendung(entity.getBegruendung().getName());
        }
        if (entity.getErgebnisType() != null) {
            dto.setErgebnis(entity.getErgebnisType().getName());
        }
        if (entity.getErgebnisInProzent() != null) {
            dto.setErgebnisInProzent(entity.getErgebnisInProzent().toString());
        }
        if (entity.getPruefungAm() != null) {
            dto.setPruefungAm(entity.getPruefungAm().toString());
        }
        dto.setErrors(dto.getErrorsMap() != null
                ? new ArrayList<>(dto.getErrorsMap().keySet())
                : new ArrayList<>());
        return dto;
    }

    private SeminarPruefung createNewEntity(Integer teilnehmerId, Integer seminarId) {
        return teilnehmer2SeminarService.findByTeilnehmerIdAndSeminarId(teilnehmerId, seminarId)
                .map(tn2sm -> {
                    SeminarPruefung seminarPruefung = new SeminarPruefung();
                    seminarPruefung.setTeilnehmer2Seminar(tn2sm);
                    return seminarPruefung;
                }).orElse(null);
    }

    private SeminarPruefung loadExistingEntity(Integer id) {
        return seminarPruefungService.findById(id).orElse(null);
    }

    private void setFields(SeminarPruefung entity, SeminarPruefungDto dto) {
        if (isFieldValid(dto, "bezeichnung")) {
            entity.setBezeichnung(seminarPruefungBezeichnungService.findByName(dto.getBezeichnung()));
        }
        if (isFieldValid(dto, "pruefungArt")) {
            entity.setPruefungArt(seminarPruefungArtService.findByName(dto.getPruefungArt()));
        }
        String gegenstand = dto.getGegenstand();
        String niveau = dto.getNiveau();
        boolean gegenstandValid = isFieldValid(dto, "gegenstand");
        boolean niveauValid = isFieldValid(dto, "niveau");

        if ("Deutsch".equals(gegenstand)) {
            if (gegenstandValid && niveauValid) {
                entity.setGegenstand(seminarPruefungGegenstandService.findByName(gegenstand));
                entity.setNiveau(seminarPruefungNiveauService.findByName(niveau));
            }
        } else {
            if (gegenstandValid) {
                entity.setGegenstand(seminarPruefungGegenstandService.findByName(gegenstand));
            }
            if (niveauValid) {
                entity.setNiveau(seminarPruefungNiveauService.findByName(niveau));
            }
        }

        if (isFieldValid(dto, "institut")) {
            entity.setInstitut(seminarPruefungInstitutService.findByName(dto.getInstitut()));
        }

        Boolean antritt = dto.getAntritt();
        String begruendung = dto.getBegruendung();
        boolean begruendungValid = isFieldValid(dto, "begruendung");

        if (antritt == null || Boolean.TRUE.equals(antritt)) {
            entity.setAntritt(antritt);
            if (begruendungValid) {
                entity.setBegruendung(seminarPruefungBegruendungService.findByName(begruendung));
            }
        } else if (Boolean.FALSE.equals(antritt)) {
            if (begruendungValid) {
                entity.setAntritt(false);
                entity.setBegruendung(seminarPruefungBegruendungService.findByName(begruendung));
            }
        }
        if (isFieldValid(dto, "ergebnis")) {
            entity.setErgebnisType(seminarPruefungErgebnisTypeService.findByName(dto.getErgebnis()));
        }
        if (isFieldValid(dto, "ergebnisInProzent")) {
            entity.setErgebnisInProzent(parseStringToInteger(dto.getErgebnisInProzent()));
        }
        if (isFieldValid(dto, "pruefungAm")) {
            entity.setPruefungAm(parseDate(dto.getPruefungAm()));
        }
    }

    private boolean isFieldValid(SeminarPruefungDto dto, String fieldName) {
        return !dto.getErrorsMap().containsKey(fieldName);
    }

    private SeminarPruefung saveIfValid(SeminarPruefung entity, SeminarPruefungDto dto, String changedBy) {
        if (dto.getErrorsMap().isEmpty()) {
            setAuditFields(entity, changedBy);
            return seminarPruefungService.save(entity);
        }
        return entity;
    }

    private void setAuditFields(SeminarPruefung entity, String changedBy) {
        if (entity.getId() == null) {
            entity.setCreatedBy(changedBy);
            entity.setCreatedOn(getLocalDateNow());
        } else {
            entity.setChangedBy(changedBy);
            entity.setChangedOn(getLocalDateNow());
        }
    }
}
