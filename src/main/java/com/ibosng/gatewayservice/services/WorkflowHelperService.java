package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.workflows.WorkflowDto;
import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;

public interface WorkflowHelperService {
    PayloadTypeList<WorkflowDto> createPayloadForWorkflow(WWorkflow workflow);

    PayloadTypeList<WorkflowDto> createPayloadForWorkflowGroup(WWorkflowGroup workflowGroup);

    String getChangedBy(String changedBy);

    WorkflowItemDto getCurrentWfForVereinbarung(WWorkflow workflow);
}
