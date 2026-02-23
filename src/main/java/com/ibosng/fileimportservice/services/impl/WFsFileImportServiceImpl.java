package com.ibosng.fileimportservice.services.impl;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.impl.workflows.WWorkflowItemServiceImpl;
import com.ibosng.dbservice.services.impl.workflows.WWorkflowServiceImpl;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.fileimportservice.services.WFsFileImportService;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.WFStartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ibosng.fileimportservice.utils.Constants.FILE_IMPORT_SERVICE;

@Service
@Slf4j
public class WFsFileImportServiceImpl implements WFsFileImportService {

    private final WFStartService wfStartService;
    private final ManageWFItemsService manageWFItemsService;
    private final WWorkflowServiceImpl wWorkflowService;
    private final WWorkflowItemServiceImpl wWorkflowItemService;
    private final WWorkflowGroupService wWorkflowGroupService;

    public WFsFileImportServiceImpl(WFStartService wfStartService, ManageWFItemsService manageWFItemsService, WWorkflowServiceImpl wWorkflowService, WWorkflowItemServiceImpl wWorkflowItemService, WWorkflowGroupService wWorkflowGroupService) {
        this.wfStartService = wfStartService;
        this.manageWFItemsService = manageWFItemsService;
        this.wWorkflowService = wWorkflowService;
        this.wWorkflowItemService = wWorkflowItemService;
        this.wWorkflowGroupService = wWorkflowGroupService;
    }

    @Override
    public WWorkflowGroup createWWGroup(List<FileDetails> files) {
        WWorkflowGroup workflowGroup = wfStartService.createImportParticipantsWFGroup(
                files.stream()
                        .map(FileDetails::getFilename)
                        .collect(Collectors.joining(", ")),
                FILE_IMPORT_SERVICE);
        workflowGroup.setStatus(WWorkflowStatus.IN_PROGRESS);
        return wWorkflowGroupService.save(workflowGroup);

    }

    @Override
    public WWorkflow startWFWithWFItem(WWorkflowGroup workflowGroup, String filename) {
        WWorkflow workflow = wfStartService.createImportParticipantsWF(workflowGroup, filename, FILE_IMPORT_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.IMPORTING_TEILNEHMER, WWorkflowStatus.IN_PROGRESS, FILE_IMPORT_SERVICE);
        manageWFItemsService.setWFItemSuccessorAndPredecessor(workflow, SWorkflowItems.VALIDATING_TEILNEHMER, SWorkflowItems.IMPORTING_TEILNEHMER, FILE_IMPORT_SERVICE);
        workflow.setStatus(WWorkflowStatus.IN_PROGRESS);
        wWorkflowService.save(workflow);
        return workflow;
    }

    @Override
    public void closeWWItem(WWorkflow workflow, String payload) {
        log.info("Closing wwi for importing files.");
        WWorkflowItem workflowItem = wWorkflowItemService.findByWorkflowAndWorkflowItemName(workflow, SWorkflowItems.IMPORTING_TEILNEHMER.getValue());
        workflowItem.setData(payload);
        wWorkflowItemService.save(workflowItem);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.IMPORTING_TEILNEHMER, WWorkflowStatus.COMPLETED, FILE_IMPORT_SERVICE);
    }

    @Override
    public void setWWItemToError(WWorkflow workflow) {
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.IMPORTING_TEILNEHMER, WWorkflowStatus.ERROR, FILE_IMPORT_SERVICE);
        workflow.setStatus(WWorkflowStatus.PENDING);
        wWorkflowService.save(workflow);
    }
}
