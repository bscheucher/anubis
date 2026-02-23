package com.ibosng.dbservice.services.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface WWorkflowGroupService extends BaseService<WWorkflowGroup> {

    List<WWorkflowGroup> findAllByDataAndWorkflowGroup(String data, SWorkflowGroup workflowGroup);

    List<WWorkflowGroup> findAllByDataAndWorkflowGroupAndStatus(String data, SWorkflowGroup workflowGroup, WWorkflowStatus status);
}
