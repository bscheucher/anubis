package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungBezeichnungService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class BezeichnungValidator implements FieldValidator<SeminarPruefungDto> {

    private final SeminarPruefungBezeichnungService seminarPruefungBezeichnungService;

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getBezeichnung())) {
            dto.getErrorsMap().put("bezeichnung", "Das Feld ist leer");
        } else if (seminarPruefungBezeichnungService.findByName(dto.getBezeichnung()) == null) {
            dto.getErrorsMap().put("bezeichnung", "Ungültige Bezeichnung");
        }
    }
}