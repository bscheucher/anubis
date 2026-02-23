package com.ibosng.personalverwaltung.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointCondition;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.ibosng.personalverwaltung.domain.exceptions.DmsFolderStructureServiceException;
import com.microsoft.graph.models.DriveItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
@Conditional(SharePointCondition.class)
@RequiredArgsConstructor
public class DmsFolderStructureServiceImpl implements DmsFolderStructureService {

    public static final String BASE_FOLDER = "baseFolder";
    public static final String STRUCTURE = "structure";
    private final SharePointService sharePointService;
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Override
    public void enforceStructure(String resourcePath, Map<String, String> placeholders) {
        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("resourcePath must not be null or blank");
        }
        if (placeholders == null) {
            placeholders = Map.of();
        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            JsonNode yamlRoot = readYamlRoot(resourcePath, inputStream);
            String baseFolderPath = extractBaseFolderPath(resourcePath, yamlRoot);
            JsonNode structure = extractStructure(yamlRoot);
            if (structure != null && structure.isArray()) {
                for (JsonNode node : structure) {
                    createFoldersRecursive(baseFolderPath, node, placeholders);
                }
            }
        } catch (Exception e) {
            String errorMessage = "Failed to enforce structure " + resourcePath;
            log.error(errorMessage, e);
            throw new DmsFolderStructureServiceException(errorMessage, e);
        }
    }

    private JsonNode readYamlRoot(String resourcePath, InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new DmsFolderStructureServiceException("Structure definition not found: " + resourcePath);
        }
        JsonNode yamlRoot = yamlMapper.readTree(inputStream);
        if (yamlRoot == null) {
            throw new DmsFolderStructureServiceException("Failed to parse YAML root from: " + resourcePath);
        }
        return yamlRoot;
    }

    private String extractBaseFolderPath(String resourcePath, JsonNode yamlRoot) throws DmsFolderStructureServiceException {
        JsonNode baseFolderNode = yamlRoot.get(BASE_FOLDER);
        if (baseFolderNode == null || baseFolderNode.isNull()) {
            throw new DmsFolderStructureServiceException("Missing required '" + BASE_FOLDER + "' in " + resourcePath);
        }
        String baseFolderPath = baseFolderNode.asText();
        // remove leading/trailing slashes for SharePoint itemWithPath
        return sanitizePath(baseFolderPath);
    }

    private JsonNode extractStructure(JsonNode yamlRoot) {
        return yamlRoot.get(STRUCTURE);
    }

    private void createFoldersRecursive(String currentPath, JsonNode node, Map<String, String> placeholders) throws MSGraphServiceException {
        if (node == null || node.isNull()) {
            return;
        }
        JsonNode nameNode = node.get("name");
        if (nameNode == null || nameNode.isNull()) {
            log.warn("Node in structure is missing 'name' attribute, skipping");
            return;
        }
        String folderName = nameNode.asText();
        String replacedName = replacePlaceholders(folderName, placeholders);

        String fullPath = getFullPath(currentPath, replacedName);
        fullPath = sanitizePath(fullPath);

        DriveItem existingItem = sharePointService.getItemByPath(fullPath);
        if (existingItem != null && existingItem.folder != null) {
            log.info("Folder '{}' already exists at path '{}', skipping creation", replacedName, currentPath);
        } else {
            log.info("Creating folder '{}' at path '{}'", replacedName, currentPath);
            sharePointService.createFolder(currentPath, replacedName);
        }

        String nextPath = fullPath;

        JsonNode subfolders = node.get("subfolders");
        if (subfolders != null && subfolders.isArray()) {
            for (JsonNode subfolder : subfolders) {
                createFoldersRecursive(nextPath, subfolder, placeholders);
            }
        }
    }

    private static String getFullPath(String currentPath, String replacedName) {
        return (currentPath == null || currentPath.isEmpty()) ? replacedName : currentPath + "/" + replacedName;
    }

    private String replacePlaceholders(String input, Map<String, String> placeholders) {
        if (input == null) {
            return "";
        }
        if (placeholders == null || placeholders.isEmpty()) {
            return input;
        }
        String result = input;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                result = result.replace(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private String sanitizePath(String path) {
        if (path == null) return "";
        String result = path.trim();
        while (result.startsWith("/")) {
            result = result.substring(1);
        }
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
