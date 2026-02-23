package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseStringToInteger;

@Service
@RequiredArgsConstructor
public class ErgebnisInProzentValidator implements FieldValidator<SeminarPruefungDto> {

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getErgebnisInProzent())) return;
        Integer ergebnisInProzentAsInteger = parseStringToInteger(dto.getErgebnisInProzent());
        if (ergebnisInProzentAsInteger == null || ergebnisInProzentAsInteger < 0 || ergebnisInProzentAsInteger > 100) {
            dto.getErrorsMap().put("ergebnisInProzent", "Ungültiges Ergebnis in Prozent");
        }
    }
}