package com.ibosng.dbservice.services.impl.workflows;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.repositories.workflows.WWorkflowRepository;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WWorkflowServiceImpl implements WWorkflowService {

    private final WWorkflowRepository wWorkflowRepository;

    public WWorkflowServiceImpl(WWorkflowRepository wWorkflowRepository) {
        this.wWorkflowRepository = wWorkflowRepository;
    }

    @Override
    public List<WWorkflow> findAll() {
        return wWorkflowRepository.findAll();
    }

    @Override
    public Optional<WWorkflow> findById(Integer id) {
        return wWorkflowRepository.findById(id);
    }

    @Override
    public WWorkflow save(WWorkflow object) {
        return wWorkflowRepository.save(object);
    }

    @Override
    public List<WWorkflow> saveAll(List<WWorkflow> objects) {
        return wWorkflowRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        wWorkflowRepository.deleteById(id);
    }

    @Override
    public List<WWorkflow> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<WWorkflow> findAllByWorkflowGroup(WWorkflowGroup wWorkflowGroup) {
        return wWorkflowRepository.findAllByWorkflowGroup(wWorkflowGroup);
    }

    @Override
    public List<WWorkflow> findAllByWorkflowGroupId(Integer wWorkflowGroupId) {
        return wWorkflowRepository.findAllByWorkflowGroupId(wWorkflowGroupId);
    }

    @Override
    public WWorkflow findBySuccessor(WWorkflow wWorkflow) {
        return wWorkflowRepository.findBySuccessor(wWorkflow);
    }

    @Override
    public WWorkflow findBySuccessorId(Integer wWorkflowId) {
        return wWorkflowRepository.findBySuccessorId(wWorkflowId);
    }

    @Override
    public List<WWorkflow> findAllByPredecessor(WWorkflow wWorkflow) {
        return wWorkflowRepository.findAllByPredecessor(wWorkflow);
    }

    @Override
    public List<WWorkflow> findAllByPredecessorId(Integer wWorkflowId) {
        return wWorkflowRepository.findAllByPredecessorId(wWorkflowId);
    }

    @Override
    public List<WWorkflow> findAllByData(String data) {
        return wWorkflowRepository.findAllByData(data);
    }

    @Override
    public WWorkflow findByIdWithGroup(Integer id) {
        return wWorkflowRepository.findByIdWithGroup(id);
    }

    @Override
    public List<WWorkflow> findByDataAndNameAndStatus(String data, String name, WWorkflowStatus status) {
        return wWorkflowRepository.findByDataAndNameAndStatus(data, name, status);
    }

    @Override
    public WWorkflow findFreshWorkflowById(Integer id) {
        return wWorkflowRepository.findFreshWorkflowById(id);
    }

    @Override
    public WWorkflow findLastWorkflowInChain(Integer currentWfId) {
        return wWorkflowRepository.findLastWorkflowInChain(currentWfId);
    }
}
