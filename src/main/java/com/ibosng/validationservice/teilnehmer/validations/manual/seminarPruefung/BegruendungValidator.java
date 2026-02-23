package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungBegruendungService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class BegruendungValidator implements FieldValidator<SeminarPruefungDto> {

    private final SeminarPruefungBegruendungService seminarPruefungBegruendungService;

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (Boolean.FALSE.equals(dto.getAntritt()) && (isNullOrBlank(dto.getBegruendung()))) {
            dto.getErrorsMap().put("begruendung", "Das Feld ist leer");
        }
        if (isNullOrBlank(dto.getBegruendung())) return;
        if (seminarPruefungBegruendungService.findByName(dto.getBegruendung()) == null) {
            dto.getErrorsMap().put("begruendung", "Ungültige Begründung");
        }
    }
}