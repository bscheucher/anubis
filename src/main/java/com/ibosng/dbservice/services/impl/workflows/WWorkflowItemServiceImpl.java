package com.ibosng.dbservice.services.impl.workflows;

import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.repositories.workflows.WWorkflowItemRepository;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional("postgresTransactionManager")
public class WWorkflowItemServiceImpl implements WWorkflowItemService {

    private final WWorkflowItemRepository wWorkflowItemRepository;

    public WWorkflowItemServiceImpl(WWorkflowItemRepository wWorkflowItemRepository) {
        this.wWorkflowItemRepository = wWorkflowItemRepository;
    }

    @Override
    public List<WWorkflowItem> findAll() {
        return wWorkflowItemRepository.findAll();
    }

    @Override
    public Optional<WWorkflowItem> findById(Integer id) {
        return wWorkflowItemRepository.findById(id);
    }

    @Override
    public WWorkflowItem save(WWorkflowItem object) {
        return wWorkflowItemRepository.save(object);
    }

    @Override
    public List<WWorkflowItem> saveAll(List<WWorkflowItem> objects) {
        return wWorkflowItemRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        wWorkflowItemRepository.deleteById(id);
    }

    @Override
    public List<WWorkflowItem> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<WWorkflowItem> findAllByWorkflow(WWorkflow wWorkflow) {
        return wWorkflowItemRepository.findAllByWorkflow(wWorkflow);
    }

    @Override
    public List<WWorkflowItem> findAllByWorkflowId(Integer wWorkflowId) {
        return wWorkflowItemRepository.findAllByWorkflowId(wWorkflowId);
    }

    @Override
    public List<WWorkflowItem> findAllByWorkflowAndStatus(WWorkflow wWorkflow, WWorkflowStatus status) {
        return wWorkflowItemRepository.findAllByWorkflowAndStatus(wWorkflow, status);
    }

    @Override
    public List<WWorkflowItem> findAllByWorkflowIdAndStatus(Integer wWorkflowId, WWorkflowStatus status) {
        return wWorkflowItemRepository.findAllByWorkflowIdAndStatus(wWorkflowId, status);
    }

    @Override
    public WWorkflowItem findBySuccessor(WWorkflowItem workflowItem) {
        return wWorkflowItemRepository.findBySuccessor(workflowItem);
    }

    @Override
    public WWorkflowItem findBySuccessorId(Integer wWorkflowItemId) {
        return wWorkflowItemRepository.findBySuccessorId(wWorkflowItemId);
    }

    @Override
    public List<WWorkflowItem> findAllByPredecessor(WWorkflowItem workflowItem) {
        return wWorkflowItemRepository.findAllByPredecessor(workflowItem);
    }

    @Override
    public List<WWorkflowItem> findAllByPredecessorId(Integer wWorkflowItemId) {
        return wWorkflowItemRepository.findAllByPredecessorId(wWorkflowItemId);
    }

    @Override
    public WWorkflowItem findByWorkflowAndWorkflowItemName(WWorkflow wWorkflow, String name) {
        return wWorkflowItemRepository.findByWorkflowAndWorkflowItemName(wWorkflow, name);
    }

    @Override
    public List<WWorkflowItem> findAllByNameAndStatus(String sWorkflowItem, WWorkflowStatus status) {
        return wWorkflowItemRepository.findAllByNameAndStatus(sWorkflowItem, status);
    }

    @Override
    public WorkflowItemDto findFirstIncompleteItemWithCompletedPredecessor(String data, String wwgName) {
        List<Object[]> objects = wWorkflowItemRepository.findFirstIncompleteItemWithCompletedPredecessor(data, wwgName);
        if (objects.isEmpty()) {
            return null;
        }
        return mapObjectToWorkflowITemDto(objects.get(0));
    }

    private WorkflowItemDto mapObjectToWorkflowITemDto(Object[] object) {
        WorkflowItemDto dto = new WorkflowItemDto();
        dto.setWorkflowItemId((Integer) object[0]);
        dto.setReferenceWorkflowItemId((Integer) object[1]);
        dto.setWorkflowItemName((String) object[2]);
        dto.setWorkflowItemStatus(WWorkflowStatus.fromCode(((Number) object[3]).intValue()));
        dto.setChangedOn(((Timestamp) object[4]).toLocalDateTime());
        dto.setChangedBy((String) object[5]);
        return dto;
    }

    @Override
    public void refreshWorkflowItem(WWorkflowItem wWorkflowItem) {
        wWorkflowItemRepository.refresh(wWorkflowItem);
    }

    @Override
    public WWorkflowItem findFreshWorkflowItemById(Integer id) {
        return wWorkflowItemRepository.findFreshWorkflowItemById(id);
    }
}
