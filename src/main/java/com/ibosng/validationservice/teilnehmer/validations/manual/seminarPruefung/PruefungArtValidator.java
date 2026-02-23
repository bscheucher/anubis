package com.ibosng.validationservice.teilnehmer.validations.manual.seminarPruefung;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungArtService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class PruefungArtValidator implements FieldValidator<SeminarPruefungDto> {

    private final SeminarPruefungArtService seminarPruefungArtService;

    @Override
    public void validate(SeminarPruefungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getPruefungArt())) {
            dto.getErrorsMap().put("pruefungArt", "Das Feld ist leer");
        } else if (seminarPruefungArtService.findByName(dto.getPruefungArt()) == null) {
            dto.getErrorsMap().put("pruefungArt", "Ungültige PrüfungArt");
        }
    }
}