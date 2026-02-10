package com.ibosng.workflowservice.services;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.workflowservice.enums.SWorkflows;

public interface WFStartService {
    WWorkflowGroup createImportParticipantsWFGroup(String identifier, String createdBy);
    WWorkflow createImportParticipantsWF(WWorkflowGroup wWorkflowGroup, String identifier, String createdBy);
    WWorkflowGroup createOnboardingEmployeesWFGroup(String identifier, String createdBy);
    WWorkflow createOnboardingEmployeesWF(WWorkflowGroup wWorkflowGroup, String data, String createdBy);
    WWorkflow createWFAndWFI(WWorkflowGroup wWorkflowGroup, SWorkflows sWorkflowEnum, String data, String createdBy);
    WWorkflowGroup createWorkflowGroupAndInstances(String sWorkflowGroupName, String identifier, String createdBy);
}
