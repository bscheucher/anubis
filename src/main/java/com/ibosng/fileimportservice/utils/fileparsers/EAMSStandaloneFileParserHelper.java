package com.ibosng.fileimportservice.utils.fileparsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.fileimportservice.dtos.ParticipantEAMSStandaloneDto;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import com.ibosng.fileimportservice.exceptions.CsvParserException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibosng.fileimportservice.utils.Helpers.*;

@Slf4j
@UtilityClass
public class EAMSStandaloneFileParserHelper {

    public static List<ParticipantEAMSStandaloneDto> mapDataRows(List<String[]> parsedFile) throws FieldTypeException, CsvParserException {
        List<Map<String, Object>> participantMaps = new ArrayList<>();
        try {
            for (int i = 1; i < parsedFile.size(); i++) {
                Map<String, Object> row = new HashMap<>();
                for (int j = 0; j < parsedFile.get(0).length; j++) {
                    row.put(parsedFile.get(0)[j].strip(), parsedFile.get(i)[j].strip());
                }
                participantMaps.add(row);
            }
            return mapParticipantDTOS(participantMaps);
        } catch (ArrayIndexOutOfBoundsException ex) {
            log.error("Exception while parsing file: " + ex.getMessage());
            throw new CsvParserException("Exception while parsing file: ", ex);
        }
    }

    public static List<ParticipantEAMSStandaloneDto> mapParticipantDTOS(List<Map<String, Object>> dataRows) throws FieldTypeException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<ParticipantEAMSStandaloneDto> participantEAMSStandaloneDtos = new ArrayList<>();
            for (Map<String, Object> row : dataRows) {
                ParticipantEAMSStandaloneDto participantCsvDto = objectMapper.convertValue(row, ParticipantEAMSStandaloneDto.class);
                if (!isNullOrBlank(participantCsvDto.getSvNummer())) {
                    participantCsvDto.setSvNummer(participantCsvDto.getSvNummer().replaceAll("\\s+", ""));
                }
                participantEAMSStandaloneDtos.add(participantCsvDto);
            }
            return participantEAMSStandaloneDtos;
        } catch (IllegalArgumentException ex) {
            log.warn("Cannot parse field in csv parser: {}", ex.getMessage());
            throw new FieldTypeException("Cannot parse field in csv parser", ex);
        }

    }

    public static List<String[]> parseFile(Reader fileReader) throws CsvParserException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();
        try (CSVReader csvReader = new CSVReaderBuilder(fileReader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build()) {
            return csvReader.readAll();
        } catch (IOException | CsvException e) {
            log.error("Exception while parsing file: " + e.getMessage());
            throw new CsvParserException("Exception while parsing file: ", e);
        }
    }

    public static BufferedReader createFileReader(File file) throws CsvParserException {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1));
        } catch (IOException e) {
            log.error("Exception while creating file reader: " + e.getMessage());
            throw new CsvParserException("Exception while creating file reader: ", e);
        }
    }
}
