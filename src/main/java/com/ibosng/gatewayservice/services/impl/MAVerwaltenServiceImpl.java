package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragFixIbosService;
import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MAFilteredResultDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MASearchCriteriaDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MitarbeiterDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungMultiRequestDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungOverviewDto;
import com.ibosng.dbservice.dtos.workflows.WorkflowDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsaenderung;
import com.ibosng.dbservice.entities.mitarbeiter.VertragsaenderungStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.entities.reports.ReportParamType;
import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.SWorkflowItem;
import com.ibosng.dbservice.entities.workflows.SWorkflowStatus;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.UnterhaltsberechtigteService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsaenderungService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VordienstzeitenService;
import com.ibosng.dbservice.services.workflows.*;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.enums.Action;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.enums.ReportOutputFormat;
import com.ibosng.gatewayservice.enums.UserType;
import com.ibosng.gatewayservice.exceptions.BusinessLogicException;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.Gateway2Validation;
import com.ibosng.gatewayservice.services.GenericMAService;
import com.ibosng.gatewayservice.services.JasperReportService;
import com.ibosng.gatewayservice.services.MAVerwaltenService;
import com.ibosng.gatewayservice.services.WorkflowHelperService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Mappers.getSubdirectoryForDocument;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_ABBRECHEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_DATENVERVOLLSTAENDIGEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_PRUEFEN_GENEHMIGENDEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_PRUEFEN_LOHNVERRECHNUNG;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_PRUEFEN_PEOPLE;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_STARTEN;
import static com.ibosng.gatewayservice.utils.Constants.GATEWAY_SERVICE;
import static com.ibosng.gatewayservice.utils.Constants.VERTRAGSAENDERUNG_PRUFUNG_LINK;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.createPageable;
import static com.ibosng.gatewayservice.utils.Helpers.createPagination;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;
import static com.ibosng.microsoftgraphservice.utils.Helpers.updateSubdirectoryName;

@Service
@Slf4j
@Qualifier("maVerwaltenServiceImpl")
@RequiredArgsConstructor
public class MAVerwaltenServiceImpl implements MAVerwaltenService {

    @Getter
    @Value("${nextAuthUrl:#{null}}")
    private String nextAuthUrl;

    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    private final AzureSSOService azureSSOService;
    private final VertragsaenderungService vertragsaenderungService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final SWorkflowItemService sWorkflowItemService;
    private final WFService wfService;
    private final SWorkflowGroupService sWorkflowGroupService;
    private final MailService mailService;
    private final ArbeitsvertragFixIbosService arbeitsvertragFixIbosService;
    private final BenutzerService benutzerService;
    private final FileShareService fileShareService;
    private final JasperReportService jasperReportService;
    private final GenericMAService genericMAService;
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
    private final WWorkflowService wWorkflowService;
    private final AdresseIbosService adresseIbosService;

    public static final String DIENSTVERTRAG = "dienstvertrag";
    public static final String MOCK_ZUSATZ = "Mock_Zusatzdokument";
    private static final String PDF_EXTENSION = ".pdf";
    private static final String PLACEHOLDER_REPORT = "Dienstvertrag";

    @Override
    public PayloadResponse findMAByCriteria(String searchTerm,
                                            String wohnort,
                                            List<String> firmen,
                                            List<String> kostenstellen,
                                            List<String> beschaeftigungstatusen,
                                            List<String> jobbezeichnungen,
                                            List<String> kategorien,
                                            String sortBy,
                                            String direction,
                                            int page,
                                            int size) {
        if (isNullOrBlank(sortBy)) {
            sortBy = "nachname";
        }
        MASearchCriteriaDto searchCriteria = new MASearchCriteriaDto(searchTerm, wohnort, firmen, kostenstellen, beschaeftigungstatusen, jobbezeichnungen, kategorien);
        Pageable pageable = createPageable(sortBy, direction, page, size);
        Page<MAFilteredResultDto> maFilteredResultDtos = stammdatenService.findMAByCriteria(searchCriteria, pageable);
        PayloadTypeList<MAFilteredResultDto> mitarbeiterSummaryDtoPayloadType = new PayloadTypeList<>(PayloadTypes.MA_FILTERED.getValue());
        mitarbeiterSummaryDtoPayloadType.setAttributes(maFilteredResultDtos.getContent());
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setData(List.of(mitarbeiterSummaryDtoPayloadType));
        payloadResponse.setSuccess(true);
        payloadResponse.setPagination(createPagination(maFilteredResultDtos));
        return payloadResponse;
    }

    @Override
    public PayloadResponse findVertragsaenderungenByCriteria(String searchTerm, List<String> statuses, String sortBy, String direction, int page, int size) {
        if (isNullOrBlank(sortBy)) {
            sortBy = "nachname";
        }
        Pageable pageable = createPageable(sortBy, direction, page, size);
        Page<VertragsaenderungOverviewDto> vertragsaenderungOverviewDtos = vertragsaenderungService.findAllOrderedAndFilteredForOverview(searchTerm, statuses, pageable);
        PayloadTypeList<VertragsaenderungOverviewDto> overviewDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.VERTRAGSAENDERUNG_FILTERED.getValue());
        overviewDtoPayloadTypeList.setAttributes(vertragsaenderungOverviewDtos.getContent());
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setData(List.of(overviewDtoPayloadTypeList));
        payloadResponse.setSuccess(true);
        payloadResponse.setPagination(createPagination(vertragsaenderungOverviewDtos));
        return payloadResponse;
    }

    private void createZusatzdokument(WWorkflow workflow) {

        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(workflow.getData());

        String firma = stammdaten.getPersonalnummer().getFirma().getName();
        String vorname = stammdaten.getVorname();
        String nachname = stammdaten.getNachname();

        String updatedDirectoryName = updateSubdirectoryName(workflow.getData(), vorname, nachname);
        String subdirectoryPath = firma + "/" + updatedDirectoryName + "/" + getSubdirectoryForDocument(DIENSTVERTRAG);
        String filename = MOCK_ZUSATZ + PDF_EXTENSION;

//        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(workflow.getData());
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerStringAndStatus(workflow.getData(),
                List.of(MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED)).stream().findFirst().orElse(null);
        MitarbeiterDto mitarbeiterDto = vertragsdatenService.mapStammdatenToMADto(stammdaten, vertragsdaten);
//        String firma = stammdaten.getPersonalnummer().getFirma().getName();

//        String subdirectory = getSubdirectoryForDocument(DIENSTVERTRAG);
        String directoryPath = firma + "/" + workflow.getData() + "/" + subdirectoryPath;


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
            reportRequestDto.setReportName(PLACEHOLDER_REPORT);
            reportRequestDto.setOutputFormat(ReportOutputFormat.PDF.name());
            reportRequestDto.setReportParameters(reportParameterDtos);

            ReportResponse reportResponse = jasperReportService.generateReport(reportRequestDto);
            byte[] zusatzDokument = reportResponse.getReportBytes();
            // Convert byte[] to InputStream
            InputStream pdfInputStream = new ByteArrayInputStream(zusatzDokument);
            fileShareService.uploadOrReplaceInFileShare(
                    getFileSharePersonalunterlagen(),
                    directoryPath,
                    filename,
                    pdfInputStream,
                    zusatzDokument.length
            );


            //TODO Check if WFs need to be updated at this point
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_ZUSATZDOKUMENT,
                    WWorkflowStatus.COMPLETED, GATEWAY_SERVICE);

        } catch (Exception ex) {
            log.error("Error occurred during creating the contract for personalnummer {}, ", workflow.getData(), ex);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_ZUSATZDOKUMENT, WWorkflowStatus.ERROR, GATEWAY_SERVICE);
        }
    }

    public ReportResponse downloadVertragsaenderungFile(String personalnummer){
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("jasperReports");
        } catch (IOException e) {
            log.error("Unable to create temp dir for downloading Zusatz Mock");
            throw new RuntimeException(e);
        }
        log.debug("Temporary directory created: {}", tempDir);

        String firma = stammdaten.getPersonalnummer().getFirma().getName();
        String vorname = stammdaten.getVorname();
        String nachname = stammdaten.getNachname();
        String updatedDirectoryName = updateSubdirectoryName(personalnummer, vorname, nachname);
        String subdirectoryPath = firma + "/" + updatedDirectoryName + "/" + getSubdirectoryForDocument(DIENSTVERTRAG);
        String filename = MOCK_ZUSATZ + PDF_EXTENSION;
        fileShareService.downloadFiles(subdirectoryPath, tempDir, getFileSharePersonalunterlagen());
        Path mockFile = tempDir.resolve(filename);
        try {
            InputStream inputStream = Files.newInputStream(mockFile);

            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setReportBytes(inputStream.readAllBytes());
            reportResponse.setReportName(filename);
            reportResponse.setOutputFormat(ReportOutputFormat.PDF);
            JasperReportServiceImpl.cleanUp(tempDir);

            return reportResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Perform vertragsaenderung and send email to responsbile parties.
     *
     * @param vertragsaenderungMultiRequestDto
     * @param token
     * @return Array list of vertragsaenderungDto, vertragsDatenDtoPayloadType and workflowDtoPayloadType
     */
    @Override
    public PayloadResponse performVertragsaenderung(VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto,
                                                    String token) {
        final Benutzer antragssteller = benutzerDetailsService.getUserFromToken(token);
        final String changedBy = (antragssteller != null) ? antragssteller.getEmail() : GATEWAY_SERVICE;
        String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);

        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(vertragsaenderungMultiRequestDto
                .getVertragsaenderungDto().getPersonalnummer());

        if (personalnummer == null) {
            throw new BusinessLogicException("Personalnummer must not be null");
        }

        // starting workflow for MA vertragsaenderung
        WWorkflowGroup wWorkflowGroup = wfStartService.createWorkflowGroupAndInstances(
                SWorkflowGroups.MA_VD_VERAENDERUNG.getValue(), personalnummer.getPersonalnummer(), changedBy);

        manageWFsService.setWFStatus(
                wWorkflowGroup, SWorkflows.MA_VD_VEREINBARUNG_STARTEN, WWorkflowStatus.IN_PROGRESS, changedBy);

        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer.getPersonalnummer(),
                SWorkflows.MA_VD_VEREINBARUNG_STARTEN);

        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN,
                WWorkflowStatus.IN_PROGRESS, changedBy);

        // getting the latest vertragsdatenDto
        Vertragsdaten oldVertragsdaten = vertragsdatenService.findByPersonalnummerStringAndStatus(
                vertragsaenderungMultiRequestDto.getVertragsdatenDto().getPersonalnummer(), List.of(MitarbeiterStatus.ACTIVE)).stream().findFirst().orElse(null);

        if (oldVertragsdaten == null) {
            log.warn("No active vertragsdaten found for personalnummer {}", vertragsaenderungMultiRequestDto.getVertragsdatenDto().getPersonalnummer());
            return PayloadResponse.builder().success(false).message("No active vertragsdaten found!").build();
        }

        List<FieldChangeDto> fieldChangesDtos = vertragsaenderungService.compareVertragsdaten(
                vertragsdatenService.mapVertragsdatenToDto(oldVertragsdaten), vertragsaenderungMultiRequestDto.getVertragsdatenDto());
        vertragsaenderungMultiRequestDto.getVertragsaenderungDto().setFieldChanges(fieldChangesDtos);

        if (fieldChangesDtos.isEmpty() && vertragsaenderungMultiRequestDto.getVertragsaenderungDto().getId() == null) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN,
                    WWorkflowStatus.CANCELED, changedBy);

            return PayloadResponse.builder().success(false).message("No changes were made").build();
        }

        if (Objects.equals(oldVertragsdaten.getId(), vertragsaenderungMultiRequestDto.getVertragsdatenDto().getId())) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN,
                    WWorkflowStatus.CANCELED, changedBy);

            return PayloadResponse.builder().success(false).message("Successor and predecessor are same").build();
        }

        VertragsdatenDto validatedVertragsdaten = gateway2Validation.validateMitarbeiterVertragsdaten(vertragsaenderungMultiRequestDto.getVertragsdatenDto(), false, changedBy);
        Optional<Vertragsdaten> optionalVertragsdaten = vertragsdatenService.findById(validatedVertragsdaten.getId());

        if (optionalVertragsdaten.isEmpty()) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN,
                    WWorkflowStatus.CANCELED, changedBy);

            return PayloadResponse.builder().success(false).message("Validation failed for vertragsdaten!").build();
        }

        Vertragsdaten newVertragsdaten = optionalVertragsdaten.get();

        Vertragsaenderung vertragsaenderung = null;
        if (vertragsaenderungMultiRequestDto.getVertragsaenderungDto().getId() != null && vertragsaenderungService.findById(vertragsaenderungMultiRequestDto.getVertragsaenderungDto().getId()).isPresent()) {
            vertragsaenderung = vertragsaenderungService.updateVertragsaenderung(vertragsaenderungMultiRequestDto.getVertragsaenderungDto());
        } else {
            vertragsaenderung = vertragsaenderungService.map(vertragsaenderungMultiRequestDto.getVertragsaenderungDto(), antragssteller, oldVertragsdaten, newVertragsdaten);
            vertragsaenderung.setCreatedBy(changedBy);

            if (vertragsaenderung != null
                    && vertragsaenderung.getPredecessor() != null
                    && vertragsaenderung.getPredecessor().getKostenstelle() != null) {

                Integer id = vertragsaenderung.getPredecessor().getKostenstelle().getNummer();

                if (id != null) {
                    String upn = adresseIbosService.findKostenstelle2UpnByKostenstelleId(id);

                    if (upn != null && !upn.isBlank()) {
                        Benutzer benutzer = benutzerService.findByUpn(upn);

                        if (benutzer == null) {
                            gateway2Validation.validateSyncMitarbeiterWithUPN(upn);
                            benutzer = benutzerService.findByUpn(upn);
                        }

                        vertragsaenderung.setGenehmigender(benutzer);
                    }
                }
            }

            vertragsaenderungService.save(vertragsaenderung);
            vertragsaenderungService.setEmails(vertragsaenderung, vertragsaenderungMultiRequestDto.getVertragsaenderungDto());
        }

        VertragsaenderungDto vertragsaenderungDto = vertragsaenderungService.mapToVetragsaenderungDto(vertragsaenderung);

        if (!vertragsaenderungDto.getErrors().isEmpty() && newVertragsdaten.getStatus().equals(MitarbeiterStatus.VALIDATED)) {
            vertragsaenderung.setStatus(VertragsaenderungStatus.CANCELED);
            vertragsaenderungService.save(vertragsaenderung);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN,
                    WWorkflowStatus.ERROR, changedBy);
        } else {
            gateway2Validation.calculateKVEinstufung(personalnummer.getPersonalnummer(), changedBy, false);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN,
                    WWorkflowStatus.COMPLETED, changedBy);
            String nameFK = String.join(" ", vertragsaenderung.getGenehmigender().getFirstName(), vertragsaenderung.getGenehmigender().getLastName());
            Benutzer benutzerMA = benutzerService.findByPersonalnummerAndFirmaBmdClient(vertragsaenderung.getSuccessor().getPersonalnummer().getPersonalnummer(),
                    vertragsaenderung.getSuccessor().getPersonalnummer().getFirma().getBmdClient());
            String nameMA = "";
            if (benutzerMA != null) {
                nameMA = String.join(" ", benutzerMA.getFirstName(), benutzerMA.getLastName());
            }

            Integer swiId = sWorkflowItemService.findAllByWorkflowIdAndStatus(workflow.getWorkflow().getId(), SWorkflowStatus.ACTIVE)
                    .stream().filter(swi -> Objects.equals(swi.getName(), SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN.getValue()))
                    .map(SWorkflowItem::getId).findFirst().orElse(0);
            mailService.sendEmail("gateway-service.ma-verwaltung-beteiligten-informieren",
                    "german",
                    null,
                    ArrayUtils.addAll(
                            new String[]{vertragsaenderung.getGenehmigender().getEmail()},
                            mailLohnverrechnungRecipients),
                    toObjectArray(nameFK, nameMA),
                    toObjectArray(nameFK, nameMA, vertragsaenderung.getId(),
                            vertragsaenderung.getGueltigAb(),
                            toObjectArray(VERTRAGSAENDERUNG_PRUFUNG_LINK.formatted(getNextAuthUrl(), vertragsaenderung.getId(), swiId))));
        }


        if (optionalVertragsdaten.get().getStatus().equals(MitarbeiterStatus.VALIDATED) && !vertragsaenderung.getStatus().equals(VertragsaenderungStatus.CANCELED)) {

            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_ZUSATZDOKUMENT,
                    WWorkflowStatus.IN_PROGRESS, changedBy);

            //todo add creation of zusatz dokumente from jasper!!!
            createZusatzdokument(workflow);

//            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_ZUSATZDOKUMENT,
//                    WWorkflowStatus.COMPLETED, changedBy);

            String[] people = ArrayUtils.addAll(
                    vertragsaenderung.getAllRecipients().stream().map(Benutzer::getEmail).toArray(String[]::new),
                    mailLohnverrechnungRecipients
            );

            Benutzer ben = benutzerService.findByPersonalnummerAndFirmaBmdClient(vertragsaenderung.getPersonalnummer().getPersonalnummer(),
                    vertragsaenderung.getPersonalnummer().getFirma().getBmdClient());

            if (ben != null) {
                String nameMA = String.join(" ", ben.getFirstName(), ben.getLastName());

                mailService.sendEmail("gateway-service.ma-zusatz-vereinbart-info",
                        "german",
                        null,
                        people,
                        toObjectArray(nameMA),
                        toObjectArray(nameMA,
                                vertragsaenderungDto.getFieldChanges().stream()
                                        .map(f -> f.getFieldName() + ": " + f.getOldValue() + " -> " + f.getNewValue())
                                        .collect(Collectors.joining("/n"))
                        )
                );
            }

            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_PRUEFEN,
                    WWorkflowStatus.IN_PROGRESS, changedBy);
        }

        vertragsaenderung.setStatus(VertragsaenderungStatus.IN_PROGRESS);
        vertragsaenderungService.save(vertragsaenderung);

        PayloadTypeList<VertragsaenderungDto> vertragsaenderungDtoPayloadType = new PayloadTypeList<>(
                PayloadTypes.VERTRAGSAENDERUNG.getValue(),
                List.of(vertragsaenderungDto));

        PayloadTypeList<VertragsdatenDto> vertragsDatenDtoPayloadType =
                new PayloadTypeList<>(PayloadTypes.VERTRAGSDATEN.getValue(), List.of(vertragsdatenService.mapVertragsdatenToDto(newVertragsdaten)));
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vertragsaenderungDtoPayloadType, vertragsDatenDtoPayloadType, workflowDtoPayloadType))
                .build();
    }

    @Override
    public PayloadResponse getVertragsaenderung(Integer vertragsaenderungId) {

        Optional<Vertragsaenderung> vertragsaenderung = vertragsaenderungService.findById(vertragsaenderungId);
        if (vertragsaenderung.isEmpty()) {
            return PayloadResponse.builder()
                    .success(false)
                    .message("No Vertragsaenderung for personalnummer %s".formatted(vertragsaenderungId))
                    .build();
        }

        String personalnummer = vertragsaenderung.get().getPersonalnummer().getPersonalnummer();
        Vertragsdaten vertragsdaten = vertragsaenderung.get().getSuccessor();
        if (vertragsdaten == null) {
            return PayloadResponse.builder()
                    .success(false)
                    .message("No Vertragsdaten for personalnummer %s".formatted(personalnummer))
                    .build();
        }

        VertragsaenderungDto vertragsaenderungDto = vertragsaenderungService.mapToVetragsaenderungDto(vertragsaenderung.get());

        PayloadTypeList<VertragsaenderungDto> vertragsaenderungDtoPayloadTypeList = new PayloadTypeList<>(
                PayloadTypes.VERTRAGSAENDERUNG.getValue(), List.of(vertragsaenderungDto)
        );
        PayloadTypeList<VertragsdatenDto> vertragsdatenDtoPayloadTypeList = new PayloadTypeList<>(
                PayloadTypes.VERTRAGSDATEN.getValue(), List.of(vertragsdatenService.mapVertragsdatenToDto(vertragsdaten))
        );

        SWorkflowGroup sWorkflowGroup = sWorkflowGroupService.findByName(SWorkflowGroups.MA_VD_VERAENDERUNG.getValue());
        WWorkflowGroup workflowGroup = wWorkflowGroupService.findAllByDataAndWorkflowGroup(personalnummer, sWorkflowGroup).stream().filter(wwg -> WWorkflowStatus.IN_PROGRESS.equals(wwg.getStatus())).findFirst().orElse(null);
        if (workflowGroup != null) {
            PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflowGroup(workflowGroup);
            return PayloadResponse.builder()
                    .success(true)
                    .data(List.of(vertragsaenderungDtoPayloadTypeList, vertragsdatenDtoPayloadTypeList, workflowDtoPayloadType))
                    .build();
        }

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vertragsaenderungDtoPayloadTypeList, vertragsdatenDtoPayloadTypeList))
                .build();
    }

    @Override
    public ResponseEntity<PayloadResponse> acceptVertragsaenderung(Action action, UserType userType, Integer id, VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto, String authorizationHeader) {
        //CHECK FOR PERMISSIONS
        if (Action.DECLINE.equals(action) && !benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_ABBRECHEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else if (Action.ACCEPT.equals(action)) {
            if (UserType.PEOPLE.equals(userType) && !benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_PRUEFEN_PEOPLE))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (UserType.LHR.equals(userType) && !benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_PRUEFEN_LOHNVERRECHNUNG))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (UserType.GENEHMIGENDER.equals(userType) && !benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_PRUEFEN_GENEHMIGENDEN))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (UserType.CREATOR_PRUEFUNG.equals(userType) && !benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_STARTEN))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else if (Action.SAVE.equals(action) && !benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_DATENVERVOLLSTAENDIGEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String token = getTokenFromAuthorizationHeader(authorizationHeader);

        //Logic
        Vertragsaenderung vertragsaenderung = vertragsaenderungService.findById(id).orElse(null);
        if (vertragsaenderung == null) {
            return ResponseEntity.ok(PayloadResponse.builder().message("Vertragsaenderung not found").success(false).build());
        }
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();

        return checkResultIfNull(switch (userType) {
            case PEOPLE -> peopleAction(action, vertragsaenderung, vertragsaenderungMultiRequestDto, token);
            case LHR -> lhrAction(action, vertragsaenderung, vertragsaenderungMultiRequestDto, token);
            case GENEHMIGENDER -> genehmigenderAction(action, vertragsaenderung, changedBy);
            case CREATOR_PRUEFUNG -> creatorAction(action, vertragsaenderung, changedBy);
        });
    }

    private PayloadResponse peopleAction(Action action, Vertragsaenderung vertragsaenderung, VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto, String token) {
        boolean isSuccess = false;
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        if (vertragsaenderung.getPersonalnummer() == null || isNullOrBlank(vertragsaenderung.getPersonalnummer().getPersonalnummer())) {
            return PayloadResponse.builder().message("Personalnummer is null or blank in vertragsaenderung").success(false).build();
        }

        String personalnummer = vertragsaenderung.getPersonalnummer().getPersonalnummer();

        WWorkflow peopleWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_PEOPLE);
        if (WWorkflowStatus.IN_PROGRESS.equals(peopleWorkflow.getStatus())) {
            if (Action.ACCEPT.equals(action)) {
                String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);

                manageWFItemsService.setWFItemStatus(peopleWorkflow, SWorkflowItems.MA_VD_PEOPLE_PRUEFT, WWorkflowStatus.COMPLETED, changedBy);
                WWorkflow lhrWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_LOHNVERRECHNUNG);
                WWorkflowGroup group = lhrWorkflow.getWorkflowGroup();
                manageWFsService.setWFStatus(group, SWorkflows.MA_VD_LOHNVERRECHNUNG, WWorkflowStatus.IN_PROGRESS, changedBy);
                manageWFItemsService.setWFItemStatus(lhrWorkflow, SWorkflowItems.MA_VD_LOHNVERRECHNUNG_INFORMIEREN, WWorkflowStatus.IN_PROGRESS, changedBy);
                String[] lhr = ArrayUtils.addAll(
                        vertragsaenderung.getAllRecipients().stream().map(Benutzer::getEmail).toArray(String[]::new),
                        mailLohnverrechnungRecipients);

                Integer swiId = sWorkflowItemService.findAllByWorkflowIdAndStatus(lhrWorkflow.getWorkflow().getId(), SWorkflowStatus.ACTIVE)
                        .stream().filter(swi -> Objects.equals(swi.getName(), SWorkflowItems.MA_VD_LOHNVERRECHNUNG_PRUEFT.getValue()))
                        .map(SWorkflowItem::getId).findFirst().orElse(0);

                mailService.sendEmail("gateway-service.vertragsaenderung.lohnverrechnung-informiren", "german", null, lhr, toObjectArray(vertragsaenderung.getId()),
                        toObjectArray(toObjectArray(VERTRAGSAENDERUNG_PRUFUNG_LINK.formatted(getNextAuthUrl(), vertragsaenderung.getId(), swiId))));

                manageWFItemsService.setWFItemStatus(lhrWorkflow, SWorkflowItems.MA_VD_LOHNVERRECHNUNG_INFORMIEREN, WWorkflowStatus.COMPLETED, changedBy);
                manageWFItemsService.setWFItemStatus(lhrWorkflow, SWorkflowItems.MA_VD_LOHNVERRECHNUNG_PRUEFT, WWorkflowStatus.IN_PROGRESS, changedBy);
                isSuccess = true;
            }
            if (Action.DECLINE.equals(action)) {
                String[] antragsteller = new String[]{vertragsaenderung.getAntragssteller().getEmail()};
                WWorkflowItem wWorkflowItem = manageWFItemsService.setWFItemStatus(peopleWorkflow, SWorkflowItems.MA_VD_PEOPLE_PRUEFT, WWorkflowStatus.COMPLETED, changedBy);

                mailService.sendEmail("gateway-service.vertragsaenderung.people-lehnt-ab", "german", null, antragsteller, toObjectArray(vertragsaenderung.getId()),
                        toObjectArray(VERTRAGSAENDERUNG_PRUFUNG_LINK.formatted(getNextAuthUrl(), vertragsaenderung.getId(), wWorkflowItem.getWorkflowItem().getId())));

                manageWFItemsService.setWFItemStatus(peopleWorkflow, SWorkflowItems.MA_VD_PEOPLE_PRUEFT, WWorkflowStatus.CANCELED, changedBy);
            }
            if (Action.SAVE.equals(action)) {
                if (vertragsaenderungMultiRequestDto == null) {
                    return PayloadResponse.builder().success(false).message("Request body should contain data in order to apply changes").build();
                }

                WWorkflow startenWf = peopleWorkflow;
                while (!startenWf.getWorkflow().getName().equals(SWorkflows.MA_VD_VEREINBARUNG_STARTEN.getValue())) {
                    startenWf = startenWf.getPredecessor();
                }
                wfService.resetWfChainToStatus(peopleWorkflow, startenWf, WWorkflowStatus.NEW, changedBy);
                manageWFsService.setWFStatus(peopleWorkflow.getWorkflowGroup(), SWorkflows.MA_VD_VEREINBARUNG_STARTEN, WWorkflowStatus.IN_PROGRESS, changedBy);

                return performVertragsaenderung(vertragsaenderungMultiRequestDto, token);
            }
            manageWFsService.setWFStatus(peopleWorkflow.getWorkflowGroup(), SWorkflows.MA_VD_PEOPLE, WWorkflowStatus.COMPLETED, changedBy);
        }

        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(peopleWorkflow);

        return PayloadResponse.builder()
                .success(isSuccess)
                .data(List.of(workflowDtoPayloadType))
                .build();
    }

    private PayloadResponse lhrAction(Action action, Vertragsaenderung vertragsaenderung, VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto, String token) {
        boolean isSuccess = false;
        if (vertragsaenderung.getPersonalnummer() == null || isNullOrBlank(vertragsaenderung.getPersonalnummer().getPersonalnummer())) {
            return PayloadResponse.builder().message("Personalnummer is null or blank in vertragsaenderung").success(false).build();
        }
        String personalnummer = vertragsaenderung.getPersonalnummer().getPersonalnummer();
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        WWorkflow lhrWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_LOHNVERRECHNUNG);
        if (WWorkflowStatus.IN_PROGRESS.equals(lhrWorkflow.getStatus())) {
            if (Action.ACCEPT.equals(action)) {
                manageWFItemsService.setWFItemStatus(lhrWorkflow, SWorkflowItems.MA_VD_LOHNVERRECHNUNG_PRUEFT, WWorkflowStatus.COMPLETED, changedBy);
                WWorkflow genehmigenWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_GEHNEHMIGEN);
                WWorkflowGroup group = genehmigenWorkflow.getWorkflowGroup();
                manageWFsService.setWFStatus(group, SWorkflows.MA_VD_GEHNEHMIGEN, WWorkflowStatus.IN_PROGRESS, changedBy);
                manageWFItemsService.setWFItemStatus(genehmigenWorkflow, SWorkflowItems.MA_VD_GENEHHMIGER_INFORMIEREN, WWorkflowStatus.IN_PROGRESS, changedBy);

                Integer swiId = sWorkflowItemService.findAllByWorkflowIdAndStatus(genehmigenWorkflow.getWorkflow().getId(), SWorkflowStatus.ACTIVE)
                        .stream().filter(swi -> Objects.equals(swi.getName(), SWorkflowItems.MA_VD_GENEHMIGER_PRUEFT.getValue()))
                        .map(SWorkflowItem::getId).findFirst().orElse(0);

                if (vertragsaenderung.getGenehmigender() != null) {
                    String[] genehmigenden = ArrayUtils.addAll(new String[]{vertragsaenderung.getGenehmigender().getEmail()}, vertragsaenderung.getAllRecipients().stream().map(Benutzer::getEmail).toArray(String[]::new));

                    mailService.sendEmail("gateway-service.vertragsaenderung.genehmigenden-informiren", "german", null, genehmigenden, toObjectArray(vertragsaenderung.getId()),
                            toObjectArray(VERTRAGSAENDERUNG_PRUFUNG_LINK.formatted(getNextAuthUrl(), vertragsaenderung.getId(), swiId)));

                    manageWFItemsService.setWFItemStatus(genehmigenWorkflow, SWorkflowItems.MA_VD_GENEHHMIGER_INFORMIEREN, WWorkflowStatus.COMPLETED, changedBy);
                    manageWFItemsService.setWFItemStatus(genehmigenWorkflow, SWorkflowItems.MA_VD_GENEHMIGER_PRUEFT, WWorkflowStatus.IN_PROGRESS, changedBy);
                    isSuccess = true;
                } else {
                    return PayloadResponse.builder().success(false).message("No genehmigenden found!").build();
                }
            }
            if (Action.DECLINE.equals(action)) {
                String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);

                String[] people = ArrayUtils.addAll(
                        vertragsaenderung.getAllRecipients().stream().map(Benutzer::getEmail).toArray(String[]::new),
                        mailLohnverrechnungRecipients);
                mailService.sendEmail("gateway-service.vertragsaenderung.lohnverrechnung-lehnt-ab", "german", null, people, toObjectArray(vertragsaenderung.getId()), toObjectArray());

                manageWFItemsService.setWFItemStatus(lhrWorkflow, SWorkflowItems.MA_VD_LOHNVERRECHNUNG_PRUEFT, WWorkflowStatus.CANCELED, changedBy);
            }
            if (Action.SAVE.equals(action)) {
                if (vertragsaenderungMultiRequestDto == null) {
                    return PayloadResponse.builder().success(false).message("Request body should contain data in order to apply changes").build();
                }
                WWorkflow startenWf = lhrWorkflow;
                while (!startenWf.getWorkflow().getName().equals(SWorkflows.MA_VD_VEREINBARUNG_STARTEN.getValue())) {
                    startenWf = startenWf.getPredecessor();
                }
                wfService.resetWfChainToStatus(lhrWorkflow, startenWf, WWorkflowStatus.NEW, changedBy);
                manageWFsService.setWFStatus(lhrWorkflow.getWorkflowGroup(), SWorkflows.MA_VD_VEREINBARUNG_STARTEN, WWorkflowStatus.IN_PROGRESS, changedBy);

                return performVertragsaenderung(vertragsaenderungMultiRequestDto, token);
            }
            manageWFsService.setWFStatus(lhrWorkflow.getWorkflowGroup(), SWorkflows.MA_VD_LOHNVERRECHNUNG, WWorkflowStatus.COMPLETED, changedBy);
        }

        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(lhrWorkflow);

        return PayloadResponse.builder()
                .success(isSuccess)
                .data(List.of(workflowDtoPayloadType))
                .build();
    }

    private PayloadResponse genehmigenderAction(Action action, Vertragsaenderung vertragsaenderung, String changedBy) {
        boolean isSuccess = false;
        if (vertragsaenderung.getPersonalnummer() == null || isNullOrBlank(vertragsaenderung.getPersonalnummer().getPersonalnummer())) {
            return PayloadResponse.builder().message("Personalnummer is null or blank in vertragsaenderung").success(false).build();
        }
        String personalnummer = vertragsaenderung.getPersonalnummer().getPersonalnummer();
        WWorkflow genehmigenWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_GEHNEHMIGEN);
        if (WWorkflowStatus.IN_PROGRESS.equals(genehmigenWorkflow.getStatus())) {
            if (Action.ACCEPT.equals(action)) {
                manageWFItemsService.setWFItemStatus(genehmigenWorkflow, SWorkflowItems.MA_VD_GENEHMIGER_PRUEFT, WWorkflowStatus.COMPLETED, changedBy);
                WWorkflow unterschriftenlaufWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_UNTERSCHRIFTENLAUF);
                WWorkflowGroup group = unterschriftenlaufWorkflow.getWorkflowGroup();
                manageWFsService.setWFStatus(group, SWorkflows.MA_VD_UNTERSCHRIFTENLAUF, WWorkflowStatus.IN_PROGRESS, changedBy);
                manageWFItemsService.setWFItemStatus(unterschriftenlaufWorkflow, SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.IN_PROGRESS, changedBy);
                isSuccess = true;
            }
            if (Action.DECLINE.equals(action)) {
                String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);

                String[] people = ArrayUtils.addAll(
                        vertragsaenderung.getAllRecipients().stream().map(Benutzer::getEmail).toArray(String[]::new),
                        mailLohnverrechnungRecipients);
                mailService.sendEmail("gateway-service.vertragsaenderung.genehmigenden-lehnt-ab", "german", null, people, toObjectArray(vertragsaenderung), toObjectArray());
                manageWFItemsService.setWFItemStatus(genehmigenWorkflow, SWorkflowItems.MA_VD_GENEHMIGER_PRUEFT, WWorkflowStatus.CANCELED, changedBy);
            }
            manageWFsService.setWFStatus(genehmigenWorkflow.getWorkflowGroup(), SWorkflows.MA_VD_GEHNEHMIGEN, WWorkflowStatus.COMPLETED, changedBy);
        }

        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(genehmigenWorkflow);

        return PayloadResponse.builder()
                .success(isSuccess)
                .data(List.of(workflowDtoPayloadType))
                .build();
    }

    private PayloadResponse creatorAction(Action action, Vertragsaenderung vertragsaenderung, String changedBy) {
        boolean isSuccess = false;
        if (vertragsaenderung.getPersonalnummer() == null || isNullOrBlank(vertragsaenderung.getPersonalnummer().getPersonalnummer())) {
            return PayloadResponse.builder().message("Personalnummer is null or blank in vertragsaenderung").success(false).build();
        }
        String personalnummer = vertragsaenderung.getPersonalnummer().getPersonalnummer();

        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_VEREINBARUNG_STARTEN);
        if (WWorkflowStatus.IN_PROGRESS.equals(workflow.getStatus())) {
            if (Action.ACCEPT.equals(action)) {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_PRUEFEN, WWorkflowStatus.COMPLETED, changedBy);

                if (!wWorkflowItemService.findAllByWorkflow(workflow).stream().allMatch(item -> WWorkflowStatus.COMPLETED.equals(item.getStatus()))) {
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_PRUEFEN, WWorkflowStatus.IN_PROGRESS, changedBy);
                    return PayloadResponse.builder().success(false).message("Not all workflow items have correct status").build();
                }

                manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.MA_VD_PEOPLE, WWorkflowStatus.IN_PROGRESS, changedBy);
                workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_PEOPLE);
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_PEOPLE_INFORMIEREN, WWorkflowStatus.IN_PROGRESS, changedBy);
                String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);

                String[] people =
                        ArrayUtils.addAll(
                                vertragsaenderung.getAllRecipients().stream().map(Benutzer::getEmail).toArray(String[]::new),
                                mailLohnverrechnungRecipients);

                Integer swiId = sWorkflowItemService.findAllByWorkflowIdAndStatus(workflow.getWorkflow().getId(), SWorkflowStatus.ACTIVE)
                        .stream().filter(swi -> Objects.equals(swi.getName(), SWorkflowItems.MA_VD_PEOPLE_PRUEFT.getValue()))
                        .map(SWorkflowItem::getId).findFirst().orElse(0);

                mailService.sendEmail("gateway-service.vertragsaenderung.people-informiren", "german", null, people, toObjectArray(vertragsaenderung.getId()),
                        toObjectArray(VERTRAGSAENDERUNG_PRUFUNG_LINK.formatted(getNextAuthUrl(), vertragsaenderung.getId(), swiId)));

                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_PEOPLE_INFORMIEREN, WWorkflowStatus.COMPLETED, changedBy);
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_PEOPLE_PRUEFT, WWorkflowStatus.IN_PROGRESS, changedBy);
                isSuccess = true;
            }

            if (Action.DECLINE.equals(action)) {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_PRUEFEN, WWorkflowStatus.CANCELED, changedBy);
            }

            manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.MA_VD_VEREINBARUNG_STARTEN, WWorkflowStatus.COMPLETED, changedBy);
        }
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);

        return PayloadResponse.builder()
                .success(isSuccess)
                .data(List.of(workflowDtoPayloadType))
                .build();
    }

    private List<Benutzer> getFuehrungskraefteEmails(String personalnummer) {
        List<String> fuhrungskraefteUPNs = arbeitsvertragFixIbosService.findFuehrungskraftUPNsByPersnr(personalnummer);
        List<Benutzer> benutzers = new ArrayList<>();
        for (String upn : fuhrungskraefteUPNs) {
            upn = upn.toLowerCase();
            Benutzer fuehrungskraft = benutzerService.findByUpn(upn);

            if (fuehrungskraft == null) {
                // Sync the supervisor if not found
                gateway2Validation.validateSyncMitarbeiterWithUPN(upn);
            }
            fuehrungskraft = benutzerService.findByUpn(upn);
            benutzers.add(fuehrungskraft);
        }
        return benutzers;
    }

    @Override
    public PayloadResponse saveStammdaten(StammdatenDto stammdatenDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        PayloadResponse response = new PayloadResponse();
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(stammdatenDto.getPersonalnummer());

        log.info("Starting save stammdaten");
        log.info("Sending data to the validation-service for stammdaten");
        StammdatenDto stammdatenDtoResponse = gateway2Validation.validateMitarbeiterStammdaten(stammdatenDto, false, changedBy);
        if (stammdatenDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", stammdatenDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        boolean isMitarbeiter = personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
        WWorkflow workflow;
        try {
            if (isMitarbeiter) {
                workflow = manageWFsService.getWorkflowFromDataAndWFType(stammdatenDto.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            } else {
                workflow = manageWFsService.getWorkflowFromDataAndWFType(stammdatenDto.getPersonalnummer(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            }
            WWorkflow lastWorkflowInChain = wWorkflowService.findLastWorkflowInChain(workflow.getId());
            WWorkflow successorWF = workflow.getSuccessor();
            // Set other WFs to NEW
            log.info("Resetting subsequent workflows to status NEW");
            wfService.resetWfChainToStatus(lastWorkflowInChain, successorWF, WWorkflowStatus.NEW, changedBy);
        } catch (NullPointerException e) {
            log.info("No workflows found for personalnummer {}", personalnummer.getPersonalnummer());
        }

        Optional<Stammdaten> stammdatenOptional = stammdatenService.findById(stammdatenDtoResponse.getId());
        if (!isMitarbeiter && stammdatenOptional.isPresent()) {
            Stammdaten stammdaten = stammdatenOptional.get();
            genericMAService.updateTeilnehmerStammdaten(stammdaten, changedBy);
        }
        if (stammdatenDtoResponse.getErrors().isEmpty()) {
            log.info("Validation successfully completed for stammdaten");
            if (isMitarbeiter && areStammAndVertragsdatenCompleted(personalnummer.getPersonalnummer())) {
                genericMAService.manageKVCalculation(personalnummer.getPersonalnummer(), null, changedBy, false);
            }
        }

        PayloadTypeList<StammdatenDto> stammDatenPayloadType = stammdatenOptional.map(stammdaten -> genericMAService.createPayloadForStammdaten(stammdatenService.mapStammdatenToDto(stammdaten))).orElseGet(() -> genericMAService.createPayloadForStammdaten(new StammdatenDto()));
        response.setSuccess(true);
        response.setData(List.of(stammDatenPayloadType));
        return response;
    }

    private boolean areStammAndVertragsdatenCompleted(String personalnummer) {
        return genericMAService.isVertragsdatenWithoutError(personalnummer, false) && genericMAService.isStammdatenWithoutError(personalnummer, false);
    }

    @Override
    public PayloadResponse saveVertragsdaten(VertragsdatenDto vertragsDatenDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        PayloadResponse response = new PayloadResponse();
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(vertragsDatenDto.getPersonalnummer());
        boolean isMitarbeiter = personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
        log.info("Starting save Vertragsdaten");
        log.info("Sending data to the validation-service for Vertragsdaten");
        VertragsdatenDto vertragsDtoResponse = gateway2Validation.validateMitarbeiterVertragsdaten(vertragsDatenDto, false, changedBy);
        if (vertragsDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", vertragsDatenDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        WWorkflow workflow;

        try {
            if (isMitarbeiter) {
                workflow = manageWFsService.getWorkflowFromDataAndWFType(vertragsDatenDto.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            } else {
                workflow = manageWFsService.getWorkflowFromDataAndWFType(vertragsDatenDto.getPersonalnummer(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            }
            WWorkflow lastWorkflowInChain = wWorkflowService.findLastWorkflowInChain(workflow.getId());
            WWorkflow successorWF = workflow.getSuccessor();
            // Set other WFs to NEW
            log.info("Resetting subsequent workflows to status NEW");
            wfService.resetWfChainToStatus(lastWorkflowInChain, successorWF, WWorkflowStatus.NEW, changedBy);
            if (genericMAService.isVertragsdatenWithoutError(vertragsDatenDto.getPersonalnummer(), false)) {
                log.info("Validation successfully completed for Vertragsdaten");
                if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER) && areStammAndVertragsdatenCompleted(personalnummer.getPersonalnummer())) {
                    genericMAService.manageKVCalculation(personalnummer.getPersonalnummer(), null, changedBy, false);
                }
            }
        } catch (NullPointerException e) {
            log.info("No workflows found for personalnummer {}", personalnummer.getPersonalnummer());
        }

        VertragsdatenDto vertragsdatenDtoWithStufe = vertragsdatenService.mapVertragsdatenToDto(vertragsdatenService.findById(vertragsDtoResponse.getId()).orElse(null));
        //TODO Remove this after FE Rework. Current workaround for FE Toggle.
        if (vertragsdatenDtoWithStufe != null) {
            vertragsdatenDtoWithStufe.setIsBefristet(vertragsDtoResponse.getIsBefristet());
        }
        manageVertragsdatenRelatedEntitiesStatus(personalnummer.getPersonalnummer());

        PayloadTypeList<VertragsdatenDto> vertragsDatenDtoPayloadType = genericMAService.createPayloadForVertragsdaten(vertragsdatenDtoWithStufe, isMitarbeiter);
        response.setSuccess(true);
        response.setData(Collections.singletonList(vertragsDatenDtoPayloadType));
        return response;
    }

    private void manageVertragsdatenRelatedEntitiesStatus(String personalnummer) {
        Vertragsdaten vertragsdaten = genericMAService.getVertragsdatenEntity(personalnummer, false);
        if (MitarbeiterStatus.VALIDATED.equals(vertragsdaten.getStatus())) {
            vertragsdaten.setStatus(MitarbeiterStatus.ACTIVE);
            vertragsdatenService.save(vertragsdaten);
        }
        List<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findAllByVertragsdatenId(vertragsdaten.getId());
        for (Vordienstzeiten vordienstzeit : vordienstzeiten) {
            if (vordienstzeit.getErrors().isEmpty() && MitarbeiterStatus.VALIDATED.equals(vordienstzeit.getStatus())) {
                vordienstzeit.setStatus(MitarbeiterStatus.ACTIVE);
                vordienstzeitenService.save(vordienstzeit);
            }
        }
        List<Unterhaltsberechtigte> unterhaltsberechtigte = unterhaltsberechtigteService.findAllByVertragsdatenId(vertragsdaten.getId());
        for (Unterhaltsberechtigte unterhaltsberechtigt : unterhaltsberechtigte) {
            if (unterhaltsberechtigt.getErrors().isEmpty() && MitarbeiterStatus.VALIDATED.equals(unterhaltsberechtigt.getStatus())) {
                unterhaltsberechtigt.setStatus(MitarbeiterStatus.ACTIVE);
                unterhaltsberechtigteService.save(unterhaltsberechtigt);
            }
        }
    }

    @Override
    public PayloadResponse saveVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        PayloadResponse response = new PayloadResponse();
        VordienstzeitenDto vordienstzeitenDtoResponse = gateway2Validation.validateMitarbeiterVordienstzeiten(vordienstzeitenDto, changedBy);
        if (vordienstzeitenDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", vordienstzeitenDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        boolean validVertragsdaten = genericMAService.isVertragsdatenWithoutError(vordienstzeitenDto.getPersonalnummer(), false);
        if (validVertragsdaten && vordienstzeitenDtoResponse.getErrors().isEmpty()) {
            log.info("Validation successfully completed for Vordienstzeiten");
            Optional<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findById(vordienstzeitenDto.getId());
            vordienstzeiten.ifPresent(value -> {
                value.setStatus(MitarbeiterStatus.ACTIVE);
                vordienstzeitenService.save(value);
            });
            if (areStammAndVertragsdatenCompleted(vordienstzeitenDto.getPersonalnummer())) {
                genericMAService.manageKVCalculation(vordienstzeitenDto.getPersonalnummer(), null, changedBy, false);
            }
        }
        PayloadTypeList<VordienstzeitenDto> vordienstzeitenDtoPayloadType = genericMAService.createPayloadForSingleVordienstzeiten(vordienstzeitenDtoResponse);
        PayloadTypeList<VordienstzeitenDto> allVordienstzeitenDtoPayloadType = genericMAService.createPayloadForMultipleVordienstzeiten(vordienstzeitenDto.getPersonalnummer(), false);
        response.setSuccess(true);
        response.setData(Arrays.asList(vordienstzeitenDtoPayloadType, allVordienstzeitenDtoPayloadType));
        return response;
    }

    @Override
    public PayloadResponse saveUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        UnterhaltsberechtigteDto unterhaltsberechtigteDtoResponse = gateway2Validation.validateMitarbeiterUnterhaltsberechtigte(unterhaltsberechtigteDto, changedBy);
        PayloadResponse response = new PayloadResponse();
        if (unterhaltsberechtigteDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", unterhaltsberechtigteDto.getPersonalnummer());
            response.setSuccess(false);
            return response;
        }
        if (genericMAService.isVertragsdatenWithoutError(unterhaltsberechtigteDto.getPersonalnummer(), true)) {
            log.info("Validation successfully completed for Unterhaltsberechtigte");
            if (areStammAndVertragsdatenCompleted(unterhaltsberechtigteDto.getPersonalnummer())) {
                genericMAService.manageKVCalculation(unterhaltsberechtigteDto.getPersonalnummer(), null, changedBy, false);
            }
            if (unterhaltsberechtigteDtoResponse.getId() != null) {
                Optional<Unterhaltsberechtigte> unterhaltsberechtigte = unterhaltsberechtigteService.findById(unterhaltsberechtigteDtoResponse.getId());
                unterhaltsberechtigte.ifPresent(value -> {
                    value.setStatus(MitarbeiterStatus.ACTIVE);
                    unterhaltsberechtigteService.save(value);
                });
            }


        }
        PayloadTypeList<UnterhaltsberechtigteDto> unterhaltsberechtigteDtoPayloadType = genericMAService.createPayloadForSingleUnterhaltsberechtigte(unterhaltsberechtigteDtoResponse);
        PayloadTypeList<UnterhaltsberechtigteDto> allUnterhaltsberechtigteDtoPayloadType = genericMAService.createPayloadForMultipleUnterhaltsberechtigte(unterhaltsberechtigteDto.getPersonalnummer(), false);
        response.setSuccess(true);
        response.setData(Arrays.asList(unterhaltsberechtigteDtoPayloadType, allUnterhaltsberechtigteDtoPayloadType));
        return response;
    }
}