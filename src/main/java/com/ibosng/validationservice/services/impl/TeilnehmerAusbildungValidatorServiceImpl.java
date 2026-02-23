package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.entities.teilnehmer.TnAusbildung;
import com.ibosng.dbservice.services.TeilnehmerAusbildungService;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.masterdata.TeilnehmerAusbildungTypeService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.TeilnehmerAusbildungValidatorService;
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
public class TeilnehmerAusbildungValidatorServiceImpl extends AbstractValidator<TnAusbildung, TnAusbildungDto>
        implements TeilnehmerAusbildungValidatorService {

    private final TeilnehmerAusbildungService teilnehmerAusbildungService;
    private final TeilnehmerAusbildungTypeService teilnehmerAusbildungTypeService;
    private final TeilnehmerService teilnehmerService;
    private final List<FieldValidator<TnAusbildungDto>> fieldValidators;

    @Override
    public TnAusbildungDto validateAndSaveTeilnehmerAusbildung(TnAusbildungDto dto, String changedBy, String teilnehmerId) {
        log.info("Validating Ausbildung for Teilnehmer {} with id {}", teilnehmerId, dto.getId());
        return validateDto(dto, teilnehmerId, changedBy, null);
    }

    @Override
    protected TnAusbildung validateEntity(TnAusbildungDto dto, String parentId, String changedBy, String secondParentId) {
        Integer tnId = Parsers.parseStringToInteger(parentId);
        if (tnId == null) return null;

        TnAusbildung ausbildung = dto.getId() == null
                ? createNewEntity(tnId)
                : loadExistingEntity(dto.getId());

        if (ausbildung == null) return null;

        fieldValidators.forEach(v -> v.validate(dto, null));
        setFields(ausbildung, dto);
        return saveIfValid(ausbildung, dto, changedBy);
    }

    @Override
    protected TnAusbildungDto mapToDto(TnAusbildung entity, TnAusbildungDto dto) {
        if (entity == null) return null;
        dto.setId(entity.getId());
        if (entity.getAusbildungType() != null) {
            dto.setAusbildungstyp(entity.getAusbildungType().getName());
        }
        dto.setHoechsterAbschluss(entity.getHoechsterAbschluss());
        dto.setErkanntInAt(entity.getErkanntInAt());
        dto.setErrors(dto.getErrorsMap() != null
                ? new ArrayList<>(dto.getErrorsMap().keySet())
                : new ArrayList<>());
        return dto;
    }

    private TnAusbildung createNewEntity(Integer teilnehmerId) {
        return teilnehmerService.findById(teilnehmerId)
                .map(tn -> {
                    TnAusbildung tnAusbildung = new TnAusbildung();
                    tnAusbildung.setTeilnehmer(tn);
                    return tnAusbildung;
                }).orElse(null);
    }

    private TnAusbildung loadExistingEntity(Integer id) {
        return teilnehmerAusbildungService.findById(id).orElse(null);
    }

    private void setFields(TnAusbildung entity, TnAusbildungDto dto) {
        if (isFieldValid(dto, "ausbildungstyp")) {
            entity.setAusbildungType(teilnehmerAusbildungTypeService.findByName(dto.getAusbildungstyp()));
        }
        entity.setHoechsterAbschluss(dto.isHoechsterAbschluss());
        entity.setErkanntInAt(dto.isErkanntInAt());
    }

    private boolean isFieldValid(TnAusbildungDto dto, String fieldName) {
        return !dto.getErrorsMap().containsKey(fieldName);
    }

    private TnAusbildung saveIfValid(TnAusbildung entity, TnAusbildungDto dto, String changedBy) {
        if (dto.getErrorsMap().isEmpty()) {
            setAuditFields(entity, changedBy);
            return teilnehmerAusbildungService.save(entity);
        }
        return entity;
    }

    private void setAuditFields(TnAusbildung entity, String changedBy) {
        if (entity.getId() == null) {
            entity.setCreatedBy(changedBy);
            entity.setCreatedOn(getLocalDateNow());
        } else {
            entity.setChangedBy(changedBy);
            entity.setChangedOn(getLocalDateNow());
        }
    }
}
