package com.ibosng.validationservice.teilnehmer.validations.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeilnehmerLehrjahrValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    @Override
    public boolean executeValidation(VertragsdatenDto teilnehmerVertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (teilnehmerVertragsdatenDto.getLehrjahr() == null) {
            vertragsdaten.addError("lehrjahr", "Das Feld ist leer", VALIDATION_SERVICE);
            setGehaltCalculatedFields(teilnehmerVertragsdatenDto, null);
            return false;
        }
        if (teilnehmerVertragsdatenDto.getLehrjahr() < 1 || teilnehmerVertragsdatenDto.getLehrjahr() > 4) {
            vertragsdaten.addError("lehrjahr", "Lehrjahr muss ein Wert zwischen 1 und 4 haben", VALIDATION_SERVICE);
            setGehaltCalculatedFields(teilnehmerVertragsdatenDto, null);
            return false;
        }
        if (teilnehmerVertragsdatenDto.getLehrjahr() == 1 || teilnehmerVertragsdatenDto.getLehrjahr() == 2) {
            setGehaltCalculatedFields(teilnehmerVertragsdatenDto, 1200d);
        }
        if (teilnehmerVertragsdatenDto.getLehrjahr() == 3 || teilnehmerVertragsdatenDto.getLehrjahr() == 4) {
            setGehaltCalculatedFields(teilnehmerVertragsdatenDto, 1350d);
        }
        return true;
    }

    private void setGehaltCalculatedFields(VertragsdatenDto teilnehmerVertragsdatenDto, Double value) {
        teilnehmerVertragsdatenDto.setGehaltVereinbart(value);
        teilnehmerVertragsdatenDto.setKvGehaltBerechnet(value);
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().contains(TeilnehmerSource.TN_ONBOARDING);
    }
}

