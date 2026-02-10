package com.ibosng.dbservice.services.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.services.BaseService;

public interface SWorkflowGroupService extends BaseService<SWorkflowGroup> {
    SWorkflowGroup findByName(String name);
}
