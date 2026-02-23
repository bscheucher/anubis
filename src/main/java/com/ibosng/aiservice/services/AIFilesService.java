package com.ibosng.aiservice.services;

import com.azure.ai.openai.assistants.models.UpdateToolResourcesOptions;

import java.io.File;
import java.util.List;

public interface AIFilesService {

    void updateFiles(List<File> files);

    void logVectorStoreFileStatuses(String vectorStoreId);

    UpdateToolResourcesOptions getUpdateToolResources(String vectorStoreId);

    String getOrCreateVectorStore();
}
