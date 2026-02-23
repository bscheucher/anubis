package com.ibosng.dbservice.services.workflows;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface WWorkflowService extends BaseService<WWorkflow> {
    List<WWorkflow> findAllByWorkflowGroup(WWorkflowGroup wWorkflowGroup);

    List<WWorkflow> findAllByWorkflowGroupId(Integer wWorkflowGroupId);

    WWorkflow findBySuccessor(WWorkflow wWorkflow);

    WWorkflow findBySuccessorId(Integer wWorkflowId);

    List<WWorkflow> findAllByPredecessor(WWorkflow wWorkflow);

    List<WWorkflow> findAllByPredecessorId(Integer wWorkflowId);

    List<WWorkflow> findAllByData(String data);
    WWorkflow findByIdWithGroup(Integer id);
    List<WWorkflow> findByDataAndNameAndStatus(String data, String name, WWorkflowStatus status);

    WWorkflow findFreshWorkflowById(Integer id);
    WWorkflow findLastWorkflowInChain(Integer currentWfId);
}
