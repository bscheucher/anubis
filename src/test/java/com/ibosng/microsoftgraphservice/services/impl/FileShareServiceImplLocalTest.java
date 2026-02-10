package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.microsoftgraphservice.dtos.TreeNode;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileShareServiceImplLocalTest extends BaseIntegrationTest {

    @Value("${localStorageRoot}")
    private String localStorageRoot;

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

    @Autowired
    FileShareService fileShareService;

    @Test
    void roundtrip_upload_download_delete() throws Exception {
        String share = "reports";
        String dir = "invoices/2025-11";
        String name = "jan.txt";
        byte[] payload = "hello local".getBytes(StandardCharsets.UTF_8);

        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(payload), payload.length);
        try (InputStream in = fileShareService.downloadFromFileShare(share, dir, "jan")) {
            byte[] got = in.readAllBytes();
            assertArrayEquals(payload, got);
        }

        fileShareService.deleteFromFileShare(share, "unused-personalnummer", dir, name);
        assertFalse(fileShareService.fileExists(share, dir + "/" + name));
    }

    @Test
    void uploadOrReplaceInFileShare() throws Exception {
        String share = "uploads";
        String dir = "docs";
        String name = "foo.txt";
        byte[] v1 = "v1".getBytes();
        byte[] v2 = "v2".getBytes();

        // first upload
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(v1), v1.length);
        assertTrue(fileShareService.fileExists(share, dir + "/" + name));

        // replace upload with same name should overwrite
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(v2), v2.length);
        try (InputStream in = fileShareService.downloadFromFileShare(share, dir, "foo")) {
            assertArrayEquals(v2, in.readAllBytes());
        }
    }

    @Test
    void downloadFromFileShare() throws Exception {
        String share = "downloads";
        String dir = "inbox";
        String name = "jan.txt";
        byte[] payload = "hello".getBytes();

        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream(payload), payload.length);
        try (InputStream in = fileShareService.downloadFromFileShare(share, dir, "jan")) {
            assertArrayEquals(payload, in.readAllBytes());
        }

        // when directory does not exist -> IllegalStateException
        assertThrows(IllegalStateException.class, () -> fileShareService.downloadFromFileShare(share, "missing-dir", "x"));
    }

    @Test
    void downloadFileById() throws Exception {
        String share = "byId";
        String dir = "sub/dir";
        String name = "file.txt";
        byte[] payload = "content".getBytes();
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
        String dir = fileShareService.getVereinbarungenDirectory("12345", null);
        assertEquals("vereinbarungen/12345", dir);
    }

    @Test
    void emptyFolderFromFileShare() throws Exception {
        String share = "clean-folder";
        String dir = "to/clean";
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
        String share = "delete";
        String dir = "docs";
        String name = "gone.txt";
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream("1".getBytes()), 1);
        assertTrue(fileShareService.fileExists(share, dir + "/" + name));
        fileShareService.deleteFromFileShare(share, "pn", dir, name);
        assertFalse(fileShareService.fileExists(share, dir + "/" + name));
    }

    @Test
    void cleanFileshare() {
        String share = "wipe";
        String dir = "x/y";
        fileShareService.uploadOrReplaceInFileShare(share, dir, "a.txt", new ByteArrayInputStream("z".getBytes()), 1);
        fileShareService.cleanFileshare(share);
        // root share folder should be deleted too
        assertFalse(fileShareService.fileExists(share, ""));
    }

    @Test
    void createStructureForNewMA() {
        String share = "company";
        String firma = "ACME";
        String mainDir = "people";
        String pn = "123";
        // method uses root directly, not share name, but we'll still verify existence with fileExists using composed path
        fileShareService.createStructureForNewMA(pn, mainDir, firma);
        assertTrue(fileShareService.fileExists(share, "../" + firma + "/" + mainDir + "/" + pn));
    }

    @Test
    void moveSignedDocuments() {
        // no-op implementation should not throw
        assertDoesNotThrow(() -> fileShareService.moveSignedDocuments("123"));
    }

    @Test
    void renamePersonalnummerDirectory() {
        assertDoesNotThrow(() -> fileShareService.renamePersonalnummerDirectory("123"));
    }

    @Test
    void renameFilesInDirectoryRecursively() {
        assertDoesNotThrow(() -> fileShareService.renameFilesInDirectoryRecursively("123"));
    }

    @Test
    void deletePersonalnummerDirectory() {
        String share = "persons";
        String firma = "ACME";
        String pn = "999";
        // create the structure under share/firma/pn
        fileShareService.uploadOrReplaceInFileShare(share, firma + "/" + pn, "x.txt", new ByteArrayInputStream("x".getBytes()), 1);
        assertTrue(fileShareService.fileExists(share, firma + "/" + pn + "/x.txt"));
        fileShareService.deletePersonalnummerDirectory(share, firma, pn);
        assertFalse(fileShareService.fileExists(share, firma + "/" + pn));
    }

    @Test
    void fileExists() {
        String share = "exists-" + java.util.UUID.randomUUID();
        String dir = "a/b";
        String name = "c.txt";
        assertFalse(fileShareService.fileExists(share, dir + "/" + name));
        fileShareService.uploadOrReplaceInFileShare(share, dir, name, new ByteArrayInputStream("x".getBytes()), 1);
        assertTrue(fileShareService.fileExists(share, dir + "/" + name));
    }

    @Test
    void getFullFileName() {
        String share = "full";
        String firma = "ACME";
        String pn = "123";
        String dir = "folder";
        // create files under share/firma/pn/dir
        fileShareService.uploadOrReplaceInFileShare(share, firma + "/" + pn + "/" + dir, "doc-abc.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, firma + "/" + pn + "/" + dir, "note-xyz.txt", new ByteArrayInputStream("x".getBytes()), 1);
        String found = fileShareService.getFullFileName(share, firma, pn, dir, "doc-", "abc");
        assertEquals("doc-abc.txt", found);
        assertNull(fileShareService.getFullFileName(share, firma, pn, dir, "missing-", "abc"));
    }

    @Test
    void deleteFromFileShareContainingFilename() {
        String share = "contains";
        String pn = "100";
        String dir = "dir";
        fileShareService.uploadOrReplaceInFileShare(share, pn + "/" + dir, "aaa-one.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, pn + "/" + dir, "bbb-two.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, pn + "/" + dir, "ccc-one.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.deleteFromFileShareContainingFilename(share, pn, dir, "one");
        assertFalse(fileShareService.fileExists(share, pn + "/" + dir + "/aaa-one.txt"));
        assertTrue(fileShareService.fileExists(share, pn + "/" + dir + "/bbb-two.txt"));
        assertFalse(fileShareService.fileExists(share, pn + "/" + dir + "/ccc-one.txt"));
    }

    @Test
    void downloadFiles() throws Exception {
        String share = "dl";
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
        String share = "tree";
        String dir = "alpha";
        fileShareService.uploadOrReplaceInFileShare(share, dir, "a.txt", new ByteArrayInputStream("x".getBytes()), 1);
        TreeNode node = fileShareService.getDirectoryStructure(share, dir);
        assertNotNull(node);
    }

    @Test
    void renameAndMoveSignedDocumentsAndDirectories() {
        assertDoesNotThrow(() -> fileShareService.renameAndMoveSignedDocumentsAndDirectories("123"));
    }

    @Test
    void downloadFilesToTemp() {
        String share = "tmpdl";
        String dir = "d1/d2";
        fileShareService.uploadOrReplaceInFileShare(share, dir, "a.txt", new ByteArrayInputStream("x".getBytes()), 1);
        fileShareService.uploadOrReplaceInFileShare(share, dir, "b.txt", new ByteArrayInputStream("x".getBytes()), 1);
        List<File> files = fileShareService.downloadFilesToTemp(dir, share);
        assertEquals(2, files.size());
        for (File f : files) {
            assertTrue(f.exists());
        }
    }
}