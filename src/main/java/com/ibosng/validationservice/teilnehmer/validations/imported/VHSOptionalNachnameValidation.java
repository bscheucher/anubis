package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.teilnehmer.validations.OptionalFieldValidator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VHSOptionalNachnameValidation extends OptionalFieldValidator<TeilnehmerStaging, Teilnehmer> {

    public VHSOptionalNachnameValidation(AbstractValidation<TeilnehmerStaging, Teilnehmer> teilnehmerNachnameValidation) {
        super(teilnehmerNachnameValidation);
    }

    @Override
    public boolean isOptional() {
        return getSources().size() > 1 && TeilnehmerSource.VHS.equals(getSources().stream().findFirst().orElse(TeilnehmerSource.SYNC_SERVICE));
    }

    @Override
    public boolean setFieldsEmpty(TeilnehmerStaging objectT, Teilnehmer objectV) {
        return !isNullOrBlank(objectV.getNachname());
    }
}
