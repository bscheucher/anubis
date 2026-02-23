package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.BasePlz;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.PlzService;
import com.ibosng._config.GlobalUserHolder;
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
import static com.ibosng.validationservice.utils.ValidationHelpers.createNewAdresse;
import static com.ibosng.validationservice.utils.ValidationHelpers.findMatchingOrts;

@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeilnehmerDtoAdresseValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private static final String AUSTRIA_VORWAHL = "+43";
    private final PlzService plzService;
    private final PLZValidation plzValidation;
    private final OrtValidation ortValidation;
    private final LandService landService;
    private final ValidationHelpers validationHelpers;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean shouldValidationRun() {
        return true;
    }

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        boolean result = false;
        if (!isNullOrBlank(teilnehmerDto.getStrasse())) {
            if (!AdresseValidation.isAdresseValid(teilnehmerDto.getStrasse())) {
                teilnehmer.addError("strasse", "Ungültige Straße angegeben", globalUserHolder.getUsername());
                return false;
            }
            if (teilnehmer.getAdresse() != null) {
                if (!teilnehmer.getAdresse().getStatus().equals(Status.NEW) &&
                        !validationHelpers.isTheSameAdresse(
                                teilnehmer.getAdresse(),
                                teilnehmerDto.getStrasse(),
                                teilnehmerDto.getPlz(),
                                teilnehmerDto.getOrt())) {
                    teilnehmer.getAdresse().setStatus(Status.INACTIVE);
                    BasePlz oldPlz = teilnehmer.getAdresse().getPlz();
                    String oldOrt = teilnehmer.getAdresse().getOrt();
                    teilnehmer.setAdresse(createNewAdresse(globalUserHolder.getUsername()));
                    teilnehmer.getAdresse().setPlz(oldPlz);
                    teilnehmer.getAdresse().setOrt(oldOrt);
                } else if (teilnehmer.getAdresse().getStatus().equals(Status.NEW)) {
                    teilnehmer.getAdresse().setStatus(Status.ACTIVE);
                    teilnehmer.getAdresse().setChangedBy(globalUserHolder.getUsername());
                }
            } else {
                teilnehmer.setAdresse(createNewAdresse(globalUserHolder.getUsername()));
            }
            teilnehmer.getAdresse().setStrasse(teilnehmerDto.getStrasse());
            teilnehmer.getAdresse().setLand(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0));
            result = true;
            boolean isPlzValid = executePlzValidation(teilnehmerDto.getPlz(), teilnehmer);
            boolean isOrtValid = executeOrtValidation(teilnehmerDto.getOrt(), teilnehmer);
            isOrtValid = isOrtValid || isNullOrBlank(teilnehmerDto.getOrt());
            if (isPlzValid && isOrtValid && teilnehmer.getAdresse().getPlz() instanceof Plz) {
                List<String> orts = plzService.findOrtByPlz(((Plz) teilnehmer.getAdresse().getPlz()).getPlz());
                if (!findMatchingOrts(orts, teilnehmer.getAdresse().getOrt() != null ? teilnehmer.getAdresse().getOrt() : null).isEmpty()) {
                    String ort = orts.get(0);
                    teilnehmer.getAdresse().setOrt(ort);
                    teilnehmerDto.setOrt(ort);
                    teilnehmer.getErrors().removeIf(error -> error.getError().equals("ort"));
                } else {
                    teilnehmer.addError("plz", "Ungültige Zuordnung von PLZ und Ort", globalUserHolder.getUsername());
                    result = false;
                }
            } else {
                result = false;
            }
        } else {
            teilnehmer.addError("strasse", "Ungültige Straße angegeben", globalUserHolder.getUsername());
            checkIfPlzOrtLandEmpty(teilnehmerDto, teilnehmer);
        }
        return result;
    }

    private void checkIfPlzOrtLandEmpty(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (isNullOrBlank(teilnehmerDto.getPlz())) {
            teilnehmer.addError("plz", "Ungültige PLZ angegeben", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(teilnehmerDto.getOrt())) {
            teilnehmer.addError("ort", "Ungültiger Ort angegeben", globalUserHolder.getUsername());
        }
    }

    private boolean executePlzValidation(String plzInput, Teilnehmer teilnehmer) {
        Plz plz = plzValidation.validatePlz(plzInput);
        if (plz != null) {
            teilnehmer.getAdresse().setPlz(plz);
            return true;
        }
        teilnehmer.addError("plz", "Ungültige PLZ angegeben", globalUserHolder.getUsername());
        return false;
    }

    private boolean executeOrtValidation(String ortInput, Teilnehmer teilnehmer) {
        String ort = ortValidation.validateOrt(ortInput);
        if (ort != null) {
            teilnehmer.getAdresse().setOrt(ort);
            return true;
        }
        teilnehmer.addError("ort", "Ungültiger Ort angegeben", globalUserHolder.getUsername());
        return false;
    }
}
