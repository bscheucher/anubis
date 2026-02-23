package com.ibosng.validationservice.teilnehmer;

import com.ibosng.dbservice.entities.teilnehmer.*;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.dbservice.services.ValidationsService;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.validationservice.AbstractValidator;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.services.TeilnehmerValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.teilnehmer.validations.imported.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ImportedTeilnehmerValidator extends AbstractValidator<TeilnehmerStaging, Teilnehmer> {
    private final ValidationsService validationsService;
    private final TeilnehmerValidatorService teilnehmerValidatorService;
    private final TeilnehmerEmailValidation teilnehmerEmailValidation;
    private final TeilnehmerTitelValidation teilnehmerTitelValidation;
    private final TeilnehmerTitel2Validation teilnehmerTitel2Validation;
    private final TeilnehmerGeburtsdatumValidation teilnehmerGeburtsdatumValidation;
    private final TeilnehmerGeburtsdatumAndSVNValidation teilnehmerGeburtsdatumAndSVNValidation;
    private final VHSOptionalAdresseValidation vhsOptionalAdresseValidation;
    private final SeminarValidation seminarValidation;
    private final NationValidation nationValidation;
    private final TeilnehmerAnredeValidation teilnehmerAnredeValidation;
    private final TeilnehmerNotizValidation teilnehmerNotizValidation;
    private final UrsprungValidation ursprungValidation;
    private final VermittelbarAbValidation vermittelbarAbValidation;
    private final ZielValidation zielValidation;
    private final VHSOptionalGeschlechtValidation vhsOptionalGeschlechtValidation;
    private final VHSOptionalNachnameValidation vhsOptionalNachnameValidation;
    private final VHSOptionalSvnrValidation vhsOptionalSvnrValidation;
    private final VHSOptionalTelefonValidation vhsOptionalTelefonValidation;
    private final VHSOptionalVornameValidation vhsOptionalVornameValidation;
    private final TeilnehmerMutterspracheValidation teilnehmerMutterspracheValidation;

    @Getter
    @Setter
    private Set<TeilnehmerSource> sources = new HashSet<>();

    @Getter
    @Setter
    private List<TeilnehmerStaging> invalidTeilnehmerStaging = new ArrayList<>();

    public ImportedTeilnehmerValidator(BaseService<TeilnehmerStaging> baseServiceT,
                                       BaseService<Teilnehmer> baseServiceV,
                                       ValidationsService validationsService,
                                       TeilnehmerValidatorService teilnehmerValidatorService,
                                       TeilnehmerEmailValidation teilnehmerEmailValidation,
                                       TeilnehmerTitelValidation teilnehmerTitelValidation,
                                       TeilnehmerGeburtsdatumValidation teilnehmerGeburtsdatumValidation,
                                       TeilnehmerGeburtsdatumAndSVNValidation teilnehmerGeburtsdatumAndSVNValidation,
                                       VHSOptionalAdresseValidation vhsOptionalAdresseValidation,
                                       SeminarValidation seminarValidation,
                                       NationValidation nationValidation,
                                       TeilnehmerAnredeValidation teilnehmerAnredeValidation,
                                       TeilnehmerNotizValidation teilnehmerNotizValidation,
                                       UrsprungValidation ursprungValidation,
                                       VermittelbarAbValidation vermittelbarAbValidation,
                                       ZielValidation zielValidation,
                                       VHSOptionalGeschlechtValidation vhsOptionalGeschlechtValidation,
                                       VHSOptionalNachnameValidation vhsOptionalNachnameValidation,
                                       VHSOptionalSvnrValidation vhsOptionalSvnrValidation,
                                       VHSOptionalTelefonValidation vhsOptionalTelefonValidation,
                                       VHSOptionalVornameValidation vhsOptionalVornameValidation,
                                       TeilnehmerTitel2Validation teilnehmerTitel2Validation, TeilnehmerMutterspracheValidation teilnehmerMutterspracheValidation) {
        super(baseServiceT, baseServiceV);
        this.validationsService = validationsService;
        this.teilnehmerValidatorService = teilnehmerValidatorService;
        this.teilnehmerEmailValidation = teilnehmerEmailValidation;
        this.teilnehmerTitelValidation = teilnehmerTitelValidation;
        this.vhsOptionalGeschlechtValidation = vhsOptionalGeschlechtValidation;
        this.vhsOptionalNachnameValidation = vhsOptionalNachnameValidation;
        this.vhsOptionalSvnrValidation = vhsOptionalSvnrValidation;
        this.vhsOptionalTelefonValidation = vhsOptionalTelefonValidation;
        this.vhsOptionalVornameValidation = vhsOptionalVornameValidation;
        this.teilnehmerGeburtsdatumValidation = teilnehmerGeburtsdatumValidation;
        this.teilnehmerGeburtsdatumAndSVNValidation = teilnehmerGeburtsdatumAndSVNValidation;
        this.vhsOptionalAdresseValidation = vhsOptionalAdresseValidation;
        this.seminarValidation = seminarValidation;
        this.nationValidation = nationValidation;
        this.teilnehmerAnredeValidation = teilnehmerAnredeValidation;
        this.teilnehmerNotizValidation = teilnehmerNotizValidation;
        this.ursprungValidation = ursprungValidation;
        this.vermittelbarAbValidation = vermittelbarAbValidation;
        this.zielValidation = zielValidation;
        this.teilnehmerTitel2Validation = teilnehmerTitel2Validation;
        this.teilnehmerMutterspracheValidation = teilnehmerMutterspracheValidation;
    }

    @Override
    protected void prepare() {
        TeilnehmerStaging teilnehmerStaging = getObjectsToValidate().stream().findFirst().orElse(null);
        if (teilnehmerStaging == null || !teilnehmerStaging.getSource().equals(TeilnehmerSource.MANUAL)) {
            super.prepare();
        }
        addValidation(teilnehmerEmailValidation);
        addValidation(teilnehmerTitelValidation);
        addValidation(vhsOptionalVornameValidation);
        addValidation(vhsOptionalNachnameValidation);
        addValidation(vhsOptionalGeschlechtValidation);
        addValidation(teilnehmerGeburtsdatumValidation);
        addValidation(vhsOptionalSvnrValidation);
        addValidation(teilnehmerGeburtsdatumAndSVNValidation);
        addValidation(vhsOptionalAdresseValidation);
        addValidation(vhsOptionalTelefonValidation);
        addValidation(seminarValidation);
        addValidation(nationValidation);
        addValidation(teilnehmerAnredeValidation);
        addValidation(teilnehmerNotizValidation);
        addValidation(ursprungValidation);
        addValidation(vermittelbarAbValidation);
        addValidation(zielValidation);
        addValidation(teilnehmerTitel2Validation);
        addValidation(teilnehmerMutterspracheValidation);
    }

    @Override
    protected void executeValidations() {
        for (TeilnehmerStaging object : this.objectsToValidate) {
            setSources(getAllSources(object));
            Teilnehmer validatedObject = teilnehmerValidatorService.getTeilnehmer(object);
            if (validatedObject != null) {
                log.info("Object - {}", validatedObject);
                boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
                validatedObject = baseServiceV.save(validatedObject);
                validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
            } else {
                getInvalidTeilnehmerStaging().add(object);
            }

        }
    }

    @Override
    protected List<Teilnehmer> postProcess() {
        List<Teilnehmer> processedTeilnehmer = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<TeilnehmerStaging, Teilnehmer>, Boolean> entry : validationResults.entrySet()) {
            Teilnehmer teilnehmer = entry.getKey().getSecond();
            TeilnehmerStaging teilnehmerStaging = entry.getKey().getFirst();
            if (entry.getValue() && teilnehmer.getErrors().isEmpty()) {
                Teilnehmer savedTeilnehmer = setStatusAndSavedTeilnehmer(teilnehmer, teilnehmerStaging, TeilnehmerStatus.VALID);
                processedTeilnehmer.add(savedTeilnehmer);
            } else {
                Teilnehmer savedTeilnehmer = setStatusAndSavedTeilnehmer(teilnehmer, teilnehmerStaging, TeilnehmerStatus.INVALID);
                processedTeilnehmer.add(savedTeilnehmer);
                if (!teilnehmerStaging.getSource().equals(TeilnehmerSource.MANUAL)) {
                    for (TeilnehmerDataStatus dataStatus : teilnehmer.getErrors()) {
                        validationsService.createErrorValidation(getErrorMessageForTeilnehmer(teilnehmer, dataStatus.getCause()), getIdentifier(), savedTeilnehmer.getId(), VALIDATION_SERVICE);
                    }
                }
            }
        }
        if (!getInvalidTeilnehmerStaging().isEmpty() && !getInvalidTeilnehmerStaging().get(0).getSource().equals(TeilnehmerSource.MANUAL)) {
            for (TeilnehmerStaging teilnehmerStaging : getInvalidTeilnehmerStaging()) {
                validationsService.createErrorValidation(getErrorMessageForTeilnehmerStaging(teilnehmerStaging), getIdentifier(), VALIDATION_SERVICE);
            }
        }
        return processedTeilnehmer;
    }

    private Teilnehmer setStatusAndSavedTeilnehmer(Teilnehmer teilnehmer, TeilnehmerStaging teilnehmerStaging, TeilnehmerStatus status) {
        teilnehmer.setStatus(status);
        teilnehmerStaging.setStatus(status);
        teilnehmerStaging.setChangedBy(getChangedBy());
        teilnehmer.setChangedBy(VALIDATION_SERVICE);
        teilnehmer.setChangedOn(getLocalDateNow());
        teilnehmer.setImportFilename(teilnehmerStaging.getImportFilename());
        Teilnehmer savedTeilnehmer = baseServiceV.save(teilnehmer);
        teilnehmerStaging.setTeilnehmerId(savedTeilnehmer.getId());
        baseServiceT.save(teilnehmerStaging);
        return savedTeilnehmer;
    }

    private String getErrorMessageForTeilnehmer(Teilnehmer teilnehmer, String errorMessage) {
        return new StringBuilder("Teilnehmer: ")
                .append(teilnehmer.getVorname())
                .append(" ")
                .append(teilnehmer.getNachname())
                .append(" : ")
                .append(errorMessage)
                .toString();
    }

    private String getErrorMessageForTeilnehmerStaging(TeilnehmerStaging teilnehmer) {
        StringBuilder messageBuilder = new StringBuilder("Teilnehmer: ");
        if (teilnehmer.getVorname() != null) {
            messageBuilder.append(teilnehmer.getVorname()).append(" ");
        }
        if (teilnehmer.getNachname() != null) {
            messageBuilder.append(teilnehmer.getNachname()).append(" ");
        }
        if (teilnehmer.getSvNummer() != null) {
            messageBuilder.append(teilnehmer.getSvNummer());
        }
        messageBuilder.append(" : ").append("Keine Unique Identifiers");

        return messageBuilder.toString();
    }

    @Override
    protected boolean executeSingleValidationForSingleObject(Validation<TeilnehmerStaging, Teilnehmer> validation, TeilnehmerStaging object, Teilnehmer validatedObject) {
        AbstractValidation<TeilnehmerStaging, Teilnehmer> teilnehmerValidation = (AbstractValidation<TeilnehmerStaging, Teilnehmer>) validation;
        teilnehmerValidation.setSources(getSources());
        if (teilnehmerValidation.shouldValidationRun()) {
            return teilnehmerValidation.executeValidation(object, validatedObject);
        }
        return true;
    }

    protected Set<TeilnehmerSource> getAllSources(TeilnehmerStaging teilnehmerStaging) {
        if (teilnehmerStaging.getTeilnehmerId() == 0) {
            return Collections.singleton(teilnehmerStaging.getSource());
        }
        if (TeilnehmerSource.TEILNEHMER_CSV.equals(teilnehmerStaging.getSource())) {
            return Set.of(teilnehmerStaging.getSource());
        }
        List<TeilnehmerStaging> teilnehmerStagings = ((TeilnehmerStagingServiceImpl) baseServiceT).findByTeilnehmerId(teilnehmerStaging.getTeilnehmerId());
        return teilnehmerStagings.stream().sorted(Comparator.comparing(TeilnehmerStaging::getCreatedOn).reversed()).map(TeilnehmerStaging::getSource).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
