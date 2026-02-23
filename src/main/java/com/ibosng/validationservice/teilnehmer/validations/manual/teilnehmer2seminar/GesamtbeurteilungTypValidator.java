package com.ibosng.validationservice.teilnehmer.validations.manual.teilnehmer2seminar;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungGegenstandService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class GesamtbeurteilungTypValidator implements FieldValidator<SeminarDto> {

    private final SeminarPruefungGegenstandService seminarPruefungGegenstandService;

    @Override
    public void validate(SeminarDto dto, Integer parentId) {
        if (!isNullOrBlank(dto.getGesamtbeurteilungErgebnis()) && isNullOrBlank(dto.getGesamtbeurteilungTyp())) {
            dto.getErrorsMap().put("gesamtbeurteilungTyp", "Das Feld ist leer");
        }
        if (isNullOrBlank(dto.getGesamtbeurteilungTyp())) return;
        if (seminarPruefungGegenstandService.findByName(dto.getGesamtbeurteilungTyp()) == null) {
            dto.getErrorsMap().put("gesamtbeurteilungTyp", "Ungültiger Typ");
        }
    }
}