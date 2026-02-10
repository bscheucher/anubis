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
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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
public class MitarbeiterAbweichendeAdresseValidation implements Validation<StammdatenDto, Stammdaten> {
    private final PlzService plzService;
    private final LandService landService;
    private final AdresseService adresseService;
    private final ValidationHelpers validationHelpers;

    private final MitarbeiterAbweichendeOrtValidation mitarbeiterAbweichendeOrtValidation;
    private final MitarbeiterAbweichendePLZValidation mitarbeiterAbweichendePLZValidation;
    private final MitarbeiterForeignAbweichendePlzOrtValidation mitarbeiterForeignAbweichendePlzOrtValidation;

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        boolean result = true;
        if (!isNullOrBlank(stammdatenDto.getAStrasse())) {
            if (!AdresseValidation.isAdresseValid(stammdatenDto.getAStrasse())) {
                stammdaten.addError("astrasse", "Der Name der Strasse ist nicht gültig", globalUserHolder.getUsername());
                return false;
            }
            if (stammdaten.getAbweichendeAdresse() != null) {
                if (!stammdaten.getAbweichendeAdresse().getStatus().equals(Status.NEW) &&
                        !validationHelpers.isTheSameAdresse(
                                stammdaten.getAbweichendeAdresse(),
                                stammdatenDto.getAStrasse(),
                                stammdatenDto.getAPlz(),
                                stammdatenDto.getAOrt(),
                                stammdatenDto.getALand())) {
                    stammdaten.getAbweichendeAdresse().setStatus(Status.INACTIVE);
                    stammdaten.setAbweichendeAdresse(adresseService.save(createNewAdresse(globalUserHolder.getUsername())));
                } else if (stammdaten.getAbweichendeAdresse().getStatus().equals(Status.NEW)) {
                    stammdaten.getAbweichendeAdresse().setStatus(Status.ACTIVE);
                    stammdaten.getAbweichendeAdresse().setChangedBy(globalUserHolder.getUsername());
                }
            } else {
                stammdaten.setAbweichendeAdresse(adresseService.save(createNewAdresse(globalUserHolder.getUsername())));
            }

            if (StringUtils.isBlank(stammdatenDto.getAStrasse())) {
                stammdaten.getAbweichendeAdresse().setStrasse(null);
            } else {
                stammdaten.getAbweichendeAdresse().setStrasse(stammdatenDto.getAStrasse());
            }
            if (StringUtils.isBlank(stammdatenDto.getAOrt())) {
                stammdaten.getAbweichendeAdresse().setOrt(null);
            }
            if (StringUtils.isBlank(stammdatenDto.getAPlz())) {
                stammdaten.getAbweichendeAdresse().setPlz(null);
            }
            if (StringUtils.isBlank(stammdatenDto.getALand())) {
                stammdaten.getAbweichendeAdresse().setLand(null);
            } else {
                stammdaten.getAbweichendeAdresse().setLand(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0));
            }

            Land land = landService.findByLandName(stammdatenDto.getALand());
            if (land != null) {
                stammdaten.getAbweichendeAdresse().setLand(land);
                if (!land.equals(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0))) {
                    return mitarbeiterForeignAbweichendePlzOrtValidation.executeValidation(stammdatenDto, stammdaten);
                }
            }
            boolean isPlzValid = mitarbeiterAbweichendePLZValidation.executeValidation(stammdatenDto, stammdaten);
            boolean isOrtValid = mitarbeiterAbweichendeOrtValidation.executeValidation(stammdatenDto, stammdaten);
            isOrtValid = isOrtValid || isNullOrBlank(stammdatenDto.getAOrt());
            if (isPlzValid && isOrtValid && Objects.nonNull(stammdaten.getAbweichendeAdresse().getPlz())) {
                List<String> orts = plzService.findOrtByPlz(((Plz) stammdaten.getAbweichendeAdresse().getPlz()).getPlz());
                if (!findMatchingOrts(orts, stammdaten.getAbweichendeAdresse().getOrt() != null ? stammdaten.getAbweichendeAdresse().getOrt() : null).isEmpty()) {
                    String ort = orts.get(0);
                    stammdaten.getAbweichendeAdresse().setOrt(ort);
                    stammdatenDto.setAOrt(ort);
                    stammdaten.getErrors().removeIf(status -> status.getError().equals("aort"));
                } else {
                    stammdaten.addError("aplz", "Die Kombination zwischen Ort und PLZ existiert nicht.", globalUserHolder.getUsername());
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }
}
