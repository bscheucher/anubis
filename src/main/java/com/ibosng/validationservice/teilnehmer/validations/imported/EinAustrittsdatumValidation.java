package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Parsers.extractDate;

/**
 * Optional for MDLC and eAMS
 */
@RequiredArgsConstructor
public class EinAustrittsdatumValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer2Seminar> {

    @Override
    public boolean shouldValidationRun() {
        List<TeilnehmerSource> allowedSources = List.of(
                TeilnehmerSource.VHS,
                TeilnehmerSource.EAMS,
                TeilnehmerSource.VHS_EAMS,
                TeilnehmerSource.EAMS_STANDALONE,
                TeilnehmerSource.MANUAL,
                TeilnehmerSource.SYNC_SERVICE,
                TeilnehmerSource.TN_ONBOARDING,
                TeilnehmerSource.OEIF);

        return getSources().stream().anyMatch(allowedSources::contains);
    }

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer2Seminar teilnehmer2Seminar) {
        if (getSources().contains(TeilnehmerSource.VHS)) {
            if (!isNullOrBlank(teilnehmerStaging.getAnmerkung())) {
                LocalDate date = extractDate(teilnehmerStaging.getAnmerkung(), true);
                if (date != null) {
                    teilnehmer2Seminar.setEintritt(date);
                }
            }
            return true;
        } else {
            String eintritt = null;
            if (!isNullOrBlank(teilnehmerStaging.getAnmerkung())) {
                LocalDate date = extractDate(teilnehmerStaging.getAnmerkung(), false);
                if (date != null) {
                    teilnehmer2Seminar.setEintritt(date);
                    eintritt = teilnehmerStaging.getAnmerkung();
                }
            } else {
                if (!isNullOrBlank(teilnehmerStaging.getEintritt())) {
                    if (!isValidDate(teilnehmerStaging.getEintritt())) {
                        teilnehmer2Seminar.getTeilnehmer().addError("eintritt", "Ungültiger Eintritt angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                        return false;
                    } else {
                        teilnehmer2Seminar.setEintritt(parseDate(teilnehmerStaging.getEintritt()));
                        eintritt = teilnehmerStaging.getEintritt();
                    }
                }
            }
            if (!isNullOrBlank(teilnehmerStaging.getAustritt())) {
                if (!isValidDate(teilnehmerStaging.getAustritt())) {
                    teilnehmer2Seminar.getTeilnehmer().addError("austritt", "Ungültiger Austritt angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                    return false;
                } else {
                    teilnehmer2Seminar.setAustritt(parseDate(teilnehmerStaging.getAustritt()));
                }
            }

            if (!isNullOrBlank(eintritt) && !isNullOrBlank(teilnehmerStaging.getAustritt())) {
                LocalDate eintrittsdatum = parseDate(eintritt);
                LocalDate austrittsdatum = parseDate(teilnehmerStaging.getAustritt());
                if (austrittsdatum != null && eintrittsdatum != null && austrittsdatum.isBefore(eintrittsdatum)) {
                    teilnehmer2Seminar.getTeilnehmer().addError("eintritt", "Ungültiger Eintritt angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                    teilnehmer2Seminar.getTeilnehmer().addError("austritt", "Ungültiger Austritt angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                    return false;
                }
            }
            return true;
        }
    }
}
