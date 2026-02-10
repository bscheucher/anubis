package com.ibosng.dbservice.services.impl.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflow;
import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.repositories.workflows.SWorkflowRepository;
import com.ibosng.dbservice.services.workflows.SWorkflowService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SWorkflowServiceImpl implements SWorkflowService {

    private final SWorkflowRepository sWorkflowRepository;

    public SWorkflowServiceImpl(SWorkflowRepository sWorkflowRepository) {
        this.sWorkflowRepository = sWorkflowRepository;
    }

    @Override
    public List<SWorkflow> findAll() {
        return sWorkflowRepository.findAll();
    }

    @Override
    public Optional<SWorkflow> findById(Integer id) {
        return sWorkflowRepository.findById(id);
    }

    @Override
    public SWorkflow save(SWorkflow object) {
        return sWorkflowRepository.save(object);
    }

    @Override
    public List<SWorkflow> saveAll(List<SWorkflow> objects) {
        return sWorkflowRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        sWorkflowRepository.deleteById(id);
    }

    @Override
    public List<SWorkflow> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<SWorkflow> findAllByWorkflowGroup(SWorkflowGroup sWorkflowGroup) {
        return sWorkflowRepository.findAllByWorkflowGroup(sWorkflowGroup);
    }

    @Override
    public List<SWorkflow> findAllByWorkflowGroupId(Integer sWorkflowGroupId) {
        return sWorkflowRepository.findAllByWorkflowGroupId(sWorkflowGroupId);
    }

    @Override
    public SWorkflow findBySuccessor(SWorkflow sWorkflow) {
        return sWorkflowRepository.findBySuccessor(sWorkflow);
    }

    @Override
    public SWorkflow findBySuccessorId(Integer sWorkflowId) {
        return sWorkflowRepository.findBySuccessorId(sWorkflowId);
    }

    @Override
    public List<SWorkflow> findAllByPredecessor(SWorkflow sWorkflow) {
        return sWorkflowRepository.findAllByPredecessor(sWorkflow);
    }

    @Override
    public List<SWorkflow> findAllByPredecessorId(Integer sWorkflowId) {
        return sWorkflowRepository.findAllByPredecessorId(sWorkflowId);
    }

    @Override
    public SWorkflow findByName(String name) {
        return sWorkflowRepository.findByName(name);
    }
}
