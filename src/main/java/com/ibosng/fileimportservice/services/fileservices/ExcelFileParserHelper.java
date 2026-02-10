package com.ibosng.fileimportservice.services.fileservices;

import com.ibosng.dbservice.entities.FileType;
import com.ibosng.fileimportservice.exceptions.ParserException;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ExcelFileParserHelper {
    List<Map<String, Object>> parseExcelFileAndValidateHeaders(File file, FileType fileType) throws ParserException;
}
