package com.ibosng.dbservice.services.moxis;

import com.ibosng.dbservice.entities.moxis.MoxisJob;
import com.ibosng.dbservice.entities.moxis.MoxisJobStatus;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface MoxisJobService extends BaseService<MoxisJob> {
    MoxisJob findAllByReferenceId(String referenceId);
    List<MoxisJob> findAllByReferenceIdAndStatus(String referenceId, MoxisJobStatus status);
    List<MoxisJob> findAllByWorkflow(WWorkflow workflow);
    List<MoxisJob> findAllByWorkflowAndStatus(WWorkflow workflow, MoxisJobStatus status);
    List<MoxisJob> findAllByWorkflowIdAndStatus(Integer workflowId, MoxisJobStatus status);
    List<MoxisJob> findAllByStatus(MoxisJobStatus status);
    List<MoxisJob> findAllByReferenceIdAndWorkflow_Id(String referenceId, Integer workflowId);
    List<MoxisJob> findAllByReferenceIdAndWorkflow_IdAndStatus(String referenceId, Integer workflowId, List<MoxisJobStatus> statuses);
}
