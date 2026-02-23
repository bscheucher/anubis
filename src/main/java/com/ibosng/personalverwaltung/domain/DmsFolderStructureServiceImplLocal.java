package com.ibosng.personalverwaltung.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ibosng.microsoftgraphservice.services.SharePointCondition;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
@Conditional(SharePointCondition.Reverse.class)
@RequiredArgsConstructor
public class DmsFolderStructureServiceImplLocal implements DmsFolderStructureService {

    private final SharePointService sharePointService;
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Override
    public void enforceStructure(String resourcePath, Map<String, String> placeholders) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Structure definition not found: " + resourcePath);
            }
            JsonNode yamlRoot = yamlMapper.readTree(inputStream);

            String baseFolderPath = yamlRoot.get("baseFolder").asText();

            // remove leading/trailing slashes for SharePoint itemWithPath
            baseFolderPath = sanitizePath(baseFolderPath);

            JsonNode structure = yamlRoot.get("structure");
            if (structure != null && structure.isArray()) {
                for (JsonNode node : structure) {
                    createFoldersRecursive(baseFolderPath, node, placeholders);
                }
            }
        } catch (Exception e) {
            log.error("Failed to enforce structure {}", resourcePath, e);
            throw new RuntimeException("Failed to enforce structure " + resourcePath, e);
        }
    }

    private void createFoldersRecursive(String currentPath, JsonNode node, Map<String, String> placeholders) {
        String folderName = node.get("name").asText();
        String replacedName = replacePlaceholders(folderName, placeholders);

        try {
            log.info("Creating folder '{}' at path '{}'", replacedName, currentPath);
            sharePointService.createFolder(currentPath, replacedName);

            String nextPath = (currentPath == null || currentPath.isEmpty()) ? replacedName : currentPath + "/" + replacedName;
            nextPath = sanitizePath(nextPath);

            JsonNode subfolders = node.get("subfolders");
            if (subfolders != null && subfolders.isArray()) {
                for (JsonNode subfolder : subfolders) {
                    createFoldersRecursive(nextPath, subfolder, placeholders);
                }
            }
        } catch (Exception e) {
            log.error("Failed to create folder '{}' at path '{}'", replacedName, currentPath, e);
        }
    }

    private String replacePlaceholders(String input, Map<String, String> placeholders) {
        String result = input;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
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
