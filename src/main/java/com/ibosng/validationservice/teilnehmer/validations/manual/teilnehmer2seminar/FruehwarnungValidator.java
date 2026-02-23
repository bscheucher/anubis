package com.ibosng.validationservice.teilnehmer.validations.manual.teilnehmer2seminar;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.isValidDate;

@Service
@RequiredArgsConstructor
public class FruehwarnungValidator implements FieldValidator<SeminarDto> {
    @Override
    public void validate(SeminarDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getFruehwarnung())) return;
        if (!isValidDate(dto.getFruehwarnung())) {
            dto.getErrorsMap().put("fruehwarnung", "Ungültiges Datum");
        }
    }
}