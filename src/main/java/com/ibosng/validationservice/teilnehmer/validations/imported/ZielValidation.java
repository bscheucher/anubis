package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ZielValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {
    @Override
    public boolean executeValidation(TeilnehmerStaging objectT, Teilnehmer objectV) {
        if (!isNullOrBlank(objectT.getErlaeuterungZiel())) {
            objectV.setZiel(objectT.getErlaeuterungZiel());
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().contains(TeilnehmerSource.TEILNEHMER_CSV);
    }
}
