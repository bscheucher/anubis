package com.ibosng.fileimportservice.services.fileservices;

import com.ibosng.dbservice.dtos.TeilnehmerCsvDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.fileimportservice.exceptions.CsvParserException;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;

import java.util.List;

public interface TeilnehmerCsvService {

    boolean isCsvTeilnehmerFile(FileDetails file);

    List<TeilnehmerStaging> parseCsvTeilnehmerFileAndValidateHeaders(FileDetails file) throws CsvParserException;

    List<TeilnehmerCsvDto> readFile(FileDetails csvFile);

    TeilnehmerStaging getTeilnehmerStaging(TeilnehmerCsvDto teilnehmerCsvDto, String fileName);
}
