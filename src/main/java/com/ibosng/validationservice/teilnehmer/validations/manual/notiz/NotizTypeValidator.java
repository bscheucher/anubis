package com.ibosng.validationservice.teilnehmer.validations.manual.notiz;

import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.services.masterdata.TeilnehmerNotizTypeService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class NotizTypeValidator implements FieldValidator<TeilnehmerNotizDto> {

    private final TeilnehmerNotizTypeService teilnehmerNotizTypeService;

    @Override
    public void validate(TeilnehmerNotizDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getType())) {
            dto.getErrorsMap().put("type", "Das Feld ist leer");
        } else if (teilnehmerNotizTypeService.findByName(dto.getType()) == null) {
            dto.getErrorsMap().put("type", "Ungültiger Notiztyp");
        }
    }
}