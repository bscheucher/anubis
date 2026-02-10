package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.workflows.WorkflowDto;
import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.WorkflowHelperService;
import com.ibosng.workflowservice.enums.SWorkflowGroups;
import com.ibosng.workflowservice.enums.SWorkflows;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkflowHelperServiceImpl implements WorkflowHelperService {

    private final WWorkflowService wWorkflowService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final WWorkflowItemService wWorkflowItemService;
    private final BenutzerDetailsService benutzerDetailsService;

    @Override
    public PayloadTypeList<WorkflowDto> createPayloadForWorkflow(WWorkflow workflow) {
        PayloadTypeList<WorkflowDto> workflowPayloadType = new PayloadTypeList<>(PayloadTypes.WORKFLOWGROUP.getValue());
        if (workflow == null) {
            return workflowPayloadType;
        }
        WWorkflowGroup workflowGroup = null;
        if (workflow != null) {
            //Vereinbarung
            if (workflow.getWorkflowGroup().getWorkflowGroup().getName().equals(SWorkflowGroups.MA_FK_VEREINBARUNG.getValue())) {
                workflowGroup = workflow.getWorkflowGroup();
            } else {
                // MA Onboarding
                workflowGroup= wWorkflowGroupService.findAllByDataAndWorkflowGroupAndStatus(workflow.getData(), workflow.getWorkflow().getWorkflowGroup(), WWorkflowStatus.IN_PROGRESS).stream().findFirst().orElse(null);
            }

        }
        if (workflowGroup == null) {
            log.error("No wWorkflowgroup found for personal nummer: {} in status IN_PROGRESS", workflow.getData());
            return workflowPayloadType;
        }
        List<WWorkflow> allWorkflows = wWorkflowService.findAllByWorkflowGroup(workflowGroup);
        List<WorkflowDto> workflowDtos = new ArrayList<>();
        for (WWorkflow wWorkflow : allWorkflows) {
            WorkflowDto workflowDto = new WorkflowDto();
            workflowDto.setWorkflowId(wWorkflow.getId());
            workflowDto.setReferenceWorkflowId(wWorkflow.getWorkflow().getId());
            workflowDto.setWorkflowName(wWorkflow.getWorkflow().getName());
            workflowDto.setWorkflowStatus(wWorkflow.getStatus());
            workflowDto.setData(wWorkflow.getData());
            workflowDto.setWorkflowItems(getWFItemsDto(wWorkflow));
            workflowDto.setChangedBy(getChangedBy(workflow.getChangedBy()));
            workflowDto.setChangedOn(wWorkflow.getChangedOn() != null ? wWorkflow.getChangedOn() : wWorkflow.getCreatedOn());
            workflowDtos.add(workflowDto);
        }
        workflowPayloadType.setAttributes(workflowDtos);
        return workflowPayloadType;
    }

    public WorkflowItemDto getCurrentWfForVereinbarung(WWorkflow workflow){
        WWorkflowGroup workflowGroup = workflow.getWorkflowGroup();
        List<WWorkflow> allWorkflows = wWorkflowService.findAllByWorkflowGroup(workflowGroup);
        WorkflowItemDto currentWfItem = null;

        WWorkflow neueVereinbarungfWf = allWorkflows.stream().filter(wWorkflow -> wWorkflow.getWorkflow().getName().equals(SWorkflows.MA_FK_VEREINBARUNG_STARTEN.getValue())).findFirst().orElse(null);
        List<WorkflowItemDto> neueVereinbarungWfItems = getWFItemsDto(neueVereinbarungfWf);
        for(WorkflowItemDto neueVereinbarungfWfItem: neueVereinbarungWfItems){
            if(!neueVereinbarungfWfItem.getWorkflowItemStatus().equals(WWorkflowStatus.NEW)){
                currentWfItem = neueVereinbarungfWfItem;
            }
        }

        WWorkflow unterschriftenLaufWf = allWorkflows.stream().filter(wWorkflow -> wWorkflow.getWorkflow().getName().equals(SWorkflows.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF.getValue())).findFirst().orElse(null);
        List<WorkflowItemDto> unterschriftenLaufWfItems = getWFItemsDto(unterschriftenLaufWf);
        for(WorkflowItemDto unterschriftenLaufWfItem: unterschriftenLaufWfItems){
            if(!unterschriftenLaufWfItem.getWorkflowItemStatus().equals(WWorkflowStatus.NEW)){
                currentWfItem = unterschriftenLaufWfItem;
            }
        }
        return currentWfItem;
    }

    @Override
    public PayloadTypeList<WorkflowDto> createPayloadForWorkflowGroup(WWorkflowGroup workflowGroup) {
        PayloadTypeList<WorkflowDto> workflowPayloadType = new PayloadTypeList<>(PayloadTypes.WORKFLOWGROUP.getValue());
        if (workflowGroup == null) {
            log.error("No wWorkflowgroup found for personal nummer: {} in status IN_PROGRESS", workflowGroup.getData());
            return workflowPayloadType;
        }
        List<WWorkflow> allWorkflows = wWorkflowService.findAllByWorkflowGroup(workflowGroup);
        List<WorkflowDto> workflowDtos = new ArrayList<>();
        for (WWorkflow wWorkflow : allWorkflows) {
            WorkflowDto workflowDto = new WorkflowDto();
            workflowDto.setWorkflowId(wWorkflow.getId());
            workflowDto.setReferenceWorkflowId(wWorkflow.getWorkflow().getId());
            workflowDto.setWorkflowName(wWorkflow.getWorkflow().getName());
            workflowDto.setWorkflowStatus(wWorkflow.getStatus());
            workflowDto.setData(wWorkflow.getData());
            workflowDto.setWorkflowItems(getWFItemsDto(wWorkflow));
            workflowDto.setChangedBy(getChangedBy(workflowGroup.getChangedBy()));
            workflowDto.setChangedOn(wWorkflow.getChangedOn() != null ? wWorkflow.getChangedOn() : wWorkflow.getCreatedOn());
            workflowDtos.add(workflowDto);
        }
        workflowPayloadType.setAttributes(workflowDtos);
        return workflowPayloadType;
    }

    private List<WorkflowItemDto> getWFItemsDto(WWorkflow workflow) {
        List<WorkflowItemDto> workflowItemDtos = new ArrayList<>();
        List<WWorkflowItem> workflowItems = wWorkflowItemService.findAllByWorkflowId(workflow.getId());
        for (WWorkflowItem workflowItem : workflowItems) {
            workflowItemDtos.add(mapWorkflowitemToDto(workflowItem, new WorkflowItemDto()));
        }
        return workflowItemDtos;
    }

    private WorkflowItemDto mapWorkflowitemToDto(WWorkflowItem workflowItem, WorkflowItemDto workflowItemDto) {
        workflowItemDto.setWorkflowItemId(workflowItem.getId());
        workflowItemDto.setReferenceWorkflowItemId(workflowItem.getWorkflowItem().getId());
        workflowItemDto.setWorkflowItemName(workflowItem.getWorkflowItem().getName());
        workflowItemDto.setData(workflowItem.getData());
        workflowItemDto.setChangedOn(workflowItem.getChangedOn() != null ? workflowItem.getChangedOn() : workflowItem.getCreatedOn());
        workflowItemDto.setChangedBy(getChangedBy(workflowItem.getChangedBy()));
        workflowItemDto.setWorkflowItemStatus(workflowItem.getStatus());
        return workflowItemDto;
    }

    @Override
    public String getChangedBy(String changedBy) {
        if (!isNullOrBlank(changedBy)) {
            if (changedBy.contains("@")) {
                Benutzer benutzer = benutzerDetailsService.getBenutzerFromEmail(changedBy.toLowerCase());
                if (benutzer != null) {
                    if (!isNullOrBlank(benutzer.getFirstName()) && !isNullOrBlank(benutzer.getLastName())) {
                        return benutzer.getFirstName() + " " + benutzer.getLastName();
                    }
                    if (!isNullOrBlank(benutzer.getFirstName()) && isNullOrBlank(benutzer.getLastName())) {
                        return benutzer.getFirstName();
                    }
                    if (isNullOrBlank(benutzer.getFirstName()) && !isNullOrBlank(benutzer.getLastName())) {
                        return benutzer.getLastName();
                    }
                }
            }
            return changedBy;
        }
        return "";
    }
}
