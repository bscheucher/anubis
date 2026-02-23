package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.VornameValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoVornameValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (!VornameValidation.isVornameValid(teilnehmerDto.getVorname())) {
            teilnehmer.addError("vorname", "Ungültiger Vorname angegeben", globalUserHolder.getUsername());
            return false;
        } else {
            teilnehmer.setVorname(teilnehmerDto.getVorname());
            return true;
        }
    }
}
