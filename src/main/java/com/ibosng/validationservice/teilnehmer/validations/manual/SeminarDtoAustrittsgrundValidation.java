package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.entities.seminar.SeminarAustrittsgrund;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.services.masterdata.SeminarAustrittsgrundService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeminarDtoAustrittsgrundValidation extends AbstractValidation<SeminarDto, Teilnehmer> {

    private final SeminarAustrittsgrundService seminarAustrittsgrundService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(SeminarDto seminarDto, Teilnehmer teilnehmer) {
        Teilnehmer2Seminar teilnehmer2Seminar = teilnehmer.getTeilnehmerSeminars().stream()
                .filter(t2s -> t2s.getSeminar() != null && t2s.getSeminar().getId().equals(seminarDto.getId()))
                .findFirst()
                .orElse(null);
        if (teilnehmer2Seminar != null) {
            if (!isNullOrBlank(seminarDto.getAustrittsgrund())) {
                SeminarAustrittsgrund seminarAustrittsgrund = seminarAustrittsgrundService.findByName(seminarDto.getAustrittsgrund());
                if (seminarAustrittsgrund == null) {
                    teilnehmer.addError("austrittsgrund", "Ungültiger Austrittsgrund", globalUserHolder.getUsername());
                    return false;
                }
                teilnehmer2Seminar.setAustrittsgrund(seminarAustrittsgrund);
            } else {
                teilnehmer2Seminar.setAustrittsgrund(null);
            }
            return true;
        }
        return false;
    }
}
