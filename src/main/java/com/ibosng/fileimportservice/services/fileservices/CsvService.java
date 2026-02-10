package com.ibosng.fileimportservice.services.fileservices;

import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;

public interface CsvService {
    void readFile(FileDetails file) throws ParserException;

}
