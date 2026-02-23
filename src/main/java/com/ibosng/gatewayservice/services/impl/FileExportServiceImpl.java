package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.enums.ReportOutputFormat;
import com.ibosng.gatewayservice.services.FileExportService;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class FileExportServiceImpl implements FileExportService {
    @Override
    public ReportResponse exportToCsv(List<PruefungCsvDto> pruefungCsvDtos) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));

        // CSV header
        String[] headers = {
                "Geschlecht", "Nachname", "Vorname", "Strasse", "PLZ", "Ort",
                "Geburtsdatum", "Telefon", "Email", "Staat", "Herkunft", "Art", "Abrechnung"
        };

        StringWriter stringWriter = new StringWriter();
        try (CSVWriter writer = new CSVWriter(stringWriter,
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

            // Write header
            writer.writeNext(headers);

            // Write rows
            for (PruefungCsvDto dto : pruefungCsvDtos) {
                String[] row = {
                        dto.getGeschlecht() != null ? dto.getGeschlecht() : "",
                        dto.getNachname() != null ? dto.getNachname() : "",
                        dto.getVorname() != null ? dto.getVorname() : "",
                        dto.getStrasse() != null ? dto.getStrasse() : "",
                        dto.getPlz() != null ? dto.getPlz() : "",
                        dto.getOrt() != null ? dto.getOrt() : "",
                        dto.getGeburtsdatum() != null ? dto.getGeburtsdatum() : "",
                        dto.getTelefon() != null ? dto.getTelefon() : "",
                        dto.getEmail() != null ? dto.getEmail() : "",
                        dto.getStaat() != null ? dto.getStaat() : "",
                        dto.getHerkunft() != null ? dto.getHerkunft() : "",
                        dto.getArt() != null ? dto.getArt() : "",
                        dto.getAbrechnung() != null ? dto.getAbrechnung() : ""
                };
                writer.writeNext(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV", e);
        }

        // Use Byte Order Mark to ensure MS Excel interprets contents as UTF-8 instead of ISO-8859-1
        final String BOM_CHARACTER = "\uFEFF";

        byte[] csvBytes = (BOM_CHARACTER + stringWriter.toString()).getBytes(StandardCharsets.UTF_8);

        String fileName =  currentDate + "_PAL_ÖIF_" +pruefungCsvDtos.get(0).getNiveauAndSeminar() ;                  //TODO Set file name properly

        return new ReportResponse(
                csvBytes,
                fileName,
                ReportOutputFormat.CSV
        );
    }

    @Override
    public ReportResponse exportToXlsx(List<PruefungXlsxDto> pruefungCsvDtos) {
        String[] HEADERS = {
                "Prüfungsstufe", "Titel Vor", "Vorname", "Familienname", "Titel Nach", "Geschlecht",
                "Geburts-Ort", "Geburts-Land", "Nationalität", "Geburts-Datum",
                "Prüfer/In Nr 1 schriftlich", "Prüfer/In Nr 2 schriftlich",
                "Prüfer/In Nr 1 mündlich", "Prüfer/In Nr 2 mündlich",
                "Modul Lesen", "Modul Hören", "Modul Schreiben", "Modul Sprechen"
        };

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pruefungen");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }

            // Populate data rows
            int rowIdx = 1;
            for (PruefungXlsxDto dto : pruefungCsvDtos) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dto.getPruefungsstufe());
                row.createCell(1).setCellValue(dto.getTitelVor());
                row.createCell(2).setCellValue(dto.getVorname());
                row.createCell(3).setCellValue(dto.getFamilienname());
                row.createCell(4).setCellValue(dto.getTitelNach());
                row.createCell(5).setCellValue(dto.getGeschlecht());
                row.createCell(6).setCellValue(dto.getGeburtsOrt());
                row.createCell(7).setCellValue(dto.getGeburtsLand());
                row.createCell(8).setCellValue(dto.getNationalitaet());
                row.createCell(9).setCellValue(dto.getGeburtsDatum());
                row.createCell(10).setCellValue(dto.getPrueferInNr1Schriftlich());
                row.createCell(11).setCellValue(dto.getPrueferInNr2Schriftlich());
                row.createCell(12).setCellValue(dto.getPrueferInNr1Muendlich());
                row.createCell(13).setCellValue(dto.getPrueferInNr2Muendlich());
                row.createCell(14).setCellValue(dto.getModulLesen());
                row.createCell(15).setCellValue(dto.getModulHoeren());
                row.createCell(16).setCellValue(dto.getModulSchreiben());
                row.createCell(17).setCellValue(dto.getModulSprechen());
            }

            // Autosize columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            byte[] xlsxBytes = byteArrayOutputStream.toByteArray();
            String fileName =  currentDate + "_PAL_ÖSD_" + pruefungCsvDtos.get(0).getNiveauAndSeminar();                 //TODO Set file name properly
            return new ReportResponse(
                    xlsxBytes,
                    fileName,
                    ReportOutputFormat.XLSX
            );
        } catch (IOException e) {
            throw new RuntimeException("Error generating XLSX", e);
        }
    }
}
