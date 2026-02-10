package com.ibosng.workflowservice.services.impl;

import com.ibosng.dbservice.entities.workflows.*;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.workflowservice.services.WFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Service
@Slf4j
public class WFServiceImpl implements WFService {

    private final WWorkflowGroupService wWorkflowGroupService;
    private final WWorkflowService wWorkflowService;
    private final WWorkflowItemService wWorkflowItemService;

    public WFServiceImpl(WWorkflowGroupService wWorkflowGroupService, WWorkflowService wWorkflowService, WWorkflowItemService wWorkflowItemService) {
        this.wWorkflowGroupService = wWorkflowGroupService;
        this.wWorkflowService = wWorkflowService;
        this.wWorkflowItemService = wWorkflowItemService;
    }

    @Override
    public WWorkflowGroup createWFGroup(SWorkflowGroup sWorkflowGroup, String data, String createdBy) {
        WWorkflowGroup wWorkflowGroup = new WWorkflowGroup();
        wWorkflowGroup.setWorkflowGroup(sWorkflowGroup);
        wWorkflowGroup.setData(data);
        wWorkflowGroup.setCreatedOn(getLocalDateNow());
        wWorkflowGroup.setCreatedBy(createdBy);
        wWorkflowGroup.setStatus(WWorkflowStatus.IN_PROGRESS);
        wWorkflowGroupService.save(wWorkflowGroup);
        return wWorkflowGroup;
    }
    @Override
    public WWorkflow createWF(SWorkflow sWorkflow, WWorkflowGroup wWorkflowGroup, String data, String createdBy) {
        WWorkflow wWorkflow = new WWorkflow();
        wWorkflow.setWorkflow(sWorkflow);
        if(wWorkflowGroup != null) {
            wWorkflow.setWorkflowGroup(wWorkflowGroup);
        }
        wWorkflow.setData(data);
        wWorkflow.setCreatedOn(getLocalDateNow());
        wWorkflow.setCreatedBy(createdBy);
        wWorkflow.setStatus(WWorkflowStatus.NEW);
        wWorkflow = wWorkflowService.save(wWorkflow);
        return wWorkflow;
    }
    @Override
    public WWorkflowItem createWFItem(WWorkflow wWorkflow, SWorkflowItem sWorkflowItem, String data, String createdBy) {
        WWorkflowItem workflowItem = new WWorkflowItem();
        workflowItem.setWorkflow(wWorkflow);
        workflowItem.setWorkflowItem(sWorkflowItem);
        workflowItem.setCreatedOn(getLocalDateNow());
        workflowItem.setCreatedBy(createdBy);
        workflowItem.setStatus(WWorkflowStatus.NEW);
        workflowItem.setData(data);
        workflowItem = wWorkflowItemService.save(workflowItem);
        return workflowItem;
    }
    @Override
    public List<WWorkflowItem> createWFItems(WWorkflow wWorkflow, List<SWorkflowItem> sWorkflowItems, String data, String createdBy) {
        List<WWorkflowItem> workflowItems = new ArrayList<>();
        for(SWorkflowItem sWorkflowItem : sWorkflowItems) {
            workflowItems.add(createWFItem(wWorkflow, sWorkflowItem, data, createdBy));
        }
        return workflowItems;
    }

    @Override
    public WWorkflowItem updateWFItem(WWorkflowItem workflowItem, String changedBy) {
        workflowItem.setChangedBy(changedBy);
        workflowItem.setChangedOn(getLocalDateNow());
        return wWorkflowItemService.save(workflowItem);
    }

    /**
     * Resets the given workflow and its items to the specified status.
     *
     * Updates the status of all workflow items and the workflow itself,
     * sets the change metadata, and saves the changes.
     *
     * @param wworkflow The workflow to reset.
     * @param status    The new status to apply.
     * @param changedBy The user or process responsible for the change.
     */
    @Override
    public void resetWfToStatus(WWorkflow wworkflow, WWorkflowStatus status, String changedBy) {
        // Find wf items for workflow
        List<WWorkflowItem> wfItems = wWorkflowItemService.findAllByWorkflow(wworkflow);
        // Reset items and save them
        for(WWorkflowItem wfItem : wfItems){
            wfItem.setStatus(status);
            wfItem.setChangedOn(LocalDateTime.now());
            wfItem.setChangedBy(changedBy);
        }
        wWorkflowItemService.saveAll(wfItems);
        wworkflow.setStatus(status);
        // Update workflow
        wWorkflowService.save(wworkflow);
    }


    /**
     * Resets the status of all workflows in the chain from the current workflow to the target workflow.
     *
     * This method navigates through the workflow chain, either forwards (successors) or backwards (predecessors),
     * starting from the given `currentWworkflow` and ending at the `targetWworkflow`. It updates each workflow's
     * status to the specified `status` by calling the `resetWfToStatus` method.
     *
     * @param currentWworkflow The starting point of the workflow chain to reset.
     * @param targetWworkflow  The endpoint of the workflow chain to reset.
     * @param status           The status to set for each workflow in the chain.
     * @param changedBy        The user or process responsible for the change.
     * @return `true` if the chain was successfully reset; `false` if the target workflow is not part of the chain.
     *
     * If the `targetWworkflow` is not part of the chain (neither a predecessor nor a successor of the `currentWworkflow`),
     * the method logs an error and exits without making any changes.
     */
    @Override
    public boolean resetWfChainToStatus(WWorkflow currentWworkflow, WWorkflow targetWworkflow, WWorkflowStatus status, String changedBy) {
        log.info("Resetting chain from " + currentWworkflow.getWorkflow().getName() + " to " + targetWworkflow.getWorkflow().getName() + " to status: " + status.name());
        int chainDirection = isInChain(currentWworkflow, targetWworkflow);

        if (chainDirection == 0) {
            log.error("Target workflow is neither a predecessor nor a successor of the current workflow");
            return false;
        }

        WWorkflow iterator = currentWworkflow;
        while (iterator != null) {
            resetWfToStatus(iterator, status, changedBy);
            if (iterator.equals(targetWworkflow)) {
                break;
            }
            iterator = (chainDirection == -1) ? iterator.getPredecessor() : iterator.getSuccessor();
        }
        return true;
    }

    /**
     * Helper method to determine if the target is part of the chain relative to the current workflow.
     * @return -1 if target is a predecessor, 1 if it is a successor, 0 if it is not in the chain.
     */
    private int isInChain(WWorkflow current, WWorkflow target) {
        // Check predecessors
        WWorkflow iterator = current.getPredecessor();
        while (iterator != null) {
            if (iterator.equals(target)) {
                return -1;
            }
            iterator = iterator.getPredecessor();
        }

        // Check successors
        iterator = current.getSuccessor();
        while (iterator != null) {
            if (iterator.equals(target)) {
                return 1;
            }
            iterator = iterator.getSuccessor();
        }

        // Not in the chain
        return 0;
    }
}
