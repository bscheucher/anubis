package com.ibosng.dbservice.services.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflow;
import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface SWorkflowService extends BaseService<SWorkflow> {
    List<SWorkflow> findAllByWorkflowGroup(SWorkflowGroup sWorkflowGroup);

    List<SWorkflow> findAllByWorkflowGroupId(Integer sWorkflowGroupId);

    SWorkflow findBySuccessor(SWorkflow sWorkflow);

    SWorkflow findBySuccessorId(Integer sWorkflowId);

    List<SWorkflow> findAllByPredecessor(SWorkflow sWorkflow);

    List<SWorkflow> findAllByPredecessorId(Integer sWorkflowId);

    SWorkflow findByName(String name);
}
