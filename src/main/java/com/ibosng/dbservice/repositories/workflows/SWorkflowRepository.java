package com.ibosng.dbservice.repositories.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflow;
import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface SWorkflowRepository extends JpaRepository<SWorkflow, Integer> {

    List<SWorkflow> findAllByWorkflowGroup(SWorkflowGroup sWorkflowGroup);

    @Query("select sw from SWorkflow sw where sw.workflowGroup.id = :workflowGroupIp")
    List<SWorkflow> findAllByWorkflowGroupId(Integer workflowGroupIp);

    SWorkflow findBySuccessor(SWorkflow sWorkflow);

    @Query("select sw from SWorkflow sw where sw.successor.id = :sWorkflowId")
    SWorkflow findBySuccessorId(Integer sWorkflowId);

    List<SWorkflow> findAllByPredecessor(SWorkflow sWorkflow);

    @Query("select sw from SWorkflow sw where sw.predecessor.id = :sWorkflowId")
    List<SWorkflow> findAllByPredecessorId(Integer sWorkflowId);

    SWorkflow findByName(String name);
}
