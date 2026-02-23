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
public class VHSOptionalAdresseValidation extends OptionalFieldValidator<TeilnehmerStaging, Teilnehmer> {
    public VHSOptionalAdresseValidation(AbstractValidation<TeilnehmerStaging, Teilnehmer> teilnehmerAdresseValidation) {
        super(teilnehmerAdresseValidation);
    }

    @Override
    public boolean isOptional() {
        return getSources().size() > 1 && TeilnehmerSource.VHS.equals(getSources().stream().findFirst().orElse(TeilnehmerSource.SYNC_SERVICE));
    }

    @Override
    public boolean setFieldsEmpty(TeilnehmerStaging objectT, Teilnehmer objectV) {
        return !(objectV.getAdresse() != null && objectV.getAdresse().getOrt() != null) ||
                (objectV.getAdresse() != null && objectV.getAdresse().getPlz() != null) ||
                (objectV.getAdresse() != null && !isNullOrBlank(objectV.getAdresse().getStrasse()));
    }
}
