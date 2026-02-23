package com.ibosng.validationservice.teilnehmer.validations.manual.teilnehmer2seminar;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungErgebnisTypeService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class GesamtbeurteilungErgebnisValidator implements FieldValidator<SeminarDto> {

    private final SeminarPruefungErgebnisTypeService seminarPruefungErgebnisTypeService;

    @Override
    public void validate(SeminarDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getGesamtbeurteilungErgebnis())) return;
        if (seminarPruefungErgebnisTypeService.findByName(dto.getGesamtbeurteilungErgebnis()) == null) {
            dto.getErrorsMap().put("gesamtbeurteilungErgebnis", "Ungültiges Ergebnis");
        }
    }
}