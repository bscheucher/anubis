package com.ibosng.microsoftgraphservice.services.impl;

import com.azure.storage.common.ParallelTransferOptions;
import com.azure.storage.file.share.ShareClient;
import com.azure.storage.file.share.ShareDirectoryClient;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.ShareServiceClient;
import com.azure.storage.file.share.models.ShareFileItem;
import com.azure.storage.file.share.models.ShareStorageException;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.microsoftgraphservice.dtos.FileItem;
import com.ibosng.microsoftgraphservice.dtos.TreeNode;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.microsoftgraphservice.services.MSEnvironmentService;
import com.ibosng.microsoftgraphservice.utils.Helpers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.ibosng.dbservice.enums.MimeTypeMapping.getMimeTypeForExtension;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;


@Profile("!(localdev | test)")
@Service
@Slf4j
@RequiredArgsConstructor
public class FileShareServiceImpl implements FileShareService {
    private static final Long BLOCK_SIZE = 4 * 1024 * 1024L;
    private static final Integer MAX_CONCURENCY = 4;
    private static final String GEHALTSNACHWEISE = "Gehaltsnachweise";
    private static final String SOZIALVERSICHERUNGSDATEN = "Sozialversicherungsdaten, ELDA";
    private static final String STAMMDATEN = "Stammdaten";
    private static final String VERTRAG = "Vertrag laufendes DV";
    private static final String ONBOARDING = "Onboarding";
    private static final String DIENSTVERTRAG = "dienstvertrag";
    private static final String VEREINBARUNGEN = "Vereinbarungen";
    private static final String UNSIGNED = "Nicht unterschrieben";
    private static final String SIGNED = "Unterschrieben";

    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    private final ShareServiceClient shareServiceClient;
    private final MSEnvironmentService msEnvironmentService;
    private final StammdatenService stammdatenService;

    @Override
    public void uploadOrReplaceInFileShare(String shareName, String directoryPath, String fileName, InputStream data, long length) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            ShareDirectoryClient rootDirectory = shareClient.getRootDirectoryClient();
            ShareDirectoryClient targetDirectory = rootDirectory.getSubdirectoryClient(directoryPath);

            if (!targetDirectory.exists()) {
                targetDirectory.create();
                log.debug("Directory '{}' created in share '{}'.", directoryPath, shareName);
            }

            // Remove any file with the same prefix
            boolean fileDeleted = false;
            for (ShareFileItem item : targetDirectory.listFilesAndDirectories()) {
                if (!item.isDirectory() && item.getName().startsWith(fileName)) {
                    ShareFileClient existingFileClient = targetDirectory.getFileClient(item.getName());
                    if (existingFileClient.exists()) {
                        existingFileClient.delete();
                        fileDeleted = true;
                    }
                }
            }
            if (fileDeleted) {
                log.info("Existing files matching prefix '{}' in '{}' were deleted.", fileName, directoryPath);
            }

            ShareFileClient fileClient = targetDirectory.getFileClient(fileName);
            ParallelTransferOptions transferOptions = new ParallelTransferOptions().setBlockSizeLong(BLOCK_SIZE).setMaxConcurrency(MAX_CONCURENCY);

            fileClient.create(length);
            fileClient.upload(data, length, transferOptions);

            log.info("File '{}' uploaded to '{}'.", fileName, directoryPath);
        } catch (ShareStorageException e) {
            log.error("Azure Storage Exception while uploading file '{}' to '{}': exception: ", fileName, directoryPath, e);
        } catch (Exception e) {
            log.error("Unexpected error during file upload '{}': exception: ", fileName, e);
        }
    }

    @Override
    public InputStream downloadFileById(String shareName, String fileId) {
        try {
            // Get the share client
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            if (!shareClient.exists()) {
                log.error("Share '{}' does not exist.", shareName);
                return null;
            }

            // Search for the file globally in the share
            Queue<ShareDirectoryClient> directoriesToSearch = new LinkedList<>();
            directoriesToSearch.add(shareClient.getRootDirectoryClient());

            while (!directoriesToSearch.isEmpty()) {
                ShareDirectoryClient currentDirectory = directoriesToSearch.poll();

                // List all files and directories in the current directory
                for (ShareFileItem item : currentDirectory.listFilesAndDirectories()) {
                    if (item.isDirectory()) {
                        // Add subdirectory to the search queue
                        directoriesToSearch.add(currentDirectory.getSubdirectoryClient(item.getName()));
                    } else if (item.getId().equals(fileId)) {
                        log.info("Found file '{}' with ID '{}'.", item.getName(), fileId);

                        // Get the file client and return the InputStream
                        ShareFileClient fileClient = currentDirectory.getFileClient(item.getName());
                        if (fileClient.exists()) {
                            log.info("Downloading file '{}'.", item.getName());
                            return fileClient.openInputStream();
                        } else {
                            log.warn("File '{}' no longer exists.", item.getName());
                        }
                    }
                }
            }

            log.warn("No file found with ID '{}' in share '{}'.", fileId, shareName);
        } catch (Exception e) {
            log.error("Error while downloading file with ID '{}': exception: ", fileId, e);
        }
        return null;
    }

    @Override
    public InputStream downloadFromFileShare(String shareName, String directoryPath, String fileNamePrefix) {
        try {
            // Get the share client and directory client
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            if (!shareClient.exists()) {
                log.error("Share '{}' does not exist.", shareName);
                return null;
            }

            ShareDirectoryClient directoryClient = shareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(directoryPath);

            if (!directoryClient.exists()) {
                log.error("Directory '{}' does not exist in share '{}'.", directoryPath, shareName);
                return null;
            }

            // Search for the file matching the prefix
            for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
                if (!item.isDirectory() && item.getName().startsWith(fileNamePrefix)) {
                    log.info("Found file '{}' matching '{}'.", item.getName(), fileNamePrefix);
                    ShareFileClient fileClient = directoryClient.getFileClient(item.getName());

                    if (fileClient.exists()) {
                        log.info("Downloading file '{}'.", item.getName());
                        return fileClient.openInputStream();
                    } else {
                        log.warn("File '{}' no longer exists.", item.getName());
                    }
                }
            }

            log.warn("No file found with prefix '{}' in directory '{}'.", fileNamePrefix, directoryPath);
        } catch (Exception e) {
            log.error("Error while downloading file '{}': exception: ", fileNamePrefix, e);
        }
        return null;
    }

    @Override
    public void emptyFolderFromFileShare(String shareName, String directoryPath) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            if (!shareClient.exists()) {
                log.warn("Share '{}' does not exist. Cannot delete files.", shareName);
                return;
            }

            ShareDirectoryClient directoryClient = shareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(directoryPath);
            if (!directoryClient.exists()) {
                log.warn("Directory '{}' does not exist in share '{}'. No files to delete.", directoryPath, shareName);
                return;
            }

            for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
                ShareFileClient fileClient = directoryClient.getFileClient(item.getName());
                if (fileClient.exists()) {
                    log.info("Deleting file '{}' in directory '{}'.", item.getName(), directoryPath);
                    fileClient.delete();
                    log.info("File '{}' successfully deleted.", item.getName());
                } else {
                    log.warn("File '{}' no longer exists. Skipping.", item.getName());
                }
            }

        } catch (ShareStorageException e) {
            log.error("Failed to delete file(s) in directory '{}': exception: ", directoryPath, e);
        } catch (Exception e) {
            log.error("Unexpected error while deleting file(s) in directory '{}': exception", directoryPath, e);
        }
    }

    @Override
    public void deleteFromFileShare(String shareName, String personalnummer, String directoryPath, String fileNamePrefix) {
        deleteFromFileShare(shareName, personalnummer, directoryPath, fileNamePrefix, true);
    }

    @Override
    public void deleteFromFileShareContainingFilename(String shareName, String personalnummer, String directoryPath, String fileName) {
        deleteFromFileShare(shareName, personalnummer, directoryPath, fileName, false);
    }

    private void deleteFromFileShare(String shareName, String personalnummer, String directoryPath, String fileNamePrefix, boolean isPrefix) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            if (!shareClient.exists()) {
                log.warn("Share '{}' does not exist. Cannot delete files.", shareName);
                return;
            }

            ShareDirectoryClient directoryClient = shareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(directoryPath);
            if (!directoryClient.exists()) {
                log.warn("Directory '{}' does not exist in share '{}'. No files to delete.", directoryPath, shareName);
                return;
            }

            boolean fileFound = false;
            for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
                if (!item.isDirectory()) {
                    if ((isPrefix && item.getName().startsWith(fileNamePrefix)) || (!isPrefix && item.getName().toLowerCase().contains(fileNamePrefix.toLowerCase()))) {
                        ShareFileClient fileClient = directoryClient.getFileClient(item.getName());
                        if (fileClient.exists()) {
                            log.info("Deleting file '{}' in directory '{}'.", item.getName(), directoryPath);
                            fileClient.delete();
                            log.info("File '{}' successfully deleted.", item.getName());
                            fileFound = true; // Indicate at least one file was found and deleted
                        } else {
                            log.warn("File '{}' no longer exists. Skipping.", item.getName());
                        }
                    }
                }
            }

            if (!fileFound) {
                log.warn("No file found matching prefix '{}' in directory '{}'.", fileNamePrefix, directoryPath);
            }
        } catch (ShareStorageException e) {
            log.error("Failed to delete file(s) with prefix '{}' in directory '{}': exception: ", fileNamePrefix, directoryPath, e);
        } catch (Exception e) {
            log.error("Unexpected error while deleting file(s) with prefix '{}' in directory '{}': exception", fileNamePrefix, directoryPath, e);
        }
    }

    @Override
    public void downloadFiles(String remoteDirectory, Path localDirectory, String shareName) {
        ShareClient shareClient = this.shareServiceClient.getShareClient(shareName);
        if (!shareClient.exists()) {
            log.error("Share {} does not exist! Stopping the process", shareName);
            return;
        }
        ShareDirectoryClient directoryClient = shareClient.getDirectoryClient(remoteDirectory);
        directoryClient.listFilesAndDirectories().forEach(fileItem -> {
            try {
                if (fileItem.isDirectory()) {
                    Files.createDirectories(localDirectory.resolve(fileItem.getName()));
                    downloadFiles(remoteDirectory + "/" + fileItem.getName(), localDirectory.resolve(fileItem.getName()), shareName);
                } else {
                    ShareFileClient fileClient = directoryClient.getFileClient(fileItem.getName());
                    Path destination = localDirectory.resolve(fileItem.getName());
                    Files.createDirectories(destination.getParent());
                    fileClient.download(Files.newOutputStream(destination));
                }
            } catch (IOException e) {
                log.error("Failed to download files from {} to {} for the fileshare {} with exception: ", remoteDirectory, localDirectory, shareClient, e);
            }
        });
    }

    @Override
    public void cleanFileshare(String fileshare) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(fileshare);
            if (!shareClient.exists()) {
                log.error("Share {} does not exist! Stopping the process", fileshare);
                return;
            }
            ShareDirectoryClient rootDirectoryClient = shareClient.getRootDirectoryClient();
            deleteDirectoryRecursively(rootDirectoryClient);
        } catch (ShareStorageException e) {
            log.error("Failed to empty the fileshare {} with exception: ", fileshare, e);
        }
    }

    @Override
    public void createStructureForNewMA(String personalnummer, String mainDirectory, String firma) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(mainDirectory);
            if (!shareClient.exists()) {
                log.error("Share {} does not exist! Stopping the process", mainDirectory);
                return;
            }

            ShareDirectoryClient rootDirectoryClient = shareClient.getRootDirectoryClient();

            ShareDirectoryClient firmaDirectory = ensureSubdirectoryExists(rootDirectoryClient, firma);

            String directoryName = personalnummer; // Changed to use only personalnummer
            log.info("Creating directory structure for '{}'", directoryName);

            if (doesDirectoryExist(firmaDirectory, directoryName)) {
                log.error("Directory with the same name already exists: {}", directoryName);
                if (!msEnvironmentService.isProduction()) {
                    deleteDirectoryRecursively(firmaDirectory.getSubdirectoryClient(directoryName));
                }
            }

            ShareDirectoryClient personalDirectory = ensureSubdirectoryExists(firmaDirectory, directoryName);

            ShareDirectoryClient vereinbarungenDirectory = ensureSubdirectoryExists(personalDirectory, VEREINBARUNGEN);
            ShareDirectoryClient vereinbarungenUnsignedDirectory = ensureSubdirectoryExists(vereinbarungenDirectory, UNSIGNED);
            ShareDirectoryClient vereinbarungenSignedDirectory = ensureSubdirectoryExists(vereinbarungenDirectory, SIGNED);
            ShareDirectoryClient gehaltsnachweiseDirectory = ensureSubdirectoryExists(personalDirectory, GEHALTSNACHWEISE);
            ensureSubdirectoryExists(gehaltsnachweiseDirectory, "Nettozettel");
            ensureSubdirectoryExists(gehaltsnachweiseDirectory, "L16");

            ShareDirectoryClient sozialversicherungsdatenDirectory = ensureSubdirectoryExists(personalDirectory, SOZIALVERSICHERUNGSDATEN);

            ShareDirectoryClient stammdatenDirectory = ensureSubdirectoryExists(personalDirectory, STAMMDATEN);
            ensureSubdirectoryExists(stammdatenDirectory, "Ausweise");
            ensureSubdirectoryExists(stammdatenDirectory, "Aufenthaltstitel");

            ShareDirectoryClient vertragDirectory = ensureSubdirectoryExists(personalDirectory, VERTRAG);
            ensureSubdirectoryExists(vertragDirectory, "Dienstvertrag und Zusätze");

            ShareDirectoryClient onboardingDirectory = ensureSubdirectoryExists(personalDirectory, ONBOARDING);
            ensureSubdirectoryExists(onboardingDirectory, "Beschäftigungs- & Stundennachweise");

        } catch (ShareStorageException e) {
            log.error("Failed to create a structure for personalnummer {} with exception: ", personalnummer, e);
        }
    }

    private boolean doesDirectoryExist(ShareDirectoryClient parentDirectoryClient, String directoryName) {
        for (ShareFileItem item : parentDirectoryClient.listFilesAndDirectories()) {
            if (item.isDirectory() && item.getName().equals(directoryName)) {
                return true;
            }
        }
        return false;
    }

    private void deleteDirectoryRecursively(ShareDirectoryClient directoryClient) {
        for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
            if (item.isDirectory()) {
                deleteDirectoryRecursively(directoryClient.getSubdirectoryClient(item.getName()));
            } else {
                directoryClient.deleteFile(item.getName());
            }
        }
        directoryClient.delete();
        log.info("Deleted directory '{}'", directoryClient.getDirectoryPath());
    }

    private ShareDirectoryClient ensureSubdirectoryExists(ShareDirectoryClient parentDirectoryClient, String subdirectoryName) {
        ShareDirectoryClient subdirectoryClient = parentDirectoryClient.getSubdirectoryClient(subdirectoryName);
        if (!subdirectoryClient.exists()) {
            subdirectoryClient.create();
            log.info("Subdirectory '{}' was created under '{}'", subdirectoryName, parentDirectoryClient.getDirectoryPath());
        }
        return subdirectoryClient;
    }

    public void renameFilesInDirectoryRecursively(String personalnummer) {
        try {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
            String firma = stammdaten.getPersonalnummer().getFirma().getName();
            String vorname = stammdaten.getVorname();
            String nachname = stammdaten.getNachname();

            ShareClient tempShareClient = shareServiceClient.getShareClient(getFileShareTemp());
            ShareDirectoryClient personalnummerDirectory = tempShareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(firma)
                    .getSubdirectoryClient(personalnummer);

            if (!personalnummerDirectory.exists()) {
                log.error("Directory for personalnummer '{}' does not exist in firma '{}'.", personalnummer, firma);
                return;
            }

            renameFilesInSubdirectories(personalnummerDirectory, vorname, nachname);

            log.info("Successfully renamed all files in directory for personalnummer '{}'.", personalnummer);
        } catch (Exception e) {
            log.error("Error renaming files in directory for personalnummer '{}' exception: ", personalnummer, e);
        }
    }

    private void renameFilesInSubdirectories(ShareDirectoryClient directory, String vorname, String nachname) {
        for (ShareFileItem item : directory.listFilesAndDirectories()) {
            if (item.isDirectory()) {
                ShareDirectoryClient subdirectory = directory.getSubdirectoryClient(item.getName());
                renameFilesInSubdirectories(subdirectory, vorname, nachname);
            } else {
                ShareFileClient sourceFile = directory.getFileClient(item.getName());
                String updatedFileName = Helpers.updateFileName(item.getName(), vorname, nachname);

                ShareFileClient targetFile = directory.getFileClient(updatedFileName);
                if (!updatedFileName.equals(item.getName())) {
                    try {
                        long fileSize = sourceFile.getProperties().getContentLength();
                        targetFile.create(fileSize);
                        targetFile.uploadRange(sourceFile.openInputStream(), fileSize);
                        sourceFile.delete();

                        log.info("Renamed file '{}' to '{}'.", item.getName(), updatedFileName);
                    } catch (Exception ex) {
                        log.error("Error renaming file '{}' in directory '{}': exception: ", item.getName(), directory.getDirectoryPath(), ex);
                    }
                }
            }
        }
    }

    public void renamePersonalnummerDirectory(String personalnummer) {
        try {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
            String firma = stammdaten.getPersonalnummer().getFirma().getName();
            String vorname = stammdaten.getVorname();
            String nachname = stammdaten.getNachname();

            ShareClient tempShareClient = shareServiceClient.getShareClient(getFileShareTemp());
            ShareDirectoryClient firmaDirectory = tempShareClient.getRootDirectoryClient().getSubdirectoryClient(firma);

            if (!firmaDirectory.exists()) {
                log.error("Firma directory '{}' does not exist in temp storage.", firma);
                return;
            }

            ShareDirectoryClient personalnummerDirectory = firmaDirectory.getSubdirectoryClient(personalnummer);
            if (!personalnummerDirectory.exists()) {
                log.error("Directory for personalnummer '{}' does not exist in firma '{}'.", personalnummer, firma);
                return;
            }

            String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
            personalnummerDirectory.setMetadata(new HashMap<>());
            log.info("Existing metadata: {}", personalnummerDirectory.getProperties().getMetadata());
            personalnummerDirectory.rename(firmaDirectory.getDirectoryPath() + "/" + updatedDirectoryName);

            log.info("Successfully renamed personalnummer directory '{}' to '{}'.", personalnummer, updatedDirectoryName);
        } catch (ShareStorageException e) {
            log.error("Rename failed: status={}, code={}, message={}, rawBody={}",
                    e.getStatusCode(), e.getErrorCode(), e.getMessage(), e.getResponse().getBodyAsString().block());
        } catch (Exception e) {
            log.error("Error renaming personalnummer directory '{}': exception", personalnummer, e);
        }
    }

    @Override
    public String getVereinbarungenDirectory(String personalnummer, Stammdaten stammdaten) {
        String firma = stammdaten.getPersonalnummer().getFirma().getName();
        String vorname = stammdaten.getVorname();
        String nachname = stammdaten.getNachname();
        String mitarbeiterDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);
        ShareClient personalunterlagenShareClient = shareServiceClient.getShareClient(getFileSharePersonalunterlagen());
        ShareDirectoryClient personalUnterlagenDirectory = personalunterlagenShareClient.getRootDirectoryClient()
                .getSubdirectoryClient(firma)
                .getSubdirectoryClient(mitarbeiterDirectoryName);
        ShareDirectoryClient vereinbarungenDirectory = personalUnterlagenDirectory.getSubdirectoryClient(VEREINBARUNGEN);
        return vereinbarungenDirectory.getDirectoryPath();
    }

    public void moveSignedDocuments(String personalnummer) {
        try {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
            String firma = stammdaten.getPersonalnummer().getFirma().getName();
            String vorname = stammdaten.getVorname();
            String nachname = stammdaten.getNachname();

            String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, vorname, nachname);

            ShareClient tempShareClient = shareServiceClient.getShareClient(getFileShareTemp());
            ShareClient personalunterlagenShareClient = shareServiceClient.getShareClient(getFileSharePersonalunterlagen());

            ShareDirectoryClient sourceDirectory = tempShareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(firma)
                    .getSubdirectoryClient(personalnummer);

            if (sourceDirectory == null || !sourceDirectory.exists()) {
                log.error("Source directory '{}' does not exist.", updatedDirectoryName);
                return;
            }

            createDirectoryIfNotExists(personalunterlagenShareClient, firma + "/" + updatedDirectoryName);

            ShareDirectoryClient targetDirectory = personalunterlagenShareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(firma)
                    .getSubdirectoryClient(updatedDirectoryName);

            if (targetDirectory != null && !targetDirectory.exists()) {
                targetDirectory.create();
                log.info("Created target directory '{}'.", targetDirectory.getDirectoryPath());
            }

            processDirectoryContents(sourceDirectory, targetDirectory);

            deleteDirectoryContents(sourceDirectory);

            sourceDirectory.delete();
            log.info("Successfully processed documents for personalnummer '{}' from temp to personalunterlagen.", personalnummer);
        } catch (Exception ex) {
            log.error("Error during document processing for personalnummer '{}': exception", personalnummer, ex);
        }
    }

    private void createDirectoryIfNotExists(ShareClient directoryClient, String targetPath) {
        String[] pathSegments = targetPath.split("/");

        ShareDirectoryClient currentDirectory = directoryClient.getRootDirectoryClient();

        for (String segment : pathSegments) {
            // Get or create each directory in the path
            currentDirectory = currentDirectory.getSubdirectoryClient(segment);
            if (!currentDirectory.exists()) {
                log.info("Directory '{}' does not exist, it will be created.", currentDirectory.getDirectoryPath());
                currentDirectory.create(); // Create the directory if it doesn't exist
            }
        }
    }

    private void processDirectoryContents(ShareDirectoryClient sourceDirectory, ShareDirectoryClient targetDirectory) {
        for (ShareFileItem item : sourceDirectory.listFilesAndDirectories()) {
            if (item.isDirectory()) {
                ShareDirectoryClient sourceSubDir = sourceDirectory.getSubdirectoryClient(item.getName());
                ShareDirectoryClient targetSubDir = targetDirectory.getSubdirectoryClient(item.getName());

                if (!targetSubDir.exists()) {
                    targetSubDir.create();
                }

                if (sourceSubDir.getDirectoryPath().contains(DIENSTVERTRAG)) {
                    deleteDirectoryRecursively(sourceSubDir);
                    log.info("Deleted DIENSTVERTRAEGE directory '{}'.", sourceSubDir.getDirectoryPath());
                } else {
                    processDirectoryContents(sourceSubDir, targetSubDir);
                }
            } else {
                ShareFileClient sourceFile = sourceDirectory.getFileClient(item.getName());
                ShareFileClient targetFile = targetDirectory.getFileClient(item.getName());

                long fileSize = sourceFile.getProperties().getContentLength();
                targetFile.create(fileSize);
                targetFile.uploadRange(sourceFile.openInputStream(), fileSize);
                sourceFile.delete();

                log.info("Moved file '{}' to target directory '{}'.", item.getName(), targetDirectory.getDirectoryPath());
            }
        }
    }

    @Override
    public void deletePersonalnummerDirectory(String shareName, String firma, String personalnummer) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            if (!shareClient.exists()) {
                log.error("Share '{}' does not exist.", shareName);
                return;
            }

            ShareDirectoryClient firmaDirectory = shareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(firma);
            if (!firmaDirectory.exists()) {
                log.error("Firma directory '{}' does not exist.", firma);
                return;
            }

            String updatedDirectoryName = Helpers.updateSubdirectoryName(personalnummer, "", "");
            deleteDirectoryBasedOnType(firmaDirectory.getSubdirectoryClient(personalnummer), DIENSTVERTRAG);
            deleteDirectoryBasedOnType(firmaDirectory.getSubdirectoryClient(updatedDirectoryName), DIENSTVERTRAG);

            log.info("Deleted directories for personalnummer '{}' under firma '{}'.", personalnummer, firma);
        } catch (Exception e) {
            log.error("Error while deleting directories for personalnummer '{}' under firma '{}': exception: ", personalnummer, firma, e);
        }
    }

    private void deleteDirectoryBasedOnType(ShareDirectoryClient directoryClient, String typeToDelete) {
        if (directoryClient.exists()) {
            for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
                if (item.isDirectory()) {
                    ShareDirectoryClient subDirectory = directoryClient.getSubdirectoryClient(item.getName());

                    if (subDirectory.getDirectoryPath().contains(typeToDelete)) {
                        deleteDirectoryRecursively(subDirectory);
                        log.info("Deleted {} directory '{}'.", typeToDelete, subDirectory.getDirectoryPath());
                    } else {
                        deleteDirectoryContents(subDirectory);
                        subDirectory.delete();
                    }
                } else {
                    ShareFileClient file = directoryClient.getFileClient(item.getName());
                    file.delete();
                    log.info("Deleted file '{}' in directory '{}'.", item.getName(), directoryClient.getDirectoryPath());
                }
            }

            directoryClient.delete();
            log.info("Deleted directory '{}'.", directoryClient.getDirectoryPath());
        } else {
            log.warn("Directory '{}' does not exist and cannot be deleted.", directoryClient.getDirectoryPath());
        }
    }

    private void deleteDirectoryContents(ShareDirectoryClient directoryClient) {
        for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
            try {
                if (item.isDirectory()) {
                    ShareDirectoryClient subDir = directoryClient.getSubdirectoryClient(item.getName());
                    deleteDirectoryContents(subDir); // Recursive cleanup
                    subDir.delete(); // Delete the empty subdirectory
                    log.info("Deleted subdirectory '{}'.", subDir.getDirectoryPath());
                } else {
                    ShareFileClient fileClient = directoryClient.getFileClient(item.getName());
                    fileClient.delete(); // Delete individual file
                    log.info("Deleted file '{}' from directory '{}'.", item.getName(), directoryClient.getDirectoryPath());
                }
            } catch (Exception e) {
                log.error("Error deleting item '{}' in directory '{}': exception: ", item.getName(), directoryClient.getDirectoryPath(), e);
            }
        }
    }

    @Override
    public boolean fileExists(String shareName, String directoryPath) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            ShareDirectoryClient directoryClient = shareClient.getRootDirectoryClient().getSubdirectoryClient(directoryPath);
            return directoryClient.exists();
        } catch (Exception e) {
            log.error("Error checking directory existence for fileshare {} and directory path {} with exception: ", shareName, directoryPath, e);
            return false;
        }
    }

    @Override
    public String getFullFileName(String shareName, String firma, String personalnummer, String directoryPath, String fileNamePrefix, String fileNameMiddle) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            ShareDirectoryClient directoryClient = shareClient.getRootDirectoryClient()
                    .getSubdirectoryClient(directoryPath);

            if (!directoryClient.exists()) {
                log.error("Directory '{}' does not exist in share '{}'.", directoryPath, shareName);
                return null;
            }

            for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
                if (!isNullOrBlank(fileNamePrefix) && !item.isDirectory() && item.getName().startsWith(fileNamePrefix)) {
                    log.info("Full file name found: '{}'.", item.getName());
                    return item.getName();
                }
                if (!isNullOrBlank(fileNameMiddle) && !item.isDirectory() && item.getName().contains(fileNameMiddle)) {
                    log.info("Full file name found: '{}'.", item.getName());
                    return item.getName();
                }
            }
            log.warn("No file found with prefix '{}' in directory '{}'.", fileNamePrefix, directoryPath);
        } catch (Exception e) {
            log.error("Error while retrieving file name with prefix '{}': exception: ", fileNamePrefix, e);
        }
        return null;
    }

    @Override
    public TreeNode getDirectoryStructure(String shareName, String rootDirectoryPath) {
        try {
            ShareClient shareClient = shareServiceClient.getShareClient(shareName);
            if (!shareClient.exists()) {
                String errorMessage = "Personalunterlagen existieren nicht";
                log.error("Share '{}' does not exist.", shareName);
                TreeNode node = new TreeNode();
                node.setErrorMessage(errorMessage);
                return node;
            }

            // This should help with path segments containing spaces or dots (e.g., in employee names)
            // to be correctly encoded, preventing issues where exists() would incorrectly return false
            ShareDirectoryClient currentDirectory = shareClient.getRootDirectoryClient();
            for (String segment : rootDirectoryPath.split("/")) {
                if (segment != null && !segment.isEmpty()) {
                    currentDirectory = currentDirectory.getSubdirectoryClient(segment);
                }
            }
            ShareDirectoryClient rootDirectoryClient = currentDirectory;

            if (!rootDirectoryClient.exists()) {
                String errorMessage = "Personalunterlagen existieren nicht";
                log.error("Root directory '{}' does not exist.", rootDirectoryPath);
                TreeNode node = new TreeNode();
                node.setErrorMessage(errorMessage);
                return node;
            }
            return buildTreeNode(rootDirectoryClient);
        } catch (Exception e) {
            String errorMessage = "Fehler beim Laden der Personalunterlagen";
            log.error("Error while fetching directory structure for share '{}' and directory '{}': ", shareName, rootDirectoryPath, e);
            TreeNode node = new TreeNode();
            node.setErrorMessage(errorMessage);
            return node;
        }
    }

    private TreeNode buildTreeNode(ShareDirectoryClient directoryClient) {
        TreeNode node = new TreeNode();
        node.setId(directoryClient.getDirectoryPath());
        node.setPath(directoryClient.getDirectoryPath());
        node.setTitle(extractName(directoryClient.getDirectoryPath()));
        node.setCreatedAt(directoryClient.getProperties().getLastModified().toString());

        // Always initialize the content list for folders
        List<TreeNode> content = new ArrayList<>();
        node.setContent(new ArrayList<>());

        for (ShareFileItem item : directoryClient.listFilesAndDirectories()) {
            if (item.isDirectory()) {
                ShareDirectoryClient subDirectory = directoryClient.getSubdirectoryClient(item.getName());
                content.add(buildTreeNode(subDirectory)); // RECURSIVE CALL
            } else {
                content.add(buildFileNode(item, directoryClient)); // Add file to folder
            }
        }

        // Set the content to an empty list if no files or directories were found
        node.setContent(content);
        node.setMimeType(null);
        return node;
    }

    private TreeNode buildFileNode(ShareFileItem fileItem, ShareDirectoryClient parentDirectory) {
        FileItem fileNode = new FileItem();
        fileNode.setId(fileItem.getId());
        fileNode.setTitle(fileItem.getName());
        fileNode.setPath(parentDirectory.getDirectoryPath());
        fileNode.setCreatedAt(parentDirectory.getFileClient(fileItem.getName()).getProperties().getLastModified().toString());
        fileNode.setMimeType(deriveMimeType(fileItem, parentDirectory));
        return fileNode;
    }

    private String deriveMimeType(ShareFileItem fileItem, ShareDirectoryClient parentDirectory) {
        try {
            ShareFileClient fileClient = parentDirectory.getFileClient(fileItem.getName());
            String contentType = fileClient.getProperties().getContentType(); // Fetch from Azure SDK metadata
            if (!isNullOrBlank(contentType) && !contentType.equals("application/octet-stream")) {
                return contentType;
            }
        } catch (Exception e) {
            log.warn("Could not fetch MIME type for file '{}'. Falling back to file extension.", fileItem.getName());
        }

        //FALLBACK in case the above doesnt work
        return getMimeTypeFromExtension(fileItem.getName());
    }


    private String getMimeTypeFromExtension(String fileName) {
        String extension = fileName.lastIndexOf('.') != -1 ? fileName.substring(fileName.lastIndexOf('.')) : "";
        return getMimeTypeForExtension(extension.toLowerCase());
    }

    private String extractName(String path) {
        // Extract the name by splitting on "/"
        if (path == null || path.isEmpty()) {
            return path;
        }
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    @Override
    public void renameAndMoveSignedDocumentsAndDirectories(String personalnummer) {
        renameFilesInDirectoryRecursively(personalnummer);
        //renamePersonalnummerDirectory(personalnummer);
        moveSignedDocuments(personalnummer);
    }

    @Override
    public List<File> downloadFilesToTemp(String remoteDirectory, String shareName) {
        List<File> downloadedFiles = new ArrayList<>();

        ShareClient shareClient = this.shareServiceClient.getShareClient(shareName);
        if (!shareClient.exists()) {
            log.error("Share {} does not exist!", shareName);
            return downloadedFiles;
        }

        ShareDirectoryClient directoryClient = shareClient.getDirectoryClient(remoteDirectory);
        Path tempDir;

        try {
            tempDir = Files.createTempDirectory("azure_download_");
        } catch (IOException e) {
            log.error("Failed to create temp directory for downloads", e);
            return downloadedFiles;
        }

        directoryClient.listFilesAndDirectories().forEach(fileItem -> {
            if (!fileItem.isDirectory()) {
                ShareFileClient fileClient = directoryClient.getFileClient(fileItem.getName());
                Path tempFilePath = tempDir.resolve(fileItem.getName());
                File tempFile = tempFilePath.toFile();

                try {
                    Files.createDirectories(tempFilePath.getParent());
                    try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                        fileClient.download(outputStream);
                    }
                    downloadedFiles.add(tempFile);
                } catch (IOException e) {
                    log.error("Failed to download file {} from Azure Fileshare: {}", fileItem.getName(), e.getMessage(), e);
                }
            }
        });

        return downloadedFiles;
    }
}
