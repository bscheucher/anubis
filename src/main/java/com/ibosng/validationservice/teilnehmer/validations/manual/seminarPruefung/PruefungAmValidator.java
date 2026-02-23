package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.isValidDate;

@Service
@RequiredArgsConstructor
public class PruefungAmValidator implements FieldValidator<SeminarPruefungDto> {
    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getPruefungAm())) return;
        if (!isValidDate(dto.getPruefungAm())) {
            dto.getErrorsMap().put("pruefungAm", "Ungültiges Datum");
        }
    }
}