package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeminarDtoZusaetzlicheUnterstuetzungValidation extends AbstractValidation<SeminarDto, Teilnehmer> {

    @Override
    public boolean executeValidation(SeminarDto seminarDto, Teilnehmer teilnehmer) {
        Teilnehmer2Seminar teilnehmer2Seminar = teilnehmer.getTeilnehmerSeminars().stream()
                .filter(t2s -> t2s.getSeminar() != null && t2s.getSeminar().getId().equals(seminarDto.getId()))
                .findFirst()
                .orElse(null);
        if (teilnehmer2Seminar != null) {
            teilnehmer2Seminar.setZusaetzlicheUnterstuetzung(seminarDto.getZusaetzlicheUnterstuetzung());
            return true;
        }
        return false;
    }
}
