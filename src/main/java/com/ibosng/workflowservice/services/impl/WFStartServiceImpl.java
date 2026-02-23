package com.ibosng.workflowservice.services.impl;

import com.ibosng.dbservice.entities.workflows.*;
import com.ibosng.dbservice.services.workflows.SWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.SWorkflowItemService;
import com.ibosng.dbservice.services.workflows.SWorkflowService;
import com.ibosng.workflowservice.enums.SWorkflowGroups;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.WFService;
import com.ibosng.workflowservice.services.WFStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WFStartServiceImpl implements WFStartService {

    private final SWorkflowGroupService sWorkflowGroupService;
    private final SWorkflowService sWorkflowService;
    private final SWorkflowItemService sWorkflowItemService;
    private final WFService wfService;


    @Override
    public WWorkflowGroup createImportParticipantsWFGroup(String identifier, String createdBy) {
        SWorkflowGroup sWorkflowGroup = sWorkflowGroupService.findByName(SWorkflowGroups.IMPORT_TEILNEHMER_FILES.getValue());
        return wfService.createWFGroup(sWorkflowGroup, identifier, createdBy);
    }

    @Override
    public WWorkflow createImportParticipantsWF(WWorkflowGroup wWorkflowGroup, String identifier, String createdBy) {
        SWorkflow sWorkflow = sWorkflowService.findByName(SWorkflows.IMPORT_TEILNEHMER.getValue());
        List<SWorkflowItem> sWorkflowItems = sWorkflowItemService.findAllByWorkflow(sWorkflow);
        WWorkflow wWorkflow = wfService.createWF(sWorkflow, wWorkflowGroup, identifier, createdBy);
        wfService.createWFItems(wWorkflow, sWorkflowItems, identifier, createdBy);
        return wWorkflow;
    }

    @Override
    public WWorkflowGroup createOnboardingEmployeesWFGroup(String identifier, String createdBy) {
        SWorkflowGroup sWorkflowGroup = sWorkflowGroupService.findByName(SWorkflowGroups.NEW_MITARBEITER.getValue());
        return wfService.createWFGroup(sWorkflowGroup, identifier, createdBy);
    }

    @Override
    public WWorkflow createOnboardingEmployeesWF(WWorkflowGroup wWorkflowGroup, String data, String createdBy) {
        SWorkflow sWorkflow = sWorkflowService.findByName(SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN.getValue());
        List<SWorkflowItem> sWorkflowItems = sWorkflowItemService.findAllByWorkflow(sWorkflow);
        WWorkflow wWorkflow = wfService.createWF(sWorkflow, wWorkflowGroup, data, createdBy);
        wfService.createWFItems(wWorkflow, sWorkflowItems, data, createdBy);
        return wWorkflow;
    }

    @Override
    public WWorkflow createWFAndWFI(WWorkflowGroup wWorkflowGroup, SWorkflows sWorkflowEnum, String data, String createdBy) {
        SWorkflow sWorkflow = sWorkflowService.findByName(sWorkflowEnum.getValue());
        List<SWorkflowItem> sWorkflowItems = sWorkflowItemService.findAllByWorkflow(sWorkflow);
        WWorkflow wWorkflow = wfService.createWF(sWorkflow, wWorkflowGroup, data, createdBy);
        wfService.createWFItems(wWorkflow, sWorkflowItems, data, createdBy);
        return wWorkflow;
    }

    @Override
    public WWorkflowGroup createWorkflowGroupAndInstances(String sWorkflowGroupName, String identifier, String createdBy) {
        // Step 1: Create WWorkflowGroup based on SWorkflowGroup
        SWorkflowGroup sWorkflowGroup = sWorkflowGroupService.findByName(sWorkflowGroupName);
        WWorkflowGroup wWorkflowGroup = wfService.createWFGroup(sWorkflowGroup, identifier, createdBy);

        // Step 2: Retrieve all SWorkflows associated with the SWorkflowGroup
        List<SWorkflow> sWorkflows = sWorkflowService.findAllByWorkflowGroup(sWorkflowGroup);

        // Step 3: Create a global map to store all WWorkflowItems
        Map<Integer, WWorkflow> globalWWorkflowMap = new HashMap<>();
        Map<Integer, WWorkflowItem> globalWWorkflowItemMap = new HashMap<>();

        // Iterate through each SWorkflow and create corresponding WWorkflow
        for (SWorkflow sWorkflow : sWorkflows) {
            List<SWorkflowItem> sWorkflowItems = sWorkflowItemService.findAllByWorkflow(sWorkflow);
            WWorkflow wWorkflow = wfService.createWF(sWorkflow, wWorkflowGroup, identifier, createdBy);
            globalWWorkflowMap.put(sWorkflow.getId(), wWorkflow);

            // Step 4: Create WWorkflowItems based on SWorkflowItems and add them to the global map
            for (SWorkflowItem sWorkflowItem : sWorkflowItems) {
                WWorkflowItem wWorkflowItem = wfService.createWFItem(wWorkflow, sWorkflowItem, identifier, createdBy);
                globalWWorkflowItemMap.put(sWorkflowItem.getId(), wWorkflowItem);
            }
        }

        // Step 5: Set predecessors and successors for WWorkflowItems
        for (SWorkflow sWorkflow : sWorkflows) {
            List<SWorkflowItem> sWorkflowItems = sWorkflowItemService.findAllByWorkflow(sWorkflow);
            WWorkflow wWorkflow = globalWWorkflowMap.get(sWorkflow.getId());
            if (sWorkflow.getPredecessor() != null) {
                wWorkflow.setPredecessor(globalWWorkflowMap.get(sWorkflow.getPredecessor().getId()));
            }
            if (sWorkflow.getSuccessor() != null) {
                wWorkflow.setSuccessor(globalWWorkflowMap.get(sWorkflow.getSuccessor().getId()));
            }
            for (SWorkflowItem sWorkflowItem : sWorkflowItems) {
                WWorkflowItem wWorkflowItem = globalWWorkflowItemMap.get(sWorkflowItem.getId());
                if (sWorkflowItem.getPredecessor() != null) {
                    wWorkflowItem.setPredecessor(globalWWorkflowItemMap.get(sWorkflowItem.getPredecessor().getId()));
                }
                if (sWorkflowItem.getSuccessor() != null) {
                    wWorkflowItem.setSuccessor(globalWWorkflowItemMap.get(sWorkflowItem.getSuccessor().getId()));
                }
                wfService.updateWFItem(wWorkflowItem, createdBy); // Persist changes to the database
            }
        }

        return wWorkflowGroup;
    }


}
