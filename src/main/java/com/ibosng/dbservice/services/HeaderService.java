package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.FileImportHeaders;
import com.ibosng.dbservice.entities.FileType;

import java.util.List;

public interface HeaderService extends BaseService<FileImportHeaders> {

    List<String> getActiveHeadersNamesByFileType(FileType fileType);

    List<String> getInactiveHeadersNamesByFileType(FileType fileType);

    FileImportHeaders findByFileType(FileType fileType);
}
