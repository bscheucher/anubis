package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;

import static com.ibosng.dbservice.utils.Parsers.*;

/**
 * Optional for MDLC and eAMS
 */
@RequiredArgsConstructor
public class GeplantValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer2Seminar> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer2Seminar teilnehmer2Seminar) {
        boolean result = true;
        if (!isNullOrBlank(teilnehmerStaging.getGeplant())) {
            result = isValidDate(teilnehmerStaging.getGeplant());
            if (!result) {
                teilnehmer2Seminar.getTeilnehmer().addError("geplant", "Ungültige Planung angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                return false; // Either date is invalid
            } else {
                teilnehmer2Seminar.setGeplant(parseDate(teilnehmerStaging.getGeplant()));
            }
        }
        return result;
    }
}
