package com.ibosng.dbservice.repositories.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflow;
import com.ibosng.dbservice.entities.workflows.SWorkflowItem;
import com.ibosng.dbservice.entities.workflows.SWorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface SWorkflowItemRepository extends JpaRepository<SWorkflowItem, Integer> {

    List<SWorkflowItem> findAllByWorkflow(SWorkflow sWorkflow);

    @Query("select ww from SWorkflowItem ww where ww.workflow.id = :workflowId")
    List<SWorkflowItem> findAllByWorkflowId(Integer workflowId);

    List<SWorkflowItem> findAllByWorkflowAndStatus(SWorkflow sWorkflow, SWorkflowStatus status);

    @Query("select ww from SWorkflowItem ww where ww.workflow.id = :sWorkflowId and ww.status = :status")
    List<SWorkflowItem> findAllByWorkflowIdAndStatus(Integer sWorkflowId, SWorkflowStatus status);

    SWorkflowItem findBySuccessor(SWorkflowItem workflowItem);

    @Query("select ww from SWorkflowItem ww where ww.successor.id = :workflowItemId")
    SWorkflowItem findBySuccessorId(Integer workflowItemId);

    List<SWorkflowItem> findAllByPredecessor(SWorkflowItem workflowItem);

    @Query("select ww from SWorkflowItem ww where ww.predecessor.id = :workflowItemId")
    List<SWorkflowItem> findAllByPredecessorId(Integer workflowItemId);
}
