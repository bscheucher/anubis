package com.ibosng.fileimportservice.services.impl.fileservices;

import com.ibosng.dbservice.entities.FileType;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.services.HeaderService;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.fileimportservice.dtos.ParticipantEAMSStandaloneDto;
import com.ibosng.fileimportservice.exceptions.CsvParserException;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.fileimportservice.services.fileservices.EAMSStandaloneService;
import com.ibosng.fileimportservice.services.fileservices.ExcelFileParserHelper;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import lombok.RequiredArgsConstructor;
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
import static com.ibosng.fileimportservice.utils.Helpers.isNullOrBlank;
import static com.ibosng.fileimportservice.utils.fileparsers.EAMSStandaloneFileParserHelper.*;
import static com.ibosng.fileimportservice.utils.fileparsers.HeadersValidator.csvHeadersValidator;

@Service
@Slf4j
@RequiredArgsConstructor
public class EAMSStandaloneServiceImpl implements EAMSStandaloneService {

    private static final String BEWILLIGT = "bewilligt";
    private static final String INTERESSIERT = "interessiert";
    private static final String EINGETRETEN = "eingetreten";

    private final TeilnehmerStagingServiceImpl teilnehmerStagingService;
    private final ExcelFileParserHelper excelFileParserHelper;
    private final HeaderService headerService;

    @Override
    public void readFile(FileDetails fileDetails) throws ParserException {
        List<ParticipantEAMSStandaloneDto> participants = new ArrayList<>();
        try {
            participants = parseEAMSStandaloneFileToParticipantDTOS(fileDetails.getFile());
        } catch (FieldTypeException e) {
            log.error("File for UEAB could not be read: {}", e.getMessage());
        }
        List<TeilnehmerStaging> teilnehmerStagingList = new ArrayList<>();

        for (ParticipantEAMSStandaloneDto participant : participants) {
            TeilnehmerStaging teilnehmerStaging = saveParticipant(participant, fileDetails.getFilename());
            if (teilnehmerStaging != null) {
                teilnehmerStagingList.add(teilnehmerStaging);
            }
        }
        if (teilnehmerStagingList.isEmpty()) {
            log.warn("Malformed file : {}", fileDetails.getFilename());
            throw new ParserException(String.format("Malformed file : %s", fileDetails.getFilename()));
        }
        teilnehmerStagingService.saveAll(teilnehmerStagingList);
    }

    private TeilnehmerStaging saveParticipant(ParticipantEAMSStandaloneDto participant, String fileName) {
        TeilnehmerStaging teilnehmerStaging = null;
        if (((participant.getBuchungsstatus().equalsIgnoreCase(BEWILLIGT) || participant.getBuchungsstatus().equalsIgnoreCase(INTERESSIERT)) && isNullOrBlank(participant.getEintritt())) || participant.getBuchungsstatus().equalsIgnoreCase(EINGETRETEN)) {
            teilnehmerStaging = new TeilnehmerStaging();
            teilnehmerStaging.setStatus(TeilnehmerStatus.NEW);
            teilnehmerStaging.setTitel(participant.getTitel());
            teilnehmerStaging.setVorname(participant.getVorname());
            teilnehmerStaging.setNachname(participant.getNachname());
            teilnehmerStaging.setGeschlecht(participant.getGeschlecht());
            teilnehmerStaging.setSvNummer(participant.getSvNummer());
            teilnehmerStaging.setPlz(participant.getPlz());
            teilnehmerStaging.setOrt(participant.getOrt());
            teilnehmerStaging.setGeburtsdatum(participant.getGeburtsdatum());
            teilnehmerStaging.setStrasse(participant.getStrasse());
            teilnehmerStaging.setLandesvorwahl(participant.getLandesvorwahl());
            teilnehmerStaging.setVorwahl(participant.getVorwahl());
            teilnehmerStaging.setTelefon(participant.getTelefonNr());
            teilnehmerStaging.setBuchungsstatus(participant.getBuchungsstatus());
            teilnehmerStaging.setAnmerkung(participant.getAnmerkung());
            teilnehmerStaging.setZubuchung(participant.getZubuchung());
            teilnehmerStaging.setGeplant(participant.getGeplant());
            teilnehmerStaging.setEintritt(participant.getEintritt());
            teilnehmerStaging.setAustritt(participant.getAustritt());
            teilnehmerStaging.setRgs(participant.getRgs());
            teilnehmerStaging.setBetreuerTitel(participant.getTitelBetreuer());
            teilnehmerStaging.setBetreuerNachname(participant.getNachnameBetreuer());
            teilnehmerStaging.setBetreuerVorname(participant.getVornameBetreuer());
            teilnehmerStaging.setMassnahmennummer(participant.getMassnahmennummer());
            teilnehmerStaging.setVeranstaltungsnummer(participant.getVeranstaltungsnummer());
            teilnehmerStaging.setEmail(participant.getEmailAdresse());
            teilnehmerStaging.setSeminarIdentifier(participant.getSeminarId());
            teilnehmerStaging.setImportFilename(fileName);
            teilnehmerStaging.setSource(TeilnehmerSource.EAMS_STANDALONE);
            teilnehmerStaging.setCreatedBy(FILE_IMPORT_SERVICE);
        }
        return teilnehmerStaging;
    }

    private List<String[]> parseEAMSStandaloneFileAndValidateHeaders(File file) throws CsvParserException {
        Reader fileReader = createFileReader(file);
        List<String[]> parsedFile = parseFile(fileReader);
        if (parsedFile.isEmpty()) {
            log.warn("CSV file is empty");
            throw new CsvParserException("CSV file is empty");
        }
        try {
            csvHeadersValidator(parsedFile.get(0),
                    headerService.getActiveHeadersNamesByFileType(FileType.EAMS_STANDALONE),
                    headerService.getInactiveHeadersNamesByFileType(FileType.EAMS_STANDALONE));
        } catch (FieldTypeException ex) {
            log.warn("Invalid header in csv file: {}", ex.getMessage());
            throw new CsvParserException("Exception while parsing headers: ", ex);
        }

        return parsedFile;
    }

    public List<ParticipantEAMSStandaloneDto> parseEAMSStandaloneFileToParticipantDTOS(File file) throws FieldTypeException, ParserException {
        if (FilenameUtils.getExtension(file.getName()).equals(CSV_EXTENSION)) {
            List<String[]> parsedFile = parseEAMSStandaloneFileAndValidateHeaders(file);
            return mapDataRows(parsedFile);
        }
        if (FilenameUtils.getExtension(file.getName()).equals(EXCEL_EXTENSION)) {
            List<Map<String, Object>> dataRows = excelFileParserHelper.parseExcelFileAndValidateHeaders(file, FileType.EAMS_STANDALONE);
            return mapParticipantDTOS(dataRows);
        }
        throw new ParserException("Wrong eAMS exctention for %s-file".formatted(file.getName()));
    }
}
