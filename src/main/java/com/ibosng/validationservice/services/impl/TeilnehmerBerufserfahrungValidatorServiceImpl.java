package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.entities.teilnehmer.TnBerufserfahrung;
import com.ibosng.dbservice.services.TeilnehmerBerufserfahrungService;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.masterdata.BerufService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.TeilnehmerBerufserfahrungValidatorService;
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
public class TeilnehmerBerufserfahrungValidatorServiceImpl extends AbstractValidator<TnBerufserfahrung, TnBerufserfahrungDto>
        implements TeilnehmerBerufserfahrungValidatorService {

    private final TeilnehmerBerufserfahrungService teilnehmerBerufserfahrungService;
    private final TeilnehmerService teilnehmerService;
    private final BerufService berufService;
    private final List<FieldValidator<TnBerufserfahrungDto>> fieldValidators;

    @Override
    public TnBerufserfahrungDto validateAndSaveTeilnehmerBerufserfahrung(TnBerufserfahrungDto dto, String changedBy, String teilnehmerId) {
        log.info("Validating Berufserfahrung for Teilnehmer {} with id {}", teilnehmerId, dto.getId());
        return validateDto(dto, teilnehmerId, changedBy, null);
    }

    @Override
    protected TnBerufserfahrung validateEntity(TnBerufserfahrungDto dto, String parentId, String changedBy, String secondParentId) {
        Integer tnId = Parsers.parseStringToInteger(parentId);
        if (tnId == null) return null;

        TnBerufserfahrung tnBerufserfahrung = dto.getId() == null
                ? createNewEntity(tnId)
                : loadExistingEntity(dto.getId());

        if (tnBerufserfahrung == null) return null;

        fieldValidators.forEach(v -> v.validate(dto, tnId));
        setFields(tnBerufserfahrung, dto);
        return saveIfValid(tnBerufserfahrung, dto, changedBy);
    }

    @Override
    protected TnBerufserfahrungDto mapToDto(TnBerufserfahrung entity, TnBerufserfahrungDto dto) {
        if (entity == null) return null;
        dto.setId(entity.getId());
        dto.setBeruf(entity.getBeruf() != null ? entity.getBeruf().getName() : null);
        dto.setDauer(entity.getDauer());
        dto.setErrors(dto.getErrorsMap() != null
                ? new ArrayList<>(dto.getErrorsMap().keySet())
                : new ArrayList<>());
        return dto;
    }

    private TnBerufserfahrung createNewEntity(Integer teilnehmerId) {
        return teilnehmerService.findById(teilnehmerId)
                .map(tn -> {
                    TnBerufserfahrung tnBerufserfahrung = new TnBerufserfahrung();
                    tnBerufserfahrung.setTeilnehmer(tn);
                    return tnBerufserfahrung;
                }).orElse(null);
    }

    private TnBerufserfahrung loadExistingEntity(Integer id) {
        return teilnehmerBerufserfahrungService.findById(id).orElse(null);
    }

    private void setFields(TnBerufserfahrung entity, TnBerufserfahrungDto dto) {
        if (isFieldValid(dto, "beruf")) {
            entity.setBeruf(berufService.findByName(dto.getBeruf()));
        }
        entity.setDauer(dto.getDauer());
    }

    private boolean isFieldValid(TnBerufserfahrungDto dto, String fieldName) {
        return !dto.getErrorsMap().containsKey(fieldName);
    }

    private TnBerufserfahrung saveIfValid(TnBerufserfahrung entity, TnBerufserfahrungDto dto, String changedBy) {
        if (dto.getErrorsMap().isEmpty()) {
            setAuditFields(entity, changedBy);
            return teilnehmerBerufserfahrungService.save(entity);
        }
        return entity;
    }

    private void setAuditFields(TnBerufserfahrung entity, String changedBy) {
        if (entity.getId() == null) {
            entity.setCreatedBy(changedBy);
            entity.setCreatedOn(getLocalDateNow());
        } else {
            entity.setChangedBy(changedBy);
            entity.setChangedOn(getLocalDateNow());
        }
    }
}
