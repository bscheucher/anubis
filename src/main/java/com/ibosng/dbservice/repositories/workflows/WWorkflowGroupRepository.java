package com.ibosng.dbservice.repositories.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface WWorkflowGroupRepository extends JpaRepository<WWorkflowGroup, Integer> {

    List<WWorkflowGroup> findAllByDataAndWorkflowGroup(String data, SWorkflowGroup workflowGroup);
    List<WWorkflowGroup> findAllByDataAndWorkflowGroupAndStatus(String data, SWorkflowGroup workflowGroup, WWorkflowStatus status);
}
