package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungInstitutService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class InstitutValidator implements FieldValidator<SeminarPruefungDto> {

    private final SeminarPruefungInstitutService seminarPruefungInstitutService;

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getInstitut())) return;
        if (seminarPruefungInstitutService.findByName(dto.getInstitut()) == null) {
            dto.getErrorsMap().put("institut", "Ungültiges Institut");
        }
    }
}