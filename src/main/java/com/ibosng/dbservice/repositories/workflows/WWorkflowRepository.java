package com.ibosng.dbservice.repositories.workflows;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface WWorkflowRepository extends JpaRepository<WWorkflow, Integer> {

    List<WWorkflow> findAllByWorkflowGroup(WWorkflowGroup workflowGroup);

    @Query("select ww from WWorkflow ww where ww.workflowGroup.id = :workflowGroupId")
    List<WWorkflow> findAllByWorkflowGroupId(Integer workflowGroupId);

    List<WWorkflow> findAllByWorkflowGroupAndStatus(WWorkflowGroup workflowGroup, WWorkflowStatus status);

    @Query("select ww from WWorkflow ww where ww.workflowGroup.id = :workflowGroupIp and ww.status = :status")
    List<WWorkflow> findAllByWorkflowGroupIdAndStatus(Integer workflowGroupIp, WWorkflowStatus status);

    WWorkflow findBySuccessor(WWorkflow wWorkflow);

    @Query("select ww from WWorkflow ww where ww.successor.id = :wWorkflowId")
    WWorkflow findBySuccessorId(Integer wWorkflowId);

    List<WWorkflow> findAllByPredecessor(WWorkflow wWorkflow);

    @Query("select ww from WWorkflow ww where ww.predecessor.id = :wWorkflowId")
    List<WWorkflow> findAllByPredecessorId(Integer wWorkflowId);

    List<WWorkflow> findAllByData(String data);

    @Query("select w from WWorkflow w join fetch w.workflowGroup where w.id = :id")
    WWorkflow findByIdWithGroup(Integer id);

    @Query("select w from WWorkflow w where w.data = :data and w.workflow.name = :name and w.status = :status")
    List<WWorkflow> findByDataAndNameAndStatus(String data, String name, WWorkflowStatus status);

    @Query(value = "select ww from WWorkflow ww where ww.id = :id")
    WWorkflow findFreshWorkflowById(Integer id);

    @Query(value = "select w_successor from WWorkflow ws " +
            "join WWorkflow w_successor on w_successor.workflowGroup = ws.workflowGroup " +
            "where w_successor.successor is null and ws.id = :currentWfId")
    WWorkflow findLastWorkflowInChain(Integer currentWfId);

}
