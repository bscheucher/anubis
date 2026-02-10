package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.entities.teilnehmer.TnZertifikat;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.TeilnehmerZertifikatService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.TeilnehmerZertifikatValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.manual.AbstractValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeilnehmerZertifikatValidatorServiceImpl extends AbstractValidator<TnZertifikat, TnZertifikatDto>
        implements TeilnehmerZertifikatValidatorService {
    private final TeilnehmerService teilnehmerService;
    private final TeilnehmerZertifikatService teilnehmerZertifikatService;

    @Override
    public TnZertifikatDto validateAndSaveZertifikat(TnZertifikatDto dto, String changedBy, String teilnehmerId) {
        log.info("Validating Zertifikat for Teilnehmer {} with id {}", teilnehmerId, dto.getId());
        return validateDto(dto, teilnehmerId, changedBy, null);
    }

    @Override
    protected TnZertifikat validateEntity(TnZertifikatDto dto, String parentId, String changedBy, String secondParentId) {
        Integer tnId = Parsers.parseStringToInteger(parentId);
        if (tnId == null) return null;

        TnZertifikat tnZertifikat = dto.getId() == null
                ? createNewEntity(tnId)
                : loadExistingEntity(dto.getId());

        if (tnZertifikat == null) return null;

        setFields(tnZertifikat, dto);
        return saveIfValid(tnZertifikat, changedBy);
    }

    @Override
    protected TnZertifikatDto mapToDto(TnZertifikat entity, TnZertifikatDto dto) {
        if (entity == null) return null;
        dto.setBezeichnung(entity.getBezeichnung());
        dto.setId(entity.getId());
        return dto;
    }

    private TnZertifikat createNewEntity(Integer teilnehmerId) {
        return teilnehmerService.findById(teilnehmerId)
                .map(tn -> {
                    TnZertifikat tnZertifikat = new TnZertifikat();
                    tnZertifikat.setTeilnehmer(tn);
                    return tnZertifikat;
                }).orElse(null);
    }

    private TnZertifikat loadExistingEntity(Integer id) {
        return teilnehmerZertifikatService.findById(id).orElse(null);
    }

    private void setFields(TnZertifikat entity, TnZertifikatDto dto) {
        entity.setBezeichnung(dto.getBezeichnung());
    }

    private TnZertifikat saveIfValid(TnZertifikat entity, String changedBy) {
        if (entity.getId() == null) {
            entity.setCreatedBy(changedBy);
            entity.setCreatedOn(getLocalDateNow());
        } else {
            entity.setChangedBy(changedBy);
            entity.setChangedOn(getLocalDateNow());
        }
        return teilnehmerZertifikatService.save(entity);
    }
}
