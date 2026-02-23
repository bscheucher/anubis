package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.teilnehmer.validations.OptionalFieldValidator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VHSOptionalTelefonValidation extends OptionalFieldValidator<TeilnehmerStaging, Teilnehmer> {

    public VHSOptionalTelefonValidation(AbstractValidation<TeilnehmerStaging, Teilnehmer> teilnehmerTelefonValidation) {
        super(teilnehmerTelefonValidation);
    }

    @Override
    public boolean isOptional() {
        return getSources().size() > 1 && TeilnehmerSource.VHS.equals(getSources().stream().findFirst().orElse(TeilnehmerSource.SYNC_SERVICE));
    }

    @Override
    public boolean setFieldsEmpty(TeilnehmerStaging objectT, Teilnehmer objectV) {
        return objectV.getTelefons() != null && !objectV.getTelefons().isEmpty();
    }
}
