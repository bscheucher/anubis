package com.ibosng.workflowservice.services;

import com.ibosng.dbservice.entities.workflows.*;

import java.util.List;


public interface WFService {

    WWorkflowGroup createWFGroup(SWorkflowGroup sWorkflowGroup, String data, String createdBy);
    WWorkflow createWF(SWorkflow sWorkflow, WWorkflowGroup wWorkflowGroup, String data, String createdBy);
    WWorkflowItem createWFItem(WWorkflow wWorkflow, SWorkflowItem sWorkflowItem, String data, String createdBy);
    List<WWorkflowItem> createWFItems(WWorkflow wWorkflow, List<SWorkflowItem> sWorkflowItems, String data, String createdBy);
    WWorkflowItem updateWFItem(WWorkflowItem workflowItem, String changedBy);
    void resetWfToStatus(WWorkflow wworkflow, WWorkflowStatus status, String changedBy);
    boolean resetWfChainToStatus(WWorkflow currentWworkflow, WWorkflow targetWworkflow, WWorkflowStatus status, String changedBy);
}
