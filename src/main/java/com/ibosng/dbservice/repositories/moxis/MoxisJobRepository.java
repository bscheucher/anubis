package com.ibosng.dbservice.repositories.moxis;

import com.ibosng.dbservice.entities.moxis.MoxisJob;
import com.ibosng.dbservice.entities.moxis.MoxisJobStatus;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface MoxisJobRepository extends JpaRepository<MoxisJob, Integer> {

    List<MoxisJob> findAllByReferenceId(String referenceId);

    List<MoxisJob> findAllByReferenceIdAndStatus(String referenceId, MoxisJobStatus status);

    List<MoxisJob> findAllByWorkflow(WWorkflow workflow);

    List<MoxisJob> findAllByWorkflowAndStatus(WWorkflow workflow, MoxisJobStatus status);

    @Query("select mj from MoxisJob mj where mj.workflow.id = :workflowId and mj.status = :status")
    List<MoxisJob> findAllByWorkflowIdAndStatus(Integer workflowId, MoxisJobStatus status);

    List<MoxisJob> findAllByStatus(MoxisJobStatus status);

    List<MoxisJob> findAllByReferenceIdAndWorkflow_Id(String referenceId, Integer workflowId);

    List<MoxisJob> findAllByReferenceIdAndWorkflow_IdAndStatusIn(String referenceId, Integer workflowId, List<MoxisJobStatus> statuses);
}
