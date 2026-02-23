package com.ibosng.fileimportservice.utils.fileparsers;

import com.ibosng.fileimportservice.dtos.ParticipantOEIFDto;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ibosng.fileimportservice.utils.Constants.*;
import static com.ibosng.fileimportservice.utils.Helpers.isAnyValueEmptyOrBlank;
import static com.ibosng.fileimportservice.utils.Helpers.safelyConvertToString;

@Slf4j
@UtilityClass
public class OEIFFileParserHelper {

    public static List<ParticipantOEIFDto> mapParticipantDTOS(List<Map<String, Object>> dataRows) throws FieldTypeException {
        try {
            List<ParticipantOEIFDto> participantDTOS = new ArrayList<>();
            for (Map<String, Object> row : dataRows) {
                participantDTOS.add(convertToDto(row));
            }
            return participantDTOS;
        } catch (IllegalArgumentException ex) {
            log.warn(String.format("Cannot parse field in OEIF parser: %s", ex.getMessage()));
            throw new FieldTypeException("Cannot parse field in OEIF parser", ex);
        }
    }

    private static ParticipantOEIFDto convertToDto(Map<String, Object> data) throws FieldTypeException {
        ParticipantOEIFDto participantOEIFDto = new ParticipantOEIFDto();
        if(data == null) {
            return participantOEIFDto;
        }
        if(isAnyValueEmptyOrBlank(data)) {
            log.warn("Cannot parse field in OEIF parser");
            throw new FieldTypeException("Cannot parse field in OEIF parser");
        }
        participantOEIFDto.setKennung(safelyConvertToString(data.get(KENNUNG)));
        participantOEIFDto.setNachname(safelyConvertToString(data.get(NACHNAME)));
        participantOEIFDto.setVorname(safelyConvertToString(data.get(VORNAME)));
        participantOEIFDto.setGeschlecht(safelyConvertToString(data.get(GESCHLECHT)));
        participantOEIFDto.setGeburtsdatum(safelyConvertToString(data.get(GEBURTSDATUM)));
        participantOEIFDto.setMobiltelefon(safelyConvertToString(data.get(MOBILTELEFON)));
        participantOEIFDto.setTeilnahmebeginn(safelyConvertToString(data.get(TEILNAHMEBEGINN)));
        participantOEIFDto.setAnmeldestatus(safelyConvertToString(data.get(ANMELDESTATUS)));
        participantOEIFDto.setIndividuellesNiveau(safelyConvertToString(data.get(INDIVIDUELLES_NIVEAU)));
        participantOEIFDto.setEmail(safelyConvertToString(data.get(EMAIL)));
        participantOEIFDto.setSvn(safelyConvertToString(data.get(SOZIALVERSICHERUNGSNUMMER)));
        participantOEIFDto.setAms(safelyConvertToString(data.get(AMS)));
        participantOEIFDto.setAbmeldegrund(safelyConvertToString(data.get(ABMELDEGRUND)));
        participantOEIFDto.setAnwesenheitInUE(safelyConvertToString(data.get(ANWESENHEIT_IN_UE)));
        participantOEIFDto.setEntschuldigteAnwesenheitInUE(safelyConvertToString(data.get(ENTSCHULDIGTE_ABWESENHEIT_IN_UE)));
        participantOEIFDto.setUnentschuldigteAnwesenheitInUE(safelyConvertToString(data.get(UNENTSCHULDIGTE_ABWESENHEIT_IN_UE)));
        participantOEIFDto.setSummeErfassteAnUndAbwesenheit(safelyConvertToString(data.get(SUMME_ERFASSTE_AN_UND_ABWESENHEIT)));
        participantOEIFDto.setAnwesenheitErfasstBis(safelyConvertToString(data.get(ANWESENHEIT_ERFASST_BIS_INKL)));
        participantOEIFDto.setAbsoluteAnwesenheit(safelyConvertToString(data.get(ABSOLUTE_ANWESENHEIT_IN_PROZENT)));
        participantOEIFDto.setRelativeAnwesenheit(safelyConvertToString(data.get(RELATIVE_ANWESENHEIT_IN_PROZENT)));
        participantOEIFDto.setSeminarId(safelyConvertToString(data.get(SEMINAR_ID)));
        return participantOEIFDto;
    }
}