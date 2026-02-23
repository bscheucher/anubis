package com.ibosng.workflowservice.services;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.workflowservice.enums.SWorkflows;


public interface ManageWFsService {
    WWorkflow setWFStatus(WWorkflowGroup wWorkflowGroup, SWorkflows sWorkflow, WWorkflowStatus status, String changedBy);
    WWorkflow getWorkflowFromDataAndWFType(String personalnummer, SWorkflows sWorkflow);
}
