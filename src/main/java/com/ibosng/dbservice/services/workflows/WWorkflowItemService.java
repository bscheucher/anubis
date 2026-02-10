package com.ibosng.dbservice.services.workflows;

import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface WWorkflowItemService extends BaseService<WWorkflowItem> {
    List<WWorkflowItem> findAllByWorkflow(WWorkflow wWorkflow);

    List<WWorkflowItem> findAllByWorkflowId(Integer wWorkflowId);

    List<WWorkflowItem> findAllByWorkflowAndStatus(WWorkflow wWorkflow, WWorkflowStatus status);

    List<WWorkflowItem> findAllByWorkflowIdAndStatus(Integer wWorkflowId, WWorkflowStatus status);

    WWorkflowItem findBySuccessor(WWorkflowItem workflowItem);

    WWorkflowItem findBySuccessorId(Integer wWorkflowItemId);

    List<WWorkflowItem> findAllByPredecessor(WWorkflowItem workflowItem);

    List<WWorkflowItem> findAllByPredecessorId(Integer wWorkflowItemId);

    WWorkflowItem findByWorkflowAndWorkflowItemName(WWorkflow wWorkflow, String name);

    List<WWorkflowItem> findAllByNameAndStatus(String sWorkflowItem, WWorkflowStatus status);

    WorkflowItemDto findFirstIncompleteItemWithCompletedPredecessor(String data, String wwgName);

    void refreshWorkflowItem(WWorkflowItem wWorkflowItem);

    WWorkflowItem findFreshWorkflowItemById(Integer id);
}
