package com.ibosng.workflowservice.services.impl;

import com.ibosng.dbservice.entities.workflows.SWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.workflows.SWorkflowService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageWFsServiceImpl implements ManageWFsService {

    private final WWorkflowService wWorkflowService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final SWorkflowService sWorkflowService;


    @Override
    public WWorkflow setWFStatus(WWorkflowGroup wWorkflowGroup, SWorkflows sWorkflow, WWorkflowStatus status, String changedBy) {
        List<WWorkflow> wWorkflows = wWorkflowService.findAllByWorkflowGroup(wWorkflowGroup);
        WWorkflow workflow = wWorkflows.stream().filter(ww -> ww.getWorkflow().getName().equals(sWorkflow.getValue())).findFirst().orElse(null);
        if (workflow != null) {
            workflow.setStatus(status);
            workflow.setChangedBy(changedBy);
            workflow.setChangedOn(getLocalDateNow());
            return wWorkflowService.save(workflow);
        }
        log.warn("No wWorkflow found for wWorkflowgroup with id: {}", wWorkflowGroup.getId());
        return null;
    }

    @Override
    public WWorkflow getWorkflowFromDataAndWFType(String personalnummer, SWorkflows sWorkflowEnum) {
        SWorkflow sWorkflow = sWorkflowService.findByName(sWorkflowEnum.getValue());
        WWorkflowGroup wWorkflowGroup = wWorkflowGroupService.findAllByDataAndWorkflowGroupAndStatus(personalnummer, sWorkflow.getWorkflowGroup(), WWorkflowStatus.IN_PROGRESS).stream().findFirst().orElse(null);
        if (wWorkflowGroup == null) {
            log.error("No wWorkflowgroup found for data: {} in status IN_PROGRESS", personalnummer);
            return null;
        }
        List<WWorkflow> workflows = wWorkflowService.findAllByWorkflowGroup(wWorkflowGroup);
        WWorkflow workflow = workflows.stream().filter(ww -> ww.getWorkflow().getName().equals(sWorkflowEnum.getValue())).toList().stream().findFirst().orElse(null);
        if (workflow == null) {
            log.error("No wWorkflow found for data: {}", personalnummer);
        }
        return workflow;
    }
}
