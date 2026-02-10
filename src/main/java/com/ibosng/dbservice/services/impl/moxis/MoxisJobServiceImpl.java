package com.ibosng.dbservice.services.impl.moxis;

import com.ibosng.dbservice.entities.moxis.MoxisJob;
import com.ibosng.dbservice.entities.moxis.MoxisJobStatus;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.repositories.moxis.MoxisJobRepository;
import com.ibosng.dbservice.services.moxis.MoxisJobService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;

@Service
public class MoxisJobServiceImpl implements MoxisJobService {

    private final MoxisJobRepository moxisJobRepository;

    public MoxisJobServiceImpl(MoxisJobRepository moxisJobRepository) {
        this.moxisJobRepository = moxisJobRepository;
    }

    @Override
    public List<MoxisJob> findAll() {
        return moxisJobRepository.findAll();
    }

    @Override
    public Optional<MoxisJob> findById(Integer id) {
        return moxisJobRepository.findById(id);
    }

    @Override
    public MoxisJob save(MoxisJob object) {
        return moxisJobRepository.save(object);
    }

    @Override
    public List<MoxisJob> saveAll(List<MoxisJob> objects) {
        return moxisJobRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        moxisJobRepository.deleteById(id);
    }

    @Override
    public List<MoxisJob> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public MoxisJob findAllByReferenceId(String referenceId) {
        return findFirstObject(moxisJobRepository.findAllByReferenceId(referenceId), new HashSet<>(List.of(referenceId)), "MoxisJob");
    }

    @Override
    public List<MoxisJob> findAllByReferenceIdAndStatus(String referenceId, MoxisJobStatus status) {
        return moxisJobRepository.findAllByReferenceIdAndStatus(referenceId, status);
    }

    @Override
    public List<MoxisJob> findAllByWorkflow(WWorkflow workflow) {
        return moxisJobRepository.findAllByWorkflow(workflow);
    }

    @Override
    public List<MoxisJob> findAllByWorkflowAndStatus(WWorkflow workflow, MoxisJobStatus status) {
        return moxisJobRepository.findAllByWorkflowAndStatus(workflow, status);
    }

    @Override
    public List<MoxisJob> findAllByWorkflowIdAndStatus(Integer workflowId, MoxisJobStatus status) {
        return moxisJobRepository.findAllByWorkflowIdAndStatus(workflowId, status);
    }

    @Override
    public List<MoxisJob> findAllByStatus(MoxisJobStatus status) {
        return moxisJobRepository.findAllByStatus(status);
    }

    @Override
    public List<MoxisJob> findAllByReferenceIdAndWorkflow_Id(String referenceId, Integer workflowId) {
        return moxisJobRepository.findAllByReferenceIdAndWorkflow_Id(referenceId, workflowId);
    }

    @Override
    public List<MoxisJob> findAllByReferenceIdAndWorkflow_IdAndStatus(String referenceId, Integer workflowId, List<MoxisJobStatus> statuses) {
        return moxisJobRepository.findAllByReferenceIdAndWorkflow_IdAndStatusIn(referenceId, workflowId, statuses);
    }
}
