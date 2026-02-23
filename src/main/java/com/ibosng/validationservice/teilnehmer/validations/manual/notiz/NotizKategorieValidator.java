package com.ibosng.validationservice.teilnehmer.validations.manual.notiz;

import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.services.masterdata.TeilnehmerNotizKategorieService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class NotizKategorieValidator implements FieldValidator<TeilnehmerNotizDto> {

    private final TeilnehmerNotizKategorieService teilnehmerNotizKategorieService;

    @Override
    public void validate(TeilnehmerNotizDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getKategorie())) {
            dto.getErrorsMap().put("kategorie", "Das Feld ist leer");
        } else if (teilnehmerNotizKategorieService.findByName(dto.getKategorie()) == null) {
            dto.getErrorsMap().put("kategorie", "Ungültige Notizkategorie");
        }
    }
}