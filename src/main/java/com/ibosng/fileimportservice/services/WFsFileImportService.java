package com.ibosng.fileimportservice.services;

import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;

import java.util.List;

public interface WFsFileImportService {
    WWorkflowGroup createWWGroup(List<FileDetails> files);

    WWorkflow startWFWithWFItem(WWorkflowGroup workflowGroup, String filename);

    void closeWWItem(WWorkflow workflow, String payload);

    void setWWItemToError(WWorkflow workflow);
}
