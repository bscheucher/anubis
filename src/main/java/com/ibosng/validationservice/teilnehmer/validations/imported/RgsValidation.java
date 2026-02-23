package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.RgsService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.utils.Parsers;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Required for VHS, eAMS, MDLC
 */
@RequiredArgsConstructor
public class RgsValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer2Seminar> {
    private final RgsService rgsService;

    @Override
    public boolean shouldValidationRun() {
        return true;
    }

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer2Seminar teilnehmer2Seminar) {
        if (isNullOrBlank(teilnehmerStaging.getRgs())) {
            return true;
        } else if (!teilnehmerStaging.getRgs().matches("^\\d+$")) {
            teilnehmer2Seminar.getTeilnehmer().addError("rgs", "Ungültige RGS angegeben", GlobalUserHolder.IBOSNG_BACKEND);
            return false;
        } else {
            List<Integer> allRgs = rgsService.getAllRgs();
            Integer rgs = Parsers.parseStringToInteger(teilnehmerStaging.getRgs());
            if (allRgs.contains(rgs)) {
                teilnehmer2Seminar.setRgs(rgsService.findByRgs(Parsers.parseStringToInteger(teilnehmerStaging.getRgs())));
                return true;
            }
            teilnehmer2Seminar.getTeilnehmer().addError("rgs", "Ungültige RGS angegeben", GlobalUserHolder.IBOSNG_BACKEND);
            return false;
        }
    }
}
