package com.ibosng.fileimportservice.services.impl.fileservices;

import com.ibosng.dbservice.entities.FileType;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.services.HeaderService;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.fileimportservice.dtos.ParticipantOEIFDto;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import com.ibosng.fileimportservice.exceptions.OEIFParserException;
import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.fileimportservice.services.fileservices.ExcelFileParserHelper;
import com.ibosng.fileimportservice.services.fileservices.OEIFService;
import com.ibosng.fileimportservice.utils.fileparsers.HeadersValidator;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibosng.fileimportservice.utils.Constants.FILE_IMPORT_SERVICE;
import static com.ibosng.fileimportservice.utils.fileparsers.OEIFFileParserHelper.mapParticipantDTOS;

@Service
@Slf4j
@RequiredArgsConstructor
public class OEIFServiceImpl implements OEIFService {

    private final TeilnehmerStagingServiceImpl teilnehmerStagingService;
    private final ExcelFileParserHelper excelFileParserHelper;

    public List<ParticipantOEIFDto> parseExcelFileToParticipantDTOS(File file) throws OEIFParserException {
        try {
            List<Map<String, Object>> dataRows = excelFileParserHelper.parseExcelFileAndValidateHeaders(file, FileType.OEIF);
            return mapParticipantDTOS(dataRows);
        } catch (ParserException | FieldTypeException ex) {
            log.warn("Cannot parse field in csv parser: {}", ex.getMessage());
            throw new OEIFParserException("Cannot parse field in csv parser", ex);
        }
    }

    @Override
    public void readFile(FileDetails fileDetails) throws ParserException {
        List<ParticipantOEIFDto> participants = parseExcelFileToParticipantDTOS(fileDetails.getFile());
        List<TeilnehmerStaging> teilnehmerStagingList = new ArrayList<>();

        for (ParticipantOEIFDto participant : participants) {
            if (countNullFields(participant) < 3) {
                teilnehmerStagingList.add(saveParticipant(participant, fileDetails.getFilename()));
            }
        }
        if (teilnehmerStagingList.isEmpty()) {
            log.warn("Malformed file : {}", fileDetails.getFilename());
            throw new ParserException(String.format("Malformed file : %s", fileDetails.getFilename()));
        }
        teilnehmerStagingService.saveAll(teilnehmerStagingList);
    }

    private TeilnehmerStaging saveParticipant(ParticipantOEIFDto participant, String fileName) {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStatus(TeilnehmerStatus.NEW);
        teilnehmerStaging.setSeminarIdentifier(participant.getKennung());
        teilnehmerStaging.setVorname(participant.getVorname());
        teilnehmerStaging.setNachname(participant.getNachname());
        teilnehmerStaging.setGeschlecht(participant.getGeschlecht());
        teilnehmerStaging.setGeburtsdatum(participant.getGeburtsdatum());
        teilnehmerStaging.setEintritt(participant.getTeilnahmebeginn());
        teilnehmerStaging.setTelefon(participant.getMobiltelefon());
        teilnehmerStaging.setRegistrationStatus(participant.getAnmeldestatus());
        teilnehmerStaging.setSeminarType(participant.getIndividuellesNiveau());
        teilnehmerStaging.setEmail(participant.getEmail());
        teilnehmerStaging.setSvNummer(participant.getSvn());
        teilnehmerStaging.setAms(participant.getAms());
        teilnehmerStaging.setReasonOfDeregistration(participant.getAbmeldegrund());
        teilnehmerStaging.setPresenceInUe(participant.getAnwesenheitInUE());
        teilnehmerStaging.setExcusedAbscenceFromUe(participant.getEntschuldigteAnwesenheitInUE());
        teilnehmerStaging.setUnexcusedAbscenceFromUe(participant.getUnentschuldigteAnwesenheitInUE());
        teilnehmerStaging.setTotalAbscence(participant.getSummeErfassteAnUndAbwesenheit());
        teilnehmerStaging.setAttendanceRecordedUntil(participant.getAnwesenheitErfasstBis());
        teilnehmerStaging.setAbsolutePresencePercentage(participant.getAbsoluteAnwesenheit());
        teilnehmerStaging.setRelativePresencePercentage(participant.getRelativeAnwesenheit());
        teilnehmerStaging.setSeminarIdentifier(participant.getSeminarId());
        teilnehmerStaging.setImportFilename(fileName);
        teilnehmerStaging.setSource(TeilnehmerSource.OEIF);
        teilnehmerStaging.setCreatedBy(FILE_IMPORT_SERVICE);

        return teilnehmerStaging;
    }

    private int countNullFields(ParticipantOEIFDto participant) {
        int nullCount = 0;

        if (participant.getKennung() == null) nullCount++;
        if (participant.getNachname() == null) nullCount++;
        if (participant.getVorname() == null) nullCount++;
        if (participant.getGeschlecht() == null) nullCount++;
        if (participant.getGeburtsdatum() == null) nullCount++;
        if (participant.getMobiltelefon() == null) nullCount++;
        if (participant.getTeilnahmebeginn() == null) nullCount++;
        if (participant.getAnmeldestatus() == null) nullCount++;
        if (participant.getAms() == null) nullCount++;
        if (participant.getAbmeldegrund() == null) nullCount++;
        if (participant.getAnwesenheitInUE() == null) nullCount++;
        if (participant.getEntschuldigteAnwesenheitInUE() == null) nullCount++;
        if (participant.getUnentschuldigteAnwesenheitInUE() == null) nullCount++;
        if (participant.getSummeErfassteAnUndAbwesenheit() == null) nullCount++;
        if (participant.getAnwesenheitErfasstBis() == null) nullCount++;
        if (participant.getAbsoluteAnwesenheit() == null) nullCount++;
        if (participant.getRelativeAnwesenheit() == null) nullCount++;

        return nullCount;
    }
}
