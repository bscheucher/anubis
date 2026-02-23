package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.microsoftgraphservice.dtos.TreeNode;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.microsoftgraphservice.services.MSEnvironmentService;
import com.ibosng.microsoftgraphservice.utils.Helpers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FileShareServiceImplLocalTest extends BaseIntegrationTest {

    @Autowired
    FileShareService fileShareService;

    @MockBean
    StammdatenService stammdatenService;

    @MockBean
    MSEnvironmentService msEnvironmentService;

    @Value("${localStorageRoot}")
    private String localStorageRoot;

    @Value("${fileShareTemp}")
    private String fileShareTemp;

    @Value("${fileSharePersonalunterlagen}")
    private String fileSharePersonalunterlagen;

    public static final String share = "share";

    public static final String dir = "dir";

    public static final String name = "file.txt";

    public static final byte[] payload = "payload".getBytes();

    public static final String personalnummer = "12345";

    public static final String firma = "ACME";

    public static final String vorname = "Max";

    public static final String nachname = "Mustermann";


    @AfterEach
    void cleanupLocalStorage() throws Exception {
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
    }

    @Test
    void roundtrip_upload_download_delete() throws Exception {
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(payload), payload.length);
        try (InputStream in = fileShareService.downloadFromFileShare(share, dir, "file")) {
            byte[] got = in.readAllBytes();
            assertArrayEquals(payload, got);
        }
        fileShareService.deleteFromFileShare(share, "unused-personalnummer", dir, name);
        assertFalse(fileShareService.fileExists(share, dir + "/" + name));
    }

    @Test
    void uploadOrReplaceInFileShare() throws Exception {
        byte[] payload2 = "payload2".getBytes();

        // first upload
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(payload), payload.length);
        assertTrue(fileShareService.fileExists(share, dir + "/" + name));

        // replace upload with the same name should overwrite
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(payload2), payload2.length);
        try (InputStream in = fileShareService.downloadFromFileShare(share, dir, "file")) {
            assertArrayEquals(payload2, in.readAllBytes());
        }
    }

    @Test
    void downloadFromFileShare() throws Exception {
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(payload), payload.length);
        try (InputStream in = fileShareService.downloadFromFileShare(share, dir, "file")) {
            assertArrayEquals(payload, in.readAllBytes());
        }

        // when directory does not exist -> IllegalStateException
        assertThrows(IllegalStateException.class, () -> fileShareService.downloadFromFileShare(share, "missing-dir", "x"));
    }

    @Test
    void downloadFileById() throws Exception {
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(payload), payload.length);

        String fileId = dir + "/" + name; // relative path under share
        try (InputStream in = fileShareService.downloadFileById(share, fileId)) {
            assertArrayEquals(payload, in.readAllBytes());
        }
        try (InputStream in = fileShareService.downloadFileById(share, "does/not/exist.txt")) {
            assertEquals(0, in.readAllBytes().length);
        }
    }

    @Test
    void getVereinbarungenDirectory() {
        Stammdaten stammdaten = buildStammdaten();
        String dir = fileShareService.getVereinbarungenDirectory(personalnummer, stammdaten);
        String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
        assertEquals(String.join("/", firma, updatedDirectoryName, "Vereinbarungen"), dir);
    }

    @Test
    void emptyFolderFromFileShare() {
        fileShareService.uploadOrReplaceInFileShare(share, dir + "/a", "f1.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, dir + "/b/c", "f2.txt", new ByteArrayInputStream("y".getBytes()), 1);

        assertTrue(fileShareService.fileExists(share, dir + "/a/f1.txt"));
        assertTrue(fileShareService.fileExists(share, dir + "/b/c/f2.txt"));
        fileShareService.emptyFolderFromFileShare(share, dir);

        // the directory itself may still exist but should be empty
        assertTrue(fileShareService.fileExists(share, dir));
        // ensure no files remain
        assertFalse(fileShareService.fileExists(share, dir + "/a/f1.txt"));
        assertFalse(fileShareService.fileExists(share, dir + "/b/c/f2.txt"));
    }

    @Test
    void deleteFromFileShare() {
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream("1".getBytes()), 1);
        assertTrue(fileShareService.fileExists(share, dir + "/" + name));
        fileShareService.deleteFromFileShare(share, "pn", dir, name);
        assertFalse(fileShareService.fileExists(share, dir + "/" + name));
    }

    @Test
    void cleanFileshare() {
        String share = "share";
        String dir = "dir";
        fileShareService.uploadOrReplaceInFileShare(share, dir, "a.txt", new ByteArrayInputStream("z".getBytes()), 1);
        fileShareService.cleanFileshare(share);
        // root share folder should be deleted too
        assertFalse(fileShareService.fileExists(share, ""));
    }

    @Test
    void createStructureForNewMA() {
        assertDoesNotThrow(() -> Files.createDirectories(localPath(share)));
        when(msEnvironmentService.isProduction()).thenReturn(false);

        fileShareService.createStructureForNewMA(personalnummer, share, firma);
        assertTrue(fileShareService.fileExists(share, firma + "/" + personalnummer + "/Vereinbarungen"));
        assertTrue(fileShareService.fileExists(share, firma + "/" + personalnummer + "/Vereinbarungen/Nicht unterschrieben"));
        assertTrue(fileShareService.fileExists(share, firma + "/" + personalnummer + "/Gehaltsnachweise/Nettozettel"));
    }

    @Test
    void moveSignedDocuments() {
        Stammdaten stammdaten = buildStammdaten();
        when(stammdatenService.findByPersonalnummerString(personalnummer)).thenReturn(stammdaten);

        Path sourceDirectory = localPath(fileShareTemp, firma, personalnummer);
        assertDoesNotThrow(() -> Files.createDirectories(sourceDirectory.resolve("docs")));
        assertDoesNotThrow(() -> Files.createDirectories(sourceDirectory.resolve("dienstvertrag")));
        assertDoesNotThrow(() -> Files.writeString(sourceDirectory.resolve("docs").resolve("a.txt"), "a"));
        assertDoesNotThrow(() -> Files.writeString(sourceDirectory.resolve("dienstvertrag").resolve("b.txt"), "b"));

        fileShareService.moveSignedDocuments(personalnummer);

        String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
        assertTrue(fileShareService.fileExists(fileSharePersonalunterlagen, firma + "/" + updatedDirectoryName + "/docs/a.txt"));
        assertFalse(fileShareService.fileExists(fileSharePersonalunterlagen, firma + "/" + updatedDirectoryName + "/dienstvertrag/b.txt"));
        assertFalse(Files.exists(sourceDirectory));
    }

    @Test
    void renamePersonalnummerDirectory() {
        Stammdaten stammdaten = buildStammdaten();
        when(stammdatenService.findByPersonalnummerString(personalnummer)).thenReturn(stammdaten);

        Path sourceDirectory = localPath(fileShareTemp, firma, personalnummer);
        assertDoesNotThrow(() -> Files.createDirectories(sourceDirectory));

        fileShareService.renamePersonalnummerDirectory(personalnummer);

        String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
        assertFalse(Files.exists(sourceDirectory));
        assertTrue(Files.isDirectory(localPath(fileShareTemp, firma, updatedDirectoryName)));
    }

    @Test
    void renameFilesInDirectoryRecursively() {
        Stammdaten stammdaten = buildStammdaten();
        when(stammdatenService.findByPersonalnummerString(personalnummer)).thenReturn(stammdaten);

        Path sourceDirectory = localPath(fileShareTemp, firma, personalnummer);
        assertDoesNotThrow(() -> Files.createDirectories(sourceDirectory.resolve("sub")));
        assertDoesNotThrow(() -> Files.writeString(sourceDirectory.resolve("sub").resolve("file.txt"), "x"));
        assertDoesNotThrow(() -> Files.writeString(sourceDirectory.resolve("DIENSTVERTRAG.pdf"), "y"));

        fileShareService.renameFilesInDirectoryRecursively(personalnummer);

        String renamedFile = Helpers.updateFileName("file.txt", vorname, nachname);
        assertFalse(Files.exists(sourceDirectory.resolve("sub").resolve("file.txt")));
        assertTrue(Files.exists(sourceDirectory.resolve("sub").resolve(renamedFile)));
        assertTrue(Files.exists(sourceDirectory.resolve("DIENSTVERTRAG.pdf")));
    }

    @Test
    void deletePersonalnummerDirectory() {
        // create the structure under share/firma/pn
        fileShareService.uploadOrReplaceInFileShare(share, firma + "/" + personalnummer, "x.txt", new ByteArrayInputStream("x".getBytes()), 1);
        assertTrue(fileShareService.fileExists(share, firma + "/" + personalnummer + "/x.txt"));
        fileShareService.deletePersonalnummerDirectory(share, firma, personalnummer);
        assertFalse(fileShareService.fileExists(share, firma + "/" + personalnummer));
    }

    @Test
    void fileExists() {
        String share = "share";
        String dir = "dir";
        String name = "file.txt";
        assertFalse(fileShareService.fileExists(share, dir + "/" + name));
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream("x".getBytes()), 1);
        assertTrue(fileShareService.fileExists(share, dir + "/" + name));
    }

    @Test
    void getFullFileName() {
        // create files under share/firma/pn/dir
        fileShareService.uploadOrReplaceInFileShare(share, firma + "/" + personalnummer + "/" + dir, "doc-abc.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, firma + "/" + personalnummer + "/" + dir, "note-xyz.txt", new ByteArrayInputStream("x".getBytes()), 1);
        String found = fileShareService.getFullFileName(share, firma, personalnummer, dir, "doc-", "abc");
        assertEquals("doc-abc.txt", found);
        assertNull(fileShareService.getFullFileName(share, firma, personalnummer, dir, "missing-", "abc"));
    }

    @Test
    void deleteFromFileShareContainingFilename() {
        fileShareService.uploadOrReplaceInFileShare(share, personalnummer + "/" + dir, "aaa-one.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, personalnummer + "/" + dir, "bbb-two.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, personalnummer + "/" + dir, "ccc-one.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.deleteFromFileShareContainingFilename(share, personalnummer, dir, "one");
        assertFalse(fileShareService.fileExists(share, personalnummer + "/" + dir + "/aaa-one.txt"));
        assertTrue(fileShareService.fileExists(share, personalnummer + "/" + dir + "/bbb-two.txt"));
        assertFalse(fileShareService.fileExists(share, personalnummer + "/" + dir + "/ccc-one.txt"));
    }

    @Test
    void downloadFiles() throws Exception {
        String remote = "root/sub";
        fileShareService.uploadOrReplaceInFileShare(share, remote + "/x", "a.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, remote, "b.txt", new ByteArrayInputStream("y".getBytes()), 1);

        Path tmp = Files.createTempDirectory("fs-dl-");
        try {
            fileShareService.downloadFiles(remote, tmp, share);
            assertTrue(Files.exists(tmp.resolve("x")));
            assertTrue(Files.exists(tmp.resolve("x").resolve("a.txt")));
            assertTrue(Files.exists(tmp.resolve("b.txt")));
        } finally {
            // cleanup temp dir
            if (Files.exists(tmp)) {
                try (java.util.stream.Stream<Path> s = Files.walk(tmp)) {
                    s.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (Exception ignored) {
                        }
                    });
                }
            }
        }
    }

    @Test
    void getDirectoryStructure() {
        fileShareService.uploadOrReplaceInFileShare(share, dir, "a.txt", new ByteArrayInputStream("x".getBytes()), 1);
        TreeNode node = fileShareService.getDirectoryStructure(share, dir);
        assertNotNull(node);
    }

    @Test
    void renameAndMoveSignedDocumentsAndDirectories() {
        Stammdaten stammdaten = buildStammdaten();
        when(stammdatenService.findByPersonalnummerString(personalnummer)).thenReturn(stammdaten);

        Path sourceDirectory = localPath(fileShareTemp, firma, personalnummer);
        assertDoesNotThrow(() -> Files.createDirectories(sourceDirectory));
        assertDoesNotThrow(() -> Files.writeString(sourceDirectory.resolve("doc.txt"), "x"));

        fileShareService.renameAndMoveSignedDocumentsAndDirectories(personalnummer);

        String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
        String renamedFile = Helpers.updateFileName("doc.txt", vorname, nachname);
        assertTrue(fileShareService.fileExists(fileSharePersonalunterlagen, firma + "/" + updatedDirectoryName + "/" + renamedFile));
        assertFalse(Files.exists(sourceDirectory));
    }

    @Test
    void downloadFilesToTemp() {
        fileShareService.uploadOrReplaceInFileShare(share, dir, "a.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, dir, "b.txt", new ByteArrayInputStream("x".getBytes()), 1);
        List<File> files = fileShareService.downloadFilesToTemp(dir, share);
        assertEquals(2, files.size());
        for (File f : files) {
            assertTrue(f.exists());
        }
    }

    private Stammdaten buildStammdaten() {
        IbisFirma firma = new IbisFirma();
        firma.setName(FileShareServiceImplLocalTest.firma);

        Personalnummer pn = new Personalnummer();
        pn.setPersonalnummer(FileShareServiceImplLocalTest.personalnummer);
        pn.setFirma(firma);

        Stammdaten stammdaten = new Stammdaten();
        stammdaten.setPersonalnummer(pn);
        stammdaten.setVorname(FileShareServiceImplLocalTest.vorname);
        stammdaten.setNachname(FileShareServiceImplLocalTest.nachname);
        return stammdaten;
    }

    private Path localPath(String... parts) {
        return Paths.get(localStorageRoot, parts);
    }
}