package com.ibosng.validationservice.teilnehmer;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerDataStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.services.TeilnehmerValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.teilnehmer.validations.manual.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeilnehmerValidator extends SingleEntityAbstractValidator<TeilnehmerDto, Teilnehmer> {
    private final TeilnehmerDtoAnredeValidation teilnehmerDtoAnredeValidation;
    private final TeilnehmerDtoTitelValidation teilnehmerDtoTitelValidation;
    private final TeilnehmerDtoTitel2Validation teilnehmerDtoTitel2Validation;
    private final TeilnehmerDtoNachnameValidation teilnehmerDtoNachnameValidation;
    private final TeilnehmerDtoVornameValidation teilnehmerDtoVornameValidation;
    private final TeilnehmerDtoSVNValidation teilnehmerDtoSVNValidation;
    private final TeilnehmerDtoGeschlechtValidation teilnehmerDtoGeschlechtValidation;
    private final TeilnehmerDtoGeburtsdatumValidation teilnehmerDtoGeburtsdatumValidation;
    private final TeilnehmerDtoGeburtsdatumAndSVNValidation teilnehmerDtoGeburtsdatumAndSVNValidation;
    private final TeilnehmerDtoTelefonValidation teilnehmerDtoTelefonValidation;
    private final TeilnehmerDtoAdresseValidation teilnehmerDtoAdresseValidation;
    private final TeilnehmerDtoEmailValidation teilnehmerDtoEmailValidation;
    private final TeilnehmerDtoNationValidation teilnehmerDtoNationValidation;
    private final TeilnehmerDtoUrsprungValidation teilnehmerDtoUrsprungValidation;
    private final TeilnehmerDtoErlaeuterungszielValidation teilnehmerDtoErlauterungZielValidation;
    private final TeilnehmerDtoVermittelbarAbValidation teilnehmerDtoVermittelbarAbValidation;
    private final TeilnehmerDtoVermittlungsNotizValidation teilnehmerDtoVermittlungsNotizValidation;
    private final TeilnehmerDtoVermittlelbarAusserhalbAmsValidation teilnehmerDtoVermittlelbarAusserhalbAmsValidation;
    private final TeilnehmerDtoWunschberufeValidation teilnehmerDtoWunschberufeValidation;
    private final TeilnehmerDtoBankdatenValidation teilnehmerDtoBankdatenValidation;
    private final TeilnehmerValidatorService teilnehmerValidatorService;
    private final TeilnehmerDtoMutterspracheValidation teilnehmerDtoMutterspracheValidation;

    @Getter
    @Setter
    private List<TeilnehmerDto> invalidTeilnehmerSummaryDto = new ArrayList<>();

    public TeilnehmerValidator(BaseService<Teilnehmer> baseServiceV,
                               TeilnehmerDtoAnredeValidation teilnehmerDtoAnredeValidation,
                               TeilnehmerDtoTitelValidation teilnehmerDtoTitelValidation,
                               TeilnehmerDtoNachnameValidation teilnehmerDtoNachnameValidation,
                               TeilnehmerDtoVornameValidation teilnehmerDtoVornameValidation,
                               TeilnehmerDtoSVNValidation teilnehmerDtoSVNValidation,
                               TeilnehmerDtoGeschlechtValidation teilnehmerDtoGeschlechtValidation,
                               TeilnehmerDtoGeburtsdatumValidation teilnehmerDtoGeburtsdatumValidation,
                               TeilnehmerDtoGeburtsdatumAndSVNValidation teilnehmerDtoGeburtsdatumAndSVNValidation,
                               TeilnehmerDtoTelefonValidation teilnehmerDtoTelefonValidation,
                               TeilnehmerDtoAdresseValidation teilnehmerDtoAdresseValidation,
                               TeilnehmerDtoEmailValidation teilnehmerDtoEmailValidation,
                               TeilnehmerDtoNationValidation teilnehmerDtoNationValidation,
                               TeilnehmerValidatorService teilnehmerValidatorService,
                               TeilnehmerDtoUrsprungValidation teilnehmerDtoUrsprungValidation,
                               TeilnehmerDtoErlaeuterungszielValidation teilnehmerDtoErlauterungZielValidation,
                               TeilnehmerDtoVermittelbarAbValidation teilnehmerDtoVermittelbarAbValidation,
                               TeilnehmerDtoVermittlungsNotizValidation teilnehmerDtoVermittlungsNotizValidation,
                               TeilnehmerDtoVermittlelbarAusserhalbAmsValidation teilnehmerDtoVermittlelbarAusserhalbAmsValidation,
                               TeilnehmerDtoWunschberufeValidation teilnehmerDtoWunschberufeValidation,
                               TeilnehmerDtoBankdatenValidation teilnehmerDtoBankdatenValidation,
                               TeilnehmerDtoTitel2Validation teilnehmerDtoTitel2Validation,
                               TeilnehmerDtoMutterspracheValidation teilnehmerDtoMutterspracheValidation) {
        super(baseServiceV);

        this.teilnehmerDtoAnredeValidation = teilnehmerDtoAnredeValidation;
        this.teilnehmerDtoTitelValidation = teilnehmerDtoTitelValidation;
        this.teilnehmerDtoNachnameValidation = teilnehmerDtoNachnameValidation;
        this.teilnehmerDtoVornameValidation = teilnehmerDtoVornameValidation;
        this.teilnehmerDtoSVNValidation = teilnehmerDtoSVNValidation;
        this.teilnehmerDtoGeschlechtValidation = teilnehmerDtoGeschlechtValidation;
        this.teilnehmerDtoGeburtsdatumValidation = teilnehmerDtoGeburtsdatumValidation;
        this.teilnehmerDtoGeburtsdatumAndSVNValidation = teilnehmerDtoGeburtsdatumAndSVNValidation;
        this.teilnehmerDtoTelefonValidation = teilnehmerDtoTelefonValidation;
        this.teilnehmerDtoAdresseValidation = teilnehmerDtoAdresseValidation;
        this.teilnehmerDtoEmailValidation = teilnehmerDtoEmailValidation;
        this.teilnehmerDtoNationValidation = teilnehmerDtoNationValidation;
        this.teilnehmerValidatorService = teilnehmerValidatorService;
        this.teilnehmerDtoUrsprungValidation = teilnehmerDtoUrsprungValidation;
        this.teilnehmerDtoErlauterungZielValidation = teilnehmerDtoErlauterungZielValidation;
        this.teilnehmerDtoVermittelbarAbValidation = teilnehmerDtoVermittelbarAbValidation;
        this.teilnehmerDtoVermittlungsNotizValidation = teilnehmerDtoVermittlungsNotizValidation;
        this.teilnehmerDtoVermittlelbarAusserhalbAmsValidation = teilnehmerDtoVermittlelbarAusserhalbAmsValidation;
        this.teilnehmerDtoWunschberufeValidation = teilnehmerDtoWunschberufeValidation;
        this.teilnehmerDtoBankdatenValidation = teilnehmerDtoBankdatenValidation;
        this.teilnehmerDtoTitel2Validation = teilnehmerDtoTitel2Validation;
        this.teilnehmerDtoMutterspracheValidation = teilnehmerDtoMutterspracheValidation;
    }

    @Override
    protected void prepare() {
        super.prepare();

        TeilnehmerDto teilnehmerDto = getObjectsToValidate().stream().findFirst().orElse(null);
        if (teilnehmerDto == null) {
            super.prepare();
        }
        addValidation(teilnehmerDtoAnredeValidation);
        addValidation(teilnehmerDtoTitelValidation);
        addValidation(teilnehmerDtoNachnameValidation);
        addValidation(teilnehmerDtoVornameValidation);
        addValidation(teilnehmerDtoSVNValidation);
        addValidation(teilnehmerDtoGeschlechtValidation);
        addValidation(teilnehmerDtoGeburtsdatumValidation);
        addValidation(teilnehmerDtoGeburtsdatumAndSVNValidation);
        addValidation(teilnehmerDtoTelefonValidation);
        addValidation(teilnehmerDtoAdresseValidation);
        addValidation(teilnehmerDtoEmailValidation);
        addValidation(teilnehmerDtoNationValidation);
        addValidation(teilnehmerDtoUrsprungValidation);
        addValidation(teilnehmerDtoErlauterungZielValidation);
        addValidation(teilnehmerDtoVermittelbarAbValidation);
        addValidation(teilnehmerDtoVermittlungsNotizValidation);
        addValidation(teilnehmerDtoWunschberufeValidation);
        addValidation(teilnehmerDtoVermittlelbarAusserhalbAmsValidation);
        addValidation(teilnehmerDtoBankdatenValidation);
        addValidation(teilnehmerDtoTitel2Validation);
        addValidation(teilnehmerDtoMutterspracheValidation);
/*        addValidation(new TeilnehmerDtoSeminarValidation(
        seminarService, seminarIbosService, rgsService, betreuerService, validationMapperService));*/
    }

    @Override
    protected void executeValidations() {
        for (TeilnehmerDto object : this.objectsToValidate) {
            Teilnehmer validatedObject = teilnehmerValidatorService.getTeilnehmerForManual(object);
            if (validatedObject != null) {
                boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
                validatedObject = baseServiceV.save(validatedObject);
                validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
            }
        }
    }

    @Override
    protected List<TeilnehmerDto> postProcess() {
        List<TeilnehmerDto> processedTeilnehmer = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<TeilnehmerDto, Teilnehmer>, Boolean> entry : validationResults.entrySet()) {
            Teilnehmer teilnehmer = entry.getKey().getSecond();
            TeilnehmerDto teilnehmerDto = entry.getKey().getFirst();
            for (TeilnehmerDataStatus dataStatus : teilnehmer.getErrors()) {
                teilnehmerDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
            }
            teilnehmerDto.setErrors(new ArrayList<>(teilnehmerDto.getErrorsMap().keySet()));

            if (entry.getValue() && teilnehmer.getErrors().isEmpty()) {
                setStatusAndSaveTeilnehmer(teilnehmer, teilnehmerDto, TeilnehmerStatus.VALID);
            } else {
                setStatusAndSaveTeilnehmer(teilnehmer, teilnehmerDto, TeilnehmerStatus.INVALID);
            }
            processedTeilnehmer.add(teilnehmerDto);
        }

        return processedTeilnehmer;
    }

    private void setStatusAndSaveTeilnehmer(Teilnehmer teilnehmer, TeilnehmerDto teilnehmerDto, TeilnehmerStatus status) {
        teilnehmer.setStatus(status);
        teilnehmer.setChangedBy(getChangedBy());
        teilnehmer.setChangedOn(getLocalDateNow());
        teilnehmer.setStatus(teilnehmerDto.getErrors().isEmpty() ?
                TeilnehmerStatus.VALID : TeilnehmerStatus.INVALID);
        Teilnehmer savedTeilnehmer = baseServiceV.save(teilnehmer);
        log.info("saved teilnehmer: {}", savedTeilnehmer);
        teilnehmerDto.setId(savedTeilnehmer.getId());
        teilnehmerDto.setStatus(savedTeilnehmer.getStatus().name());
    }

    @Override
    protected boolean executeSingleValidationForSingleObject(Validation<TeilnehmerDto, Teilnehmer> validation, TeilnehmerDto object, Teilnehmer validatedObject) {
        AbstractValidation<TeilnehmerDto, Teilnehmer> teilnehmerValidation = (AbstractValidation<TeilnehmerDto, Teilnehmer>) validation;
        if (teilnehmerValidation.shouldValidationRun()) {
            log.info("Teilnehmer validation is about to run: {}", teilnehmerValidation.getClass());
            return teilnehmerValidation.executeValidation(object, validatedObject);
        }
        return true;
    }
}
