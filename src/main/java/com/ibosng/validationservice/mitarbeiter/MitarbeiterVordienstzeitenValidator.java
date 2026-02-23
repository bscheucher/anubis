package com.ibosng.validationservice.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VordienstzeitenDataStatus;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.dbservice.services.masterdata.VertragsartService;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten.MitarbeiterVordienstzeitenValidation;
import com.ibosng.validationservice.services.VordienstzeitenValidatorService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterVordienstzeitenValidator extends SingleEntityAbstractValidator<VordienstzeitenDto, Vordienstzeiten> {

    private final VordienstzeitenValidatorService vordienstzeitenValidatorService;
    private final MitarbeiterVordienstzeitenValidation mitarbeiterVordienstzeitenValidation;

    public MitarbeiterVordienstzeitenValidator(BaseService<Vordienstzeiten> baseServiceV, VordienstzeitenValidatorService vordienstzeitenValidatorService, VertragsartService vertragsartService, MitarbeiterVordienstzeitenValidation mitarbeiterVordienstzeitenValidation) {
        super(baseServiceV);
        this.vordienstzeitenValidatorService = vordienstzeitenValidatorService;
        this.mitarbeiterVordienstzeitenValidation = mitarbeiterVordienstzeitenValidation;
    }

    @Override
    protected void prepare() {
        addValidation(mitarbeiterVordienstzeitenValidation);
    }

    @Override
    protected void executeValidations() {
        for (VordienstzeitenDto object : this.objectsToValidate) {
            Vordienstzeiten validatedObject = vordienstzeitenValidatorService.getVordienstzeiten(object);
            if (validatedObject != null) {
                boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
                validatedObject = baseServiceV.save(validatedObject);
                validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
            }
        }
    }

    @Override
    protected List<VordienstzeitenDto> postProcess() {
        List<VordienstzeitenDto> processedVordienstzeiten = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<VordienstzeitenDto, Vordienstzeiten>, Boolean> entry : validationResults.entrySet()) {
            Vordienstzeiten vordienstzeiten = entry.getKey().getSecond();
            VordienstzeitenDto vordienstzeitenDto = entry.getKey().getFirst();
            for (VordienstzeitenDataStatus dataStatus : vordienstzeiten.getErrors()) {
                vordienstzeitenDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
            }
            vordienstzeitenDto.setErrors(new ArrayList<>(vordienstzeitenDto.getErrorsMap().keySet()));
            if (entry.getValue()) {
                setStatusAndSaveVordienstzeiten(vordienstzeiten, vordienstzeitenDto, MitarbeiterStatus.VALIDATED);
            } else {
                baseServiceV.deleteById(vordienstzeiten.getId());
            }
            processedVordienstzeiten.add(vordienstzeitenDto);
        }
        return processedVordienstzeiten;
    }

    private void setStatusAndSaveVordienstzeiten(Vordienstzeiten vordienstzeiten, VordienstzeitenDto vordienstzeitenDto, MitarbeiterStatus status) {
        vordienstzeiten.setStatus(status);
        vordienstzeiten.setChangedBy(getChangedBy());
        vordienstzeiten.setChangedOn(getLocalDateNow());
        Vordienstzeiten savedVordienstzeiten = baseServiceV.save(vordienstzeiten);
        vordienstzeitenDto.setId(savedVordienstzeiten.getId());
    }
}
