package com.ibosng.microsoftgraphservice.services;

import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.microsoftgraphservice.dtos.TreeNode;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface FileShareService {
    void uploadOrReplaceInFileShare(String shareName, String directoryPath, String fileName, InputStream data, long length);

    InputStream downloadFromFileShare(String shareName, String directoryPath, String fileNamePrefix);

    InputStream downloadFileById(String shareName, String fileId);
    String getVereinbarungenDirectory(String personalnummer, Stammdaten stammdaten);

    void emptyFolderFromFileShare(String shareName, String directoryPath);

    void deleteFromFileShare(String shareName, String personalnummer, String directoryPath, String fileName);

    void cleanFileshare(String fileshare);

    void createStructureForNewMA(String personalnummer, String mainDirectory, String firma);

    void moveSignedDocuments(String personalnummer);

    void renamePersonalnummerDirectory(String personalnummer);

    void renameFilesInDirectoryRecursively(String personalnummer);

    void deletePersonalnummerDirectory(String shareName, String firma, String personalnummer);

    boolean fileExists(String shareName, String directoryPath);

    String getFullFileName(String shareName, String firma, String personalnummer, String directoryPath, String fileNamePrefix, String fileNameMiddle);

    void deleteFromFileShareContainingFilename(String shareName, String personalnummer, String directoryPath, String fileName);

    /**
     * Downloads all files and folders from the specified remote directory in an Azure File Share
     * to a specified local directory.
     *
     * @param remoteDirectory the path to the remote directory in the Azure File Share.
     *                        This can include nested paths (e.g., "folder/subfolder").
     * @param localDirectory  the path to the local directory where the files and folders will be downloaded.
     *                        If the directory does not exist, it will be created.
     * @param shareName       the name of the Azure File Share from which the files are to be downloaded.
     */
    void downloadFiles(String remoteDirectory, Path localDirectory, String shareName);

    TreeNode getDirectoryStructure(String shareName, String rootDirectoryPath);

    void renameAndMoveSignedDocumentsAndDirectories(String personalnummer);

    List<File> downloadFilesToTemp(String remoteDirectory, String shareName);
}
