package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.OrtValidation;
import lombok.RequiredArgsConstructor;

/**
 * Required for VHS, eAMS, MDLC
 */
@RequiredArgsConstructor
public class TeilnehmerOrtValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {
    private final OrtValidation ortValidation;

    @Override
    public boolean shouldValidationRun() {
        return !getSources().contains(TeilnehmerSource.OEIF);
    }

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        String ort = ortValidation.validateOrt(teilnehmerStaging.getOrt());
        if (ort != null) {
            return true;
        }
        teilnehmer.addError("ort", "Ungültiger Ort angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        return false;
    }

}
