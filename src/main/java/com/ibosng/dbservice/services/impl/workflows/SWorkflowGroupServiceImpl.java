package com.ibosng.dbservice.services.impl.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.repositories.workflows.SWorkflowGroupRepository;
import com.ibosng.dbservice.services.workflows.SWorkflowGroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SWorkflowGroupServiceImpl implements SWorkflowGroupService {

    private final SWorkflowGroupRepository sWorkflowGroupRepository;

    public SWorkflowGroupServiceImpl(SWorkflowGroupRepository sWorkflowGroupRepository) {
        this.sWorkflowGroupRepository = sWorkflowGroupRepository;
    }

    @Override
    public List<SWorkflowGroup> findAll() {
        return sWorkflowGroupRepository.findAll();
    }

    @Override
    public Optional<SWorkflowGroup> findById(Integer id) {
        return sWorkflowGroupRepository.findById(id);
    }

    @Override
    public SWorkflowGroup save(SWorkflowGroup object) {
        return sWorkflowGroupRepository.save(object);
    }

    @Override
    public List<SWorkflowGroup> saveAll(List<SWorkflowGroup> objects) {
        return sWorkflowGroupRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        sWorkflowGroupRepository.deleteById(id);
    }

    @Override
    public List<SWorkflowGroup> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SWorkflowGroup findByName(String name) {
        return sWorkflowGroupRepository.findByName(name);
    }
}
