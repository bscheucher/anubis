package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungGegenstandService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class GegenstandValidator implements FieldValidator<SeminarPruefungDto> {

    private final SeminarPruefungGegenstandService seminarPruefungGegenstandService;

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getGegenstand())) {
            dto.getErrorsMap().put("gegenstand", "Das Feld ist leer");
        } else if (seminarPruefungGegenstandService.findByName(dto.getGegenstand()) == null) {
            dto.getErrorsMap().put("gegenstand", "Ungültiger PrüfungGegenstand");
        }
    }
}