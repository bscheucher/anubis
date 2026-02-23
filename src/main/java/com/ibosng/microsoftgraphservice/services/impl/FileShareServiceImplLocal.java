package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.microsoftgraphservice.dtos.FileItem;
import com.ibosng.microsoftgraphservice.dtos.TreeNode;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.microsoftgraphservice.services.MSEnvironmentService;
import com.ibosng.microsoftgraphservice.utils.Helpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibosng.dbservice.enums.MimeTypeMapping.getMimeTypeForExtension;

@Slf4j
@Service
@Profile({"localdev", "test"})
@RequiredArgsConstructor
public class FileShareServiceImplLocal implements FileShareService {

    private static final String GEHALTSNACHWEISE = "Gehaltsnachweise";
    private static final String SOZIALVERSICHERUNGSDATEN = "Sozialversicherungsdaten, ELDA";
    private static final String STAMMDATEN = "Stammdaten";
    private static final String VERTRAG = "Vertrag laufendes DV";
    private static final String ONBOARDING = "Onboarding";
    private static final String DIENSTVERTRAG = "dienstvertrag";
    private static final String VEREINBARUNGEN = "Vereinbarungen";
    private static final String UNSIGNED = "Nicht unterschrieben";
    private static final String SIGNED = "Unterschrieben";

    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    @Value("${localStorageRoot}")
    private Path root;

    private final MSEnvironmentService msEnvironmentService;
    private final StammdatenService stammdatenService;


    private Path shareRoot(String shareName) {
        return root.resolve(Objects.requireNonNull(shareName, "shareName")).normalize();
    }

    private Path resolvePath(String shareName, String directoryPath) {
        Path resolvedPath = shareRoot(shareName);
        if (directoryPath != null && !directoryPath.isBlank()) resolvedPath = resolvedPath.resolve(directoryPath);
        return resolvedPath.normalize();
    }

    private static void ensureDir(Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void copyStream(InputStream inputStream, Path targetFile) {
        try (OutputStream out = Files.newOutputStream(targetFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            inputStream.transferTo(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static List<Path> listPaths(Path directory) {
        try (Stream<Path> stream = Files.list(directory)) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to list directory: " + directory, e);
        }
    }

    private static List<Path> walkPaths(Path directory) {
        try (Stream<Path> stream = Files.walk(directory)) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to walk directory: " + directory, e);
        }
    }

    private static void deletePath(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete path: " + path, e);
        }
    }

    private static void deleteRecursively(Path rootPath) {
        List<Path> entries = walkPaths(rootPath);
        entries.sort(Comparator.reverseOrder());
        entries.forEach(FileShareServiceImplLocal::deletePath);
    }

    private static Path ensureSubdirectoryExists(Path parentDirectory, String subdirectoryName) {
        Path subdirectory = parentDirectory.resolve(subdirectoryName);
        if (!Files.isDirectory(subdirectory)) {
            ensureDir(subdirectory);
            log.info("Subdirectory '{}' was created under '{}'", subdirectoryName, parentDirectory);
        }
        return subdirectory;
    }

    private static void deleteDirectoryContents(Path directory) {
        for (Path entry : listPaths(directory)) {
            if (Files.isDirectory(entry)) {
                deleteRecursively(entry);
            } else {
                deletePath(entry);
            }
        }
    }

    private static void processDirectoryContents(Path sourceDirectory, Path targetDirectory) {
        for (Path entry : listPaths(sourceDirectory)) {
            if (Files.isDirectory(entry)) {
                Path targetSubDir = targetDirectory.resolve(entry.getFileName().toString());
                ensureDir(targetSubDir);

                if (entry.toString().contains(DIENSTVERTRAG)) {
                    deleteRecursively(entry);
                    log.info("Deleted DIENSTVERTRAEGE directory '{}'.", entry);
                } else {
                    processDirectoryContents(entry, targetSubDir);
                }
            } else {
                Path targetFile = targetDirectory.resolve(entry.getFileName().toString());
                try {
                    Files.copy(entry, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    deletePath(entry);
                    log.info("Moved file '{}' to target directory '{}'.", entry.getFileName(), targetDirectory);
                } catch (IOException e) {
                    throw new UncheckedIOException("Failed to move file: " + entry, e);
                }
            }
        }
    }

    private static void renameFilesInSubdirectories(Path directory, String vorname, String nachname) {
        for (Path entry : listPaths(directory)) {
            if (Files.isDirectory(entry)) {
                renameFilesInSubdirectories(entry, vorname, nachname);
            } else {
                String originalFileName = entry.getFileName().toString();
                String updatedFileName = Helpers.updateFileName(originalFileName, vorname, nachname);
                if (!updatedFileName.equals(originalFileName)) {
                    Path targetFile = entry.resolveSibling(updatedFileName);
                    try {
                        Files.move(entry, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        log.info("Renamed file '{}' to '{}'.", originalFileName, updatedFileName);
                    } catch (IOException e) {
                        log.error("Error renaming file '{}' in directory '{}': exception: ", originalFileName, directory, e);
                    }
                }
            }
        }
    }

    private static Path createDirectoryIfNotExists(Path shareRootPath, String targetPath) {
        String[] pathSegments = targetPath.split("/");
        Path currentDirectory = shareRootPath;

        for (String segment : pathSegments) {
            currentDirectory = currentDirectory.resolve(segment);
            if (!Files.isDirectory(currentDirectory)) {
                log.info("Directory '{}' does not exist, it will be created.", currentDirectory);
                ensureDir(currentDirectory);
            }
        }
        return currentDirectory;
    }

    private TreeNode createErrorNode(String errorMessage) {
        TreeNode node = new TreeNode();
        node.setErrorMessage(errorMessage);
        return node;
    }

    // ---------- required interface methods

    @Override
    public void uploadOrReplaceInFileShare(String shareName, String directoryPath, String fileName, InputStream data, long length) {
        Path targetDirectory = resolvePath(shareName, directoryPath);
        ensureDir(targetDirectory);

        // delete any file that starts with prefix (to mimic your prod logic)
        List<Path> existingFiles = listPaths(targetDirectory).stream()
                .filter(path -> !Files.isDirectory(path) && path.getFileName().toString().startsWith(fileName))
                .toList();
        for (Path existingFile : existingFiles) {
            deletePath(existingFile);
        }

        Path target = targetDirectory.resolve(fileName);
        copyStream(data, target);
        log.info("Local FS: uploaded '{}' to '{}'", fileName, targetDirectory);
    }

    @Override
    public InputStream downloadFromFileShare(String shareName, String directoryPath, String fileNamePrefix) {
        Path sourceDirectory = resolvePath(shareName, directoryPath);
        if (!Files.isDirectory(sourceDirectory)) {
            throw new IllegalStateException("Directory not found: " + sourceDirectory);
        }

        try (Stream<Path> s = Files.list(sourceDirectory)) {
            Optional<Path> first = s
                    .filter(p -> !Files.isDirectory(p) && p.getFileName().toString().startsWith(fileNamePrefix))
                    .findFirst();
            if (first.isEmpty()) {
                return InputStream.nullInputStream();
            }

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
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String getVereinbarungenDirectory(String personalnummer, Stammdaten stammdaten) {
        String firma = stammdaten.getPersonalnummer().getFirma().getName();
        String vorname = stammdaten.getVorname();
        String nachname = stammdaten.getNachname();
        String mitarbeiterDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
        return String.join("/", firma, mitarbeiterDirectoryName, VEREINBARUNGEN);
    }

    @Override
    public void emptyFolderFromFileShare(String shareName, String directoryPath) {
        Path targetDirectory = resolvePath(shareName, directoryPath);
        if (!Files.isDirectory(targetDirectory)) return;
        for (Path entry : listPaths(targetDirectory)) {
            if (Files.isDirectory(entry)) {
                deleteRecursively(entry);
            } else {
                deletePath(entry);
            }
        }
    }

    @Override
    public void deleteFromFileShare(String shareName, String personalnummer, String directoryPath, String fileName) {
        Path targetPath = resolvePath(shareName, directoryPath).resolve(fileName);
        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void cleanFileshare(String fileshare) {
        Path shareRootPath = shareRoot(fileshare);
        if (!Files.exists(shareRootPath)) {
            return;
        }
        deleteRecursively(shareRootPath);
    }

    @Override
    public void createStructureForNewMA(String personalnummer, String mainDirectory, String firma) {
        try {
            Path shareRootPath = shareRoot(mainDirectory);
            if (!Files.isDirectory(shareRootPath)) {
                log.error("Share {} does not exist! Stopping the process", mainDirectory);
                return;
            }

            Path firmaDirectory = ensureSubdirectoryExists(shareRootPath, firma);

            String directoryName = personalnummer;
            log.info("Creating directory structure for '{}'", directoryName);

            Path personalDirectoryPath = firmaDirectory.resolve(directoryName);
            if (Files.isDirectory(personalDirectoryPath)) {
                log.error("Directory with the same name already exists: {}", directoryName);
                if (!msEnvironmentService.isProduction()) {
                    deleteRecursively(personalDirectoryPath);
                }
            }

            Path personalDirectory = ensureSubdirectoryExists(firmaDirectory, directoryName);

            Path vereinbarungenDirectory = ensureSubdirectoryExists(personalDirectory, VEREINBARUNGEN);
            ensureSubdirectoryExists(vereinbarungenDirectory, UNSIGNED);
            ensureSubdirectoryExists(vereinbarungenDirectory, SIGNED);

            Path gehaltsnachweiseDirectory = ensureSubdirectoryExists(personalDirectory, GEHALTSNACHWEISE);
            ensureSubdirectoryExists(gehaltsnachweiseDirectory, "Nettozettel");
            ensureSubdirectoryExists(gehaltsnachweiseDirectory, "L16");

            ensureSubdirectoryExists(personalDirectory, SOZIALVERSICHERUNGSDATEN);

            Path stammdatenDirectory = ensureSubdirectoryExists(personalDirectory, STAMMDATEN);
            ensureSubdirectoryExists(stammdatenDirectory, "Ausweise");
            ensureSubdirectoryExists(stammdatenDirectory, "Aufenthaltstitel");

            Path vertragDirectory = ensureSubdirectoryExists(personalDirectory, VERTRAG);
            ensureSubdirectoryExists(vertragDirectory, "Dienstvertrag und Zusätze");

            Path onboardingDirectory = ensureSubdirectoryExists(personalDirectory, ONBOARDING);
            ensureSubdirectoryExists(onboardingDirectory, "Beschäftigungs- & Stundennachweise");
        } catch (Exception e) {
            log.error("Failed to create a structure for personalnummer {} with exception: ", personalnummer, e);
        }
    }

    @Override
    public void moveSignedDocuments(String personalnummer) {
        try {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
            String firma = stammdaten.getPersonalnummer().getFirma().getName();
            String vorname = stammdaten.getVorname();
            String nachname = stammdaten.getNachname();

            String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);

            Path sourceDirectory = shareRoot(fileShareTemp)
                    .resolve(firma)
                    .resolve(personalnummer);

            if (!Files.isDirectory(sourceDirectory)) {
                log.error("Source directory '{}' does not exist.", updatedDirectoryName);
                return;
            }

            Path personalunterlagenRoot = shareRoot(fileSharePersonalunterlagen);
            createDirectoryIfNotExists(personalunterlagenRoot, firma + "/" + updatedDirectoryName);

            Path targetDirectory = personalunterlagenRoot
                    .resolve(firma)
                    .resolve(updatedDirectoryName);

            if (!Files.isDirectory(targetDirectory)) {
                ensureDir(targetDirectory);
                log.info("Created target directory '{}'.", targetDirectory);
            }

            processDirectoryContents(sourceDirectory, targetDirectory);

            deleteDirectoryContents(sourceDirectory);
            deletePath(sourceDirectory);
            log.info("Successfully processed documents for personalnummer '{}' from temp to personalunterlagen.", personalnummer);
        } catch (Exception ex) {
            log.error("Error during document processing for personalnummer '{}': exception", personalnummer, ex);
        }
    }

    @Override
    public void renamePersonalnummerDirectory(String personalnummer) {
        try {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
            String firma = stammdaten.getPersonalnummer().getFirma().getName();
            String vorname = stammdaten.getVorname();
            String nachname = stammdaten.getNachname();

            Path firmaDirectory = shareRoot(fileShareTemp).resolve(firma);
            if (!Files.isDirectory(firmaDirectory)) {
                log.error("Firma directory '{}' does not exist in temp storage.", firma);
                return;
            }

            Path personalnummerDirectory = firmaDirectory.resolve(personalnummer);
            if (!Files.isDirectory(personalnummerDirectory)) {
                log.error("Directory for personalnummer '{}' does not exist in firma '{}'.", personalnummer, firma);
                return;
            }

            String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
            Path targetDirectory = firmaDirectory.resolve(updatedDirectoryName);
            if (Files.exists(targetDirectory)) {
                log.error("Directory with the same name already exists: {}", updatedDirectoryName);
                return;
            }

            Files.move(personalnummerDirectory, targetDirectory);
            log.info("Successfully renamed personalnummer directory '{}' to '{}'.", personalnummer, updatedDirectoryName);
        } catch (IOException e) {
            log.error("Rename failed for personalnummer '{}': exception", personalnummer, e);
        } catch (Exception e) {
            log.error("Error renaming personalnummer directory '{}': exception", personalnummer, e);
        }
    }

    @Override
    public void renameFilesInDirectoryRecursively(String personalnummer) {
        try {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
            String firma = stammdaten.getPersonalnummer().getFirma().getName();
            String vorname = stammdaten.getVorname();
            String nachname = stammdaten.getNachname();

            Path personalnummerDirectory = shareRoot(fileShareTemp)
                    .resolve(firma)
                    .resolve(personalnummer);

            if (!Files.isDirectory(personalnummerDirectory)) {
                log.error("Directory for personalnummer '{}' does not exist in firma '{}'.", personalnummer, firma);
                return;
            }

            renameFilesInSubdirectories(personalnummerDirectory, vorname, nachname);
            log.info("Successfully renamed all files in directory for personalnummer '{}'.", personalnummer);
        } catch (Exception e) {
            log.error("Error renaming files in directory for personalnummer '{}' exception: ", personalnummer, e);
        }
    }

    @Override
    public void deletePersonalnummerDirectory(String shareName, String firma, String personalnummer) {
        Path personalDirectory = shareRoot(shareName).resolve(firma).resolve(personalnummer);
        if (!Files.exists(personalDirectory)) return;
        deleteRecursively(personalDirectory);
    }

    @Override
    public boolean fileExists(String shareName, String directoryPath) {
        Path targetPath = resolvePath(shareName, directoryPath);
        return Files.exists(targetPath);
    }

    @Override
    public String getFullFileName(String shareName, String firma, String personalnummer, String directoryPath, String fileNamePrefix, String fileNameMiddle) {
        Path targetDirectory = shareRoot(shareName).resolve(firma).resolve(personalnummer).resolve(directoryPath);
        if (!Files.isDirectory(targetDirectory)) return null;
        try (Stream<Path> s = Files.list(targetDirectory)) {
            return s.filter(p -> !Files.isDirectory(p))
                    .map(p -> p.getFileName().toString())
                    .filter(n -> n.startsWith(fileNamePrefix) && n.contains(fileNameMiddle))
                    .findFirst().orElse(null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void deleteFromFileShareContainingFilename(String shareName, String personalnummer, String directoryPath, String fileName) {
        Path targetDirectory = shareRoot(shareName).resolve(personalnummer).resolve(directoryPath);
        if (!Files.isDirectory(targetDirectory)) return;
        List<Path> matchingFiles = listPaths(targetDirectory).stream()
                .filter(path -> !Files.isDirectory(path) && path.getFileName().toString().contains(fileName))
                .collect(Collectors.toList());
        for (Path matchingFile : matchingFiles) {
            deletePath(matchingFile);
        }
    }

    @Override
    public void downloadFiles(String remoteDirectory, Path localDirectory, String shareName) {
        Path sourceDirectory = resolvePath(shareName, remoteDirectory);
        ensureDir(localDirectory);
        if (!Files.isDirectory(sourceDirectory)) return;
        try (Stream<Path> s = Files.walk(sourceDirectory)) {
            for (Path sourcePath : s.collect(Collectors.toList())) {
                Path targetPath = localDirectory.resolve(sourceDirectory.relativize(sourcePath).toString());
                if (Files.isDirectory(sourcePath)) {
                    ensureDir(targetPath);
                } else {
                    ensureDir(targetPath.getParent());
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public TreeNode getDirectoryStructure(String shareName, String rootDirectoryPath) {
        try {
            Path shareRootPath = shareRoot(shareName);
            if (!Files.exists(shareRootPath) || !Files.isDirectory(shareRootPath)) {
                String errorMessage = "Personalunterlagen existieren nicht";
                log.error("Share '{}' does not exist.", shareName);
                return createErrorNode(errorMessage);
            }

            Path rootDirectory = resolvePath(shareName, rootDirectoryPath);
            if (!Files.exists(rootDirectory) || !Files.isDirectory(rootDirectory)) {
                String errorMessage = "Personalunterlagen existieren nicht";
                log.error("Root directory '{}' does not exist.", rootDirectoryPath);
                return createErrorNode(errorMessage);
            }

            return buildTreeNode(rootDirectory, shareRootPath);
        } catch (Exception e) {
            String errorMessage = "Fehler beim Laden der Personalunterlagen";
            log.error("Error while fetching directory structure for share '{}' and directory '{}': ", shareName, rootDirectoryPath, e);
            return createErrorNode(errorMessage);
        }
    }

    @Override
    public void renameAndMoveSignedDocumentsAndDirectories(String personalnummer) {
        renameFilesInDirectoryRecursively(personalnummer);
        moveSignedDocuments(personalnummer);
    }

    @Override
    public List<File> downloadFilesToTemp(String remoteDirectory, String shareName) {
        List<File> downloadedFiles = new ArrayList<>();

        Path sourceDirectory = resolvePath(shareName, remoteDirectory);
        if (!Files.isDirectory(sourceDirectory)) {
            log.error("Share {} does not exist!", shareName);
            return downloadedFiles;
        }

        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("local_download_");
        } catch (IOException e) {
            log.error("Failed to create temp directory for downloads", e);
            return downloadedFiles;
        }

        for (Path entry : listPaths(sourceDirectory)) {
            if (!Files.isDirectory(entry)) {
                Path tempFilePath = tempDir.resolve(entry.getFileName().toString());
                File tempFile = tempFilePath.toFile();
                try {
                    Files.createDirectories(tempFilePath.getParent());
                    Files.copy(entry, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
                    downloadedFiles.add(tempFile);
                } catch (IOException e) {
                    log.error("Failed to download file {} from local fileshare: {}", entry.getFileName(), e.getMessage(), e);
                }
            }
        }

        return downloadedFiles;
    }

    // ---- Tree builder optimized for local filesystem
    private TreeNode buildTreeNode(Path directory, Path shareRoot) {
        TreeNode node = new TreeNode();
        Path relativeDirectory = shareRoot.relativize(directory);
        node.setId(relativeDirectory.toString().replace('\\', '/'));
        node.setPath(relativeDirectory.toString().replace('\\', '/'));
        node.setTitle(directory.getFileName() != null ? directory.getFileName().toString() : directory.toString());
        try {
            node.setCreatedAt(Files.getLastModifiedTime(directory).toInstant().toString());
        } catch (IOException e) {
            node.setCreatedAt(null);
        }

        List<TreeNode> children = new ArrayList<>();
        List<Path> entries = listPaths(directory);
        entries.sort(Comparator.comparing(Path::getFileName));
        for (Path entry : entries) {
            if (Files.isDirectory(entry)) {
                children.add(buildTreeNode(entry, shareRoot));
            } else {
                children.add(buildFileNode(entry, directory, shareRoot));
            }
        }

        node.setContent(children);
        node.setMimeType(null);
        return node;
    }

    private TreeNode buildFileNode(Path file, Path parentDir, Path shareRoot) {
        FileItem fileNode = new FileItem();
        Path relativeFile = shareRoot.relativize(file);
        fileNode.setId(relativeFile.toString().replace('\\', '/'));
        Path relativeParent = shareRoot.relativize(parentDir);
        fileNode.setPath(relativeParent.toString().replace('\\', '/'));
        fileNode.setTitle(file.getFileName().toString());
        try {
            fileNode.setCreatedAt(Files.getLastModifiedTime(file).toInstant().toString());
        } catch (IOException e) {
            fileNode.setCreatedAt(null);
        }
        fileNode.setMimeType(deriveMimeTypeFromExtension(file.getFileName().toString()));
        return fileNode;
    }

    private String deriveMimeTypeFromExtension(String fileName) {
        String extension = fileName.lastIndexOf('.') != -1 ? fileName.substring(fileName.lastIndexOf('.')) : "";
        return getMimeTypeForExtension(extension.toLowerCase());
    }
}
