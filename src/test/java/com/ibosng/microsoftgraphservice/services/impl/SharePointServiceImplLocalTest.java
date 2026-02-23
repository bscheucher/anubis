package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SharePointServiceImplLocalTest {

    private String localStorageRoot;
    private SharePointService sharePointService;
    private File tempSource;

    @BeforeEach
    void setUp() throws Exception {
        Path tmp = Files.createTempDirectory(".localStorageRoot");
        localStorageRoot = tmp.toString();

        sharePointService = new SharePointServiceImplLocal(tmp);

        tempSource = File.createTempFile("source-", ".txt");
        try (FileWriter fw = new FileWriter(tempSource)) {
            fw.write("hello-local-sharepoint");
        }
    }

    @AfterEach
    void cleanup() throws Exception {
        // Remove local storage content and temp artifacts
        Path root = Paths.get(localStorageRoot);
        if (Files.exists(root)) {
            try (Stream<Path> s = Files.walk(root)) {
                s.sorted(Comparator.reverseOrder()).forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (Exception ignored) {
                    }
                });
            }
        }
        if (tempSource != null) {
            tempSource.delete();
        }
    }

    @Test
    void uploadFile() throws Exception {
        String folder = "one/dir";
        String name = "uploaded.txt";

        DriveItem item = sharePointService.uploadFile(tempSource.getAbsolutePath(), name, folder);

        assertNotNull(item);
        assertEquals(name, item.name);
        assertNotNull(item.id);
        assertEquals("one/dir/" + name, item.id.replace('\\', '/'));

        Path expected = Paths.get(localStorageRoot, folder, name);
        assertTrue(Files.exists(expected));
        String content = Files.readString(expected);
        assertEquals("hello-local-sharepoint", content);
    }

    @Test
    void downloadFile() throws Exception {
        // First upload a file then download it by id
        DriveItem uploaded = sharePointService.uploadFile(tempSource.getAbsolutePath(), "doc.txt", "src");
        File dest = sharePointService.downloadFile(uploaded.id, File.createTempFile("dl-", ".txt").getAbsolutePath());
        assertNotNull(dest);
        assertTrue(dest.exists());
        assertEquals(Files.readString(tempSource.toPath()), Files.readString(dest.toPath()));
        // cleanup temp dest
        dest.delete();
    }

    @Test
    void createJsonFileFromDto() throws Exception {
        Map<String, Object> dto = new HashMap<>();
        dto.put("name", "Alice");
        dto.put("age", 30);

        File json = sharePointService.createJsonFileFromDto(dto, "dto-test");
        assertNotNull(json);
        assertTrue(json.exists());
        assertTrue(json.getName().startsWith("dto-test"));
        assertTrue(json.getName().endsWith(".json"));
        String text = Files.readString(json.toPath());
        assertTrue(text.contains("Alice"));
        // OS will clean temp files eventually; remove proactively
        json.delete();
    }

    @Test
    void getUploadedFiles() throws Exception {
        String folder = "folderA";
        sharePointService.uploadFile(tempSource.getAbsolutePath(), "a.txt", folder);
        sharePointService.uploadFile(tempSource.getAbsolutePath(), "b.txt", folder);

        DriveItemCollectionPage page = sharePointService.getUploadedFiles(folder);
        assertNotNull(page);
        List<String> names = page.getCurrentPage().stream().map(di -> di.name).toList();
        assertTrue(names.containsAll(List.of("a.txt", "b.txt")));
        assertEquals(2, names.size());
    }

    @Test
    void moveFile() {
        assertDoesNotThrow(() -> {
            DriveItem up = sharePointService.uploadFile(tempSource.getAbsolutePath(), "old.txt", "src");
            DriveItem moved = sharePointService.moveFile(up.id, "new.txt", "dst/sub");
            assertNotNull(moved);
            assertEquals("new.txt", moved.name);
            assertNotNull(moved.id);
            assertEquals("dst/sub/new.txt", moved.id.replace('\\', '/'));

            Path oldPath = Paths.get(localStorageRoot, "src", "old.txt");
            Path newPath = Paths.get(localStorageRoot, "dst/sub", "new.txt");
            assertFalse(Files.exists(oldPath));
            assertTrue(Files.exists(newPath));
        });
    }

    @Test
    void testMoveFile() throws MSGraphServiceException {
        DriveItem up = sharePointService.uploadFile(tempSource.getAbsolutePath(), "file.txt", "from");
        FileDetails details = new FileDetails(null, "moved.txt", null, up.id, false);
        DriveItem moved = sharePointService.moveFile(details, "to");
        assertNotNull(moved);
        assertEquals("moved.txt", moved.name);
        assertNotNull(moved.id);
        assertEquals("to/moved.txt", moved.id.replace('\\', '/'));
        assertTrue(Files.exists(Paths.get(localStorageRoot, "to", "moved.txt")));
    }

    @Test
    void getFolderIdByPath() {
        String id = sharePointService.getFolderIdByPath("some/path");
        assertNotNull(id);
        // In local impl this is the resolved filesystem path, so it should contain the configured root and end with the input path
        assertTrue(id.contains(localStorageRoot));
        assertTrue(id.replace('\\', '/').endsWith("some/path"));
    }

    @Test
    void getContentsOfFolder() throws Exception {
        // upload 2 files in root and 1 in subfolder
        sharePointService.uploadFile(tempSource.getAbsolutePath(), "root1.txt", "");
        sharePointService.uploadFile(tempSource.getAbsolutePath(), "root2.txt", "");
        sharePointService.uploadFile(tempSource.getAbsolutePath(), "sub.txt", "sub");

        // list root
        DriveItemCollectionPage rootPage = sharePointService.getContentsOfFolder(Optional.empty());
        List<String> rootNames = rootPage.getCurrentPage().stream().map(di -> di.name).sorted().toList();
        assertEquals(List.of("root1.txt", "root2.txt"), rootNames);

        // list subfolder
        DriveItemCollectionPage subPage = sharePointService.getContentsOfFolder(Optional.of("sub"));
        List<String> subNames = new ArrayList<>();
        subPage.getCurrentPage().forEach(di -> subNames.add(di.name));
        assertEquals(List.of("sub.txt"), subNames);
    }
}