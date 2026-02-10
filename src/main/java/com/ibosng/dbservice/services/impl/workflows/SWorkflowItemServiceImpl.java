package com.ibosng.dbservice.services.impl.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflow;
import com.ibosng.dbservice.entities.workflows.SWorkflowItem;
import com.ibosng.dbservice.entities.workflows.SWorkflowStatus;
import com.ibosng.dbservice.repositories.workflows.SWorkflowItemRepository;
import com.ibosng.dbservice.services.workflows.SWorkflowItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SWorkflowItemServiceImpl implements SWorkflowItemService {

    private final SWorkflowItemRepository sWorkflowItemRepository;

    public SWorkflowItemServiceImpl(SWorkflowItemRepository sWorkflowItemRepository) {
        this.sWorkflowItemRepository = sWorkflowItemRepository;
    }

    @Override
    public List<SWorkflowItem> findAll() {
        return sWorkflowItemRepository.findAll();
    }

    @Override
    public Optional<SWorkflowItem> findById(Integer id) {
        return sWorkflowItemRepository.findById(id);
    }

    @Override
    public SWorkflowItem save(SWorkflowItem object) {
        return sWorkflowItemRepository.save(object);
    }

    @Override
    public List<SWorkflowItem> saveAll(List<SWorkflowItem> objects) {
        return sWorkflowItemRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        sWorkflowItemRepository.deleteById(id);
    }

    @Override
    public List<SWorkflowItem> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<SWorkflowItem> findAllByWorkflow(SWorkflow sWorkflow) {
        return sWorkflowItemRepository.findAllByWorkflow(sWorkflow);
    }

    @Override
    public List<SWorkflowItem> findAllByWorkflowId(Integer workflowId) {
        return sWorkflowItemRepository.findAllByWorkflowId(workflowId);
    }

    @Override
    public List<SWorkflowItem> findAllByWorkflowAndStatus(SWorkflow sWorkflow, SWorkflowStatus status) {
        return sWorkflowItemRepository.findAllByWorkflowAndStatus(sWorkflow, status);
    }

    @Override
    public List<SWorkflowItem> findAllByWorkflowIdAndStatus(Integer sWorkflowId, SWorkflowStatus status) {
        return sWorkflowItemRepository.findAllByWorkflowIdAndStatus(sWorkflowId, status);
    }

    @Override
    public SWorkflowItem findBySuccessor(SWorkflowItem sWorkflowItem) {
        return sWorkflowItemRepository.findBySuccessor(sWorkflowItem);
    }

    @Override
    public SWorkflowItem findBySuccessorId(Integer sWorkflowItemId) {
        return sWorkflowItemRepository.findBySuccessorId(sWorkflowItemId);
    }

    @Override
    public List<SWorkflowItem> findAllByPredecessor(SWorkflowItem sWorkflowItem) {
        return sWorkflowItemRepository.findAllByPredecessor(sWorkflowItem);
    }

    @Override
    public List<SWorkflowItem> findAllByPredecessorId(Integer sWorkflowItemId) {
        return sWorkflowItemRepository.findAllByPredecessorId(sWorkflowItemId);
    }
}
