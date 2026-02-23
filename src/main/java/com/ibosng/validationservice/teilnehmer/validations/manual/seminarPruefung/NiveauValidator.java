package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungNiveauService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class NiveauValidator implements FieldValidator<SeminarPruefungDto> {

    private final SeminarPruefungNiveauService seminarPruefungNiveauService;

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if ("Deutsch".equals(dto.getGegenstand()) && isNullOrBlank(dto.getNiveau())) {
            dto.getErrorsMap().put("niveau", "Das Feld ist leer");
        }
        if (isNullOrBlank(dto.getNiveau())) return;
        if (seminarPruefungNiveauService.findByName(dto.getNiveau()) == null) {
            dto.getErrorsMap().put("niveau", "Ungültiges Prüfungsniveau");
        }
    }
}