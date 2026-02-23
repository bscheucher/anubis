package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotiz;
import com.ibosng.dbservice.services.TeilnehmerNotizService;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.masterdata.TeilnehmerNotizKategorieService;
import com.ibosng.dbservice.services.masterdata.TeilnehmerNotizTypeService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.TeilnehmerNotizValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.manual.AbstractValidator;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeilnehmerNotizValidatorServiceImpl extends AbstractValidator<TeilnehmerNotiz, TeilnehmerNotizDto>
        implements TeilnehmerNotizValidatorService {

    private final TeilnehmerNotizService teilnehmerNotizService;
    private final TeilnehmerNotizKategorieService teilnehmerNotizKategorieService;
    private final TeilnehmerNotizTypeService teilnehmerNotizTypeService;
    private final TeilnehmerService teilnehmerService;
    private final List<FieldValidator<TeilnehmerNotizDto>> fieldValidators;

    @Override
    public TeilnehmerNotizDto validateAndSaveTeilnehmerNotiz(TeilnehmerNotizDto dto, String changedBy, String teilnehmerId) {
        log.info("Validating TeilnehmerNotiz for Teilnehmer {} with id {}", teilnehmerId, dto.getId());
        return validateDto(dto, teilnehmerId, changedBy, null);
    }

    @Override
    protected TeilnehmerNotiz validateEntity(TeilnehmerNotizDto dto, String parentId, String changedBy, String secondParentId) {
        Integer tnId = Parsers.parseStringToInteger(parentId);
        if (tnId == null) return null;

        TeilnehmerNotiz teilnehmerNotiz = dto.getId() == null
                ? createNewEntity(tnId)
                : loadExistingEntity(dto.getId());

        if (teilnehmerNotiz == null) return null;

        fieldValidators.forEach(v -> v.validate(dto, null));
        setFields(teilnehmerNotiz, dto);
        return saveIfValid(teilnehmerNotiz, dto, changedBy);
    }

    @Override
    protected TeilnehmerNotizDto mapToDto(TeilnehmerNotiz entity, TeilnehmerNotizDto dto) {
        if (entity == null) return null;
        dto.setId(entity.getId());
        dto.setNotiz(entity.getNotiz());

        if (entity.getKategorie() != null) {
            dto.setKategorie(entity.getKategorie().getName());
        }
        if (entity.getType() != null) {
            dto.setType(entity.getType().getName());
        }
        if (entity.getCreatedOn() != null) {
            dto.setCreatedOn(entity.getCreatedOn().toLocalDate().toString());
        }
        if (entity.getCreatedBy() != null) {
            dto.setCreatedBy(entity.getCreatedBy());
        }
        dto.setErrors(dto.getErrorsMap() != null
                ? new ArrayList<>(dto.getErrorsMap().keySet())
                : new ArrayList<>());
        return dto;
    }

    private TeilnehmerNotiz createNewEntity(Integer teilnehmerId) {
        return teilnehmerService.findById(teilnehmerId)
                .map(tn -> {
                    TeilnehmerNotiz teilnehmerNotiz = new TeilnehmerNotiz();
                    teilnehmerNotiz.setTeilnehmer(tn);
                    return teilnehmerNotiz;
                }).orElse(null);
    }

    private TeilnehmerNotiz loadExistingEntity(Integer id) {
        return teilnehmerNotizService.findById(id).orElse(null);
    }

    private void setFields(TeilnehmerNotiz entity, TeilnehmerNotizDto dto) {
        if (isFieldValid(dto, "kategorie")) {
            entity.setKategorie(teilnehmerNotizKategorieService.findByName(dto.getKategorie()));
        }
        if (isFieldValid(dto, "type")) {
            entity.setType(teilnehmerNotizTypeService.findByName(dto.getType()));
        }
        entity.setNotiz(dto.getNotiz());
    }

    private boolean isFieldValid(TeilnehmerNotizDto dto, String fieldName) {
        return !dto.getErrorsMap().containsKey(fieldName);
    }

    private TeilnehmerNotiz saveIfValid(TeilnehmerNotiz entity, TeilnehmerNotizDto dto, String changedBy) {
        if (dto.getErrorsMap().isEmpty()) {
            setAuditFields(entity, changedBy);
            return teilnehmerNotizService.save(entity);
        }
        return entity;
    }

    private void setAuditFields(TeilnehmerNotiz entity, String changedBy) {
        if (entity.getId() == null) {
            entity.setCreatedBy(changedBy);
            entity.setCreatedOn(getLocalDateNow());
        } else {
            entity.setChangedBy(changedBy);
            entity.setChangedOn(getLocalDateNow());
        }
    }
}
