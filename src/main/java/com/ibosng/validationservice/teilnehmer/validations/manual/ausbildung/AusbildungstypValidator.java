package com.ibosng.validationservice.teilnehmer.validations.manual.ausbildung;

import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.services.masterdata.TeilnehmerAusbildungTypeService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class AusbildungstypValidator implements FieldValidator<TnAusbildungDto> {

    private final TeilnehmerAusbildungTypeService teilnehmerAusbildungTypeService;

    @Override
    public void validate(TnAusbildungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getAusbildungstyp())) return;
        if (teilnehmerAusbildungTypeService.findByName(dto.getAusbildungstyp()) == null) {
            dto.getErrorsMap().put("ausbildungstyp", "Ungültiger Ausbildungstyp");
        }
    }
}