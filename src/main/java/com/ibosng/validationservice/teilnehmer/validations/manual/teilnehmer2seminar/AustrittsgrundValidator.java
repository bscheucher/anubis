package com.ibosng.validationservice.teilnehmer.validations.manual.teilnehmer2seminar;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.services.masterdata.SeminarAustrittsgrundService;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class AustrittsgrundValidator implements FieldValidator<SeminarDto> {

    private final SeminarAustrittsgrundService seminarAustrittsgrundService;

    @Override
    public void validate(SeminarDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getAustrittsgrund())) return;
        if (seminarAustrittsgrundService.findByName(dto.getAustrittsgrund()) == null) {
            dto.getErrorsMap().put("austrittsgrund", "Ungültiger Austrittsgrund");
        }
    }
}