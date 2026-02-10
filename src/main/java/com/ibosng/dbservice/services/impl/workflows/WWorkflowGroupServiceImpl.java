package com.ibosng.dbservice.services.impl.workflows;

import com.ibosng.dbservice.entities.workflows.SWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.repositories.workflows.WWorkflowGroupRepository;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WWorkflowGroupServiceImpl implements WWorkflowGroupService {

    private final WWorkflowGroupRepository wWorkflowGroupRepository;

    public WWorkflowGroupServiceImpl(WWorkflowGroupRepository wWorkflowGroupRepository) {
        this.wWorkflowGroupRepository = wWorkflowGroupRepository;
    }

    @Override
    public List<WWorkflowGroup> findAll() {
        return wWorkflowGroupRepository.findAll();
    }

    @Override
    public Optional<WWorkflowGroup> findById(Integer id) {
        return wWorkflowGroupRepository.findById(id);
    }

    @Override
    public WWorkflowGroup save(WWorkflowGroup object) {
        return wWorkflowGroupRepository.save(object);
    }

    @Override
    public List<WWorkflowGroup> saveAll(List<WWorkflowGroup> objects) {
        return wWorkflowGroupRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        wWorkflowGroupRepository.deleteById(id);
    }

    @Override
    public List<WWorkflowGroup> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<WWorkflowGroup> findAllByDataAndWorkflowGroup(String data, SWorkflowGroup workflowGroup) {
        return wWorkflowGroupRepository.findAllByDataAndWorkflowGroup(data, workflowGroup);
    }

    @Override
    public List<WWorkflowGroup> findAllByDataAndWorkflowGroupAndStatus(String data, SWorkflowGroup workflowGroup, WWorkflowStatus status) {
        return wWorkflowGroupRepository.findAllByDataAndWorkflowGroupAndStatus(data, workflowGroup, status);
    }
}
