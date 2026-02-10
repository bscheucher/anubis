package com.ibosng.validationservice.teilnehmer.validations.manual.sprachkenntnis;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.services.masterdata.MutterspracheService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class SpracheValidator implements FieldValidator<SprachkenntnisDto> {

    private final MutterspracheService mutterspracheService;

    @Override
    public void validate(SprachkenntnisDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getSprache())) return;
        if (mutterspracheService.findByName(dto.getSprache()) == null) {
            dto.getErrorsMap().put("sprache", "Ungültige Sprache");
        }
    }
}