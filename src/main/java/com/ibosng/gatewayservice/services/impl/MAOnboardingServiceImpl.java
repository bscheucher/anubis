package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.dbservice.dtos.mitarbeiter.*;
import com.ibosng.dbservice.dtos.workflows.WorkflowDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.ZusatzInfo;
import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.lhr.LhrJobStatus;
import com.ibosng.dbservice.entities.masterdata.*;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.moxis.SigningJobType;
import com.ibosng.dbservice.entities.reports.ReportParamType;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.ZusatzInfoService;
import com.ibosng.dbservice.services.lhr.LhrJobService;
import com.ibosng.dbservice.services.masterdata.*;
import com.ibosng.dbservice.services.mitarbeiter.*;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.enums.ReportOutputFormat;
import com.ibosng.gatewayservice.services.*;
import com.ibosng.gatewayservice.utils.Helpers;
import com.ibosng.lhrservice.services.LHRDokumenteService;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.workflowservice.enums.SWorkflowGroups;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFService;
import com.ibosng.workflowservice.services.WFStartService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibosng.dbservice.utils.Mappers.getSubdirectoryForDocument;
import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.gatewayservice.utils.Constants.GATEWAY_SERVICE;
import static com.ibosng.gatewayservice.utils.Helpers.*;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Service
@Slf4j
@Qualifier("maOnboardingServiceImpl")
@RequiredArgsConstructor
public class MAOnboardingServiceImpl implements MAOnboardingService {
    private static final String PDF_EXTENSION = ".pdf";
    private static final String FIRMA_IBIS_ACAM_BILDUNGS_GMBH = "ibis acam Bildungs GmbH";
    private static final String DIENSTVERTRAG = "dienstvertrag";
    private static final String KATEGORIE_TN_DEFAULT = "Lehrling";
    private static final String TAETITGKEIT_TN_DEFAULT = "AMS-Lehrteilnehmer";
    private static final String DIENSTVERTAG_REPORT = "Dienstvertrag";
    private static final String KOLLEKTIVVERTRAG_TN_DEFAULT = "AMS-Lehrteilnehmer";
    private static final String VERWENDUNGSGRUPPE_TN_DEFAULT = "Lehrlinge";
    private static final String BESCHAEFTIGUNGSSTATUS_TN_DEFAULT = "Angestellter";
    @Getter
    @Value("${nextAuthUrl:#{null}}")
    private String nextAuthUrl;

    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    @Getter
    @Value("${storageContainerJasperContracts:#{null}}")
    private String jasperContractsContainer;

    private final MailService mailService;
    private final JasperReportService jasperReportService;
    private final LhrJobService lhrJobService;
    private final TitelService titelService;
    private final FileShareService fileShareService;
    private final LHRDokumenteService lhrDokumenteService;
    private final KategorieService kategorieService;
    private final TaetigkeitService taetigkeitService;
    private final KollektivvertragService kollektivvertragService;
    private final VerwendungsgruppeService verwendungsgruppeService;
    private final BeschaeftigungsstatusService beschaeftigungsstatusService;
    private final AzureSSOService azureSSOService;
    private final StammdatenService stammdatenService;
    private final PersonalnummerService personalnummerService;
    private final BenutzerDetailsService benutzerDetailsService;
    private final ManageWFsService manageWFsService;
    private final WorkflowHelperService workflowHelperService;
    private final VertragsdatenService vertragsdatenService;
    private final TeilnehmerService teilnehmerService;
    private final WWorkflowItemService wWorkflowItemService;
    private final ManageWFItemsService manageWFItemsService;
    private final Gateway2Validation gateway2Validation;
    private final VordienstzeitenService vordienstzeitenService;
    private final UnterhaltsberechtigteService unterhaltsberechtigteService;
    private final WFStartService wfStartService;
    private final GehaltInfoService gehaltInfoService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final ZusatzInfoService zusatzInfoService;
    private final LvAcceptanceService lvAcceptanceService;
    private final GenericMAService genericMAService;
    private final EnvironmentService environmentService;
    private final WWorkflowService wWorkflowService;
    private final WFService wfService;


    @Override
    public PayloadResponse getAllMitarbeiterList(String sortProperty, String sortDirection, int page, int size, String mitarbeiterType) {

        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }
        Pageable pageable = createPageable(sortProperty, sortDirection, page, size);
        String wwgName;
        if (mitarbeiterType.equals(MitarbeiterType.MITARBEITER.getValue())) {
            wwgName = SWorkflowGroups.NEW_MITARBEITER.getValue();
        } else {
            wwgName = SWorkflowGroups.TN_ONBOARDING.getValue();
        }
        Page<MitarbeiterSummaryDto> mitarbeiterSummaryDtos = stammdatenService.findAllOrderedByNachnameEintritt(pageable, mitarbeiterType, wwgName);
        PayloadTypeList<MitarbeiterSummaryDto> mitarbeiterSummaryDtoPayloadType = new PayloadTypeList<>(PayloadTypes.MITARBEITER_SUMMARY.getValue());
        mitarbeiterSummaryDtoPayloadType.setAttributes(mitarbeiterSummaryDtos.getContent());
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setData(List.of(mitarbeiterSummaryDtoPayloadType));
        payloadResponse.setSuccess(true);
        payloadResponse.setPagination(createPagination(mitarbeiterSummaryDtos));
        return payloadResponse;
    }

    @Override
    public PayloadResponse getEigeneMitarbeiterList(String sortProperty, String sortDirection, int page, int size, String mitarbeiterType, Benutzer benutzer) {

        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }
        Pageable pageable = createPageable(sortProperty, sortDirection, page, size);
        String wwgName;
        if (mitarbeiterType.equals(MitarbeiterType.MITARBEITER.getValue())) {
            wwgName = SWorkflowGroups.NEW_MITARBEITER.getValue();
        } else {
            wwgName = SWorkflowGroups.TN_ONBOARDING.getValue();
        }
        Page<MitarbeiterSummaryDto> mitarbeiterSummaryDtos = stammdatenService.findForBenutzerOrderedByNachnameEintritt(pageable, mitarbeiterType, wwgName, benutzer.getEmail());
        PayloadTypeList<MitarbeiterSummaryDto> mitarbeiterSummaryDtoPayloadType = new PayloadTypeList<>(PayloadTypes.MITARBEITER_SUMMARY.getValue());
        mitarbeiterSummaryDtoPayloadType.setAttributes(mitarbeiterSummaryDtos.getContent());
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setData(List.of(mitarbeiterSummaryDtoPayloadType));
        payloadResponse.setSuccess(true);
        payloadResponse.setPagination(createPagination(mitarbeiterSummaryDtos));
        return payloadResponse;
    }


    @Override
    public PayloadResponse generatePersonalnummerPayloadResponse(Integer teilnehmerID, String firmenName, String token) {
        MitarbeiterType mitarbeiterType;

        if (teilnehmerID == null && !isNullOrBlank(firmenName)) {
            mitarbeiterType = MitarbeiterType.MITARBEITER;
        } else {
            String existingPN = isTNAlreadyOnboarded(teilnehmerID);
            if (!isNullOrBlank(existingPN)) {
                return createPersonalnummerPayloadResponse(existingPN);
            }
            mitarbeiterType = MitarbeiterType.TEILNEHMER;
            firmenName = FIRMA_IBIS_ACAM_BILDUNGS_GMBH;
        }
        String createdBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        Personalnummer personalnummer = personalnummerService.generatePersonalNummer(firmenName, mitarbeiterType, createdBy);
        if (mitarbeiterType.equals(MitarbeiterType.MITARBEITER)) {
            WWorkflowGroup wWorkflowGroup = wfStartService.createWorkflowGroupAndInstances(SWorkflowGroups.NEW_MITARBEITER.getValue(), personalnummer.getPersonalnummer(), createdBy);
            WWorkflow workflow = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN, WWorkflowStatus.IN_PROGRESS, createdBy);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.STAMMDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, createdBy);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, createdBy);
            saveNewStammdaten(personalnummer, createdBy);
            saveNewVertragsdaten(personalnummer, createdBy);
        } else if (mitarbeiterType.equals(MitarbeiterType.TEILNEHMER)) {
            WWorkflowGroup wWorkflowGroup = wfStartService.createWorkflowGroupAndInstances(SWorkflowGroups.TN_ONBOARDING.getValue(), personalnummer.getPersonalnummer(), createdBy);
            WWorkflow workflow = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN, WWorkflowStatus.IN_PROGRESS, createdBy);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_STAMMDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, createdBy);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, createdBy);
            saveNewTeilnehmerStammdaten(personalnummer, teilnehmerID, createdBy);
            saveNewVertragsdaten(personalnummer, createdBy);
        }
        fileShareService.createStructureForNewMA(personalnummer.getPersonalnummer(), getFileShareTemp(), firmenName);

        String personalnummerString = personalnummer.getPersonalnummer();
        return createPersonalnummerPayloadResponse(personalnummerString);
    }

    private PayloadResponse createPersonalnummerPayloadResponse(String personalnummer) {
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        PayloadTypeList<String> personalnummerPayload = new PayloadTypeList<>(PayloadTypes.PERSONALNUMMER.getValue());
        personalnummerPayload.setAttributes(Collections.singletonList(personalnummer));
        response.setData(Collections.singletonList(personalnummerPayload));
        return response;
    }

    private String isTNAlreadyOnboarded(Integer teilnehmerID) {
        Optional<Teilnehmer> teilnehmer = teilnehmerService.findById(teilnehmerID);
        if (teilnehmer.isPresent() && teilnehmer.get().getPersonalnummer() != null) {
            return teilnehmer.get().getPersonalnummer().getPersonalnummer();
        }
        return null;
    }


    @Override
    public PayloadResponse saveNewTeilnehmerStammdaten(Personalnummer personalnummer, Integer teilnehmerId, String changedBy) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer.getPersonalnummer(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        // Get Data from existing TN
        Optional<Teilnehmer> teilnehmerOptional = teilnehmerService.findById(teilnehmerId);
        Stammdaten stammdaten = new Stammdaten();
        stammdaten.setPersonalnummer(personalnummer);
        stammdaten.setStatus(MitarbeiterStatus.NEW);
        stammdaten.setCreatedBy(changedBy);
        if (teilnehmerOptional.isPresent()) {
            Teilnehmer teilnehmer = teilnehmerOptional.get();
            //TODO check if titel can be set
            Helpers.updateStammdatenFromTeilnehmer(teilnehmer, stammdaten);
            if (teilnehmer.getTitel() != null) {
                stammdaten.setTitel(titelService.findByName(teilnehmer.getTitel()));
            }
            setTeilnehmerPersonalnummer(teilnehmer, personalnummer);
        }
        ZusatzInfo zusatzInfo = new ZusatzInfo();
        zusatzInfo.setCreatedBy(changedBy);
        zusatzInfo.setStatus(MitarbeiterStatus.NEW);
        stammdaten.setZusatzInfo(zusatzInfoService.save(zusatzInfo));
        stammdaten = stammdatenService.save(stammdaten);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_STAMMDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, changedBy);
        PayloadResponse response = new PayloadResponse();
        PayloadTypeList<StammdatenDto> stammDatenPayloadType = genericMAService.createPayloadForStammdaten(stammdatenService.mapStammdatenToDto(stammdaten));
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(stammDatenPayloadType, workflowDtoPayloadType));
        return response;
    }

    public PayloadResponse saveNewStammdaten(Personalnummer personalnummer, String changedBy) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        Stammdaten stammdaten = new Stammdaten();
        stammdaten.setPersonalnummer(personalnummer);
        stammdaten.setStatus(MitarbeiterStatus.NEW);
        stammdaten.setCreatedBy(changedBy);
        ZusatzInfo zusatzInfo = new ZusatzInfo();
        zusatzInfo.setCreatedBy(changedBy);
        zusatzInfo.setStatus(MitarbeiterStatus.NEW);
        stammdaten.setZusatzInfo(zusatzInfoService.save(zusatzInfo));
        stammdaten = stammdatenService.save(stammdaten);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.STAMMDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, changedBy);
        PayloadResponse response = new PayloadResponse();
        PayloadTypeList<StammdatenDto> stammDatenPayloadType = genericMAService.createPayloadForStammdaten(stammdatenService.mapStammdatenToDto(stammdaten));
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(stammDatenPayloadType, workflowDtoPayloadType));
        return response;
    }

    public PayloadResponse saveNewVertragsdaten(Personalnummer personalnummer, String changedBy) {
        WWorkflow workflow;
        boolean isMitarbeiter = personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
        if (isMitarbeiter) {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        } else {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer.getPersonalnummer(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        }
        Vertragsdaten vertragsdaten = getVertragsdatenForTN(personalnummer, changedBy);
        vertragsdaten = vertragsdatenService.save(vertragsdaten);

        ArbeitszeitenInfo arbeitszeitenInfo = new ArbeitszeitenInfo();
        arbeitszeitenInfo.setCreatedBy(GATEWAY_SERVICE);
        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NEW);
        arbeitszeitenInfo.setVertragsdaten(vertragsdaten);
        arbeitszeitenInfoService.save(arbeitszeitenInfo);
        GehaltInfo gehaltInfo = new GehaltInfo();
        gehaltInfo.setCreatedBy(changedBy);
        gehaltInfo.setStatus(MitarbeiterStatus.NEW);
        gehaltInfo.setVertragsdaten(vertragsdaten);
        if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
            Kollektivvertrag kollektivvertrag = kollektivvertragService.findByName(KOLLEKTIVVERTRAG_TN_DEFAULT);
            Verwendungsgruppe verwendungsgruppe = verwendungsgruppeService.findByName(VERWENDUNGSGRUPPE_TN_DEFAULT);
            if (verwendungsgruppe != null) {
                gehaltInfo.setVerwendungsgruppe(verwendungsgruppe);
            }
            if (gehaltInfo.getVerwendungsgruppe() != null && kollektivvertrag != null) {
                gehaltInfo.getVerwendungsgruppe().setKollektivvertrag(kollektivvertrag);
            }
        } else {
            Beschaeftigungsstatus beschaeftigungsstatus = beschaeftigungsstatusService.findByName(BESCHAEFTIGUNGSSTATUS_TN_DEFAULT);
            if (beschaeftigungsstatus != null) {
                arbeitszeitenInfo.setBeschaeftigungsstatus(beschaeftigungsstatus);
                arbeitszeitenInfoService.save(arbeitszeitenInfo);
            }
        }
        gehaltInfoService.save(gehaltInfo);

        PayloadResponse response = new PayloadResponse();

        PayloadTypeList<VertragsdatenDto> vertragsdatenPayloadType = genericMAService.createPayloadForVertragsdaten(vertragsdatenService.mapVertragsdatenToDto(vertragsdaten), isMitarbeiter);
        if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, changedBy);
        } else {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, changedBy);
        }
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(vertragsdatenPayloadType, workflowDtoPayloadType));
        return response;
    }

    private Teilnehmer setTeilnehmerPersonalnummer(Teilnehmer teilnehmer, Personalnummer personalnummer) {
        teilnehmer.setPersonalnummer(personalnummer);
        return teilnehmerService.save(teilnehmer);
    }

    /**
     * Attempts to save Stammdaten and sends mail to IT, HR and FK that MA was created or failed depending on the outcome.
     *
     * @param stammdatenDto
     * @param workflowId
     * @param token
     * @return PayloadResponse containing stammdatenDto
     */
    @Override
    public PayloadResponse saveStammdaten(StammdatenDto stammdatenDto, Integer workflowId, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        PayloadResponse response = new PayloadResponse();
        WWorkflow workflow;
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(stammdatenDto.getPersonalnummer());
        String fullname = String.join(" ", stammdatenDto.getVorname(), stammdatenDto.getNachname());
        boolean isMitarbeiter = personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
        if (isMitarbeiter) {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(stammdatenDto.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        } else {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(stammdatenDto.getPersonalnummer(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        }
        log.info("Starting save stammdaten");
        if (workflow == null) {
            log.error(String.format("Could not find workflow for personal nummer:  %s", stammdatenDto.getPersonalnummer()));
            response.setSuccess(false);
            return response;
        }
        log.info("Sending data to the validation-service for stammdaten");
        StammdatenDto stammdatenDtoResponse = gateway2Validation.validateMitarbeiterStammdaten(stammdatenDto, true, changedBy);
        if (stammdatenDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", stammdatenDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        Optional<Stammdaten> stammdatenOptional = stammdatenService.findById(stammdatenDtoResponse.getId());
        if (!isMitarbeiter && stammdatenOptional.isPresent()) {
            Stammdaten stammdaten = stammdatenOptional.get();
            genericMAService.updateTeilnehmerStammdaten(stammdaten, changedBy);
        }
        String[] mailHRRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.HR.getValue()).toArray(new String[0]);
        String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);
        String[] mailITRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.IT.getValue()).toArray(new String[0]);
        if (stammdatenDtoResponse.getErrors().isEmpty()) {
            WWorkflow lastWorkflowInChain = wWorkflowService.findLastWorkflowInChain(workflow.getId());
            WWorkflow successorWF = workflow.getSuccessor();
            // Reset subsequent Workflows to Status NEW
            //TODO HERE
            log.info("Resetting subsequent workflows to status NEW");
            wfService.resetWfChainToStatus(lastWorkflowInChain, successorWF, WWorkflowStatus.NEW, changedBy);
            log.info("Validation successfully completed for stammdaten");
            if (isMitarbeiter) {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.STAMMDATEN_ERFASSEN, WWorkflowStatus.COMPLETED, changedBy);
                mailService.sendEmail("gateway-service.ma-im-system-anlegen-erfolgreich",
                        "german",
                        null,
                        Stream.of(mailHRRecipients, mailLohnverrechnungRecipients, mailITRecipients)
                                .flatMap(Arrays::stream)
                                .distinct().toArray(String[]::new),
                        toObjectArray(fullname),
                        toObjectArray(fullname, getDateAndTimeInEmailFormat(LocalDateTime.now()))
                );
            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_STAMMDATEN_ERFASSEN, WWorkflowStatus.COMPLETED, changedBy);
                manageWFAndWFIsStammdatenVertragsdatenForTN(workflow, changedBy);
            }
            if (isMitarbeiter && areStammAndVertragsdatenCompleted(workflow, true)) {
                genericMAService.manageKVCalculation(personalnummer.getPersonalnummer(), workflow, changedBy, true);
            }
        } else {
            if (isMitarbeiter) {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.STAMMDATEN_ERFASSEN, WWorkflowStatus.ERROR, changedBy);
                mailService.sendEmail("gateway-service.ma-im-system-anlegen-fehlgeschlagen",
                        "german",
                        null,
                        ArrayUtils.addAll(
                                mailHRRecipients, mailITRecipients
                        ),
                        toObjectArray(fullname),
                        toObjectArray(fullname, getDateAndTimeInEmailFormat(LocalDateTime.now()),
                                stammdatenDto.getErrors().stream().map(e -> "- " + e)
                                        .collect(Collectors.joining("\n")))
                );
            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_STAMMDATEN_ERFASSEN, WWorkflowStatus.ERROR, changedBy);
            }
        }

        PayloadTypeList<StammdatenDto> stammDatenPayloadType = stammdatenOptional.map(stammdaten -> genericMAService.createPayloadForStammdaten(stammdatenService.mapStammdatenToDto(stammdaten))).orElseGet(() -> genericMAService.createPayloadForStammdaten(new StammdatenDto()));
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(stammDatenPayloadType, workflowDtoPayloadType));
        return response;
    }

    private Vertragsdaten getVertragsdatenForTN(Personalnummer personalnummer, String changedBy) {
        Vertragsdaten vertragsdaten = new Vertragsdaten();
        vertragsdaten.setPersonalnummer(personalnummer);
        vertragsdaten.setStatus(MitarbeiterStatus.NEW);
        vertragsdaten.setCreatedBy(changedBy);
        if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
            Kategorie kategorie = kategorieService.findByName(KATEGORIE_TN_DEFAULT);
            if (kategorie != null) {
                vertragsdaten.setKategorie(kategorie);
            }
            Taetigkeit taetigkeit = taetigkeitService.findByName(TAETITGKEIT_TN_DEFAULT);
            if (taetigkeit != null) {
                vertragsdaten.setTaetigkeit(taetigkeit);
            }
        }
        return vertragsdaten;
    }

    @Override
    public PayloadResponse saveVertragsdaten(VertragsdatenDto vertragsDatenDto, Integer workflowId, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        PayloadResponse response = new PayloadResponse();
        WWorkflow workflow;
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(vertragsDatenDto.getPersonalnummer());
        boolean isMitarbeiter = personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
        if (isMitarbeiter) {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(vertragsDatenDto.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        } else {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(vertragsDatenDto.getPersonalnummer(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        }
        log.info("Starting save Vertragsdaten");
        if (workflow == null) {
            log.error("Could not find workflow for personal nummer:  {}", vertragsDatenDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        log.info("Sending data to the validation-service for Vertragsdaten");
        VertragsdatenDto vertragsDtoResponse = gateway2Validation.validateMitarbeiterVertragsdaten(vertragsDatenDto, true, changedBy);
        if (vertragsDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", vertragsDatenDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        if (genericMAService.isVertragsdatenWithoutError(vertragsDatenDto.getPersonalnummer(), true)) {
            log.info("Validation successfully completed for Vertragsdaten");
            log.info("Resetting subsequent workflows to status NEW");
            WWorkflow lastWorkflowInChain = wWorkflowService.findLastWorkflowInChain(workflow.getId());
            WWorkflow successorWF = workflow.getSuccessor();
            // Reset subsequent Workflows to Status NEW
            wfService.resetWfChainToStatus(lastWorkflowInChain, successorWF, WWorkflowStatus.NEW, changedBy);

            if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.COMPLETED, changedBy);
            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.COMPLETED, changedBy);
                manageWFAndWFIsStammdatenVertragsdatenForTN(workflow, changedBy);
            }
            if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER) && areStammAndVertragsdatenCompleted(workflow, true)) {
                genericMAService.manageKVCalculation(personalnummer.getPersonalnummer(), workflow, changedBy, true);
            }
        } else {
            if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.ERROR, changedBy);
            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.ERROR, changedBy);
            }
        }
        VertragsdatenDto vertragsdatenDtoWithStufe = vertragsdatenService.mapVertragsdatenToDto(vertragsdatenService.findById(vertragsDtoResponse.getId()).orElse(null));
        //TODO Remove this after FE Rework. Current workaround for FE Toggle.
        if (vertragsdatenDtoWithStufe != null) {
            vertragsdatenDtoWithStufe.setIsBefristet(vertragsDtoResponse.getIsBefristet());
        }
        PayloadTypeList<VertragsdatenDto> vertragsDatenDtoPayloadType = genericMAService.createPayloadForVertragsdaten(vertragsdatenDtoWithStufe, isMitarbeiter);

        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(vertragsDatenDtoPayloadType, workflowDtoPayloadType));
        return response;
    }

    private void manageWFAndWFIsStammdatenVertragsdatenForTN(WWorkflow workflow, String changedBy) {
        if (areStammAndVertragsdatenCompleted(workflow, false)) {
            manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN, WWorkflowStatus.COMPLETED, changedBy);
            WWorkflow wwLohnverrechnung = manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.TN_ONBOARDING_LOHNVERRECHNUNG, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
            manageWFItemsService.setWFItemStatus(wwLohnverrechnung, SWorkflowItems.TN_ONBOARDING_LOHNVERRECHUNG_INFORMIEREN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
        }
    }

    @Override
    public PayloadResponse informLohnverrechnung(String token, String personalnummerString) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
        PayloadResponse payloadResponse = new PayloadResponse();
        if (personalnummer == null) {
            payloadResponse.setSuccess(false);
            return payloadResponse;
        }
        WWorkflow workflow;
        if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummerString, SWorkflows.LOHNVERRECHNUNG);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.LOHNVERRECHUNG_INFORMIEREN, WWorkflowStatus.COMPLETED, changedBy);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MITARBEITERDATEN_PRUEFEN, WWorkflowStatus.IN_PROGRESS, changedBy);
            informLohnverrechung(workflow.getData(), true);
        } else {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummerString, SWorkflows.TN_ONBOARDING_LOHNVERRECHNUNG);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_LOHNVERRECHUNG_INFORMIEREN, WWorkflowStatus.COMPLETED, changedBy);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_MITARBEITERDATEN_PRUEFEN, WWorkflowStatus.IN_PROGRESS, changedBy);
            informLohnverrechung(workflow.getData(), false);
        }
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        payloadResponse.setData(List.of(workflowDtoPayloadType));
        payloadResponse.setSuccess(true);
        return payloadResponse;
    }


    private void informLohnverrechung(String personalNummer, boolean isMitarbeiter) {
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalNummer);
        if (stammdaten == null) {
            log.error("Can not inform the Lohnverrechnung since no stammdaten found for personal nummer: {}", personalNummer);
            return;
        }
        String name = String.join(" ", stammdaten.getVorname(), stammdaten.getNachname());
        LocalDate eintritt = vertragsdatenService.findEintrittByPersonalnummer(personalNummer);
        String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);
        mailService.sendEmail(
                isMitarbeiter ? "gateway-service.lhr.neuer-ma" : "gateway-service.lhr.neuer-teilnehmer-onboarding",
                "german", null,
                mailLohnverrechnungRecipients,
                toObjectArray(),
                toObjectArray(name, eintritt));

    }


    @Override
    public PayloadResponse getLvAcceptance(String personalnummer, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        WWorkflow workflow;
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(personalnummer);
        if (personalnummerEntity.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.LOHNVERRECHNUNG);
        } else {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.TN_ONBOARDING_LOHNVERRECHNUNG);
        }
        List<LvAcceptance> lvAcceptanceList = lvAcceptanceService.findByPersonalnummer(personalnummer).stream().filter(lv -> lv.getStatus().equals(MitarbeiterStatus.ACTIVE)).toList();
        PayloadTypeList<LvAcceptanceDto> lvAcceptanceDtoPayloadType;
        if (lvAcceptanceList.size() == 1) {
            lvAcceptanceDtoPayloadType = createPayloadForLvAcceptance(lvAcceptanceService.mapLvAcceptanceToDto(lvAcceptanceList.get(0)));
        } else {
            LvAcceptance lvAcceptance = new LvAcceptance();
            lvAcceptance.setPersonalnummer(personalnummerEntity);
            lvAcceptance.setStatus(MitarbeiterStatus.ACTIVE);
            lvAcceptance.setCreatedBy(changedBy);
            lvAcceptance = lvAcceptanceService.save(lvAcceptance);
            lvAcceptanceDtoPayloadType = createPayloadForLvAcceptance(lvAcceptanceService.mapLvAcceptanceToDto(lvAcceptance));
        }
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(Arrays.asList(lvAcceptanceDtoPayloadType, workflowDtoPayloadType));
        return response;
    }

    @Override
    public PayloadResponse saveLvAcceptance(LvAcceptanceDto lvAcceptanceDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        WWorkflow workflow;
        boolean isMitarbeiter = false;
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(lvAcceptanceDto.getPersonalnummer());
        if (personalnummerEntity.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
            isMitarbeiter = true;
            workflow = manageWFsService.getWorkflowFromDataAndWFType(lvAcceptanceDto.getPersonalnummer(), SWorkflows.LOHNVERRECHNUNG);
        } else {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(lvAcceptanceDto.getPersonalnummer(), SWorkflows.TN_ONBOARDING_LOHNVERRECHNUNG);
        }
        List<LvAcceptance> lvAcceptanceList = lvAcceptanceService.findByPersonalnummer(lvAcceptanceDto.getPersonalnummer()).stream().filter(lv -> lv.getStatus().equals(MitarbeiterStatus.ACTIVE)).toList();

        LvAcceptance lvAcceptance;
        if (lvAcceptanceList.isEmpty()) {
            lvAcceptance = lvAcceptanceService.mapLvAcceptanceDtoToEntity(lvAcceptanceDto, null);
            lvAcceptance.setStatus(MitarbeiterStatus.ACTIVE);
            lvAcceptance.setCreatedBy(changedBy);
            lvAcceptance = lvAcceptanceService.save(lvAcceptance);
        } else {
            lvAcceptance = lvAcceptanceService.mapLvAcceptanceDtoToEntity(lvAcceptanceDto, lvAcceptanceList.get(0));
            lvAcceptance.setChangedBy(changedBy);
            lvAcceptance = lvAcceptanceService.save(lvAcceptance);
        }

        updateStammdatenForLvAcceptance(lvAcceptance);
        PayloadResponse response = new PayloadResponse();
        PayloadTypeList<LvAcceptanceDto> lvAcceptanceDtoPayloadType = createPayloadForLvAcceptance(lvAcceptanceService.mapLvAcceptanceToDto(lvAcceptance));
        manageLohnverrechnungWFIItem(lvAcceptanceDto, workflow, changedBy, isMitarbeiter);
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(lvAcceptanceDtoPayloadType, workflowDtoPayloadType));
        return response;
    }

    private void updateStammdatenForLvAcceptance(LvAcceptance lvAcceptance) {
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(lvAcceptance.getPersonalnummer().getPersonalnummer());
        if (stammdaten == null) {
            log.error("Stammdaten not found for personalnummer {} while trying to update them for lv acceptance", lvAcceptance.getPersonalnummer().getPersonalnummer());
            return;
        }
        if (lvAcceptance.isEcard()) {
            stammdaten.setEcardStatus(BlobStatus.VERIFIED);
        }
        if (lvAcceptance.isBankcard()) {
            stammdaten.getBank().setCard(BlobStatus.VERIFIED);
        }
        if (lvAcceptance.isArbeitsgenehmigungDok()) {
            if (stammdaten.getZusatzInfo() != null) {
                stammdaten.getZusatzInfo().setArbeitsgenehmigungStatus(BlobStatus.VERIFIED);
            }
        }
        stammdatenService.save(stammdaten);
    }

    private void manageLohnverrechnungWFIItem(LvAcceptanceDto lvAcceptanceDto, WWorkflow workflow, String changedBy, boolean isMitarbeiter) {
        if (workflow != null) {
            WWorkflow workflowStammdatenVertragsdaten;
            SWorkflowItems mitarbeiterPruefenSWI;
            SWorkflowItems lohnverechnungInformierenSWI;
            String[] mailHRRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.HR.getValue()).toArray(new String[0]);
            if (isMitarbeiter) {
                workflowStammdatenVertragsdaten = manageWFsService.getWorkflowFromDataAndWFType(workflow.getData(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
                mitarbeiterPruefenSWI = SWorkflowItems.MITARBEITERDATEN_PRUEFEN;
                lohnverechnungInformierenSWI = SWorkflowItems.LOHNVERRECHUNG_INFORMIEREN;
                boolean areStammAndVertragsdatenCompleted = areStammAndVertragsdatenCompleted(workflowStammdatenVertragsdaten, isMitarbeiter);
                Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(lvAcceptanceDto.getPersonalnummer());
                String name = String.join(" ", stammdaten.getVorname(), stammdaten.getNachname());
                if (areStammAndVertragsdatenCompleted &&
                        lvAcceptanceDto.isBankcard()
                        && lvAcceptanceDto.isEcard() &&
                        !isArbeitsgenehmigungNeccessary(stammdaten, lvAcceptanceDto.isArbeitsgenehmigungDok())
                        && lvAcceptanceDto.isGehaltEinstufung()) {
                    //deleteFileService.deleteEcardAndBankcardAndArbeitsgenehmigung(lvAcceptanceDto.getPersonalnummer());
                    mailService.sendEmail("gateway-service.hr.neuer-ma-akzeptiert", "german", null, mailHRRecipients, toObjectArray(), toObjectArray(name));
                    manageWFItemsService.setWFItemStatus(workflow, mitarbeiterPruefenSWI, WWorkflowStatus.COMPLETED, changedBy);
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.DIENSTVERTRAG_ERSTELLEN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
                    createDienstvertrag(workflow, changedBy);
                } else {
                    mailService.sendEmail("gateway-service.hr.neuer-ma-abgelehnt", "german", null, mailHRRecipients, toObjectArray(), toObjectArray(name));
                    manageWFItemsService.setWFItemStatus(workflow, lohnverechnungInformierenSWI, WWorkflowStatus.NEW, changedBy);
                    manageWFItemsService.setWFItemStatus(workflow, mitarbeiterPruefenSWI, WWorkflowStatus.ERROR, changedBy);
                    updateStammdatenAndVertragsdatenWFI(workflowStammdatenVertragsdaten, stammdaten, lvAcceptanceDto, changedBy, isMitarbeiter);
                    manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, SWorkflowItems.KV_EINSTUFUNG_BERECHNEN, WWorkflowStatus.NEW, changedBy);
                }
            } else {
                workflowStammdatenVertragsdaten = manageWFsService.getWorkflowFromDataAndWFType(workflow.getData(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
                mitarbeiterPruefenSWI = SWorkflowItems.TN_ONBOARDING_MITARBEITERDATEN_PRUEFEN;
                lohnverechnungInformierenSWI = SWorkflowItems.TN_ONBOARDING_LOHNVERRECHUNG_INFORMIEREN;
                boolean areStammAndVertragsdatenCompleted = areStammAndVertragsdatenCompleted(workflowStammdatenVertragsdaten, isMitarbeiter);
                Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(lvAcceptanceDto.getPersonalnummer());
                if (areStammAndVertragsdatenCompleted &&
                        lvAcceptanceDto.isBankcard()
                        && lvAcceptanceDto.isEcard()) {
                    //deleteFileService.deleteEcardAndBankcardAndArbeitsgenehmigung(lvAcceptanceDto.getPersonalnummer());
                    mailService.sendEmail("gateway-service.hr.neuer-teilnehmer-onboarding-akzeptiert", "german", null, mailHRRecipients, toObjectArray(), toObjectArray());
                    manageWFItemsService.setWFItemStatus(workflow, mitarbeiterPruefenSWI, WWorkflowStatus.COMPLETED, changedBy);
                    manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.TN_ONBOARDING_LOHNVERRECHNUNG, WWorkflowStatus.COMPLETED, changedBy);
                    createLhrJob(lvAcceptanceDto.getPersonalnummer(), workflow.getWorkflowGroup(), changedBy);
                } else {
                    mailService.sendEmail("gateway-service.hr.neuer-teilnehmer-onboarding-abgelehnt", "german", null, mailHRRecipients, toObjectArray(), toObjectArray());
                    manageWFItemsService.setWFItemStatus(workflow, lohnverechnungInformierenSWI, WWorkflowStatus.NEW, changedBy);
                    manageWFItemsService.setWFItemStatus(workflow, mitarbeiterPruefenSWI, WWorkflowStatus.ERROR, changedBy);
                    updateStammdatenAndVertragsdatenWFI(workflowStammdatenVertragsdaten, stammdaten, lvAcceptanceDto, changedBy, isMitarbeiter);
                }
            }
        }
    }

    private void updateStammdatenAndVertragsdatenWFI(WWorkflow workflowStammdatenVertragsdaten, Stammdaten stammdaten, LvAcceptanceDto lvAcceptanceDto, String changedBy, boolean isMitarbeiter) {
        SWorkflowItems stammdatenErfassenSWI;
        SWorkflowItems vertragsdatenErfassenSWI;
        if (isMitarbeiter) {
            stammdatenErfassenSWI = SWorkflowItems.STAMMDATEN_ERFASSEN;
            vertragsdatenErfassenSWI = SWorkflowItems.VERTRAGSDATEN_ERFASSEN;
            manageWFsService.setWFStatus(workflowStammdatenVertragsdaten.getWorkflowGroup(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN, WWorkflowStatus.IN_PROGRESS, changedBy);
            if ((!lvAcceptanceDto.isBankcard() || !lvAcceptanceDto.isEcard() || isArbeitsgenehmigungNeccessary(stammdaten, lvAcceptanceDto.isArbeitsgenehmigungDok())) && !lvAcceptanceDto.isGehaltEinstufung()) {
                manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, stammdatenErfassenSWI, WWorkflowStatus.ERROR, changedBy);
                manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, vertragsdatenErfassenSWI, WWorkflowStatus.ERROR, changedBy);
            }
            if ((!lvAcceptanceDto.isBankcard() || !lvAcceptanceDto.isEcard() || isArbeitsgenehmigungNeccessary(stammdaten, lvAcceptanceDto.isArbeitsgenehmigungDok())) && lvAcceptanceDto.isGehaltEinstufung()) {
                manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, stammdatenErfassenSWI, WWorkflowStatus.ERROR, changedBy);
                manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, vertragsdatenErfassenSWI, WWorkflowStatus.COMPLETED, changedBy);
            }
            if ((lvAcceptanceDto.isBankcard() && lvAcceptanceDto.isEcard() && !isArbeitsgenehmigungNeccessary(stammdaten, lvAcceptanceDto.isArbeitsgenehmigungDok())) && !lvAcceptanceDto.isGehaltEinstufung()) {
                manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, stammdatenErfassenSWI, WWorkflowStatus.COMPLETED, changedBy);
                manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, vertragsdatenErfassenSWI, WWorkflowStatus.ERROR, changedBy);
            }
        } else {
            manageWFsService.setWFStatus(workflowStammdatenVertragsdaten.getWorkflowGroup(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN, WWorkflowStatus.IN_PROGRESS, changedBy);
            if (!lvAcceptanceDto.isBankcard() || !lvAcceptanceDto.isEcard()) {
                manageWFItemsService.setWFItemStatus(workflowStammdatenVertragsdaten, SWorkflowItems.TN_ONBOARDING_STAMMDATEN_ERFASSEN, WWorkflowStatus.ERROR, changedBy);
            }
        }
    }

    private void createLhrJob(String personalnummer, WWorkflowGroup workflowGroup, String changedBy) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(personalnummer).stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.error("Vertragsdaten could not be found for personalnummer {}", personalnummer);
            return;
        }
        LhrJob lhrJob = new LhrJob();
        lhrJob.setCreatedBy(GATEWAY_SERVICE);
        lhrJob.setCreatedOn(getLocalDateNow());
        lhrJob.setEintritt(vertragsdaten.getEintritt());
        lhrJob.setPersonalnummer(vertragsdaten.getPersonalnummer());
        lhrJob.setStatus(LhrJobStatus.PENDING);
        lhrJob.setJobType(SigningJobType.CONTRACT);
        lhrJobService.save(lhrJob);
        WWorkflow wWorkflowLHR = manageWFsService.setWFStatus(workflowGroup, SWorkflows.TN_ONBOARDING_LHR_DATEN_UEBERGEBEN, WWorkflowStatus.IN_PROGRESS, changedBy);
        manageWFItemsService.setWFItemStatus(wWorkflowLHR, SWorkflowItems.TN_ONBOARDING_DATEN_AN_LHR_UEBERGEBEN, WWorkflowStatus.IN_PROGRESS, changedBy);
    }

    private boolean isArbeitsgenehmigungNeccessary(Stammdaten stammdaten, boolean isArbeitsgenehmigungDok) {
        if (stammdaten != null && stammdaten.getStaatsbuergerschaft() != null) {
            if (!stammdaten.getStaatsbuergerschaft().getIsInEuEeaCh()) {
                return !isArbeitsgenehmigungDok;
            }
            return false;
        }
        return false;
    }

    private void createDienstvertrag(WWorkflow workflow, String changedBy) {
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(workflow.getData());
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerStringAndStatus(workflow.getData(),
                List.of(MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED)).stream().findFirst().orElse(null);
        MitarbeiterDto mitarbeiterDto = vertragsdatenService.mapStammdatenToMADto(stammdaten, vertragsdaten);
        String firma = stammdaten.getPersonalnummer().getFirma().getName();

        String subdirectory = getSubdirectoryForDocument(DIENSTVERTRAG);
        String directoryPath = firma + "/" + workflow.getData() + "/" + subdirectory;

        String filename = getFileName(workflow.getData(), null, FileUploadTypes.DIENSTVERTRAG, true) +
                "_" + stammdaten.getVorname().toUpperCase() + "_" + stammdaten.getNachname().toUpperCase() + PDF_EXTENSION;


        try {

            // Set report Params
            List<ReportParameterDto> reportParameterDtos = new ArrayList<>();
            ReportParameterDto personalNummerParam = new ReportParameterDto();
            personalNummerParam.setName("pnr");
            personalNummerParam.setType(ReportParamType.STRING);
            personalNummerParam.setValue(mitarbeiterDto.getPersonalnummer());
            reportParameterDtos.add(personalNummerParam);
            // Create report request DTO
            ReportRequestDto reportRequestDto = new ReportRequestDto();
            reportRequestDto.setReportName(DIENSTVERTAG_REPORT);
            reportRequestDto.setOutputFormat(ReportOutputFormat.PDF.name());
            reportRequestDto.setReportParameters(reportParameterDtos);

//            byte[] vertrag = jasperReportService.generateDv(mitarbeiterDto.getPersonalnummer(), reportRequestDto);
            ReportResponse reportResponse = jasperReportService.generateReport(reportRequestDto);
            byte[] vertrag = reportResponse.getReportBytes();
            // Convert byte[] to InputStream
            InputStream pdfInputStream = new ByteArrayInputStream(vertrag);
            fileShareService.emptyFolderFromFileShare(getFileShareTemp(), directoryPath);
            fileShareService.uploadOrReplaceInFileShare(
                    getFileShareTemp(),
                    directoryPath,
                    filename,
                    pdfInputStream,
                    (long) vertrag.length
            );

            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.DIENSTVERTRAG_ERSTELLEN, WWorkflowStatus.COMPLETED, GATEWAY_SERVICE);
            manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.LOHNVERRECHNUNG, WWorkflowStatus.COMPLETED, changedBy);
            WWorkflow workflowMoxis = manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG, WWorkflowStatus.IN_PROGRESS, changedBy);
            manageWFItemsService.setWFItemStatus(workflowMoxis, SWorkflowItems.UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
            stammdaten.setStatus(MitarbeiterStatus.VALIDATED);
            stammdaten.setChangedOn(getLocalDateNow());
            stammdatenService.save(stammdaten);
            vertragsdaten.setStatus(MitarbeiterStatus.VALIDATED);
            vertragsdatenService.save(vertragsdaten);
            String[] mailHRRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.HR.getValue()).toArray(new String[0]);
            mailService.sendEmail("gateway-service.hr.neuer-ma-dv-erstellt", "german", null,
                    mailHRRecipients,
                    toObjectArray(),
                    toObjectArray(String.join(" ", stammdaten.getVorname(), stammdaten.getNachname())
                            , createDienstvertragVorschauLUrl(stammdaten.getPersonalnummer().getPersonalnummer(), getNextAuthUrl())));
        } catch (Exception ex) {
            log.error("Error occurred during creating the contract for personalnummer {}, ", workflow.getData(), ex);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.DIENSTVERTRAG_ERSTELLEN, WWorkflowStatus.ERROR, GATEWAY_SERVICE);
        }
    }

    @Override
    public PayloadResponse saveVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(vordienstzeitenDto.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        PayloadResponse response = new PayloadResponse();
        VordienstzeitenDto vordienstzeitenDtoResponse = gateway2Validation.validateMitarbeiterVordienstzeiten(vordienstzeitenDto, changedBy);
        if (vordienstzeitenDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", vordienstzeitenDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        boolean validVertragsdaten = genericMAService.isVertragsdatenWithoutError(genericMAService.getVertragsdatenEntity(vordienstzeitenDto.getPersonalnummer(), true));
        if (validVertragsdaten && vordienstzeitenDtoResponse.getErrors().isEmpty()) {
            log.info("Validation successfully completed for Vordienstzeiten");
            boolean wwiStatus = areStammAndVertragsdatenCompleted(workflow, true);
            if (wwiStatus) {
                genericMAService.manageKVCalculation(vordienstzeitenDto.getPersonalnummer(), workflow, changedBy, true);
            }
            Optional<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findById(vordienstzeitenDtoResponse.getId());
            vordienstzeiten.ifPresent(value -> {
                value.setStatus(MitarbeiterStatus.VALIDATED);
                vordienstzeitenService.save(value);
            });
        } else if (!validVertragsdaten) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.ERROR, changedBy);
        }
        PayloadTypeList<VordienstzeitenDto> vordienstzeitenDtoPayloadType = genericMAService.createPayloadForSingleVordienstzeiten(vordienstzeitenDtoResponse);
        PayloadTypeList<VordienstzeitenDto> allVordienstzeitenDtoPayloadType = genericMAService.createPayloadForMultipleVordienstzeiten(genericMAService.getVertragsdatenEntity(vordienstzeitenDto.getPersonalnummer(), true));
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(vordienstzeitenDtoPayloadType, allVordienstzeitenDtoPayloadType, workflowDtoPayloadType));
        return response;
    }


    @Override
    public PayloadResponse saveUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(unterhaltsberechtigteDto.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        UnterhaltsberechtigteDto unterhaltsberechtigteDtoResponse = gateway2Validation.validateMitarbeiterUnterhaltsberechtigte(unterhaltsberechtigteDto, changedBy);
        PayloadResponse response = new PayloadResponse();
        if (unterhaltsberechtigteDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", unterhaltsberechtigteDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        if (genericMAService.isVertragsdatenWithoutError(genericMAService.getVertragsdatenEntity(unterhaltsberechtigteDto.getPersonalnummer(), true))) {
            log.info("Validation successfully completed for Unterhaltsberechtigte");
            boolean wwiStatus = areStammAndVertragsdatenCompleted(workflow, true);
            if (wwiStatus) {
                genericMAService.manageKVCalculation(unterhaltsberechtigteDto.getPersonalnummer(), workflow, changedBy, true);
            }
            if (unterhaltsberechtigteDtoResponse.getId() != null) {
                Optional<Unterhaltsberechtigte> unterhaltsberechtigte = unterhaltsberechtigteService.findById(unterhaltsberechtigteDtoResponse.getId());
                unterhaltsberechtigte.ifPresent(value -> {
                    value.setStatus(MitarbeiterStatus.VALIDATED);
                    unterhaltsberechtigteService.save(value);
                });
            }


        } else {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.ERROR, changedBy);
        }
        PayloadTypeList<UnterhaltsberechtigteDto> unterhaltsberechtigteDtoPayloadType = genericMAService.createPayloadForSingleUnterhaltsberechtigte(unterhaltsberechtigteDtoResponse);
        PayloadTypeList<UnterhaltsberechtigteDto> allUnterhaltsberechtigteDtoPayloadType = genericMAService.createPayloadForMultipleUnterhaltsberechtigte(genericMAService.getVertragsdatenEntity(unterhaltsberechtigteDto.getPersonalnummer(), true));
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(unterhaltsberechtigteDtoPayloadType, allUnterhaltsberechtigteDtoPayloadType, workflowDtoPayloadType));
        return response;
    }

    @Override
    public PayloadResponse getWorkflowgroup(String personalNummer) {
        PayloadResponse response = new PayloadResponse();
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalNummer);
        WWorkflow workflow = null;
        if (personalnummer != null) {
            if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
                workflow = manageWFsService.getWorkflowFromDataAndWFType(personalNummer, SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            } else {
                workflow = manageWFsService.getWorkflowFromDataAndWFType(personalNummer, SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            }
        }
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(List.of(workflowDtoPayloadType));
        return response;
    }

    private boolean areStammAndVertragsdatenCompleted(WWorkflow workflow, boolean isMitarbeiter) {
        List<WWorkflowItem> workflowItems;
        if (isMitarbeiter) {
            workflowItems = wWorkflowItemService.findAllByWorkflowId(workflow.getId()).stream().filter(wwi -> wwi.getWorkflowItem().getName().equals(SWorkflowItems.STAMMDATEN_ERFASSEN.getValue()) ||
                    wwi.getWorkflowItem().getName().equals(SWorkflowItems.VERTRAGSDATEN_ERFASSEN.getValue())).toList();
        } else {
            workflowItems = wWorkflowItemService.findAllByWorkflowId(workflow.getId()).stream().filter(wwi -> wwi.getWorkflowItem().getName().equals(SWorkflowItems.TN_ONBOARDING_STAMMDATEN_ERFASSEN.getValue()) ||
                    wwi.getWorkflowItem().getName().equals(SWorkflowItems.TN_ONBOARDING_VERTRAGSDATEN_ERFASSEN.getValue())).toList();
        }

        boolean wwiStatus = true;
        for (WWorkflowItem wwi : workflowItems) {
            wwiStatus = wwiStatus && wwi.getStatus().equals(WWorkflowStatus.COMPLETED);
        }
        return wwiStatus;
    }

    private PayloadTypeList<LvAcceptanceDto> createPayloadForLvAcceptance(LvAcceptanceDto lvAcceptanceDto) {
        PayloadTypeList<LvAcceptanceDto> lvAcceptanceDtoPayloadType = new PayloadTypeList<>(PayloadTypes.LV_ACCEPTANCE.getValue());
        lvAcceptanceDtoPayloadType.setAttributes(Collections.singletonList(lvAcceptanceDto));
        return lvAcceptanceDtoPayloadType;
    }

    @Override
    public ResponseEntity<byte[]> getZeitnachweisFile(String token, String date) {
        Personalnummer personalnummerObject = benutzerDetailsService.getUserFromToken(token).getPersonalnummer();
        if (personalnummerObject == null) {
            log.error("Personalnummer object not found");
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Personalnummer object not found for user-token")).build();
        }
        ResponseEntity<byte[]> fileResponse;
        try {
            fileResponse = lhrDokumenteService.downloadZeitnachweisForPersonalnummer(personalnummerObject.getId(), date);
        } catch (Exception ex) {
            log.error("Unproccessable entity {}", ex.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }

        if (!fileResponse.getStatusCode().is2xxSuccessful()) {
            log.error("lhr service returned status: {}", fileResponse.getStatusCode());
        }

        if (fileResponse.getBody() == null || isNullOrBlank(String.valueOf(fileResponse.getBody()))) {
            return ResponseEntity.noContent().build();
        }

        byte[] file = fileResponse.getBody();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentDisposition(fileResponse.getHeaders().getContentDisposition());
        responseHeaders.set("Content-Type", MediaType.APPLICATION_PDF_VALUE);
        return ResponseEntity.ok().headers(responseHeaders).body(file);
    }

}
