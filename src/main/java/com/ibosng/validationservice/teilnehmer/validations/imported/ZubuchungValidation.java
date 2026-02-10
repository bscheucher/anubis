package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;

import static com.ibosng.dbservice.utils.Parsers.*;

/**
 * Optional for eAMS, MDLC
 */
@RequiredArgsConstructor
public class ZubuchungValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer2Seminar> {

    //ToDO: ENABLE VALIDATION!!!
    @Override
    public boolean shouldValidationRun() {
        return true;
    }

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer2Seminar teilnehmer2Seminar) {
        boolean result = true;
        if (!isNullOrBlank(teilnehmerStaging.getZubuchung())) {
            result = isValidDate(teilnehmerStaging.getZubuchung());
            if (!result) {
                teilnehmer2Seminar.getTeilnehmer().addError("zubuchung", "Ungültige Zubuchung angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                return false; // Either date is invalid
            } else {
                teilnehmer2Seminar.setZubuchung(parseDate(teilnehmerStaging.getZubuchung()));
            }
        }
        return result;
    }
}
