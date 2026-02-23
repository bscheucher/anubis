package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.services.LandService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.OrtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.ValidationHelpers.capitalizeFirstLetter;
import static com.ibosng.validationservice.utils.ValidationHelpers.createNewAdresse;

@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeilnehmerDtoUrsprungValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final LandService landService;
    private final GlobalUserHolder globalUserHolder;
    private final OrtValidation ortValidation;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        String ursprungsland = trimAndCapitalize(teilnehmerDto.getUrsprungsland());
        String geburtsort = trimAndCapitalize(teilnehmerDto.getGeburtsort());

        Land land = null;
        boolean keepExistingLand = false;

        if (!isNullOrBlank(ursprungsland)) {
            land = landService.findByLandName(ursprungsland);
            if (land == null) {
                teilnehmer.addError("ursprungsland", "Ungültiges Ursprungsland", globalUserHolder.getUsername());
                keepExistingLand = true;
            }
        }

        updateUrsprung(teilnehmer, land, geburtsort, teilnehmerDto, keepExistingLand);
        return true;
    }

    private void updateUrsprung(Teilnehmer teilnehmer, Land land, String geburtsort, TeilnehmerDto teilnehmerDto, boolean keepExistingLand) {
        if (teilnehmer.getUrsprung() == null) {
            teilnehmer.setUrsprung(createNewAdresse(globalUserHolder.getUsername()));
        } else {
            updateExistingUrsprung(teilnehmer, land, geburtsort, keepExistingLand);
        }

        updateUrsprungDetails(teilnehmer.getUrsprung(), land, geburtsort, teilnehmerDto, keepExistingLand);
    }

    private void updateExistingUrsprung(Teilnehmer teilnehmer, Land land, String geburtsort, boolean keepExistingLand) {
        Adresse ursprung = teilnehmer.getUrsprung();
        if (isLandChanged(ursprung, land, keepExistingLand) || isOrtChanged(ursprung, geburtsort)) {
            ursprung.setStatus(Status.INACTIVE);
            teilnehmer.setUrsprung(createNewAdresse(globalUserHolder.getUsername()));
        }
    }

    private void updateUrsprungDetails(Adresse ursprung, Land land, String geburtsort, TeilnehmerDto teilnehmerDto, boolean keepExistingLand) {
        if (!keepExistingLand) {
            ursprung.setLand(land);
        }
        ursprung.setOrt(validateGeburtsort(geburtsort, teilnehmerDto));
    }

    private boolean isLandChanged(Adresse ursprung, Land land, boolean keepExistingLand) {
        if (keepExistingLand) return false;
        return ursprung.getLand() == null ? land != null : !ursprung.getLand().equals(land);
    }

    private boolean isOrtChanged(Adresse ursprung, String geburtsort) {
        return !isNullOrBlank(geburtsort) && !geburtsort.equals(ursprung.getOrt());
    }

    private String validateGeburtsort(String geburtsort, TeilnehmerDto teilnehmerDto) {
        if (isNullOrBlank(geburtsort)) {
            return null;
        }
        String validatedOrt = ortValidation.validateOrt(geburtsort);
        teilnehmerDto.setGeburtsort(validatedOrt != null ? validatedOrt : geburtsort);
        return teilnehmerDto.getGeburtsort();
    }

    private String trimAndCapitalize(String input) {
        return isNullOrBlank(input) ? null : capitalizeFirstLetter(input.trim());
    }
}