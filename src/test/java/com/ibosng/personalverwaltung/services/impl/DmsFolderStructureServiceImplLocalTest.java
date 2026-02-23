package com.ibosng.personalverwaltung.services.impl;

import com.ibosng.microsoftgraphservice.services.impl.SharePointServiceImplLocal;
import com.ibosng.personalverwaltung.domain.DmsFolderStructureService;
import com.ibosng.personalverwaltung.domain.DmsFolderStructureServiceImplLocal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link DmsFolderStructureServiceImplLocal} using a local filesystem.
 * <p>
 * This test uses a fixed directory {@code build/test-storage} to persist the created folder structures.
 * This allows for manual inspection of the generated folders after the tests have finished.
 * </p>
 * <p>
 * <b>Manual Verification:</b>
 * <ol>
 *     <li>Run the tests: {@code ./gradlew test --tests DmsFolderStructureServiceImplLocalTest}</li>
 *     <li>Navigate to the project root in your terminal or file explorer.</li>
 *     <li>Open the {@code build/test-storage} directory.</li>
 *     <li>Verify the folder structures for:
 *         <ul>
 *             <li>Employees: {@code build/test-storage/Employees/MITARBEITER_123_Max_Mustermann/...}</li>
 *             <li>Participants: {@code build/test-storage/Participants/TEILNEHNMER_987_Jane_Doe/...}</li>
 *         </ul>
 *     </li>
 * </ol>
 * </p>
 */
class DmsFolderStructureServiceImplLocalTest {

    private Path root;

    private DmsFolderStructureService dmsFolderStructureService;


    @BeforeEach
    @SneakyThrows
    void setUp() {
        root = Path.of(".local-storage");
        Files.createDirectories(root);
        SharePointServiceImplLocal sharePointDocumentService = new SharePointServiceImplLocal(root);
        dmsFolderStructureService = new DmsFolderStructureServiceImplLocal(sharePointDocumentService);
    }

    @Test
    @SneakyThrows
    void shouldCreateMaFolderStructure() {
        // Arrange
        Map<String, String> placeholders = Map.of(
                "MITARBEITERPNR", "123",
                "VORNAME", "Max",
                "NACHMNAME", "Mustermann"
        );

        // Act
        dmsFolderStructureService.enforceStructure("dms/MA-file-structure.yaml", placeholders);

        // Assert
        // Top level folder
        Path employeeFolder = root.resolve("TEST/test_MA_personalakt").resolve("123_Max_Mustermann");
        assertTrue(Files.exists(employeeFolder), "Employee root folder should exist");
        assertTrue(Files.isDirectory(employeeFolder), "Employee root should be a directory");

        // Some subfolders
        assertTrue(Files.exists(employeeFolder.resolve("Stammdaten")), "Stammdaten folder should exist");
        assertTrue(Files.exists(employeeFolder.resolve("Stammdaten/Urkunden")), "Urkunden folder should exist");
        assertTrue(Files.exists(employeeFolder.resolve("Onboarding")), "Onboarding folder should exist");
    }

    @Test
    @SneakyThrows
    void shouldCreateTnFolderStructure() {
        // Arrange
        Map<String, String> placeholders = Map.of(
                "TEILNEHNMERPNR", "987",
                "VORNAME", "Jane",
                "NACHMNAME", "Doe"
        );

        // Act
        dmsFolderStructureService.enforceStructure("dms/TN-file-structure.yaml", placeholders);

        // Assert
        Path participantFolder = root.resolve("TEST/test_TN_personalakt").resolve("987_Jane_Doe");
        assertTrue(Files.exists(participantFolder), "Participant root folder should exist");
        assertTrue(Files.exists(participantFolder.resolve("Berichte - intern")), "Berichte - intern folder should exist");
        assertTrue(Files.exists(participantFolder.resolve("Berichte - intern/Terminplan")), "Terminplan folder should exist");
    }
}
