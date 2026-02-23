package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.GeburtsdatumValidation;
import com.ibosng.validationservice.validations.GeburtsdatumValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterGeburtsdatumValidation extends AbstractValidation<StammdatenDto, Stammdaten> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        GeburtsdatumValidationResult geburtsDatum = GeburtsdatumValidation.validateGeburtsdatum(stammdatenDto.getGeburtsDatum());
        if (geburtsDatum.isValid()) {
            stammdaten.setGeburtsdatum(geburtsDatum.getDate());
            stammdaten.setLebensalter(Period.between(geburtsDatum.getDate(), LocalDate.now()).getYears());
            return true;
        }
        stammdaten.addError("geburtsDatum", geburtsDatum.getErrorMessage(), globalUserHolder.getUsername());
        return false;
    }
}
