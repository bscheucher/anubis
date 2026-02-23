package com.ibosng.personalverwaltung.services.impl;

import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.ibosng.personalverwaltung.domain.DmsFolderStructureServiceImpl;
import com.ibosng.personalverwaltung.domain.exceptions.DmsFolderStructureServiceException;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.Folder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DmsFolderStructureServiceImplTest {

    @Mock
    private SharePointService sharePointService;

    @InjectMocks
    private DmsFolderStructureServiceImpl dmsFolderStructureService;

    @Test
    void shouldCreateMaFolderStructureWithLibraryFolder() throws MSGraphServiceException, DmsFolderStructureServiceException {
        // Arrange
        Map<String, String> placeholders = Map.of(
                "MITARBEITERPNR", "123",
                "VORNAME", "Max",
                "NACHMNAME", "Mustermann"
        );

        // Act
        dmsFolderStructureService.enforceStructure("dms/MA-file-structure.yaml", placeholders);

        // Assert
        ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);

        // Verify top level folder creation
        verify(sharePointService, atLeastOnce()).createFolder(pathCaptor.capture(), nameCaptor.capture());

        // The first folder created should be the employee folder under library/rootPath
        assertEquals("TEST/test_MA_personalakt", pathCaptor.getAllValues().get(0));
        assertEquals("123_Max_Mustermann", nameCaptor.getAllValues().get(0));

        // Verify a subfolder
        // path: TEST/test_MA_personalakt/123_Max_Mustermann
        // name: Stammdaten
        Map.Entry<String, String> expectedFolder = getExpectedFolderAsMap("TEST/test_MA_personalakt/123_Max_Mustermann", "Stammdaten");
        boolean foundStammdaten = hasCreatedFolder(pathCaptor, nameCaptor, expectedFolder);
        assertTrue(foundStammdaten, "Stammdaten folder should be created at the correct path");
    }

    @Test
    void shouldCreateTnFolderStructure() throws MSGraphServiceException, DmsFolderStructureServiceException {
        // Arrange
        Map<String, String> placeholders = Map.of(
                "TEILNEHNMERPNR", "987",
                "VORNAME", "Jane",
                "NACHMNAME", "Doe"
        );

        // Act
        dmsFolderStructureService.enforceStructure("dms/TN-file-structure.yaml", placeholders);

        // Assert
        ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);

        verify(sharePointService, atLeastOnce()).createFolder(pathCaptor.capture(), nameCaptor.capture());

        // The first folder created should be the participant folder under baseFolder
        assertEquals("TEST/test_TN_personalakt", pathCaptor.getAllValues().get(0));
        assertEquals("987_Jane_Doe", nameCaptor.getAllValues().get(0));

        // Verify a subfolder
        Map.Entry<String, String> expectedFolderAsMap = getExpectedFolderAsMap("TEST/test_TN_personalakt/987_Jane_Doe", "Berichte - intern");
        boolean foundBerichte = hasCreatedFolder(pathCaptor, nameCaptor, expectedFolderAsMap);
        assertTrue(foundBerichte, "Berichte - intern folder should be created at the correct path");
    }

    @Test
    void shouldNotCreateFolderIfItAlreadyExists() throws MSGraphServiceException, DmsFolderStructureServiceException {
        // Arrange
        Map<String, String> placeholders = Map.of(
                "TEILNEHNMERPNR", "987",
                "VORNAME", "Jane",
                "NACHMNAME", "Doe"
        );

        // Mock folder existence for the top-level folder via getItemByPath (this is what the impl uses)
        DriveItem existingFolderItem = new DriveItem();
        existingFolderItem.folder = new Folder();
        when(sharePointService.getItemByPath("TEST/test_TN_personalakt/987_Jane_Doe")).thenReturn(existingFolderItem);

        // Act
        dmsFolderStructureService.enforceStructure("dms/TN-file-structure.yaml", placeholders);

        // Assert
        // The top level folder "987_Jane_Doe" should NOT be created because it already exists
        verify(sharePointService, never()).createFolder("TEST/test_TN_personalakt", "987_Jane_Doe");

        // But subfolders SHOULD still be processed.
        // For subfolder "Berichte - intern", it should check if it exists:
        verify(sharePointService).getItemByPath("TEST/test_TN_personalakt/987_Jane_Doe/Berichte - intern");
        // And if it doesn't exist (mock returns null by default), it should be created
        verify(sharePointService).createFolder("TEST/test_TN_personalakt/987_Jane_Doe", "Berichte - intern");
    }

    @Test
    void shouldThrowExceptionWhenResourcePathIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                dmsFolderStructureService.enforceStructure("", Map.of()));
        assertThrows(IllegalArgumentException.class, () ->
                dmsFolderStructureService.enforceStructure(null, Map.of()));
    }

    @Test
    void shouldHandleNullPlaceholders() throws DmsFolderStructureServiceException {
        // Should not throw NPE when placeholders is null
        // If placeholders are null, they are replaced by empty map, so placeholders won't be replaced.
        dmsFolderStructureService.enforceStructure("dms/TN-file-structure.yaml", null);
        // It will try to create folder with name "TEILNEHNMERPNR_VORNAME_NACHMNAME"
        verify(sharePointService, atLeastOnce()).getItemByPath(contains("TEILNEHNMERPNR_VORNAME_NACHMNAME"));
    }

    private boolean hasCreatedFolder(ArgumentCaptor<String> pathCaptor,
                                     ArgumentCaptor<String> nameCaptor,
                                     Map.Entry<String, String> expectedFolder) {
        for (int i = 0; i < pathCaptor.getAllValues().size(); i++) {
            if (expectedFolder.getKey().equals(pathCaptor.getAllValues().get(i)) &&
                    expectedFolder.getValue().equals(nameCaptor.getAllValues().get(i))) {
                return true;
            }
        }
        return false;
    }

    private Map.Entry<String, String> getExpectedFolderAsMap(String path, String name) {
        return Map.entry(path, name);
    }

}
