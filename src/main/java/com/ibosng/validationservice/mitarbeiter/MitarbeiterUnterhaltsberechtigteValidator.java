package com.ibosng.validationservice.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.UnterhaltsberechtigteDataStatus;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten.*;
import com.ibosng.validationservice.services.UnterhaltsberechtigteValidatorService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterUnterhaltsberechtigteValidator extends SingleEntityAbstractValidator<UnterhaltsberechtigteDto, Unterhaltsberechtigte> {

    private final UnterhaltsberechtigteValidatorService unterhaltsberechtigteValidatorService;

    private final MitarbeiterUBVornameValidation mitarbeiterUBVornameValidation;
    private final MitarbeiterUBNachnameValidation mitarbeiterUBNachnameValidation;
    private final MitarbeiterUBSVNValidation mitarbeiterUBSVNValidation;
    private final MitarbeiterUBGeburtsdatumValidation mitarbeiterUBGeburtsdatumValidation;
    private final MitarbeiterUBVerwandtschaftValidation mitarbeiterUBVerwandtschaftValidation;

    public MitarbeiterUnterhaltsberechtigteValidator(BaseService<Unterhaltsberechtigte> baseServiceV,
                                                     UnterhaltsberechtigteValidatorService unterhaltsberechtigteValidatorService,
                                                     MitarbeiterUBVornameValidation mitarbeiterUBVornameValidation,
                                                     MitarbeiterUBNachnameValidation mitarbeiterUBNachnameValidation,
                                                     MitarbeiterUBSVNValidation mitarbeiterUBSVNValidation,
                                                     MitarbeiterUBGeburtsdatumValidation mitarbeiterUBGeburtsdatumValidation,
                                                     MitarbeiterUBVerwandtschaftValidation mitarbeiterUBVerwandtschaftValidation) {
        super(baseServiceV);
        this.unterhaltsberechtigteValidatorService = unterhaltsberechtigteValidatorService;
        this.mitarbeiterUBVornameValidation = mitarbeiterUBVornameValidation;
        this.mitarbeiterUBNachnameValidation = mitarbeiterUBNachnameValidation;
        this.mitarbeiterUBSVNValidation = mitarbeiterUBSVNValidation;
        this.mitarbeiterUBGeburtsdatumValidation = mitarbeiterUBGeburtsdatumValidation;
        this.mitarbeiterUBVerwandtschaftValidation = mitarbeiterUBVerwandtschaftValidation;
    }

    @Override
    protected void prepare() {
        addValidation(mitarbeiterUBVornameValidation);
        addValidation(mitarbeiterUBNachnameValidation);
        addValidation(mitarbeiterUBSVNValidation);
        addValidation(mitarbeiterUBGeburtsdatumValidation);
        addValidation(mitarbeiterUBVerwandtschaftValidation);
    }

    @Override
    protected void executeValidations() {
        for (UnterhaltsberechtigteDto object : this.objectsToValidate) {
            Unterhaltsberechtigte validatedObject = unterhaltsberechtigteValidatorService.getUnterhaltsberechtige(object);
            if(validatedObject != null) {
                boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
                validatedObject = baseServiceV.save(validatedObject);
                validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
            }
        }
    }

    @Override
    protected List<UnterhaltsberechtigteDto> postProcess() {
        List<UnterhaltsberechtigteDto> processedUnterhaltsberechtigte = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<UnterhaltsberechtigteDto, Unterhaltsberechtigte>, Boolean> entry : validationResults.entrySet()) {
            Unterhaltsberechtigte unterhaltsberechtigte = entry.getKey().getSecond();
            UnterhaltsberechtigteDto unterhaltsberechtigteDto = entry.getKey().getFirst();
            for(UnterhaltsberechtigteDataStatus dataStatus : unterhaltsberechtigte.getErrors()) {
                unterhaltsberechtigteDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
            }
            unterhaltsberechtigteDto.setErrors(new ArrayList<>(unterhaltsberechtigteDto.getErrorsMap().keySet()));
            if (entry.getValue()) {
                setStatusAndSaveUnterhaltsberechtigte(unterhaltsberechtigte, unterhaltsberechtigteDto, MitarbeiterStatus.VALIDATED);
            } else {
                baseServiceV.deleteById(unterhaltsberechtigte.getId());
            }
            processedUnterhaltsberechtigte.add(unterhaltsberechtigteDto);
        }
        return processedUnterhaltsberechtigte;
    }

    private void setStatusAndSaveUnterhaltsberechtigte(Unterhaltsberechtigte unterhaltsberechtigte, UnterhaltsberechtigteDto unterhaltsberechtigteDto, MitarbeiterStatus status) {
        unterhaltsberechtigte.setStatus(status);
        unterhaltsberechtigte.setChangedBy(getChangedBy());
        unterhaltsberechtigte.setChangedOn(getLocalDateNow());
        Unterhaltsberechtigte savedUnterhaltsberechtigte = baseServiceV.save(unterhaltsberechtigte);
        unterhaltsberechtigteDto.setId(savedUnterhaltsberechtigte.getId());
    }
}
