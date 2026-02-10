package com.ibosng.fileimportservice.services;

import com.microsoft.graph.requests.DriveItemCollectionPage;

public interface FileParserService {
    void manageFiles(DriveItemCollectionPage items);
}
