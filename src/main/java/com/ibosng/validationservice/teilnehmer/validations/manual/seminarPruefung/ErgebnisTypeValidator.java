package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungErgebnisTypeService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class ErgebnisTypeValidator implements FieldValidator<SeminarPruefungDto> {

    private final SeminarPruefungErgebnisTypeService seminarPruefungErgebnisTypeService;

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getErgebnis())) return;
        if (seminarPruefungErgebnisTypeService.findByName(dto.getErgebnis()) == null) {
            dto.getErrorsMap().put("ergebnis", "Ungültiges Ergebnis");
        }
    }
}