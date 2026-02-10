package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.GeschlechtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource.*;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Required for OEIF, VHS, MDLC and eAMS
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerGeschlechtValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    private final GeschlechtValidation geschlechtValidation;

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerStaging.getGeschlecht())) {
            Geschlecht geschlecht = geschlechtValidation.getGeschlecht(teilnehmerStaging.getGeschlecht());
            if (geschlecht != null) {
                teilnehmer.setGeschlecht(geschlecht);
                return true;
            }
        }
        teilnehmer.addError("geschlecht", "Ungültiges Geschlecht angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        return false;
    }

    @Override
    public boolean shouldValidationRun() {
        final List<TeilnehmerSource> allowedSources = List.of(VHS,
                EAMS,
                VHS_EAMS,
                OEIF,
                EAMS_STANDALONE,
                MANUAL,
                SYNC_SERVICE,
                TN_ONBOARDING,
                TEILNEHMER_CSV
        );
        return !getSources().contains(TEILNEHMER_CSV);
    }
}