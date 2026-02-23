package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Optional for eAMS, MDLC
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeilnehmerTitel2Validation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        return validateTitel2(teilnehmerStaging.getTitel2(), teilnehmer);
    }

    public boolean validateTitel2(String inputTitle, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(inputTitle)) {
            teilnehmer.setTitel2(inputTitle);
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        List<TeilnehmerSource> allowedSources = List.of(
                TeilnehmerSource.SYNC_SERVICE);
        return getSources().stream().anyMatch(allowedSources::contains) || getSources().size()> 1;
    }
}
