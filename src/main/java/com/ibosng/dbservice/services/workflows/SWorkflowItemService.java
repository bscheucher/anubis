package com.ibosng.dbservice.services.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflow;
import com.ibosng.dbservice.entities.workflows.SWorkflowItem;
import com.ibosng.dbservice.entities.workflows.SWorkflowStatus;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface SWorkflowItemService extends BaseService<SWorkflowItem> {
    List<SWorkflowItem> findAllByWorkflow(SWorkflow sWorkflow);

    List<SWorkflowItem> findAllByWorkflowId(Integer workflowId);

    List<SWorkflowItem> findAllByWorkflowAndStatus(SWorkflow sWorkflow, SWorkflowStatus status);

    List<SWorkflowItem> findAllByWorkflowIdAndStatus(Integer sWorkflowId, SWorkflowStatus status);

    SWorkflowItem findBySuccessor(SWorkflowItem sWorkflowItem);

    SWorkflowItem findBySuccessorId(Integer sWorkflowItemId);

    List<SWorkflowItem> findAllByPredecessor(SWorkflowItem sWorkflowItem);

    List<SWorkflowItem> findAllByPredecessorId(Integer sWorkflowItemId);
}
