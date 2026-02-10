package com.ibosng.validationservice.teilnehmer.validations.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterECardValidation;
import com.ibosng.validationservice.teilnehmer.validations.OptionalFieldValidator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OptionalEcardValidation extends OptionalFieldValidator<StammdatenDto, Stammdaten> {

    public OptionalEcardValidation(MitarbeiterECardValidation validator) {
        super(validator);
    }

    @Override
    public boolean setFieldsEmpty(StammdatenDto objectT, Stammdaten objectV) {
        if (isNullOrBlank(objectT.getEcard())) {
            objectV.setEcardStatus(BlobStatus.NONE);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOptional() {
        return getSources().contains(TeilnehmerSource.TN_ONBOARDING);
    }
}
