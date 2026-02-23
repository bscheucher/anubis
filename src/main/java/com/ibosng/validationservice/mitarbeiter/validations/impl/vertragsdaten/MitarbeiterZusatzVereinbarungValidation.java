package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.AdresseService;
import com.ibosng.dbservice.services.InternationalPlzService;
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

import static com.ibosng.validationservice.utils.Constants.AUSTRIA_VORWAHL;
import static com.ibosng.validationservice.utils.ValidationHelpers.createNewAdresse;
import static com.ibosng.validationservice.utils.ValidationHelpers.findMatchingOrts;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterZusatzVereinbarungValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    private final PLZValidation plzValidation;
    private final OrtValidation ortValidation;
    private final LandService landService;
    private final PlzService plzService;
    private final AdresseService adresseService;
    private final ValidationHelpers validationHelpers;
    private final InternationalPlzService internationalPlzService;
    private final GlobalUserHolder globalUserHolder;

    private final MitarbeiterPLZVertragsdatenValidation mitarbeiterPLZVertragsdatenValidation;
    private final MitarbeiterOrtVertragsdatenValidation mitarbeiterOrtVertragsdatenValidation;
    private final MitarbeiterForeignPlzOrtVertragsdatenValidation mitarbeiterForeignPlzOrtVertragsdatenValidation;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        vertragsdaten.setMobileWorking(vertragsdatenDto.getMobileWorking());
        Boolean weitereAdresse = vertragsdatenDto.getWeitereAdressezuHauptwohnsitz();
        if (Boolean.TRUE.equals(weitereAdresse)) {
            validateAndSetZusatzadresse(vertragsdaten, vertragsdatenDto);
            vertragsdaten.setWeitereAdressezuHauptwohnsitz(vertragsdatenDto.getWeitereAdressezuHauptwohnsitz());
        } else {
            Adresse currentAdresse = vertragsdaten.getAdresse();
            if (currentAdresse != null) {
                currentAdresse.setStatus(Status.INACTIVE);
                adresseService.save(currentAdresse);
                vertragsdaten.setAdresse(null);
            }
            vertragsdaten.setWeitereAdressezuHauptwohnsitz(false);
        }
        return true;
    }

    private void validateAndSetZusatzadresse(Vertragsdaten vertragsdaten, VertragsdatenDto vertragsdatenDto) {
        if (!isNullOrBlank(vertragsdatenDto.getStrasse())) {
            if (!AdresseValidation.isAdresseValid(vertragsdatenDto.getStrasse())) {
                vertragsdaten.addError("strasse", "Der Name der Strasse ist nicht gültig", globalUserHolder.getUsername());
                return;
            }
            if (vertragsdaten.getAdresse() != null) {
                //Check if any address changes are present and update accordingly
                boolean isAdressIdentical = validationHelpers.isTheSameAdresse(
                        vertragsdaten.getAdresse(),
                        vertragsdatenDto.getStrasse(),
                        vertragsdatenDto.getPlz(),
                        vertragsdatenDto.getOrt(),
                        vertragsdatenDto.getLand());
                // No changes, so just return
                if (!vertragsdaten.getAdresse().getStatus().equals(Status.NEW) && !isAdressIdentical) {
                    vertragsdaten.getAdresse().setStatus(Status.INACTIVE);
                    vertragsdaten.setAdresse(adresseService.save(createNewAdresse(globalUserHolder.getUsername())));
                } else if (vertragsdaten.getAdresse().getStatus().equals(Status.NEW)) {
                    vertragsdaten.getAdresse().setStatus(Status.ACTIVE);
                    vertragsdaten.getAdresse().setChangedBy(globalUserHolder.getUsername());
                }
            } else {
                vertragsdaten.setAdresse(adresseService.save(createNewAdresse(globalUserHolder.getUsername())));
            }

            if (!AdresseValidation.isAdresseValid(vertragsdatenDto.getStrasse())) {
                vertragsdaten.addError("strasse", "Der Name der Strasse ist nicht gültig", globalUserHolder.getUsername());
            } else {
                vertragsdaten.getAdresse().setStrasse(vertragsdatenDto.getStrasse());
            }
            if (!isNullOrBlank(vertragsdatenDto.getLand())) {
                Land land = landService.findByLandName(vertragsdatenDto.getLand());
                if (land != null) {
                    vertragsdaten.getAdresse().setLand(land);
                    if (!land.equals(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0))) {
                        mitarbeiterForeignPlzOrtVertragsdatenValidation.executeValidation(vertragsdaten, vertragsdatenDto);
                        return;
                    }
                }
            }
            boolean isPlzValid = mitarbeiterPLZVertragsdatenValidation.executeValidation(vertragsdatenDto, vertragsdaten);
            boolean isOrtValid = mitarbeiterOrtVertragsdatenValidation.executeValidation(vertragsdatenDto, vertragsdaten);
            isOrtValid = isOrtValid || isNullOrBlank(vertragsdatenDto.getOrt());
            if (isPlzValid && isOrtValid) {
                if (isNullOrBlank(vertragsdatenDto.getLand())) {
                    vertragsdaten.getAdresse().setLand(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0));
                    vertragsdatenDto.setLand(vertragsdaten.getAdresse().getLand().getLandName());
                }
                List<String> orts = plzService.findOrtByPlz(((Plz) vertragsdaten.getAdresse().getPlz()).getPlz());
                if (!findMatchingOrts(orts, vertragsdaten.getAdresse().getOrt() != null ? vertragsdaten.getAdresse().getOrt() : null).isEmpty()) {
                    String ort = orts.get(0);
                    vertragsdaten.getAdresse().setOrt(ort);
                    vertragsdatenDto.setOrt(ort);
                    vertragsdaten.getErrors().removeIf(status -> status.getError().equals("ort"));
                } else {
                    vertragsdaten.addError("plz", "Die Kombination zwischen Ort und PLZ existiert nicht.", globalUserHolder.getUsername());
                }
            }
        } else {
            vertragsdaten.addError("strasse", "Das Feld ist leer", globalUserHolder.getUsername());
            checkIfPlzOrtLandEmpty(vertragsdatenDto, vertragsdaten);
        }
    }

    private void checkIfPlzOrtLandEmpty(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (isNullOrBlank(vertragsdatenDto.getPlz())) {
            vertragsdaten.addError("plz", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getOrt())) {
            vertragsdaten.addError("ort", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getLand())) {
            vertragsdaten.addError("land", "Das Feld ist leer", globalUserHolder.getUsername());
        }
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}
