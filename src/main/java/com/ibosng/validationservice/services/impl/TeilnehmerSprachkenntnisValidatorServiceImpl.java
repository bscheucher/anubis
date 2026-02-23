package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.entities.Sprachkenntnis;
import com.ibosng.dbservice.services.SprachkenntnisService;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.masterdata.MutterspracheService;
import com.ibosng.dbservice.services.masterdata.SprachkenntnisNiveauService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.TeilnehmerSprachkenntnisValidatorService;
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
public class TeilnehmerSprachkenntnisValidatorServiceImpl extends AbstractValidator<Sprachkenntnis, SprachkenntnisDto>
        implements TeilnehmerSprachkenntnisValidatorService {
    private final TeilnehmerService teilnehmerService;
    private final SprachkenntnisNiveauService sprachkenntnisNiveauService;
    private final SprachkenntnisService sprachkenntnisService;
    private final MutterspracheService mutterspracheService;
    private final List<FieldValidator<SprachkenntnisDto>> fieldValidators;

    @Override
    public SprachkenntnisDto validateAndSaveSprachkenntnis(SprachkenntnisDto dto, String teilnehmerId, String changedBy) {
        log.info("Validating Sprachkenntnis for Teilnehmer {} with id {}", teilnehmerId, dto.getId());
        return validateDto(dto, teilnehmerId, changedBy, null);
    }

    @Override
    protected Sprachkenntnis validateEntity(SprachkenntnisDto dto, String parentId, String changedBy, String secondParentId) {
        Integer tnId = Parsers.parseStringToInteger(parentId);
        if (tnId == null) return null;

        Sprachkenntnis sprachkenntnis = dto.getId() == null
                ? createNewEntity(tnId)
                : loadExistingEntity(dto.getId());

        if (sprachkenntnis == null) return null;

        fieldValidators.forEach(v -> v.validate(dto, null));
        setFields(sprachkenntnis, dto);
        return saveIfValid(sprachkenntnis, dto, changedBy);
    }

    @Override
    protected SprachkenntnisDto mapToDto(Sprachkenntnis entity, SprachkenntnisDto dto) {
        if (entity == null) return null;
        dto.setId(entity.getId());
        if (entity.getSprache() != null) {
            dto.setSprache(entity.getSprache().getName());
        }

        if (entity.getSpracheNiveau() != null) {
            dto.setNiveau(entity.getSpracheNiveau().getName());
        }

        if (entity.getBewertungCoach() != null) {
            dto.setBewertungCoach(entity.getBewertungCoach().getName());
        }

        if (entity.getBewertungDatum() != null) {
            dto.setBewertungDatum(entity.getBewertungDatum().toString());
        }
        dto.setErrors(dto.getErrorsMap() != null
                ? new ArrayList<>(dto.getErrorsMap().keySet())
                : new ArrayList<>());
        return dto;
    }

    private Sprachkenntnis createNewEntity(Integer teilnehmerId) {
        return teilnehmerService.findById(teilnehmerId)
                .map(tn -> {
                    Sprachkenntnis sk = new Sprachkenntnis();
                    sk.setTeilnehmer(tn);
                    return sk;
                }).orElse(null);
    }

    private Sprachkenntnis loadExistingEntity(Integer id) {
        return sprachkenntnisService.findById(id).orElse(null);
    }

    private void setFields(Sprachkenntnis entity, SprachkenntnisDto dto) {
        if (isFieldValid(dto, "sprache")) {
            entity.setSprache(mutterspracheService.findByName(dto.getSprache()));
        }
        if (isFieldValid(dto, "niveau")) {
            entity.setSpracheNiveau(sprachkenntnisNiveauService.findByName(dto.getNiveau()));
        }

        String bewertungCoach = dto.getBewertungCoach();
        String bewertungDatum = dto.getBewertungDatum();
        boolean coachValid = isFieldValid(dto, "bewertungCoach");
        boolean datumValid = isFieldValid(dto, "bewertungDatum");

        if (!isNullOrBlank(bewertungCoach)) {
            if (coachValid && datumValid) {
                entity.setBewertungCoach(sprachkenntnisNiveauService.findByName(bewertungCoach));
                entity.setBewertungDatum(parseDate(bewertungDatum));
            }
        } else {
            if (coachValid) {
                entity.setBewertungCoach(sprachkenntnisNiveauService.findByName(bewertungCoach));
            }
            if (datumValid) {
                entity.setBewertungDatum(parseDate(bewertungDatum));
            }
        }
    }

    private boolean isFieldValid(SprachkenntnisDto dto, String fieldName) {
        return !dto.getErrorsMap().containsKey(fieldName);
    }

    private Sprachkenntnis saveIfValid(Sprachkenntnis entity, SprachkenntnisDto dto, String changedBy) {
        if (dto.getErrorsMap().isEmpty()) {
            setAuditFields(entity, changedBy);
            return sprachkenntnisService.save(entity);
        }
        return entity;
    }

    private void setAuditFields(Sprachkenntnis entity, String changedBy) {
        if (entity.getId() == null) {
            entity.setCreatedBy(changedBy);
            entity.setCreatedOn(getLocalDateNow());
        } else {
            entity.setChangedBy(changedBy);
            entity.setChangedOn(getLocalDateNow());
        }
    }

}
