package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.*;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.validationservice.AbstractValidator;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.services.*;
import com.ibosng.workflowservice.dtos.WorkflowPayload;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.List;

import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

@Service
@Slf4j
public class ValidatorServiceImpl implements ValidatorService {

    private final ValidationReportService reportService;
    private final TeilnehmerStagingService teilnehmerStagingService;
    private final ManageWFItemsService manageWFItemsService;
    private final WWorkflowService wWorkflowService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final TeilnehmerSprachkenntnisValidatorService teilnehmerSprachkenntnisValidatorService;
    private final Teilnehmer2SeminarPruefungValidatorService teilnehmer2SeminarPruefungValidatorService;
    private final TeilnehmerNotizValidatorService teilnehmerNotizValidatorService;
    private final Teilnehmer2SeminarValidatorService teilnehmer2SeminarValidatorService;
    private final TeilnehmerZertifikatValidatorService teilnehmerZertifikatValidatorService;
    private final TeilnehmerAusbildungValidatorService teilnehmerAusbildungValidatorService;
    private final AzureSSOService azureSSOService;
    private final TeilnehmerBerufserfahrungValidatorService teilnehmerBerufserfahrungValidatorService;
    private ValidatorFactory validatorFactory;

    public ValidatorFactory getValidatorFactory() {
        return validatorFactory;
    }

    @Autowired
    public void setValidatorFactory(@Lazy ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }


    public ValidatorServiceImpl(ValidationReportService reportService,
                                TeilnehmerStagingService teilnehmerStagingService,
                                ManageWFItemsService manageWFItemsService,
                                WWorkflowService wWorkflowService,
                                WWorkflowGroupService wWorkflowGroupService,
                                TeilnehmerSprachkenntnisValidatorService teilnehmerSprachkenntnisValidatorService,
                                TeilnehmerZertifikatValidatorService teilnehmerZertifikatValidatorService,
                                Teilnehmer2SeminarPruefungValidatorService teilnehmer2SeminarPruefungValidatorService,
                                TeilnehmerNotizValidatorService teilnehmerNotizValidatorService,
                                Teilnehmer2SeminarValidatorService teilnehmer2SeminarValidatorService,
                                TeilnehmerAusbildungValidatorService teilnehmerAusbildungValidatorService, AzureSSOService azureSSOService,
                                TeilnehmerBerufserfahrungValidatorService teilnehmerBerufserfahrungValidatorService
    ) {
        this.reportService = reportService;
        this.teilnehmerStagingService = teilnehmerStagingService;
        this.manageWFItemsService = manageWFItemsService;
        this.wWorkflowService = wWorkflowService;
        this.wWorkflowGroupService = wWorkflowGroupService;
        this.teilnehmerSprachkenntnisValidatorService = teilnehmerSprachkenntnisValidatorService;
        this.teilnehmerZertifikatValidatorService = teilnehmerZertifikatValidatorService;
        this.teilnehmer2SeminarPruefungValidatorService = teilnehmer2SeminarPruefungValidatorService;
        this.teilnehmerNotizValidatorService = teilnehmerNotizValidatorService;
        this.teilnehmer2SeminarValidatorService = teilnehmer2SeminarValidatorService;
        this.teilnehmerAusbildungValidatorService = teilnehmerAusbildungValidatorService;
        this.azureSSOService = azureSSOService;
        this.teilnehmerBerufserfahrungValidatorService = teilnehmerBerufserfahrungValidatorService;
    }

    @Override
    public ResponseEntity<String> validateImportedParticipants(WorkflowPayload workflowPayload) {
        WWorkflow workflow = wWorkflowService.findByIdWithGroup(workflowPayload.getWorkflowId());
        if (workflow != null) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VALIDATING_TEILNEHMER, WWorkflowStatus.IN_PROGRESS, VALIDATION_SERVICE);
        } else {
            log.error("Workflow could not be found!!!");
        }
        log.info("Incoming request for importing teilnehmer");
        AbstractValidator<TeilnehmerStaging, Teilnehmer> validator = validatorFactory.createValidator(workflowPayload.getData(), TeilnehmerStaging.class, Teilnehmer.class);
        validator.process();
        if (shouldCreateReport(workflowPayload)) {
            String[] mailAdminJURecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.ADMIN_PR.getValue()).toArray(new String[0]);
            reportService.createReport(workflowPayload.getData(), "validation-service.error.participant.invalid-data", "german", mailAdminJURecipients, toObjectArray(), toObjectArray(workflowPayload.getData()));
        }
        manageWfStatus(workflow);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void manageWfStatus(WWorkflow workflow) {
        if (workflow != null) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VALIDATING_TEILNEHMER, WWorkflowStatus.COMPLETED, VALIDATION_SERVICE);
            workflow.setStatus(WWorkflowStatus.COMPLETED);
            workflow = wWorkflowService.save(workflow);
            WWorkflowGroup workflowGroup = wWorkflowService.findByIdWithGroup(workflow.getId()).getWorkflowGroup();
            workflowGroup.setStatus(WWorkflowStatus.COMPLETED);
            wWorkflowGroupService.save(workflowGroup);
        } else {
            log.error("Workflow could not be found!!!");
        }
    }

    @Override
    public ResponseEntity<TeilnehmerDto> validateSingleParticipant(TeilnehmerStaging teilnehmerStaging, String changedBy) {
        log.info("Incoming request to validate teilnehmer {}", teilnehmerStaging.toString());
        AbstractValidator<TeilnehmerStaging, Teilnehmer> validator = validatorFactory.createValidator(teilnehmerStaging.getImportFilename(), TeilnehmerStaging.class, Teilnehmer.class);
        validator.setObjectsToValidate(Collections.singletonList(teilnehmerStaging));
        validator.setChangedBy(changedBy);
        Teilnehmer processedTeilnehmer = validator.process().stream().findFirst().orElse(null);
        if (processedTeilnehmer == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed teilnehmer with id: {}, vorname: {}, nachname: {}", processedTeilnehmer.getId(), processedTeilnehmer.getVorname(), processedTeilnehmer.getNachname());
        List<TeilnehmerDto> dtos = teilnehmerStagingService.mapTeilnehmer2TeilnehmerDto(processedTeilnehmer, false, false, "", true);
        if (dtos.size() > 1) {
            log.warn("More than one dtos mapped for teilnehmer after validating!!!");
            return new ResponseEntity<>(dtos.get(0), HttpStatus.OK);
        } else if (dtos.isEmpty()) {
            log.warn("Not able to match validated teilnhehmer!!!");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new ResponseEntity<>(dtos.get(0), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<StammdatenDto> validateMitarbeiterStammdaten(StammdatenDto stammdatenDto, Boolean isOnboarding, String changedBy) {
        log.info("Incoming request to validate mitarbeiter stammdaten {}", stammdatenDto.toString());
        SingleEntityAbstractValidator<StammdatenDto, Stammdaten> validator = validatorFactory.createSingleEntityValidator(StammdatenDto.class, Stammdaten.class);
        validator.setObjectsToValidate(Collections.singletonList(stammdatenDto));
        validator.setChangedBy(changedBy);
        validator.setIsOnboarding(isOnboarding);
        StammdatenDto processedStammdaten = validator.process().stream().findFirst().orElse(null);
        if (processedStammdaten == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed mitarbeiter stammdaten with personalnummer: {}, vorname: {}, nachname: {}", processedStammdaten.getPersonalnummer(), processedStammdaten.getVorname(), processedStammdaten.getNachname());
        return new ResponseEntity<>(processedStammdaten, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VertragsdatenDto> validateMitarbeiterVertragsdaten(VertragsdatenDto vertragsdatenDto, Boolean isOnboarding, String changedBy) {
        log.info("Incoming request to validate mitarbeiter vertragsdaten {}", vertragsdatenDto.toString());
        SingleEntityAbstractValidator<VertragsdatenDto, Vertragsdaten> validator = validatorFactory.createSingleEntityValidator(VertragsdatenDto.class, Vertragsdaten.class);
        validator.setObjectsToValidate(Collections.singletonList(vertragsdatenDto));
        validator.setChangedBy(changedBy);
        validator.setIsOnboarding(isOnboarding);
        VertragsdatenDto processedVertragsdaten = validator.process().stream().findFirst().orElse(null);
        if (processedVertragsdaten == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed mitarbeiter vertragsdaten with personalnummer: {}", processedVertragsdaten.getPersonalnummer());
        return new ResponseEntity<>(processedVertragsdaten, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VordienstzeitenDto> validateMitarbeiterVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String changedBy) {
        log.info("Incoming request to validate mitarbeiter Vordienstzeiten {}", vordienstzeitenDto.toString());
        SingleEntityAbstractValidator<VordienstzeitenDto, Vertragsdaten> validator = validatorFactory.createSingleEntityValidator(VordienstzeitenDto.class, Vertragsdaten.class);
        validator.setObjectsToValidate(Collections.singletonList(vordienstzeitenDto));
        validator.setChangedBy(changedBy);
        VordienstzeitenDto processedVordienstzeiten = validator.process().stream().findFirst().orElse(null);
        if (processedVordienstzeiten == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed mitarbeiter Vordienstzeiten with Personalnummer: {}", processedVordienstzeiten.getPersonalnummer());
        return new ResponseEntity<>(processedVordienstzeiten, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UnterhaltsberechtigteDto> validateMitarbeiterUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String changedBy) {
        log.info("Incoming request to validate mitarbeiter Unterhaltsberechtigte {}", unterhaltsberechtigteDto.toString());
        SingleEntityAbstractValidator<UnterhaltsberechtigteDto, Vertragsdaten> validator = validatorFactory.createSingleEntityValidator(UnterhaltsberechtigteDto.class, Vertragsdaten.class);
        validator.setObjectsToValidate(Collections.singletonList(unterhaltsberechtigteDto));
        validator.setChangedBy(changedBy);
        UnterhaltsberechtigteDto processedUnterhaltsberechtigteDto = validator.process().stream().findFirst().orElse(null);
        if (processedUnterhaltsberechtigteDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed mitarbeiter Unterhaltsberechtigte with Personalnummer: {}", processedUnterhaltsberechtigteDto.getPersonalnummer());
        return new ResponseEntity<>(processedUnterhaltsberechtigteDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TeilnehmerDto> validateTeilnehmer(TeilnehmerDto teilnehmerDto, String changedBy) {
        log.info("Incoming request to validate teilnehmer dto {}", teilnehmerDto.toString());
        SingleEntityAbstractValidator<TeilnehmerDto, Teilnehmer> validator = validatorFactory.createSingleEntityValidator(TeilnehmerDto.class, Teilnehmer.class);
        validator.setObjectsToValidate(Collections.singletonList(teilnehmerDto));
        validator.setChangedBy(changedBy);
        TeilnehmerDto proccessedTeilnehmerDto = validator.process().stream().findFirst().orElse(null);
        if (proccessedTeilnehmerDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed teilnehmer dto with id: {}", proccessedTeilnehmerDto.getId());
        return new ResponseEntity<>(proccessedTeilnehmerDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SeminarDto> validateSeminarByTeilnehmerDto(SeminarDto seminarDto, String changedBy, String teilnehmerId) {
        log.info("Incoming request to validate seminarDto with teilnehmerId {} and seminarId {}", teilnehmerId, seminarDto != null ? seminarDto.getId() : null);

        SeminarDto proccessedSeminarDto = teilnehmer2SeminarValidatorService.validateTeilnehmerSeminar(seminarDto, changedBy, teilnehmerId);
        if (proccessedSeminarDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed seminarDto with id: {}", proccessedSeminarDto.getId());
        return new ResponseEntity<>(proccessedSeminarDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TeilnehmerNotizDto> validateTeilnehmerNotizDto(TeilnehmerNotizDto teilnehmerNotizDto, String changedBy, String teilnehmerId) {
        log.info("Incoming request to validate teilnehmerNotizDto with teilnehmerId {} and teilnehmerNotizId {}", teilnehmerId,
                teilnehmerNotizDto != null ? teilnehmerNotizDto.getId() : null);
        TeilnehmerNotizDto proccessedTeilnehmerNotizDto = teilnehmerNotizValidatorService.validateAndSaveTeilnehmerNotiz(teilnehmerNotizDto, changedBy, teilnehmerId);
        if (proccessedTeilnehmerNotizDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed teilnehmerNotizDto with id: {}", proccessedTeilnehmerNotizDto.getId());
        return new ResponseEntity<>(proccessedTeilnehmerNotizDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TnAusbildungDto> validateTeilnehmerAusbildungDto(TnAusbildungDto tnAusbildungDto, String changedBy, String teilnehmerId) {
        log.info("Incoming request to validate tnAusbildungDto with teilnehmerId {} and teilnehmerAusbildungId {}", teilnehmerId,
                tnAusbildungDto != null ? tnAusbildungDto.getId() : null);
        TnAusbildungDto proccessedTeilnehmerAusbildungDto = teilnehmerAusbildungValidatorService.validateAndSaveTeilnehmerAusbildung(tnAusbildungDto, changedBy, teilnehmerId);
        if (proccessedTeilnehmerAusbildungDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed tnAusbildungDto with id: {}", proccessedTeilnehmerAusbildungDto.getId());
        return new ResponseEntity<>(proccessedTeilnehmerAusbildungDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TnBerufserfahrungDto> validateTeilnehmerBerufserfahrungDto(TnBerufserfahrungDto tnBerufserfahrungDto, String changedBy, String teilnehmerId) {
        log.info("Incoming request to validate tnBerufserfahrungDto with teilnehmerId {} and teilnehmerBerufserfahrungId {}", teilnehmerId,
                tnBerufserfahrungDto != null ? tnBerufserfahrungDto.getId() : null);
        TnBerufserfahrungDto proccessedTeilnehmerBerufserfahrungDto = teilnehmerBerufserfahrungValidatorService.validateAndSaveTeilnehmerBerufserfahrung(tnBerufserfahrungDto, changedBy, teilnehmerId);
        if (proccessedTeilnehmerBerufserfahrungDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed tnBerufserfahrungDto with id: {}", proccessedTeilnehmerBerufserfahrungDto.getId());
        return new ResponseEntity<>(proccessedTeilnehmerBerufserfahrungDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SeminarPruefungDto> validateSeminarPruefungDto(SeminarPruefungDto seminarPruefungDto, String changedBy, String teilnehmerId, String seminarId) {
        log.info("Incoming request to validate teilnehmerSeminarPruefung with teilnehmerId {}, seminarId {} and pruefungId {}", teilnehmerId, seminarId,
                seminarPruefungDto != null ? seminarPruefungDto.getId() : null);
        SeminarPruefungDto proccessedSeminarPruefungDto = teilnehmer2SeminarPruefungValidatorService.validateTeilnehmerSeminarPruefung(seminarPruefungDto, changedBy, teilnehmerId, seminarId);
        if (proccessedSeminarPruefungDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed SeminarPruefungDto with id: {}", proccessedSeminarPruefungDto.getId());
        return new ResponseEntity<>(proccessedSeminarPruefungDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SprachkenntnisDto> validateTeilnehmerSprachkenntnisDto(SprachkenntnisDto sprachkenntnisDto, String teilnehmerId, String changedBy) {
        log.info("Incoming request to validate teilnehmer sprachkenntnisDto with teilnehmerId {} and sprachkenntnisId {}", teilnehmerId,
                sprachkenntnisDto != null ? sprachkenntnisDto.getId() : null);
        SprachkenntnisDto proccessedSprachkenntnisDto = teilnehmerSprachkenntnisValidatorService.validateAndSaveSprachkenntnis(sprachkenntnisDto, teilnehmerId, changedBy);
        if (proccessedSprachkenntnisDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed sprachkenntnisDto with id: {}", proccessedSprachkenntnisDto.getId());
        return new ResponseEntity<>(proccessedSprachkenntnisDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TnZertifikatDto> validateTeilnehmerZertifikatDto(TnZertifikatDto tnZertifikatDto, String changedBy, String teilnehmerId) {
        log.info("Incoming request to validate tnZertifikatDto with teilnehmerId {} and tnZertifikatId {}", teilnehmerId, tnZertifikatDto != null ? tnZertifikatDto.getId() : null);
        TnZertifikatDto proccessedZertifikatDto = teilnehmerZertifikatValidatorService.validateAndSaveZertifikat(tnZertifikatDto, changedBy, teilnehmerId);
        if (proccessedZertifikatDto == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("Processed tnZertifikatDto with id: {}", proccessedZertifikatDto.getId());
        return new ResponseEntity<>(proccessedZertifikatDto, HttpStatus.OK);
    }

    @Override
    public Mono<ResponseEntity<String>> validateZeiterfassungAsync(ZeiterfassungTransferDto zeiterfassungDto, String changedBy) {
        log.info("Incoming request to validate zeiterfassung dto {}", zeiterfassungDto);

        // Fire-and-forget: process validation asynchronously
        Mono.fromRunnable(() -> processValidation(zeiterfassungDto, changedBy))
                .subscribeOn(Schedulers.boundedElastic()) // Offload to a thread pool for blocking operations
                .doOnSuccess(unused -> log.info("Validation process completed successfully for dto {}", zeiterfassungDto))
                .doOnError(error -> log.error("Error during validation process: {}", error.getMessage()))
                .subscribe();

        // Return an immediate response
        return Mono.just(ResponseEntity.accepted().body("Validation request received and is being processed."));
    }

    @Override
    public ResponseEntity<ZeitbuchungenDto> validateZeitbuchung(ZeitbuchungenDto zeitbuchungenDto, String changedBy) {
        log.info("Incoming request to validate zeitbuchung dto {}", zeitbuchungenDto.toString());
        SingleEntityAbstractValidator<ZeitbuchungenDto, Zeitbuchung> validator =
                validatorFactory.createSingleEntityValidator(ZeitbuchungenDto.class, Zeitbuchung.class);
        validator.setObjectsToValidate(Collections.singletonList(zeitbuchungenDto));
        validator.setChangedBy(changedBy);
        ZeitbuchungenDto proccessedZeitbuchung = validator.process().stream().findFirst().orElse(null);
        if (proccessedZeitbuchung == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(proccessedZeitbuchung, HttpStatus.OK);
    }

    private void processValidation(ZeiterfassungTransferDto zeiterfassungDto, String changedBy) {
        log.info("Processing validation for dto: {}", zeiterfassungDto);
        SingleEntityAbstractValidator<ZeiterfassungTransferDto, ZeiterfassungTransfer> validator =
                validatorFactory.createSingleEntityValidator(ZeiterfassungTransferDto.class, ZeiterfassungTransfer.class);
        validator.setObjectsToValidate(Collections.singletonList(zeiterfassungDto));
        validator.setChangedBy(changedBy);

        ZeiterfassungTransferDto processedZeiterfassungDto = validator.process().stream().findFirst().orElse(null);
        if (processedZeiterfassungDto == null) {
            log.warn("Validation failed for dto: {}", zeiterfassungDto);
        } else {
            log.info("Validation succeeded for dto: {}", processedZeiterfassungDto);
        }
    }

    private boolean shouldCreateReport(WorkflowPayload workflowPayload) {
        TeilnehmerStaging teilnehmerStaging = teilnehmerStagingService.findAllByIdentifier(workflowPayload.getData()).stream().findFirst().orElse(null);
        return teilnehmerStaging != null && !teilnehmerStaging.getSource().equals(TeilnehmerSource.SYNC_SERVICE);
    }
}