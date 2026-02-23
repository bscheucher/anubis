package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.GeburtsdatumAndSVNValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Required for VHS, eAMS, MDLC
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerGeburtsdatumAndSVNValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (teilnehmer.getErrors().stream()
                .noneMatch(error -> "svNummer".equals(error.getError())) && teilnehmer.getErrors().stream()
                .noneMatch(error -> "geburtsdatum".equals(error.getError()))) {
            boolean geburtsDatumAndSVNMatch = GeburtsdatumAndSVNValidation.validateGeburtsdatumAndSVN(teilnehmerStaging.getSvNummer(), teilnehmer.getGeburtsdatum() != null ? teilnehmer.getGeburtsdatum() : null);
            if (!geburtsDatumAndSVNMatch) {
                teilnehmer.addError("svNummer", "Ungültige SV-Nummer angegeben", GlobalUserHolder.IBOSNG_BACKEND);
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
