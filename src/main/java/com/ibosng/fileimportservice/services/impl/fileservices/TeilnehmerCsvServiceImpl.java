package com.ibosng.fileimportservice.services.impl.fileservices;

import com.ibosng.dbservice.dtos.TeilnehmerCsvDto;
import com.ibosng.dbservice.entities.FileType;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.HeaderService;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import com.ibosng.fileimportservice.exceptions.CsvParserException;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import com.ibosng.fileimportservice.services.fileservices.TeilnehmerCsvService;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.ibosng.fileimportservice.utils.Constants.FILE_IMPORT_SERVICE;
import static com.ibosng.fileimportservice.utils.fileparsers.HeadersValidator.csvHeadersValidator;

@Service
@RequiredArgsConstructor
public class TeilnehmerCsvServiceImpl implements TeilnehmerCsvService {
    private final HeaderService headerService;
    private final TeilnehmerStagingService teilnehmerStagingService;
    private static final Logger log = LoggerFactory.getLogger(TeilnehmerCsvServiceImpl.class);


    @Override
    public boolean isCsvTeilnehmerFile(FileDetails fileDetails) {
        try {
            String[] headers = readHeaders(fileDetails.getFile());
            csvHeadersValidator(headers,
                    headerService.getActiveHeadersNamesByFileType(FileType.TEILNEHMER_CSV),
                    headerService.getInactiveHeadersNamesByFileType(FileType.TEILNEHMER_CSV));
            return true;
        } catch (FieldTypeException | CsvParserException ex) {
            return false;
        }
    }

    @Override
    public List<TeilnehmerStaging> parseCsvTeilnehmerFileAndValidateHeaders(FileDetails fileDetails) throws CsvParserException {
        File file = fileDetails.getFile();
        String[] headers = readHeaders(file);
        if (headers == null || headers.length == 0) {
            log.warn("CSV file is empty");
            throw new CsvParserException("CSV file is empty");
        }
        try {
            csvHeadersValidator(headers,
                    headerService.getActiveHeadersNamesByFileType(FileType.TEILNEHMER_CSV),
                    headerService.getInactiveHeadersNamesByFileType(FileType.TEILNEHMER_CSV));
        } catch (FieldTypeException ex) {
            log.warn("Invalid header in csv file: {}", ex.getMessage());
            throw new CsvParserException("Exception while parsing headers: ", ex);
        }

        List<TeilnehmerStaging> teilnehmerStagingList = readFile(fileDetails).stream().map(dto -> getTeilnehmerStaging(dto, fileDetails.getFilename())).toList();
        return teilnehmerStagingService.saveAll(teilnehmerStagingList);
    }

    @Override
    public List<TeilnehmerCsvDto> readFile(FileDetails fileDetails) {
        File csvFile = fileDetails.getFile();
        try (Reader reader = new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8)) {

            CsvToBean<TeilnehmerCsvDto> csvToBean = new CsvToBeanBuilder<TeilnehmerCsvDto>(reader)
                    .withType(TeilnehmerCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false)
                    .withSeparator(';')
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            log.error("Error during CSV parsing: ", e);
            return List.of();
        }
    }


    @Override
    public TeilnehmerStaging getTeilnehmerStaging(TeilnehmerCsvDto teilnehmerCsvDto, String fileName) {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        if (teilnehmerCsvDto.getId() !=null) {
            teilnehmerStaging.setTeilnehmerId(teilnehmerCsvDto.getId());
        }
        teilnehmerStaging.setNachname(teilnehmerCsvDto.getNachname());
        teilnehmerStaging.setVorname(teilnehmerCsvDto.getVorname());
        if (teilnehmerCsvDto.getSvnr() != null) {
            teilnehmerStaging.setSvNummer(String.valueOf(teilnehmerCsvDto.getSvnr()));
        }
        teilnehmerStaging.setGeburtsdatum(teilnehmerCsvDto.getGerburtsdatum());
        teilnehmerStaging.setUrsprungsland(teilnehmerCsvDto.getUrsprungsland());
        teilnehmerStaging.setGerburtsort(teilnehmerCsvDto.getGerburtsort());
        teilnehmerStaging.setErlaeuterungZiel(teilnehmerCsvDto.getZiel());
        teilnehmerStaging.setVermittelbarAb(teilnehmerCsvDto.getVermittelbarAb());
        teilnehmerStaging.setNotiz(teilnehmerCsvDto.getNotiz());
        teilnehmerStaging.setImportFilename(fileName);
        teilnehmerStaging.setSource(TeilnehmerSource.TEILNEHMER_CSV);
        teilnehmerStaging.setCreatedBy(FILE_IMPORT_SERVICE);
        return teilnehmerStaging;
    }

    public static String[] readHeaders(File file) throws CsvParserException {
        try (CSVReader csvReader = new CSVReaderBuilder(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            return csvReader.readNext();
        } catch (IOException | CsvValidationException e) {
            log.error("Exception while parsing file: {}", e.getMessage());
            throw new CsvParserException("Exception while parsing file: ", e);
        }
    }

}
