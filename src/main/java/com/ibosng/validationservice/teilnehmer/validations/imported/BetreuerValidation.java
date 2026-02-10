package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.betreuer.Betreuer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.BetreuerService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

/**
 * Required vorname and nachname for MDLC and eAMS
 */
@Getter
@Setter
@RequiredArgsConstructor
public class BetreuerValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer2Seminar> {

    private final BetreuerService betreuerService;

    private boolean isVornameValid;
    private boolean isNachnameValid;

    @Getter
    @Setter
    private List<String> DbTitles;

    @Override
    public boolean shouldValidationRun() {
        return true;
    }

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer2Seminar teilnehmer2Seminar) {
        setVornameValid(isBetreuerStringValid(teilnehmerStaging.getBetreuerVorname(), teilnehmer2Seminar.getTeilnehmer(), "betreuerVorname"));
        setNachnameValid(isBetreuerStringValid(teilnehmerStaging.getBetreuerNachname(), teilnehmer2Seminar.getTeilnehmer(), "betreuerNachname"));

        boolean result = isVornameValid() && isNachnameValid();
        if (!isNullOrBlank(teilnehmerStaging.getBetreuerVorname()) && !isNullOrBlank(teilnehmerStaging.getBetreuerNachname())) {
            teilnehmer2Seminar.setBetreuer(getBetreuerForTeilnehmer(teilnehmer2Seminar, teilnehmerStaging));
        }
        return result;
    }

    private boolean isBetreuerStringValid(String teilnehmerString, Teilnehmer teilnehmer, String dataStatus) {
        if (!isNullOrBlank(teilnehmerString)) {
            boolean result = teilnehmerString.matches("^[\\p{L} .\\-\\']+$");
            if (!result) {
                if (dataStatus.equals("betreuerVorname")) {
                    teilnehmer.addError(dataStatus, "Ungültiger Betreuer-Vorname angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                }
                if (dataStatus.equals("betreuerNachname")) {
                    teilnehmer.addError(dataStatus, "Ungültiger Betreuer-Nachname angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                }

            }
            return result;
        }
        return true;
    }

    private Betreuer getBetreuerForTeilnehmer(Teilnehmer2Seminar teilnehmer2Seminar, TeilnehmerStaging teilnehmerStaging) {
        Betreuer betreuer = null;
        if (!isNullOrBlank(teilnehmerStaging.getBetreuerVorname()) && !isNullOrBlank(teilnehmerStaging.getBetreuerNachname())) {
            betreuer = betreuerService.findByVornameAndNachname(teilnehmerStaging.getBetreuerVorname(), teilnehmerStaging.getBetreuerNachname());
        } else if (!isNullOrBlank(teilnehmerStaging.getBetreuerVorname()) && isNullOrBlank(teilnehmerStaging.getBetreuerNachname())) {
            betreuer = betreuerService.findByVorname(teilnehmerStaging.getBetreuerVorname());
        } else if (isNullOrBlank(teilnehmerStaging.getBetreuerVorname()) && !isNullOrBlank(teilnehmerStaging.getBetreuerNachname())) {
            betreuer = betreuerService.findByNachname(teilnehmerStaging.getBetreuerNachname());
        }
        if (betreuer == null) {
            betreuer = new Betreuer();
            betreuer.setCreatedBy(VALIDATION_SERVICE);
            betreuer.setStatus(Status.ACTIVE);
        }
        if (!isNullOrBlank(teilnehmerStaging.getBetreuerTitel())) {
            betreuer.setTitel(teilnehmerStaging.getBetreuerTitel());
        }
        if (!isNullOrBlank(teilnehmerStaging.getBetreuerVorname()) && isVornameValid()) {
            betreuer.setVorname(teilnehmerStaging.getBetreuerVorname());
        }
        if (!isNullOrBlank(teilnehmerStaging.getBetreuerNachname()) && isNachnameValid()) {
            betreuer.setNachname(teilnehmerStaging.getBetreuerNachname());
        }
        return betreuerService.save(betreuer);
    }

}
