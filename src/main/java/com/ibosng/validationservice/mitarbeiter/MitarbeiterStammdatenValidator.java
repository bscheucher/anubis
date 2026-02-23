package com.ibosng.validationservice.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.StammdatenDataStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterAbweichendeAdresseValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterAdresseValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterArbeitsgenehmigungValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterBankDatenValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterEmailValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterFamilienstandValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterFotoValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterGeburtsnameValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterGeschlechtValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterMobilnummerValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterMutterspracheValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterNachnameValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterSVNValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterStaatsbuergerschaftValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterTitelValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterVornameValidation;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterZusatzInfoValidation;
import com.ibosng.validationservice.services.StammdatenValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.teilnehmer.validations.stammdaten.OptionalAnredeValidation;
import com.ibosng.validationservice.teilnehmer.validations.stammdaten.OptionalEcardValidation;
import com.ibosng.validationservice.teilnehmer.validations.stammdaten.OptionalGeburtsdatumValidation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterStammdatenValidator extends SingleEntityAbstractValidator<StammdatenDto, Stammdaten> {

    private final MitarbeiterEmailValidation mitarbeiterEmailValidation;
    private final OptionalAnredeValidation optionalAnredeValidation;
    private final MitarbeiterVornameValidation mitarbeiterVornameValidation;
    private final MitarbeiterNachnameValidation mitarbeiterNachnameValidation;
    private final MitarbeiterGeburtsnameValidation mitarbeiterGeburtsnameValidation;
    private final MitarbeiterFamilienstandValidation mitarbeiterFamilienstandValidation;
    private final MitarbeiterTitelValidation mitarbeiterTitelValidation;
    private final MitarbeiterGeschlechtValidation mitarbeiterGeschlechtValidation;
    private final OptionalGeburtsdatumValidation optionalGeburtsdatumValidation;
    private final MitarbeiterSVNValidation mitarbeiterSVNValidation;
    private final MitarbeiterStaatsbuergerschaftValidation mitarbeiterStaatsbuergerschaftValidation;
    private final MitarbeiterMutterspracheValidation mitarbeiterMutterspracheValidation;
    private final MitarbeiterAdresseValidation mitarbeiterAdresseValidation;
    private final MitarbeiterAbweichendeAdresseValidation mitarbeiterAbweichendeAdresseValidation;
    private final MitarbeiterMobilnummerValidation mitarbeiterMobilnummerValidation;
    private final MitarbeiterBankDatenValidation mitarbeiterBankDatenValidation;
    private final MitarbeiterArbeitsgenehmigungValidation mitarbeiterArbeitsgenehmigungValidation;
    private final OptionalEcardValidation optionalEcardValidation;
    private final MitarbeiterFotoValidation mitarbeiterFotoValidation;
    private final MitarbeiterZusatzInfoValidation mitarbeiterZusatzInfoValidation;

    private final StammdatenValidatorService stammdatenValidatorService;
    private final PersonalnummerService personalnummerService;

    private boolean isTeilnehmer;

    public MitarbeiterStammdatenValidator(BaseService<Stammdaten> baseServiceV,
                                          MitarbeiterEmailValidation mitarbeiterEmailValidation,
                                          OptionalAnredeValidation optionalAnredeValidation,
                                          MitarbeiterVornameValidation mitarbeiterVornameValidation,
                                          MitarbeiterNachnameValidation mitarbeiterNachnameValidation,
                                          MitarbeiterGeburtsnameValidation mitarbeiterGeburtsnameValidation,
                                          MitarbeiterFamilienstandValidation mitarbeiterFamilienstandValidation,
                                          MitarbeiterTitelValidation mitarbeiterTitelValidation,
                                          MitarbeiterGeschlechtValidation mitarbeiterGeschlechtValidation,
                                          OptionalGeburtsdatumValidation optionalGeburtsdatumValidation,
                                          MitarbeiterSVNValidation mitarbeiterSVNValidation,
                                          MitarbeiterStaatsbuergerschaftValidation mitarbeiterStaatsbuergerschaftValidation,
                                          MitarbeiterMutterspracheValidation mitarbeiterMutterspracheValidation,
                                          MitarbeiterAdresseValidation mitarbeiterAdresseValidation,
                                          MitarbeiterAbweichendeAdresseValidation mitarbeiterAbweichendeAdresseValidation,
                                          MitarbeiterMobilnummerValidation mitarbeiterMobilnummerValidation,
                                          MitarbeiterBankDatenValidation mitarbeiterBankDatenValidation,
                                          MitarbeiterArbeitsgenehmigungValidation mitarbeiterArbeitsgenehmigungValidation,
                                          OptionalEcardValidation optionalEcardValidation,
                                          MitarbeiterFotoValidation mitarbeiterFotoValidation,
                                          MitarbeiterZusatzInfoValidation mitarbeiterZusatzInfoValidation,
                                          StammdatenValidatorService stammdatenValidatorService,
                                          PersonalnummerService personalnummerService) {
        super(baseServiceV);
        this.mitarbeiterEmailValidation = mitarbeiterEmailValidation;
        this.optionalAnredeValidation = optionalAnredeValidation;
        this.mitarbeiterVornameValidation = mitarbeiterVornameValidation;
        this.mitarbeiterNachnameValidation = mitarbeiterNachnameValidation;
        this.mitarbeiterGeburtsnameValidation = mitarbeiterGeburtsnameValidation;
        this.mitarbeiterFamilienstandValidation = mitarbeiterFamilienstandValidation;
        this.mitarbeiterTitelValidation = mitarbeiterTitelValidation;
        this.mitarbeiterGeschlechtValidation = mitarbeiterGeschlechtValidation;
        this.optionalGeburtsdatumValidation = optionalGeburtsdatumValidation;
        this.mitarbeiterSVNValidation = mitarbeiterSVNValidation;
        this.mitarbeiterStaatsbuergerschaftValidation = mitarbeiterStaatsbuergerschaftValidation;
        this.mitarbeiterMutterspracheValidation = mitarbeiterMutterspracheValidation;
        this.mitarbeiterAdresseValidation = mitarbeiterAdresseValidation;
        this.mitarbeiterAbweichendeAdresseValidation = mitarbeiterAbweichendeAdresseValidation;
        this.mitarbeiterMobilnummerValidation = mitarbeiterMobilnummerValidation;
        this.mitarbeiterBankDatenValidation = mitarbeiterBankDatenValidation;
        this.mitarbeiterArbeitsgenehmigungValidation = mitarbeiterArbeitsgenehmigungValidation;
        this.optionalEcardValidation = optionalEcardValidation;
        this.mitarbeiterFotoValidation = mitarbeiterFotoValidation;
        this.mitarbeiterZusatzInfoValidation = mitarbeiterZusatzInfoValidation;
        this.stammdatenValidatorService = stammdatenValidatorService;
        this.personalnummerService = personalnummerService;
    }

    @Override
    protected void prepare() {
        addValidation(mitarbeiterEmailValidation);
        addValidation(optionalAnredeValidation);
        addValidation(mitarbeiterVornameValidation);
        addValidation(mitarbeiterNachnameValidation);
        addValidation(mitarbeiterGeburtsnameValidation);
        addValidation(mitarbeiterFamilienstandValidation);
        addValidation(mitarbeiterTitelValidation);
        addValidation(mitarbeiterGeschlechtValidation);
        addValidation(optionalGeburtsdatumValidation);
        addValidation(mitarbeiterSVNValidation);
        addValidation(mitarbeiterStaatsbuergerschaftValidation);
        addValidation(mitarbeiterMutterspracheValidation);
        addValidation(mitarbeiterAdresseValidation);
        addValidation(mitarbeiterAbweichendeAdresseValidation);
        addValidation(mitarbeiterMobilnummerValidation);
        addValidation(mitarbeiterBankDatenValidation);
        addValidation(mitarbeiterArbeitsgenehmigungValidation);
        addValidation(optionalEcardValidation);
        addValidation(mitarbeiterFotoValidation);
        addValidation(mitarbeiterZusatzInfoValidation);
    }

    @Override
    protected void executeValidations() {
        for (StammdatenDto object : this.objectsToValidate) {
            Personalnummer personalnummer = personalnummerService.findByPersonalnummer(object.getPersonalnummer());
            isTeilnehmer = (personalnummer != null) && MitarbeiterType.TEILNEHMER.equals(personalnummer.getMitarbeiterType());

            Stammdaten validatedObject = stammdatenValidatorService.getStammdaten(object);
            boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
            validatedObject = baseServiceV.save(validatedObject);
            validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
        }
    }

    @Override
    protected List<StammdatenDto> postProcess() {
        List<StammdatenDto> processedStammdaten = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<StammdatenDto, Stammdaten>, Boolean> entry : validationResults.entrySet()) {
            Stammdaten stammdaten = entry.getKey().getSecond();
            StammdatenDto stammdatenDto = entry.getKey().getFirst();
            for (StammdatenDataStatus dataStatus : stammdaten.getErrors()) {
                stammdatenDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
            }
            stammdatenDto.setErrors(new ArrayList<>(stammdatenDto.getErrorsMap().keySet()));
            if (Boolean.TRUE.equals(entry.getValue())) {
                setStatusAndSaveStammdaten(stammdaten, stammdatenDto, getIsOnboarding() ? MitarbeiterStatus.VALIDATED : MitarbeiterStatus.ACTIVE);
            } else {
                setStatusAndSaveStammdaten(stammdaten, stammdatenDto, MitarbeiterStatus.NOT_VALIDATED);
            }
            processedStammdaten.add(stammdatenDto);
        }
        return processedStammdaten;
    }

    private void setStatusAndSaveStammdaten(Stammdaten stammdaten, StammdatenDto stammdatenDto, MitarbeiterStatus status) {
        stammdaten.setStatus(status);
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(stammdatenDto.getPersonalnummer());
        personalnummer.setStatus(Status.ACTIVE);
        stammdaten.setPersonalnummer(personalnummer);
        stammdaten.setChangedBy(getChangedBy());
        stammdaten.setChangedOn(getLocalDateNow());
        if (stammdatenDto.getHandySignatur() != null) {
            stammdaten.setHandySignatur(stammdatenDto.getHandySignatur());
        }
        Stammdaten savedStammdaten = baseServiceV.save(stammdaten);
        stammdatenDto.setId(savedStammdaten.getId());
        if (stammdaten.getBank() != null && stammdaten.getBank().getCard() != null) {
            stammdatenDto.setBankcard(stammdaten.getBank().getCard().getValue());
        }
    }

    @Override
    protected boolean executeSingleValidationForSingleObject(Validation<StammdatenDto, Stammdaten> validation, StammdatenDto object, Stammdaten validatedObject) {
        if (validation instanceof AbstractValidation<StammdatenDto, Stammdaten> teilnehmerValidation) {
            if (isTeilnehmer) {
                teilnehmerValidation.setSources(Set.of(TeilnehmerSource.TN_ONBOARDING));
            } else {
                teilnehmerValidation.setSources(new HashSet<>());
            }
            if (teilnehmerValidation.shouldValidationRun()) {
                return teilnehmerValidation.executeValidation(object, validatedObject);
            }
            return true;
        }
        return super.executeSingleValidationForSingleObject(validation, object, validatedObject);
    }
}
