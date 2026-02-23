package com.ibosng.personalverwaltung.domain;

import java.util.Map;

/**
 * Service for enforcing folder structures based on predefined configurations.
 */
public interface DmsFolderStructureService {

    /**
     * Enforces a folder structure at the specified root path.
     *
     * @param resourcePath The path to the structure configuration resource (e.g., "dms/MA-file-structure.yaml").
     * @param placeholders A map of placeholders to be replaced in the folder names.
     */
    void enforceStructure(String resourcePath, Map<String, String> placeholders);
}
