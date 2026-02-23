package com.ibosng.microsoftgraphservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointCondition;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Conditional(SharePointCondition.Reverse.class)
public class SharePointServiceImplLocal implements SharePointService {

    private final Path root;

    @Autowired
    public SharePointServiceImplLocal(@Value("${localStorageRoot}") Path root) {
        this.root = root;
    }

    private Path resolvePath(String path) {
        Path p = Objects.requireNonNull(root, "localStorageRoot must be configured");
        if (path != null && !path.isBlank()) p = p.resolve(path);
        return p.normalize();
    }

    private void ensureFolder(Path p) {
        try {
            Files.createDirectories(p);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private DriveItem getDriveItemFromPath(Path filePath, Path root) {
        DriveItem driveItem = new DriveItem();
        driveItem.name = filePath.getFileName().toString();
        // Use a normalized forward-slash relative path as a stable local "id"
        driveItem.id = root.relativize(filePath).toString().replace('\\', '/');
        return driveItem;
    }

    @Override
    public DriveItem uploadFile(String filePath, String fileName, String folderPath) throws MSGraphServiceException {
        Path targetFolderPath = resolvePath(folderPath);
        ensureFolder(targetFolderPath);
        Path sourcePath = Paths.get(filePath);
        Path targetFilePath = targetFolderPath.resolve(fileName).normalize();
        try {
            Files.copy(sourcePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MSGraphServiceException("File could not be copied to local storage", e);
        }
        log.info("Local SharePoint: uploaded '{}' to '{}'", fileName, targetFolderPath);
        return getDriveItemFromPath(targetFilePath, root);
    }

    @Override
    public File downloadFile(String fileId, String fileName) throws MSGraphServiceException {
        Path sourcePath = resolvePath(fileId);
        File file = new File(fileName);
        try (InputStream in = Files.newInputStream(sourcePath, StandardOpenOption.READ);
             OutputStream os = new FileOutputStream(file)) {
            in.transferTo(os);
        } catch (IOException e) {
            throw new MSGraphServiceException("Error while downloading file from local SharePoint", e);
        }
        return file;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public File createJsonFileFromDto(Object dto, String filename) throws MSGraphServiceException {
        File tempFile;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            tempFile = File.createTempFile(filename, ".json");
            try (FileWriter fileWriter = new FileWriter(tempFile)) {
                objectMapper.writeValue(fileWriter, dto);
            }
            return tempFile;
        } catch (Exception e) {
            throw new MSGraphServiceException("Unexpected error during JSON file creation", e);
        }
    }

    @Override
    public DriveItemCollectionPage getUploadedFiles(String folder) {
        Path folderPath = resolvePath(folder);
        if (!Files.isDirectory(folderPath)) {
            ensureFolder(folderPath);
        }
        List<DriveItem> items;
        try (Stream<Path> s = Files.list(folderPath)) {
            items = s.filter(p -> !Files.isDirectory(p))
                    .map(p -> getDriveItemFromPath(p, root))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
        // nextRequestBuilder not needed in local usage; pass null
        return new DriveItemCollectionPage(items, null);
    }

    @Override
    public DriveItem moveFile(String fileId, String filename, String targetFolder) {
        Path sourcePath = resolvePath(fileId);
        Path targetFolderPath = resolvePath(targetFolder);
        ensureFolder(targetFolderPath);
        Path targetFilePath = targetFolderPath.resolve(filename).normalize();
        try {
            Files.createDirectories(targetFilePath.getParent());
            Files.move(sourcePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        log.info("Local OneDrive: moved '{}' to '{}'", sourcePath, targetFilePath);
        return getDriveItemFromPath(targetFilePath, root);
    }

    @Override
    public DriveItem moveFile(FileDetails fileDetails, String targetFolder) {
        String filename = fileDetails.getFilename();
        String idOrPath = fileDetails.getSharePointId();
        if (idOrPath == null || idOrPath.isBlank()) {
            // fallback: use filePath from details
            idOrPath = Optional.ofNullable(fileDetails.getFilePath()).orElse("");
        }
        return moveFile(idOrPath, filename, targetFolder);
    }

    @Override
    public DriveItem createFolder(String folderPath, String folderName) {
        Path targetFolderPath = resolvePath(folderPath).resolve(folderName).normalize();
        ensureFolder(targetFolderPath);
        log.info("Local OneDrive: created folder '{}'", targetFolderPath);
        return getDriveItemFromPath(targetFolderPath, root);
    }

    @Override
    public String getFolderIdByPath(String folderPath) {
        // In local mode, the "id" is simply the normalized path string relative to root
        return resolvePath(folderPath).toString();
    }

    @Override
    public DriveItemCollectionPage getContentsOfFolder(Optional<String> folderPath) {
        return getUploadedFiles(folderPath.orElse(""));
    }

    @Override
    public DriveItem getItemByPath(String fullPath) {
        Path path = resolvePath(fullPath);
        if (Files.exists(path)) {
            return getDriveItemFromPath(path, root);
        }
        return null;
    }
}
