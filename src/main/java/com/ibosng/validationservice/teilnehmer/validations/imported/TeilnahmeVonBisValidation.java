package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;

import static com.ibosng.dbservice.utils.Parsers.*;

/**
 * Optional for MDLC and eAMS
 */
public class TeilnahmeVonBisValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer2Seminar> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer2Seminar teilnehmer2Seminar) {
        if (!isNullOrBlank(teilnehmerStaging.getTeilnahmeVon())) {
            if (isValidDate(teilnehmerStaging.getTeilnahmeVon())) {
                teilnehmer2Seminar.setTeilnahmeVon(parseDate(teilnehmerStaging.getTeilnahmeVon()));
            }
        }
        if (!isNullOrBlank(teilnehmerStaging.getTeilnahmeBis())) {
            if (isValidDate(teilnehmerStaging.getTeilnahmeBis())) {
                teilnehmer2Seminar.setTeilnahmeBis(parseDate(teilnehmerStaging.getTeilnahmeBis()));
            }
        }
        return true;
    }
}
