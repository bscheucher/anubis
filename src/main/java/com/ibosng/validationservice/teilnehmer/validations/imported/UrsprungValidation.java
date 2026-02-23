package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.LandService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.OrtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbibosservice.utils.Helpers.isNullOrBlank;
import static com.ibosng.validationservice.utils.ValidationHelpers.*;

@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UrsprungValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    private final LandService landService;

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        //TODO validation
        String ursprungsland = capitalizeFirstLetter(teilnehmerStaging.getUrsprungsland());
        String geburtsort = trimAndCapitalize(teilnehmerStaging.getGerburtsort());

        Land land = isNullOrBlank(ursprungsland) ? null : landService.findByLandName(ursprungsland);
        updateUrsprung(teilnehmer, land, geburtsort, teilnehmerStaging);
        return true;
    }

    private void updateUrsprung(Teilnehmer teilnehmer, Land land, String geburtsort, TeilnehmerStaging teilnehmerStaging) {
        if (teilnehmer.getUrsprung() != null) {
            handleExistingUrsprung(teilnehmer, teilnehmer.getUrsprung(), land, geburtsort);
        } else {
            teilnehmer.setUrsprung(createNewAdresse(GlobalUserHolder.IBOSNG_BACKEND));
            teilnehmer.getUrsprung().setLand(land);
        }

        if (teilnehmer.getUrsprung() != null) {
            updateUrsprungDetails(teilnehmer.getUrsprung(), land, geburtsort, teilnehmer, teilnehmerStaging);
        }
    }

    private void handleExistingUrsprung(Teilnehmer teilnehmer, Adresse ursprung, Land land, String geburtsort) {
        if (!ursprung.getStatus().equals(Status.NEW)) {
            boolean landChanged = ((ursprung.getLand() == null && land != null) || (ursprung.getLand() != null && (land == null || !ursprung.getLand().equals(land))));
            boolean ortChanged = (!isNullOrBlank(geburtsort) && !geburtsort.equals(ursprung.getOrt()));

            if (landChanged || ortChanged) {
                ursprung.setStatus(Status.INACTIVE);
                teilnehmer.setUrsprung(createNewAdresse(GlobalUserHolder.IBOSNG_BACKEND));
            }
        } else {
            ursprung.setStatus(Status.ACTIVE);
            ursprung.setChangedBy(GlobalUserHolder.IBOSNG_BACKEND);
        }
    }

    private void updateUrsprungDetails(Adresse ursprung, Land land, String geburtsort, Teilnehmer teilnehmer, TeilnehmerStaging teilnehmerStaging) {
        ursprung.setLand(land);

        if (!isNullOrBlank(geburtsort)) {
            teilnehmer.getUrsprung().setOrt(geburtsort);
        } else {
            ursprung.setOrt(null);
        }
    }

    private String trimAndCapitalize(String input) {
        return isNullOrBlank(input) ? null : capitalizeFirstLetter(input.trim());
    }


    @Override
    public boolean shouldValidationRun() {
        return (getSources().size() == 1 && getSources().contains(TeilnehmerSource.TEILNEHMER_CSV)) || getSources().size() > 1;
    }
}
