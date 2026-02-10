package com.ibosng.fileimportservice.utils.fileparsers;

import com.ibosng.fileimportservice.dtos.ParticipantVHSDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;

import java.text.DecimalFormat;

@Slf4j
@UtilityClass
public class VHSFileParserHelper {

    public static void setCourseParticipantFields(ParticipantVHSDto participantVHSDto, Row row) {
        if (row.getCell(0) != null) {
            participantVHSDto.setInfo(row.getCell(0).getStringCellValue());
        }
        if (row.getCell(1) != null) {
            participantVHSDto.setVorname(row.getCell(1).getStringCellValue());
        }
        if (row.getCell(2) != null) {
            participantVHSDto.setNachname(row.getCell(2).getStringCellValue());
        }
        if (row.getCell(3) != null) {
            participantVHSDto.setGeschlecht(row.getCell(3).getStringCellValue());
        }
        if (row.getCell(4) != null) {
            participantVHSDto.setSvNummer(row.getCell(4).getStringCellValue().replaceAll("\\s+", ""));
        }
        if (row.getCell(5) != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#");
            participantVHSDto.setPlz(decimalFormat.format(row.getCell(5).getNumericCellValue()));
        }
        if (row.getCell(6) != null) {
            participantVHSDto.setOrt(row.getCell(6).getStringCellValue());
        }
        if (row.getCell(7) != null) {
            participantVHSDto.setStrasse(row.getCell(7).getStringCellValue());
        }
        if (row.getCell(8) != null) {
            participantVHSDto.setTelefon(row.getCell(8).getStringCellValue());
        }
        if (row.getCell(9) != null) {
            participantVHSDto.setNation(row.getCell(9).getStringCellValue());
        }
        if (row.getCell(10) != null) {
            participantVHSDto.setRgs(row.getCell(10).getStringCellValue());
        }
        if (row.getCell(11) != null) {
            participantVHSDto.setAnmerkung(row.getCell(11).getStringCellValue());
        }
    }
}
