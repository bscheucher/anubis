package com.ibosng.fileimportservice.services.fileservices;

import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;

public interface OEIFService {
    void readFile(FileDetails fileDetails) throws ParserException;
}
