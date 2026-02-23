package com.ibosng.workflowservice.services.impl;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageWFItemsServiceImpl implements ManageWFItemsService {

    private final WWorkflowItemService wWorkflowItemService;
    private final WWorkflowService wWorkflowService;

    @Override
    public WWorkflowItem setWFItemStatus(WWorkflow workflow, SWorkflowItems workflowItem, WWorkflowStatus status, String changedBy) {
        WWorkflowItem wWorkflowItem = wWorkflowItemService.findByWorkflowAndWorkflowItemName(workflow, workflowItem.getValue());
        wWorkflowItem.setStatus(status);
        wWorkflowItem.setChangedBy(changedBy);
        wWorkflowItem.setChangedOn(getLocalDateNow());
        return wWorkflowItemService.save(wWorkflowItem);
    }

    @Override
    public void setWFItemSuccessorAndPredecessor(WWorkflow workflow, SWorkflowItems successor, SWorkflowItems predecessor, String changedBy) {
        WWorkflowItem successorWWItem = wWorkflowItemService.findByWorkflowAndWorkflowItemName(workflow, successor.getValue());
        WWorkflowItem predecessorWWItem = wWorkflowItemService.findByWorkflowAndWorkflowItemName(workflow, predecessor.getValue());
        successorWWItem.setPredecessor(predecessorWWItem);
        successorWWItem.setChangedBy(changedBy);
        successorWWItem.setChangedOn(getLocalDateNow());
        predecessorWWItem.setSuccessor(successorWWItem);
        predecessorWWItem.setChangedBy(changedBy);
        predecessorWWItem.setChangedOn(getLocalDateNow());
        wWorkflowItemService.saveAll(Arrays.asList(successorWWItem, predecessorWWItem));
    }

    @Override
    public List<WWorkflowItem> findAllByWorkflowGroup(WWorkflowGroup wWorkflowGroup) {
        List<WWorkflowItem> wWorkflowItems = new ArrayList<>();
        List<WWorkflow> allWorkflows = wWorkflowService.findAllByWorkflowGroup(wWorkflowGroup);
        for (WWorkflow wWorkflow : allWorkflows) {
            wWorkflowItems.addAll(wWorkflowItemService.findAllByWorkflow(wWorkflow));
        }
        return wWorkflowItems;
    }

    @Override
    public WWorkflowItem findFirstIncompleteWorkflowItem(WWorkflowGroup wWorkflowGroup) {
        List<WWorkflowItem> result = new ArrayList<>();

        // Retrieve all WWorkflowItems in the WWorkflowGroup
        List<WWorkflow> wWorkflows = wWorkflowService.findAllByWorkflowGroup(wWorkflowGroup);
        for (WWorkflow wWorkflow : wWorkflows) {
            List<WWorkflowItem> wWorkflowItems = wWorkflowItemService.findAllByWorkflow(wWorkflow);

            for (WWorkflowItem wWorkflowItem : wWorkflowItems) {
                // Check if the predecessor is completed but the current item is not
                if (wWorkflowItem.getPredecessor() != null &&
                        wWorkflowItem.getPredecessor().getStatus() == WWorkflowStatus.COMPLETED &&
                        wWorkflowItem.getStatus() != WWorkflowStatus.COMPLETED) {
                    result.add(wWorkflowItem);
                }
                if (wWorkflowItem.getPredecessor() == null && wWorkflowItem.getStatus() != WWorkflowStatus.COMPLETED) {
                    result.add(wWorkflowItem);
                }
            }
        }

        if (result.size() > 1) {
            List<WWorkflowItem> errors = result.stream().filter(wwi -> wwi.getStatus().equals(WWorkflowStatus.ERROR)).toList();
            if (errors.size() == 1) {
                return errors.get(0);
            } else if (errors.size() > 1) {
                if (findWWorkflowItemWithLowestSWorkflowItemId(errors).isPresent()) {
                    return findWWorkflowItemWithLowestSWorkflowItemId(errors).get();
                }
            } else {
                if (findWWorkflowItemWithLowestSWorkflowItemId(result).isPresent()) {
                    return findWWorkflowItemWithLowestSWorkflowItemId(result).get();
                }
            }
        } else if (result.size() == 1) {
            return result.get(0);
        }
        return null;
    }

    private Optional<WWorkflowItem> findWWorkflowItemWithLowestSWorkflowItemId(List<WWorkflowItem> wWorkflowItems) {
        if (wWorkflowItems == null || wWorkflowItems.isEmpty()) {
            return Optional.empty();
        }

        WWorkflowItem minWWorkflowItem = wWorkflowItems.get(0);
        int minSWorkflowItemId = minWWorkflowItem.getWorkflowItem().getId();

        for (WWorkflowItem wWorkflowItem : wWorkflowItems) {
            int currentSWorkflowItemId = wWorkflowItem.getWorkflowItem().getId();
            if (currentSWorkflowItemId < minSWorkflowItemId) {
                minWWorkflowItem = wWorkflowItem;
                minSWorkflowItemId = currentSWorkflowItemId;
            }
        }

        return Optional.of(minWWorkflowItem);
    }

}
