package com.ibosng.microsoftgraphservice.services;

import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.requests.DriveItemCollectionPage;

import java.io.File;
import java.util.Optional;

public interface SharePointService {


    DriveItem uploadFile(String filePath, String fileName, String folderPath) throws MSGraphServiceException;

    File downloadFile(String fileId, String filename) throws MSGraphServiceException;

    File createJsonFileFromDto(Object dto, String filename) throws MSGraphServiceException;

    DriveItemCollectionPage getUploadedFiles(String folder);

    DriveItem moveFile(String fileId, String filename, String targetFolder);

    DriveItem moveFile(FileDetails fileDetails, String targetFolder);

    String getFolderIdByPath(String folderPath);

    DriveItem createFolder(String folderPath, String folderName) throws MSGraphServiceException;

    DriveItemCollectionPage getContentsOfFolder(Optional<String> folderPath);

    DriveItem getItemByPath(String fullPath);
}
