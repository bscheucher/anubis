package com.ibosng.workflowservice.services;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.workflowservice.enums.SWorkflowItems;

import java.util.List;


public interface ManageWFItemsService {
    WWorkflowItem setWFItemStatus(WWorkflow workflow, SWorkflowItems workflowItem, WWorkflowStatus status, String changedBy);
    void setWFItemSuccessorAndPredecessor(WWorkflow workflow, SWorkflowItems successor, SWorkflowItems predecessor, String changedBy);
    List<WWorkflowItem> findAllByWorkflowGroup(WWorkflowGroup wWorkflowGroup);
    WWorkflowItem findFirstIncompleteWorkflowItem(WWorkflowGroup wWorkflowGroup);
}
