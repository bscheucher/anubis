package com.ibosng.validationservice.teilnehmer.validations.manual.sprachkenntnis;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.isValidDate;

@Service
@RequiredArgsConstructor
public class BewertungDatumValidator implements FieldValidator<SprachkenntnisDto> {
    @Override
    public void validate(SprachkenntnisDto dto, Integer parentId) {
        if (!isNullOrBlank(dto.getBewertungCoach()) && (isNullOrBlank(dto.getBewertungDatum()))) {
            dto.getErrorsMap().put("bewertungDatum", "Das Feld ist leer");
        }
        if (isNullOrBlank(dto.getBewertungDatum())) return;
        if (!isValidDate(dto.getBewertungDatum())) {
            dto.getErrorsMap().put("bewertungDatum", "Ungültiges Datum");
        }
    }
}