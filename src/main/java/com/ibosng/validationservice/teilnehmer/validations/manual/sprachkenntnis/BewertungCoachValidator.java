package com.ibosng.validationservice.teilnehmer.validations.manual.sprachkenntnis;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.services.masterdata.SprachkenntnisNiveauService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class BewertungCoachValidator implements FieldValidator<SprachkenntnisDto> {

    private final SprachkenntnisNiveauService sprachkenntnisNiveauService;

    @Override
    public void validate(SprachkenntnisDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getBewertungCoach())) return;
        if (sprachkenntnisNiveauService.findByName(dto.getBewertungCoach()) == null) {
            dto.getErrorsMap().put("bewertungCoach", "Ungültige Bewertung");
        }
    }
}