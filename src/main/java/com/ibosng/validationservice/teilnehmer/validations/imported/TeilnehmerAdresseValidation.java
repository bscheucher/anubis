package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.PlzService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.utils.ValidationHelpers;
import com.ibosng.validationservice.validations.AdresseValidation;
import com.ibosng.validationservice.validations.OrtValidation;
import com.ibosng.validationservice.validations.PLZValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseStringToInteger;
import static com.ibosng.validationservice.utils.ValidationHelpers.createNewAdresse;
import static com.ibosng.validationservice.utils.ValidationHelpers.findMatchingOrts;

/**
 * Required for VHS, MDLC and eAMS
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerAdresseValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    private static final String AUSTRIA_VORWAHL = "+43";
    private final PlzService plzService;
    private final PLZValidation plzValidation;
    private final OrtValidation ortValidation;
    private final LandService landService;
    private final ValidationHelpers validationHelpers;

    @Override
    public boolean shouldValidationRun() {
        if (getSources().size() == 1) {
            return getSources().stream().noneMatch(source -> List.of(TeilnehmerSource.OEIF, TeilnehmerSource.TEILNEHMER_CSV).contains(source))
                    || getSources().contains(TeilnehmerSource.MANUAL);
        }
        return true;
    }

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        boolean result = false;
        if (!isNullOrBlank(teilnehmerStaging.getStrasse())) {
            if (!AdresseValidation.isAdresseValid(teilnehmerStaging.getStrasse())) {
                teilnehmer.addError("strasse", "Ungültige Straße angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                return false;
            }
            checkForNewAdress(teilnehmerStaging, teilnehmer);
            teilnehmer.getAdresse().setLand(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0));
            result = checkPlzOrt(teilnehmerStaging, teilnehmer, result, false);

        } else {
            teilnehmer.addError("strasse", "Ungültige Straße angegeben", GlobalUserHolder.IBOSNG_BACKEND);
            checkIfPlzOrtLandEmpty(teilnehmerStaging, teilnehmer);
//            return true;
        }
        return result;
    }

    private void checkForNewAdress(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (teilnehmer.getAdresse() != null) {
            if (!teilnehmer.getAdresse().getStatus().equals(Status.NEW) && !validationHelpers.isTheSameAdresse(
                    teilnehmer.getAdresse(),
                    teilnehmerStaging.getStrasse(),
                    teilnehmerStaging.getPlz(),
                    teilnehmerStaging.getOrt(),
                    teilnehmerStaging.getLandesvorwahl())) {
                teilnehmer.getAdresse().setStatus(Status.INACTIVE);
                teilnehmer.setAdresse((createNewAdresse(GlobalUserHolder.IBOSNG_BACKEND)));
            } else if (teilnehmer.getAdresse().getStatus().equals(Status.NEW)) {
                teilnehmer.getAdresse().setStatus(Status.ACTIVE);
                teilnehmer.getAdresse().setChangedBy(GlobalUserHolder.IBOSNG_BACKEND);
            }
        } else {
            teilnehmer.setAdresse(createNewAdresse(GlobalUserHolder.IBOSNG_BACKEND));
        }
    }

    private void checkIfPlzOrtLandEmpty(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (isNullOrBlank(teilnehmerStaging.getPlz())) {
            teilnehmer.addError("plz", "Ungültige PLZ angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        } else {
            checkPlzOrt(teilnehmerStaging, teilnehmer, true, true);
        }
        if (isNullOrBlank(teilnehmerStaging.getOrt())) {
            teilnehmer.addError("ort", "Ungültiger Ort angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        } else {
            checkPlzOrt(teilnehmerStaging, teilnehmer, true, true);
        }
    }

    private boolean checkPlzOrt(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer, Boolean result, Boolean isStrasseInvalid) {
        if (TeilnehmerSource.VHS.equals(getSources().stream().findFirst().orElse(TeilnehmerSource.EAMS))) {
            if (!AdresseValidation.isAdresseValid(teilnehmerStaging.getStrasse())) {
                teilnehmer.addError("strasse", "Ungültige Straße angegeben", GlobalUserHolder.IBOSNG_BACKEND);
            } else {
                checkForNewAdress(teilnehmerStaging, teilnehmer);
                teilnehmer.getAdresse().setStrasse(teilnehmerStaging.getStrasse());
            }
        }
        boolean isPlzValid = new TeilnehmerPlzValidation(plzValidation).executeValidation(teilnehmerStaging, teilnehmer);
        boolean isOrtValid = new TeilnehmerOrtValidation(ortValidation).executeValidation(teilnehmerStaging, teilnehmer);
        isOrtValid = isOrtValid || isNullOrBlank(teilnehmerStaging.getOrt());
        if (isPlzValid && isOrtValid) {
            List<String> orts = plzService.findOrtByPlz(parseStringToInteger(teilnehmerStaging.getPlz()));
            if (!findMatchingOrts(orts, teilnehmerStaging.getOrt() != null ? teilnehmerStaging.getOrt() : null).isEmpty()) {
                List<String> matchingOrts = findMatchingOrts(orts, teilnehmerStaging.getOrt() != null ? teilnehmerStaging.getOrt() : null);
                String ort = matchingOrts.get(0);
                teilnehmer.getAdresse().setOrt(ort);
                Plz plz = plzValidation.validatePlz(teilnehmerStaging.getPlz());
                if (plz != null) {
                    teilnehmer.getAdresse().setPlz(plz);
                }
                teilnehmer.getErrors().removeIf(error -> error.getError().equals("ort"));
                teilnehmer.getErrors().removeIf(error -> error.getError().equals("plz"));
                if (!isStrasseInvalid) {
                    teilnehmer.getAdresse().setStrasse(teilnehmerStaging.getStrasse());
                }
                result = true;
            } else {
                teilnehmer.addError("plz", "Ungültige Zuordnung von PLZ und Ort", GlobalUserHolder.IBOSNG_BACKEND);
                teilnehmer.addError("ort", "Ungültige Zuordnung von PLZ und Ort", GlobalUserHolder.IBOSNG_BACKEND);
                result = false;
            }
        } else {
            teilnehmer.addError("plz", "Ungültige Zuordnung von PLZ und Ort", GlobalUserHolder.IBOSNG_BACKEND);
            teilnehmer.addError("ort", "Ungültige Zuordnung von PLZ und Ort", GlobalUserHolder.IBOSNG_BACKEND);
            result = false;
        }
        return result;
    }

}
