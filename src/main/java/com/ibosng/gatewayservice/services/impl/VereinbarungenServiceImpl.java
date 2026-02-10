package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.dtos.workflows.WorkflowDto;
import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungReportParameter;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.*;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.JasperReportService;
import com.ibosng.gatewayservice.services.PermissionService;
import com.ibosng.gatewayservice.services.VereinbarungenService;
import com.ibosng.gatewayservice.services.WorkflowHelperService;
import com.ibosng.gatewayservice.utils.Parsers;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.workflowservice.enums.SWorkflowGroups;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFStartService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Mappers.mapVereinbarungToDto;
import static com.ibosng.gatewayservice.utils.Constants.GATEWAY_SERVICE;
import static com.ibosng.gatewayservice.utils.Helpers.getSortDirection;

@Slf4j
@Service
@RequiredArgsConstructor
public class VereinbarungenServiceImpl implements VereinbarungenService {

    private final VereinbarungService vereinbarungService;
    private final FileShareService fileShareService;
    private final VertragsdatenService vertragsdatenService;

    private final PersonalnummerService personalnummerService;

    private final StammdatenService stammdatenService;
    private final ManageWFItemsService manageWFItemsService;
    private final ManageWFsService manageWFsService;
    private final WFStartService wfStartService;
    private final JasperReportService jasperReportService;
    private final WorkflowHelperService workflowHelperService;

    private final WWorkflowService wWorkflowService;
    private final PermissionService permissionService;


    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    private static final String UNSIGNED = "Nicht unterschrieben";

    @Override
    public PayloadResponse listAll(int page, int size) {

        List<VereinbarungDto> responses = listVereinbarungenRaw(page, size);
        PayloadTypeList<VereinbarungDto> vereinbarungDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue(), responses);
        Pagination pagination = new Pagination(responses.size(), size, page);

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungDtoPayloadType))
                .pagination(pagination)
                .build();

    }

    @Override
    public PayloadResponse listVereinbarungenForBenutzer(Benutzer benutzer, int page, int size) {
        List<VereinbarungDto> responsesRaw = listVereinbarungenRaw(page, size);
        List<VereinbarungDto> responsesForBenutzer = responsesRaw.stream()
                .filter(v -> permissionService.canReadVereinbarung(benutzer, v.getId()))
                .collect(Collectors.toList());

        PayloadTypeList<VereinbarungDto> vereinbarungDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue(), responsesForBenutzer);
        Pagination pagination = new Pagination(responsesForBenutzer.size(), size, page);

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungDtoPayloadType))
                .pagination(pagination)
                .build();
    }

    @Override
    public PayloadResponse getFilteredVereinbarungen(List<VereinbarungStatus> vereinbarungStatuses, String firma, String searchTerm, String sortProperty, String sortDirection, int page, int size) {
        Page<VereinbarungDto> vereinbarungPage = vereinbarungService.findVereinbarungByCriteria(vereinbarungStatuses, firma, searchTerm, sortProperty, getSortDirection(sortDirection), page, size);
        List<VereinbarungDto> responses = getFilteredVereinbarungenRaw(vereinbarungPage);

        PayloadTypeList<VereinbarungDto> vereinbarungDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue(), responses);
        Pagination pagination = new Pagination(vereinbarungPage.getTotalElements(), size, page);

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungDtoPayloadType))
                .pagination(pagination)
                .build();

    }

    @Override
    public PayloadResponse getFilteredVereinbarungenForBenutzer(Benutzer benutzer, List<VereinbarungStatus> vereinbarungStatuses, String firma, String searchTerm, String sortProperty, String sortDirection, int page, int size) {
        Page<VereinbarungDto> vereinbarungPage = vereinbarungService.findVereinbarungByCriteria(vereinbarungStatuses, firma, searchTerm, sortProperty, getSortDirection(sortDirection), page, size);
        List<VereinbarungDto> responsesRaw = getFilteredVereinbarungenRaw(vereinbarungPage);
        List<VereinbarungDto> responsesForBenutzer = responsesRaw.stream()
                .filter(v -> permissionService.canReadVereinbarung(benutzer, v.getId()))
                .collect(Collectors.toList());

        PayloadTypeList<VereinbarungDto> vereinbarungDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue(), responsesForBenutzer);
        Pagination pagination = new Pagination(responsesForBenutzer.size(), size, page);

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungDtoPayloadType))
                .pagination(pagination)
                .build();
    }

    private List<VereinbarungDto> getFilteredVereinbarungenRaw(Page<VereinbarungDto> vereinbarungPage) {
        List<VereinbarungDto> vereinbarungDtos = vereinbarungPage.stream().toList();
        for (VereinbarungDto vereinbarungDto : vereinbarungDtos) {
            WWorkflow vereinbarungWorkflow = wWorkflowService.findById(vereinbarungDto.getWorkflowId()).orElse(null);
            WorkflowItemDto workflowItemDto = workflowHelperService.getCurrentWfForVereinbarung(vereinbarungWorkflow);
            vereinbarungDto.setWorkflowItem(workflowItemDto);

        }
        return vereinbarungDtos;
    }

    private List<VereinbarungDto> listVereinbarungenRaw(int page, int size) {
        Page<VereinbarungDto> resp = vereinbarungService.getAllVereinbarungenPageable(page, size);
        List<VereinbarungDto> vereinbarungDtos = resp.stream().toList();
        for (VereinbarungDto vereinbarungDto : vereinbarungDtos) {

            WWorkflow vereinbarungWorkflow = wWorkflowService.findById(vereinbarungDto.getWorkflowId()).orElse(null);
            vereinbarungDto.setWorkflowItem(workflowHelperService.getCurrentWfForVereinbarung(vereinbarungWorkflow));

        }
        return vereinbarungDtos;
    }


    @Override
    public PayloadResponse getVereinbarung(Integer vereinbarungId) {
        Optional<Vereinbarung> vereinbarungOptional = vereinbarungService.findById(vereinbarungId);
        if (vereinbarungOptional.isEmpty()) {
            log.error("No Vereinbarung found for id: " + vereinbarungId);
            return PayloadResponse.builder()
                    .success(false)
                    .data(new ArrayList<>())
                    .build();
        }
        Stammdaten stammdaten = this.stammdatenService.findByPersonalnummerString((vereinbarungOptional.get()).getPersonalnummer().getPersonalnummer());
        if (stammdaten == null) {
            log.error("No stammdaten found for: " + vereinbarungOptional.get().toString());
        }
        VereinbarungDto vereinbarungDto = mapVereinbarungToDto(vereinbarungOptional.get(), stammdaten);
        PayloadTypeList<VereinbarungDto> vereinbarungTypePayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue());


        WWorkflow vereinbarungWorkflow = vereinbarungOptional.get().getWorkflow();
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(vereinbarungWorkflow);

        if (vereinbarungDto != null) {
            vereinbarungTypePayloadType.setAttributes(Collections.singletonList(vereinbarungDto));

        } else {
            vereinbarungTypePayloadType.setAttributes(new ArrayList<>());
        }
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungTypePayloadType, workflowDtoPayloadType))
                .build();
    }

    @Override
    public PayloadResponse updateVereinbarung(VereinbarungDto vereinbarungDto) {
        Vereinbarung vereinbarung = vereinbarungService.save(mapVereinbarungDtoToVereinbarung(vereinbarungDto));
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(vereinbarungDto.getPersonalnummer());
        vereinbarungDto = mapVereinbarungToDto(vereinbarung, stammdaten);

        PayloadTypeList<VereinbarungDto> vereinbarungTypePayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue());
        vereinbarungTypePayloadType.setAttributes(Collections.singletonList(vereinbarungDto));

        WWorkflow vereinbarungWorkflow = vereinbarung.getWorkflow();
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(vereinbarungWorkflow);

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungTypePayloadType, workflowDtoPayloadType))
                .build();
    }

    public Vereinbarung mapVereinbarungDtoToVereinbarung(VereinbarungDto dto) {
        if (dto == null) {
            return null;
        }
        Vereinbarung vereinbarung = new Vereinbarung();
        if (dto.getId() != null) {
            Optional<Vereinbarung> vereinbarungOptional = vereinbarungService.findById(dto.getId());
            if (vereinbarungOptional.isPresent()) {
                vereinbarung = vereinbarungOptional.get();
            }
        }

        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(dto.getPersonalnummer());

        vereinbarung.setVereinbarungName(dto.getVereinbarungName());
        vereinbarung.setFuehrungskraft(dto.getFuehrungskraft());
        vereinbarung.setStatus(dto.getStatus());
        vereinbarung.setGueltigAb(dto.getGueltigAb());
        vereinbarung.setGueltigBis(dto.getGueltigBis());
        vereinbarung.setCreatedOn(dto.getCreatedOn());
        vereinbarung.setCreatedBy(dto.getCreatedBy());
        vereinbarung.setChangedOn(dto.getChangedOn());
        vereinbarung.setChangedBy(dto.getChangedBy());

        vereinbarung.setFirma(personalnummer.getFirma());

        vereinbarung.setPersonalnummer(personalnummer);

        // Map parameters
        if (dto.getParameters() != null) {
            Vereinbarung finalVereinbarung = vereinbarung;
            List<VereinbarungReportParameter> parameterList = dto.getParameters().stream()
                    .map(p -> {
                        VereinbarungReportParameter param = new VereinbarungReportParameter();
                        param.setId(p.getId());
                        param.setName(p.getName());
                        param.setValue(p.getValue());
                        param.setType(p.getType());
                        param.setVereinbarung(finalVereinbarung);
                        return param;
                    })
                    .collect(Collectors.toList());

            vereinbarung.setParameters(parameterList);
        }

        return vereinbarung;
    }


    public PayloadResponse createVereinbarung(ReportRequestDto reportRequestDto) {

        WWorkflowGroup wWorkflowGroup = wfStartService.createWorkflowGroupAndInstances(SWorkflowGroups.MA_FK_VEREINBARUNG.getValue(), reportRequestDto.getPersonalnummer(), reportRequestDto.getCreatedBy());
        WWorkflow workflow = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.MA_FK_VEREINBARUNG_STARTEN, WWorkflowStatus.IN_PROGRESS, reportRequestDto.getCreatedBy());
        WWorkflow moxisWorkflow = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF, WWorkflowStatus.NEW, reportRequestDto.getCreatedBy());
        WWorkflow workflowMoxis = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF, WWorkflowStatus.NEW, reportRequestDto.getCreatedBy());

        manageWFItemsService.setWFItemStatus(moxisWorkflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.NEW, GATEWAY_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_ERSTELLEN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_ERSTELLEN, WWorkflowStatus.COMPLETED, GATEWAY_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_VERVOLLSTAENDIGEN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);


        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(reportRequestDto.getPersonalnummer());
        List<Vertragsdaten> vertragsdaten = vertragsdatenService.findByPersonalnummerString(reportRequestDto.getPersonalnummer());
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(reportRequestDto.getPersonalnummer());
        if (personalnummer == null) {
            log.error("No Personalnummer found for: " + reportRequestDto.getPersonalnummer());
            return null;
        }
        if (stammdaten == null) {
            log.error("No Stammdaten found for: " + reportRequestDto.getPersonalnummer());
            return null;
        }
        Vereinbarung vereinbarung = new Vereinbarung();
        vereinbarung.setPersonalnummer(personalnummer);
        vereinbarung.setCreatedBy(reportRequestDto.getCreatedBy());
        vereinbarung.setFirma(personalnummer.getFirma());
        vereinbarung.setVereinbarungName(reportRequestDto.getReportName());
        vereinbarung.setStatus(VereinbarungStatus.NEW);
        vereinbarung.setWorkflow(workflowMoxis);
        if (!vertragsdaten.isEmpty()) {
            Benutzer fuehrungskraft = vertragsdaten.get(0).getFuehrungskraft();
            vereinbarung.setFuehrungskraft(fuehrungskraft.getFirstName() + " " + fuehrungskraft.getLastName());
        } else {
            log.error("No Vertragsdaten found for Personalnummer: " + reportRequestDto.getPersonalnummer());
        }

        // Create and add report parameters
        List<VereinbarungReportParameter> parameters = new ArrayList<>();
        for (ReportParameterDto paramDto : reportRequestDto.getReportParameters()) {
            VereinbarungReportParameter param = new VereinbarungReportParameter();
            param.setVereinbarung(vereinbarung);
            param.setName(paramDto.getName());
            param.setValue(paramDto.getValue());
            param.setType(paramDto.getType());
            parameters.add(param);
        }
        // Associate parameters with Vereinbarung
        vereinbarung.setParameters(parameters);
        if (reportRequestDto.getGueltigAb() != null) {
            vereinbarung.setGueltigAb(Parsers.parseDate(reportRequestDto.getGueltigAb()));
        }
        if (reportRequestDto.getGueltigBis() != null) {
            vereinbarung.setGueltigBis(Parsers.parseDate(reportRequestDto.getGueltigBis()));
        }
        vereinbarung = vereinbarungService.save(vereinbarung);
        VereinbarungDto vereinbarungDto = mapVereinbarungToDto(vereinbarung, stammdaten);
        // Set workflow info
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflowMoxis);

        PayloadTypeList<VereinbarungDto> vereinbarungTypePayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue());
        vereinbarungTypePayloadType.setAttributes(Collections.singletonList(vereinbarungDto));
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungTypePayloadType, workflowDtoPayloadType))
                .build();
    }

    private ReportRequestDto mapVereinbarungDtoToReportRequestDto(VereinbarungDto dto) {
        if (dto == null) {
            return null;
        }

        ReportRequestDto request = new ReportRequestDto();
        request.setPersonalnummer(dto.getPersonalnummer());

        if (dto.getGueltigAb() != null) {
            request.setGueltigAb(dto.getGueltigAb().toString()); // format as ISO string
        }

        request.setCreatedBy(dto.getCreatedBy());
        request.setReportName(dto.getVereinbarungName());

        // Default values you may want to set
        request.setIsPreview(false); // or true if applicable
        request.setOutputFormat("PDF"); // or another default

        if (dto.getParameters() != null) {
            List<ReportParameterDto> reportParams = dto.getParameters().stream()
                    .map(p -> {
                        ReportParameterDto param = new ReportParameterDto();
                        param.setName(p.getName());
                        param.setType(p.getType());
                        param.setValue(p.getValue());
                        // description, label, required are not available in VereinbarungParameterDto
                        return param;
                    })
                    .collect(Collectors.toList());

            request.setReportParameters(reportParams);
        }

        return request;
    }

    public ReportResponse generateVereinbarungPreview(VereinbarungDto vereinbarungDto) {
        Optional<Vereinbarung> vereinbarung = vereinbarungService.findById(vereinbarungDto.getId());
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(vereinbarungDto.getPersonalnummer());

        if (vereinbarung == null) {
            log.error("No Vereinbarung found with ID: " + vereinbarungDto.getId());
            return null;
        }

        if (stammdaten == null) {
            log.error("No Stammdaten found for: " + vereinbarungDto.getPersonalnummer());
            return null;
        }
        ReportRequestDto reportRequestDto = mapVereinbarungDtoToReportRequestDto(vereinbarungDto);

        ReportResponse reportResponse = jasperReportService.generateReport(reportRequestDto);

        return reportResponse;
    }

    public PayloadResponse persistVereinbarungFile(VereinbarungDto vereinbarungDto) {

        Optional<Vereinbarung> vereinbarung = vereinbarungService.findById(vereinbarungDto.getId());
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(vereinbarungDto.getPersonalnummer());

        if (vereinbarung == null) {
            log.error("No Vereinbarung found with ID: " + vereinbarungDto.getId());
            return null;
        }

        if (stammdaten == null) {
            log.error("No Stammdaten found for: " + vereinbarungDto.getPersonalnummer());
            return null;
        }

        manageWFItemsService.setWFItemStatus(vereinbarung.get().getWorkflow().getPredecessor(), SWorkflowItems.MA_FK_VEREINBARUNG_VERVOLLSTAENDIGEN, WWorkflowStatus.COMPLETED, GATEWAY_SERVICE);
        manageWFItemsService.setWFItemStatus(vereinbarung.get().getWorkflow().getPredecessor(), SWorkflowItems.MA_FK_VEREINBARUNG_DOKUMENT_ERSTELLEN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
        ReportRequestDto reportRequestDto = mapVereinbarungDtoToReportRequestDto(vereinbarungDto);

        ReportResponse reportResponse = jasperReportService.generateReport(reportRequestDto);
        saveVereinbarungFile(vereinbarung.get(), stammdaten, reportResponse);

        manageWFItemsService.setWFItemStatus(vereinbarung.get().getWorkflow().getPredecessor(), SWorkflowItems.MA_FK_VEREINBARUNG_DOKUMENT_ERSTELLEN, WWorkflowStatus.COMPLETED, GATEWAY_SERVICE);
        manageWFsService.setWFStatus(vereinbarung.get().getWorkflow().getPredecessor().getWorkflowGroup(), SWorkflows.MA_FK_VEREINBARUNG_STARTEN, WWorkflowStatus.COMPLETED, reportRequestDto.getCreatedBy());

        PayloadTypeList<VereinbarungDto> vereinbarungTypePayloadType = new PayloadTypeList<>(PayloadTypes.VEREINBARUNEN.getValue());
        vereinbarungTypePayloadType.setAttributes(Collections.singletonList(vereinbarungDto));

        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(vereinbarung.get().getWorkflow());
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(vereinbarungTypePayloadType, workflowDtoPayloadType))
                .build();
    }

    @Override
    public PayloadResponse getWorkflowgroup(Integer vereinbarungId) {
        PayloadResponse response = new PayloadResponse();
        Optional<Vereinbarung> vereinbarung = vereinbarungService.findById(vereinbarungId);
        WWorkflow workflow = null;
        if (vereinbarung.isPresent()) {
            workflow = vereinbarung.get().getWorkflow();
        }
        if (workflow == null) {
            response.setSuccess(false);
            return response;
        }
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(List.of(workflowDtoPayloadType));
        return response;
    }


    private void saveVereinbarungFile(Vereinbarung vereinbarung, Stammdaten stammdaten, ReportResponse reportResponse) {
        String filename = reportResponse.getReportName() + "." + reportResponse.getOutputFormat().getValue();
        vereinbarung.setVereinbarungFile(filename);
        vereinbarung = vereinbarungService.save(vereinbarung);

        String directoryPath = fileShareService.getVereinbarungenDirectory(vereinbarung.getPersonalnummer().getPersonalnummer(), stammdaten);
        // Use "/" as separator since we are refering to the AZ file share
        directoryPath += "/" + UNSIGNED;


        InputStream pdfInputStream = new ByteArrayInputStream(reportResponse.getReportBytes());
        fileShareService.uploadOrReplaceInFileShare(
                getFileSharePersonalunterlagen(),
                directoryPath,
                filename,
                pdfInputStream,
                (long) reportResponse.getReportBytes().length
        );
    }
}
