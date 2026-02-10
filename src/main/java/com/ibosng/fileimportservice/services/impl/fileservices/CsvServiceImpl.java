package com.ibosng.fileimportservice.services.impl.fileservices;

import com.ibosng.dbservice.entities.FileType;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.HeaderService;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.fileimportservice.dtos.ParticipantCsvDto;
import com.ibosng.fileimportservice.exceptions.CsvParserException;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.fileimportservice.services.fileservices.CsvService;
import com.ibosng.fileimportservice.services.fileservices.ExcelFileParserHelper;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ibosng.fileimportservice.utils.Constants.CSV_EXTENSION;
import static com.ibosng.fileimportservice.utils.Constants.EXCEL_EXTENSION;
import static com.ibosng.fileimportservice.utils.Constants.FILE_IMPORT_SERVICE;
import static com.ibosng.fileimportservice.utils.fileparsers.CsvFileParserHelper.createFileReader;
import static com.ibosng.fileimportservice.utils.fileparsers.CsvFileParserHelper.mapDataRows;
import static com.ibosng.fileimportservice.utils.fileparsers.CsvFileParserHelper.mapParticipantDTOS;
import static com.ibosng.fileimportservice.utils.fileparsers.CsvFileParserHelper.parseFile;
import static com.ibosng.fileimportservice.utils.fileparsers.HeadersValidator.csvHeadersValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService {

    private final TeilnehmerStagingServiceImpl teilnehmerStagingService;
    private final HeaderService headerService;
    private final ExcelFileParserHelper excelFileParserHelper;

    @Getter
    @Setter
    private TeilnehmerSource source;

    @Override
    public void readFile(FileDetails file) throws ParserException {
        try {
            List<ParticipantCsvDto> participants = parseAnyFileToParticipantDTOS(file.getFile());
            List<TeilnehmerStaging> teilnehmerStagingList = new ArrayList<>();

            for (ParticipantCsvDto participant : participants) {
                teilnehmerStagingList.add(saveParticipant(participant, file.getFilename()));
            }
            if (teilnehmerStagingList.isEmpty()) {
                log.warn("Malformed file : {}", file.getFilename());
                throw new ParserException(String.format("Malformed file : %s", file.getFilename()));
            }
            teilnehmerStagingService.saveAll(teilnehmerStagingList);
        } catch (FieldTypeException e) {
            log.error("Error while reading csv file: " + e.getMessage());
            throw new ParserException("Error while reading csv file: ", e);
        }
    }


    private TeilnehmerStaging saveParticipant(ParticipantCsvDto participant, String filename) {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        if (participant.getTitel() != null) {
            teilnehmerStaging.setTitel(participant.getTitel());
        }
        if (participant.getVorname() != null) {
            teilnehmerStaging.setVorname(participant.getVorname());
        }
        if (participant.getNachname() != null) {
            teilnehmerStaging.setNachname(participant.getNachname());
        }
        if (participant.getGeschlecht() != null) {
            teilnehmerStaging.setGeschlecht(participant.getGeschlecht());
        }
        if (participant.getSvNummer() != null) {
            teilnehmerStaging.setSvNummer(participant.getSvNummer());
        }
        if (participant.getPlz() != null) {
            teilnehmerStaging.setPlz(participant.getPlz());
        }
        if (participant.getStrasse() != null) {
            teilnehmerStaging.setStrasse(participant.getStrasse());
        }
        if (participant.getGeburtsdatum() != null) {
            teilnehmerStaging.setGeburtsdatum(participant.getGeburtsdatum());
        }
        if (participant.getOrt() != null) {
            teilnehmerStaging.setOrt(participant.getOrt());
        }
        if (participant.getLandesvorwahl() != null) {
            teilnehmerStaging.setLandesvorwahl(participant.getLandesvorwahl());
        }
        if (participant.getVorwahl() != null) {
            teilnehmerStaging.setVorwahl(participant.getVorwahl());
        }
        if (participant.getTelefonnummer() != null) {
            teilnehmerStaging.setTelefonNummer(participant.getTelefonnummer());
        }
        if (participant.getBuchungsstatus() != null) {
            teilnehmerStaging.setBuchungsstatus(participant.getBuchungsstatus());
        }
        if (participant.getAnmerkung() != null) {
            teilnehmerStaging.setAnmerkung(participant.getAnmerkung());
        }
        if (participant.getZubuchung() != null) {
            teilnehmerStaging.setZubuchung(participant.getZubuchung());
        }
        if (participant.getGeplant() != null) {
            teilnehmerStaging.setGeplant(participant.getGeplant());
        }
        if (participant.getEintritt() != null) {
            teilnehmerStaging.setEintritt(participant.getEintritt());
        }
        if (participant.getAustritt() != null) {
            teilnehmerStaging.setAustritt(participant.getAustritt());
        }
        if (participant.getRgs() != null) {
            teilnehmerStaging.setRgs(participant.getRgs());
        }
        if (participant.getTitelBetreuer() != null) {
            teilnehmerStaging.setBetreuerTitel(participant.getTitelBetreuer());
        }
        if (participant.getVornameBetreuer() != null) {
            teilnehmerStaging.setBetreuerVorname(participant.getVornameBetreuer());
        }
        if (participant.getNachnameBetreuer() != null) {
            teilnehmerStaging.setBetreuerNachname(participant.getNachnameBetreuer());
        }
        if (participant.getMassnahmennummer() != null) {
            teilnehmerStaging.setMassnahmennummer(participant.getMassnahmennummer());
        }
        if (participant.getVeranstaltungsnummer() != null) {
            teilnehmerStaging.setVeranstaltungsnummer(participant.getVeranstaltungsnummer());
        }
        if (participant.getEmail() != null) {
            teilnehmerStaging.setEmail(participant.getEmail());
        }
        teilnehmerStaging.setImportFilename(filename);
        teilnehmerStaging.setSource(getSource());
        teilnehmerStaging.setCreatedBy(FILE_IMPORT_SERVICE);

        return teilnehmerStaging;
    }

    private List<String[]> parseCsvFileAndValidateHeaders(File file) throws CsvParserException {
        Reader fileReader = createFileReader(file);
        List<String[]> parsedFile = parseFile(fileReader);
        if (parsedFile.isEmpty()) {
            log.warn("CSV file is empty");
            throw new CsvParserException("CSV file is empty");
        }
        try {
            csvHeadersValidator(parsedFile.get(0),
                    headerService.getActiveHeadersNamesByFileType(FileType.EAMS),
                    headerService.getInactiveHeadersNamesByFileType(FileType.EAMS));
        } catch (FieldTypeException ex) {
            log.warn("Invalid header in csv file: {}", ex.getMessage());
            throw new CsvParserException("Exception while parsing headers: ", ex);
        }

        return parsedFile;
    }

    public List<ParticipantCsvDto> parseAnyFileToParticipantDTOS(File file) throws FieldTypeException, ParserException {
        if (FilenameUtils.getExtension(file.getName()).equals(CSV_EXTENSION)) {
            List<String[]> parsedFile = parseCsvFileAndValidateHeaders(file);
            return mapDataRows(parsedFile);
        }
        if (FilenameUtils.getExtension(file.getName()).equals(EXCEL_EXTENSION)) {
            List<Map<String, Object>> dataRows = excelFileParserHelper.parseExcelFileAndValidateHeaders(file, FileType.EAMS);
            return mapParticipantDTOS(dataRows);
        }
        throw new ParserException("Wrong eAMS exctention for %s-file".formatted(file.getName()));
    }

}
