package com.ibosng.aiservice.services.impl;

import com.azure.ai.openai.assistants.AssistantsClient;
import com.azure.ai.openai.assistants.models.*;
import com.azure.core.util.BinaryData;
import com.ibosng.aiservice.services.AIFilesService;
import com.ibosng.aiservice.utils.Helpers;
import com.ibosng.dbservice.services.ai.OpenaiAssistantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.ibosng.aiservice.utils.Constants.CHAT_ASSISTANT;
import static com.ibosng.aiservice.utils.Constants.VECTOR_STORE_CHAT;
import static com.ibosng.aiservice.utils.Helpers.normalizeFilename;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIFilesServiceImpl implements AIFilesService {
    private final OpenaiAssistantService openaiAssistantService;
    private final AssistantsClient assistantsClient;

    @Override
    public void updateFiles(List<File> files) {
        try {
            String assistantId = openaiAssistantService.findByAssistantName(CHAT_ASSISTANT).getAssistantId();
            String vectorStoreId = getOrCreateVectorStore();

            Map<String, OpenAIFile> assistantFileMap = prepareAssistantFiles();
            Set<String> targetFileIds = syncUploadedFiles(files, assistantFileMap);
            syncVectorStoreFiles(vectorStoreId, targetFileIds);
            logVectorStoreFileStatuses(vectorStoreId); // ✅ Log current status of all vector store files

            updateAssistantTools(assistantId, vectorStoreId);

        } catch (Exception e) {
            log.error("Failed to update files and vector store", e);
        }
    }

    private Map<String, OpenAIFile> prepareAssistantFiles() {
        List<OpenAIFile> allAssistantFiles = assistantsClient.listFiles().stream()
                .filter(f -> "assistants".equalsIgnoreCase(f.getPurpose().toString()))
                .toList();

        return deduplicateAssistantFiles(allAssistantFiles);
    }

    private Set<String> syncUploadedFiles(List<File> files, Map<String, OpenAIFile> assistantFileMap) {
        Set<String> targetFileIds = new HashSet<>();

        for (File sourceFile : files) {
            try {
                String fileName = sourceFile.getName();

                OpenAIFile fileToUse = assistantFileMap.get(fileName);
                if (fileToUse == null) {
                    fileToUse = uploadFileToAssistant(sourceFile);
                    assistantFileMap.put(fileName, fileToUse);
                    log.info("Uploaded new file: {}", fileName);
                } else {
                    log.info("File already uploaded: {}", fileName);
                }

                targetFileIds.add(fileToUse.getId());

            } catch (Exception e) {
                log.error("Failed to process file {}: {}", sourceFile.getName(), e.getMessage(), e);
            } finally {
                sourceFile.delete();
            }
        }

        return targetFileIds;
    }

    private void syncVectorStoreFiles(String vectorStoreId, Set<String> targetFileIds) {
        // 1. Get assistant files for filename mapping
        List<OpenAIFile> allAssistantFiles = assistantsClient.listFiles().stream()
                .filter(f -> "assistants".equalsIgnoreCase(f.getPurpose().toString()))
                .toList();

        Map<String, String> fileIdToFilename = allAssistantFiles.stream()
                .collect(Collectors.toMap(
                        OpenAIFile::getId,
                        f -> normalizeFilename(f.getFilename()), // 🔄 normalize filename
                        (f1, f2) -> f1
                ));

        // 2. Get current vector store file -> filename map
        List<VectorStoreFile> currentVectorStoreFiles = assistantsClient
                .listVectorStoreFiles(vectorStoreId)
                .getData();

        Map<String, String> currentVectorFileIdToFilename = currentVectorStoreFiles.stream()
                .filter(vf -> fileIdToFilename.containsKey(vf.getId()))
                .collect(Collectors.toMap(
                        VectorStoreFile::getId,
                        vf -> fileIdToFilename.get(vf.getId()),
                        (f1, f2) -> f1
                ));

        Set<String> currentFilenames = new HashSet<>(currentVectorFileIdToFilename.values());

        // 3. Map targetFileIds -> filenames (normalize each one)
        Set<String> targetFilenames = targetFileIds.stream()
                .map(fileIdToFilename::get)
                .filter(Objects::nonNull)
                .map(Helpers::normalizeFilename) // 🔄 normalize
                .collect(Collectors.toSet());

        // 4. Diff filenames
        Set<String> filenamesToRemove = new HashSet<>(currentFilenames);
        filenamesToRemove.removeAll(targetFilenames);

        Set<String> filenamesToAdd = new HashSet<>(targetFilenames);
        filenamesToAdd.removeAll(currentFilenames);

        // 5. Remove old files
        currentVectorFileIdToFilename.forEach((fileId, filename) -> {
            if (filenamesToRemove.contains(filename)) {
                assistantsClient.deleteVectorStoreFile(vectorStoreId, fileId);
                log.info("Removed obsolete file from vector store: {} ({})", fileId, filename);
            }
        });

        // 6. Add new files
        Map<String, String> filenameToId = allAssistantFiles.stream()
                .collect(Collectors.toMap(
                        f -> normalizeFilename(f.getFilename()), // 🔄 normalize
                        OpenAIFile::getId,
                        (f1, f2) -> f1
                ));

        for (String filename : filenamesToAdd) {
            String fileId = filenameToId.get(filename);
            if (fileId != null) {
                assistantsClient.createVectorStoreFile(vectorStoreId, fileId);
                log.info("Added file to vector store: {} ({})", fileId, filename);
            }
        }
    }

    @Override
    public void logVectorStoreFileStatuses(String vectorStoreId) {
        assistantsClient.listVectorStoreFiles(vectorStoreId).getData().forEach(file -> {
            log.info("Vector Store File: {}, Status: {}", file.getId(), file.getStatus());
        });
    }

    private void updateAssistantTools(String assistantId, String vectorStoreId) {
        try {
            UpdateAssistantOptions updateOptions = new UpdateAssistantOptions()
                    .setToolResources(getUpdateToolResources(vectorStoreId));

            assistantsClient.updateAssistant(assistantId, updateOptions);
            log.info("Assistant '{}' tool resources updated with vector store '{}'", assistantId, vectorStoreId);

        } catch (Exception e) {
            log.error("Failed to update assistant tool resources", e);
        }
    }

    @Override
    public UpdateToolResourcesOptions getUpdateToolResources(String vectorStoreId) {
        UpdateFileSearchToolResourceOptions fileSearchOptions = new UpdateFileSearchToolResourceOptions()
                .setVectorStoreIds(List.of(vectorStoreId));

        UpdateToolResourcesOptions toolResources = new UpdateToolResourcesOptions()
                .setFileSearch(fileSearchOptions);
        return toolResources;
    }

    @Override
    public String getOrCreateVectorStore() {
        PageableList<VectorStore> vectorStores = assistantsClient.listVectorStores();
        return vectorStores.getData().stream()
                .filter(store -> store.getName().equals(VECTOR_STORE_CHAT))
                .findFirst()
                .map(VectorStore::getId)
                .orElseGet(() -> {
                    VectorStore newStore = assistantsClient.createVectorStore(
                            new VectorStoreOptions().setName(VECTOR_STORE_CHAT)
                    );
                    log.info("Created new vector store '{}'", VECTOR_STORE_CHAT);
                    return newStore.getId();
                });
    }

    private OpenAIFile uploadFileToAssistant(File file) {
        try {
            BinaryData fileData = BinaryData.fromFile(file.toPath());
            FileDetails fileDetails = new FileDetails(fileData, file.getName());

            OpenAIFile openAIFile = assistantsClient.uploadFile(fileDetails, FilePurpose.ASSISTANTS);
            log.info("Uploaded file '{}' with ID: {}", openAIFile.getFilename(), openAIFile.getId());
            return openAIFile;

        } catch (Exception e) {
            log.error("Failed to upload file to assistant: {}", file.getName(), e);
            return null;
        }
    }

    private Map<String, OpenAIFile> deduplicateAssistantFiles(List<OpenAIFile> allAssistantFiles) {
        Map<String, OpenAIFile> fileMap = new HashMap<>();

        for (OpenAIFile file : allAssistantFiles) {
            String filename = file.getFilename();

            if (fileMap.containsKey(filename)) {
                OpenAIFile existing = fileMap.get(filename);

                if (file.getCreatedAt().isAfter(existing.getCreatedAt())) {
                    // Newer file found, delete the older one
                    deleteAssistantFile(existing);
                    fileMap.put(filename, file);
                } else {
                    // Current file is older, delete it
                    deleteAssistantFile(file);
                }

                log.warn("Duplicate assistant file detected for '{}'. Older version removed.", filename);
            } else {
                fileMap.put(filename, file);
            }
        }

        return fileMap;
    }

    private void deleteAssistantFile(OpenAIFile file) {
        try {
            assistantsClient.deleteFile(file.getId());
            log.info("Deleted old assistant file: {}", file.getFilename());
        } catch (Exception e) {
            log.error("Failed to delete assistant file '{}': {}", file.getFilename(), e.getMessage(), e);
        }
    }
}
