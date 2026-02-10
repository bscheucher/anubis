package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.microsoftgraphservice.dtos.TreeNode;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Profile({"localdev","test"})
@RequiredArgsConstructor
public class FileShareServiceImplLocal implements FileShareService {

    @Value("${localStorageRoot}")
    private Path root;


    private Path shareRoot(String shareName) {
        return root.resolve(Objects.requireNonNull(shareName, "shareName")).normalize();
    }

    private Path resolvePath(String shareName, String directoryPath) {
        Path p = shareRoot(shareName);
        if (directoryPath != null && !directoryPath.isBlank()) p = p.resolve(directoryPath);
        return p.normalize();
    }

    private static void ensureDir(Path p) {
        try { Files.createDirectories(p); } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    private static void copyStream(InputStream in, Path target, long length) {
        try (OutputStream out = Files.newOutputStream(target, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            in.transferTo(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // ---------- required interface methods

    @Override
    public void uploadOrReplaceInFileShare(String shareName, String directoryPath, String fileName, InputStream data, long length) {
        Path dir = resolvePath(shareName, directoryPath);
        ensureDir(dir);

        // delete any file that starts with prefix (to mimic your prod logic)
        try (Stream<Path> s = Files.list(dir)) {
            s.filter(p -> !Files.isDirectory(p) && p.getFileName().toString().startsWith(fileName))
             .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Path target = dir.resolve(fileName);
        copyStream(data, target, length);
        log.info("Local FS: uploaded '{}' to '{}'", fileName, dir);
    }

    @Override
    public InputStream downloadFromFileShare(String shareName, String directoryPath, String fileNamePrefix) {
        Path dir = resolvePath(shareName, directoryPath);
        if (!Files.isDirectory(dir)) throw new IllegalStateException("Directory not found: " + dir);

        try (Stream<Path> s = Files.list(dir)) {
            Optional<Path> first = s
                .filter(p -> !Files.isDirectory(p) && p.getFileName().toString().startsWith(fileNamePrefix))
                .findFirst();
            if (first.isEmpty()) return InputStream.nullInputStream();
            return Files.newInputStream(first.get(), StandardOpenOption.READ);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public InputStream downloadFileById(String shareName, String fileId) {
        // In local mode, interpret fileId as a *relative path* under the share root.
        Path p = shareRoot(shareName).resolve(fileId).normalize();
        try {
            if (!Files.exists(p) || Files.isDirectory(p)) return InputStream.nullInputStream();
            return Files.newInputStream(p, StandardOpenOption.READ);
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public String getVereinbarungenDirectory(String personalnummer, Stammdaten stammdaten) {
        // Keep same semantics your prod code expects; here we just compose a path string.
        // Adjust to your current prod rules.
        return "vereinbarungen/" + personalnummer;
    }

    @Override
    public void emptyFolderFromFileShare(String shareName, String directoryPath) {
        Path dir = resolvePath(shareName, directoryPath);
        if (!Files.isDirectory(dir)) return;
        try (Stream<Path> s = Files.list(dir)) {
            s.forEach(p -> { try {
                if (Files.isDirectory(p)) {
                    try (Stream<Path> deep = Files.walk(p)) {
                        deep.sorted(Comparator.reverseOrder()).forEach(q -> { try { Files.deleteIfExists(q); } catch (IOException ignored) {} });
                    }
                } else {
                    Files.deleteIfExists(p);
                }
            } catch (IOException ignored) {} });
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public void deleteFromFileShare(String shareName, String personalnummer, String directoryPath, String fileName) {
        Path p = resolvePath(shareName, directoryPath).resolve(fileName);
        try { Files.deleteIfExists(p); } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public void cleanFileshare(String fileshare) {
        Path rootShare = shareRoot(fileshare);
        if (!Files.exists(rootShare)) return;
        try (Stream<Path> s = Files.walk(rootShare)) {
            s.sorted(Comparator.reverseOrder()).forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public void createStructureForNewMA(String personalnummer, String mainDirectory, String firma) {
        // mimic whatever structure prod creates; example:
        Path p = root.resolve(firma).resolve(mainDirectory).resolve(personalnummer);
        ensureDir(p);
    }

    @Override
    public void moveSignedDocuments(String personalnummer) {
        // implement the same rules you have in prod.
        // Example placeholder: no-op for local unless you specify paths.
    }

    @Override
    public void renamePersonalnummerDirectory(String personalnummer) {
        // placeholder: add your prod rename rules here.
    }

    @Override
    public void renameFilesInDirectoryRecursively(String personalnummer) {
        // placeholder: mirror prod renaming logic.
    }

    @Override
    public void deletePersonalnummerDirectory(String shareName, String firma, String personalnummer) {
        Path p = shareRoot(shareName).resolve(firma).resolve(personalnummer);
        if (!Files.exists(p)) return;
        try (Stream<Path> s = Files.walk(p)) {
            s.sorted(Comparator.reverseOrder()).forEach(q -> { try { Files.deleteIfExists(q); } catch (IOException ignored) {} });
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public boolean fileExists(String shareName, String directoryPath) {
        Path p = resolvePath(shareName, directoryPath);
        return Files.exists(p);
    }

    @Override
    public String getFullFileName(String shareName, String firma, String personalnummer, String directoryPath, String fileNamePrefix, String fileNameMiddle) {
        Path dir = shareRoot(shareName).resolve(firma).resolve(personalnummer).resolve(directoryPath);
        if (!Files.isDirectory(dir)) return null;
        try (Stream<Path> s = Files.list(dir)) {
            return s.filter(p -> !Files.isDirectory(p))
                    .map(p -> p.getFileName().toString())
                    .filter(n -> n.startsWith(fileNamePrefix) && n.contains(fileNameMiddle))
                    .findFirst().orElse(null);
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public void deleteFromFileShareContainingFilename(String shareName, String personalnummer, String directoryPath, String fileName) {
        Path dir = shareRoot(shareName).resolve(personalnummer).resolve(directoryPath);
        if (!Files.isDirectory(dir)) return;
        try (Stream<Path> s = Files.list(dir)) {
            s.filter(p -> !Files.isDirectory(p) && p.getFileName().toString().contains(fileName))
             .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public void downloadFiles(String remoteDirectory, Path localDirectory, String shareName) {
        Path src = resolvePath(shareName, remoteDirectory);
        ensureDir(localDirectory);
        if (!Files.isDirectory(src)) return;
        try (Stream<Path> s = Files.walk(src)) {
            for (Path from : s.collect(Collectors.toList())) {
                Path to = localDirectory.resolve(src.relativize(from).toString());
                if (Files.isDirectory(from)) {
                    ensureDir(to);
                } else {
                    ensureDir(to.getParent());
                    Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    @Override
    public TreeNode getDirectoryStructure(String shareName, String rootDirectoryPath) {
        // Build your TreeNode from local FS. Implementation depends on your TreeNode class.
        // Sketch:
        Path rootPath = resolvePath(shareName, rootDirectoryPath);
        return buildTree(rootPath);
    }

    @Override
    public void renameAndMoveSignedDocumentsAndDirectories(String personalnummer) {
        // implement to mirror prod rules
    }

    @Override
    public List<File> downloadFilesToTemp(String remoteDirectory, String shareName) {
        Path src = resolvePath(shareName, remoteDirectory);
        if (!Files.isDirectory(src)) return List.of();
        try (Stream<Path> s = Files.walk(src)) {
            List<File> files = s.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
            // If you *must* copy to OS temp, do so here; otherwise returning handles to local paths is fine in dev.
            return files;
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    // ---- Tree builder sketch (adjust to your DTO)
    private TreeNode buildTree(Path dir) {
        // Pseudo: create node(name=dir.getFileName(), children=[...])
        // Implement according to your TreeNode API.
        return new TreeNode(); // TODO implement to your DTO
    }
}
