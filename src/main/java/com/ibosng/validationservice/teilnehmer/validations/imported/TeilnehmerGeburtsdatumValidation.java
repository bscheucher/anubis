package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.GeburtsdatumValidation;
import com.ibosng.validationservice.validations.GeburtsdatumValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Optional for OEIF, MDLC and eAMS
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerGeburtsdatumValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerStaging.getGeburtsdatum())) {
            GeburtsdatumValidationResult geburtsdatum = GeburtsdatumValidation.validateGeburtsdatum(teilnehmerStaging.getGeburtsdatum());
            if (geburtsdatum.isValid()) {
                teilnehmer.setGeburtsdatum(geburtsdatum.getDate());
            } else {
                teilnehmer.addError("geburtsdatum", "Ungültiges Geburtsdatum angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        List<TeilnehmerSource> allowedSources = List.of(
                TeilnehmerSource.EAMS,
                TeilnehmerSource.VHS_EAMS,
                TeilnehmerSource.OEIF,
                TeilnehmerSource.EAMS_STANDALONE,
                TeilnehmerSource.MANUAL,
                TeilnehmerSource.SYNC_SERVICE,
                TeilnehmerSource.TN_ONBOARDING,
                TeilnehmerSource.TEILNEHMER_CSV);

        return getSources().stream().anyMatch(allowedSources::contains);
    }
}
