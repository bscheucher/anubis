package com.ibosng.validationservice.teilnehmer.validations.manual.sprachkenntnis;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.services.masterdata.SprachkenntnisNiveauService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class SprachkenntnisNiveauValidator implements FieldValidator<SprachkenntnisDto> {

    private final SprachkenntnisNiveauService sprachkenntnisNiveauService;

    @Override
    public void validate(SprachkenntnisDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getNiveau())) return;
        if (sprachkenntnisNiveauService.findByName(dto.getNiveau()) == null) {
            dto.getErrorsMap().put("niveau", "Ungültiges Sprachniveau");
        }
    }
}