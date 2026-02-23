package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.betreuer.Betreuer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.services.BetreuerService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

@Getter
@Setter
@RequiredArgsConstructor
public class TeilnehmerDtoBetreuerValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> {

    private final BetreuerService betreuerService;
    private final GlobalUserHolder globalUserHolder;

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
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer2Seminar teilnehmer2Seminar) {
        setVornameValid(isBetreuerStringValid(teilnehmerDto.getBetreuerVorname(), teilnehmer2Seminar.getTeilnehmer(), "betreuerVorname"));
        setNachnameValid(isBetreuerStringValid(teilnehmerDto.getBetreuerNachname(), teilnehmer2Seminar.getTeilnehmer(), "betreuerNachname"));

        boolean result = isVornameValid() && isNachnameValid();
        if (!isNullOrBlank(teilnehmerDto.getBetreuerVorname()) && !isNullOrBlank(teilnehmerDto.getBetreuerNachname())) {
            teilnehmer2Seminar.setBetreuer(getBetreuerForTeilnehmer(teilnehmerDto));
        }
        return result;
    }

    private boolean isBetreuerStringValid(String teilnehmerString, Teilnehmer teilnehmer, String dataStatus) {
        if (!isNullOrBlank(teilnehmerString)) {
            boolean result = teilnehmerString.matches("^[\\p{L} .\\-\\']+$");

            if (!result) {
                if (dataStatus.equals("betreuerVorname")) {
                    teilnehmer.addError(dataStatus, "Ungültiger Betreuer-Vorname angegeben", globalUserHolder.getUsername());
                }
                if (dataStatus.equals("betreuerNachname")) {
                    teilnehmer.addError(dataStatus, "Ungültiger Betreuer-Nachname angegeben", globalUserHolder.getUsername());
                }

            }
            return result;
        }
        return true;
    }

    private Betreuer getBetreuerForTeilnehmer(TeilnehmerDto teilnehmerDto) {
        Betreuer betreuer = null;
        if (!isNullOrBlank(teilnehmerDto.getBetreuerVorname()) && !isNullOrBlank(teilnehmerDto.getBetreuerNachname())) {
            betreuer = betreuerService.findByVornameAndNachname(teilnehmerDto.getBetreuerVorname(), teilnehmerDto.getBetreuerNachname());
        } else if (!isNullOrBlank(teilnehmerDto.getBetreuerVorname()) && isNullOrBlank(teilnehmerDto.getBetreuerNachname())) {
            betreuer = betreuerService.findByVorname(teilnehmerDto.getBetreuerVorname());
        } else if (isNullOrBlank(teilnehmerDto.getBetreuerVorname()) && !isNullOrBlank(teilnehmerDto.getBetreuerNachname())) {
            betreuer = betreuerService.findByNachname(teilnehmerDto.getBetreuerNachname());
        }
        if (betreuer == null) {
            betreuer = new Betreuer();
            betreuer.setCreatedBy(VALIDATION_SERVICE);
            betreuer.setStatus(Status.ACTIVE);
            betreuer = betreuerService.save(betreuer);
        }
        if (!isNullOrBlank(teilnehmerDto.getBetreuerTitel())) {
            betreuer.setTitel(teilnehmerDto.getBetreuerTitel());
        }
        if (!isNullOrBlank(teilnehmerDto.getBetreuerVorname()) && isVornameValid()) {
            betreuer.setVorname(teilnehmerDto.getBetreuerVorname());
        }
        if (!isNullOrBlank(teilnehmerDto.getBetreuerNachname()) && isNachnameValid()) {
            betreuer.setNachname(teilnehmerDto.getBetreuerNachname());
        }
        return betreuer;
    }

}
