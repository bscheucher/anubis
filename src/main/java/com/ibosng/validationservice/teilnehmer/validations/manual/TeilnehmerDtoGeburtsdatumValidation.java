package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.GeburtsdatumValidation;
import com.ibosng.validationservice.validations.GeburtsdatumValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoGeburtsdatumValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerDto.getGeburtsdatum())) {
            GeburtsdatumValidationResult geburtsdatum = GeburtsdatumValidation.validateGeburtsdatum(teilnehmerDto.getGeburtsdatum());
            if (geburtsdatum.isValid()) {
                teilnehmer.setGeburtsdatum(geburtsdatum.getDate());
                return true;
            } else {
                teilnehmer.addError("geburtsdatum", "Ungültiges Geburtsdatum angegeben", globalUserHolder.getUsername());
                return false;
            }
        }
        teilnehmer.addError("geburtsdatum", "Ungültiges Geburtsdatum angegeben", globalUserHolder.getUsername());
        return false;
    }
}
