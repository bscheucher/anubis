package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.AdresseService;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.PlzService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.utils.ValidationHelpers;
import com.ibosng.validationservice.validations.AdresseValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.validationservice.utils.Constants.AUSTRIA_VORWAHL;
import static com.ibosng.validationservice.utils.ValidationHelpers.createNewAdresse;
import static com.ibosng.validationservice.utils.ValidationHelpers.findMatchingOrts;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Required for VHS, MDLC and eAMS
 */

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterAdresseValidation implements Validation<StammdatenDto, Stammdaten> {

    private final PlzService plzService;
    private final LandService landService;
    private final AdresseService adresseService;
    private final ValidationHelpers validationHelpers;
    private final MitarbeiterOrtValidation mitarbeiterOrtValidation;
    private final MitarbeiterForeignPlzOrtValidation mitarbeiterForeignPlzOrtValidation;
    private final MitarbeiterPLZValidation mitarbeiterPLZValidation;

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        boolean result = false;
        if (!isNullOrBlank(stammdatenDto.getStrasse())) {
            if (!AdresseValidation.isAdresseValid(stammdatenDto.getStrasse())) {
                stammdaten.addError("strasse", "Der Name der Strasse ist nicht gültig", globalUserHolder.getUsername());
                return false;
            }
            if (stammdaten.getAdresse() != null) {
                if (!stammdaten.getAdresse().getStatus().equals(Status.NEW) && !validationHelpers.isTheSameAdresse(
                        stammdaten.getAdresse(),
                        stammdatenDto.getStrasse(),
                        stammdatenDto.getPlz(),
                        stammdatenDto.getOrt(),
                        stammdatenDto.getLand())) {
                    stammdaten.getAdresse().setStatus(Status.INACTIVE);
                    stammdaten.setAdresse(adresseService.save(createNewAdresse(globalUserHolder.getUsername())));
                } else if (stammdaten.getAdresse().getStatus().equals(Status.NEW)) {
                    stammdaten.getAdresse().setStatus(Status.ACTIVE);
                    stammdaten.getAdresse().setChangedBy(globalUserHolder.getUsername());
                }
            } else {
                stammdaten.setAdresse(adresseService.save(createNewAdresse(globalUserHolder.getUsername())));
            }
            stammdaten.getAdresse().setStrasse(stammdatenDto.getStrasse());

            result = true;
            if (!isNullOrBlank(stammdatenDto.getLand())) {
                Land land = landService.findByLandName(stammdatenDto.getLand());
                if (land != null) {
                    stammdaten.getAdresse().setLand(land);
                    if (!land.equals(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0))) {
                        return mitarbeiterForeignPlzOrtValidation.executeValidation(stammdatenDto, stammdaten);
                    }
                }
            }

            boolean isPlzValid = mitarbeiterPLZValidation.executeValidation(stammdatenDto, stammdaten);
            boolean isOrtValid = mitarbeiterOrtValidation.executeValidation(stammdatenDto, stammdaten);
            isOrtValid = isOrtValid || isNullOrBlank(stammdatenDto.getOrt());
            if (isPlzValid && isOrtValid) {
                if (isNullOrBlank(stammdatenDto.getLand())) {
                    stammdaten.getAdresse().setLand(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0));
                    stammdatenDto.setLand(stammdaten.getAdresse().getLand().getLandName());
                }
                List<String> orts = plzService.findOrtByPlz(((Plz) stammdaten.getAdresse().getPlz()).getPlz());
                if (!findMatchingOrts(orts, stammdaten.getAdresse().getOrt() != null ? stammdaten.getAdresse().getOrt() : null).isEmpty()) {
                    String ort = orts.get(0);
                    stammdaten.getAdresse().setOrt(ort);
                    stammdatenDto.setOrt(ort);
                    stammdaten.getErrors().removeIf(status -> status.getError().equals("ort"));
                } else {
                    stammdaten.addError("plz", "Die Kombination zwischen Ort und PLZ existiert nicht.", globalUserHolder.getUsername());
                    result = false;
                }
            } else {
                result = false;
            }
        } else {
            stammdaten.addError("strasse", "Das Feld ist leer", globalUserHolder.getUsername());
            checkIfPlzOrtLandEmpty(stammdatenDto, stammdaten);
        }
        return result;
    }

    private void checkIfPlzOrtLandEmpty(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (isNullOrBlank(stammdatenDto.getPlz())) {
            stammdaten.addError("plz", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(stammdatenDto.getOrt())) {
            stammdaten.addError("ort", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(stammdatenDto.getLand())) {
            stammdaten.addError("land", "Das Feld ist leer", globalUserHolder.getUsername());
        }
    }

}
