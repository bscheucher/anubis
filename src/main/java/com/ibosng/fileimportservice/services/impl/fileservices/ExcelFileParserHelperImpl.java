package com.ibosng.fileimportservice.services.impl.fileservices;

import com.ibosng.dbservice.entities.FileType;
import com.ibosng.dbservice.services.HeaderService;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.fileimportservice.services.fileservices.ExcelFileParserHelper;
import com.ibosng.fileimportservice.utils.fileparsers.HeadersValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExcelFileParserHelperImpl implements ExcelFileParserHelper {
    private final HeaderService headerService;

    @Override
    public List<Map<String, Object>> parseExcelFileAndValidateHeaders(File file, FileType fileType) throws ParserException {
        try (Workbook workbook = WorkbookFactory.create(file)) {
            List<Map<String, Object>> dataRows = new ArrayList<>();
            for (Sheet sheet : workbook) {
                HeadersValidator.xlsxHeadersValidator(sheet.getRow(0),
                        headerService.getActiveHeadersNamesByFileType(fileType),
                        headerService.getInactiveHeadersNamesByFileType(fileType));

                for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                    Map<String, Object> row = new HashMap<>();
                    for (Cell cell : sheet.getRow(i)) {
                        final String headerCell = sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
                        switch (cell.getCellType()) {
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    // Handle date
                                    row.put(headerCell, cell.getDateCellValue());
                                } else {
                                    // Handle numeric value
                                    double numericValue = cell.getNumericCellValue();
                                    if (numericValue % 1 == 0) {
                                        // If it's an integer value (no fractional part)
                                        row.put(headerCell, (long) numericValue);
                                    } else {
                                        // If it has a fractional part, handle as double
                                        row.put(headerCell, numericValue);
                                    }
                                }
                            }
                            case STRING -> row.put(headerCell, cell.getStringCellValue().strip());
                            case BOOLEAN -> row.put(headerCell, cell.getBooleanCellValue());
                            default -> row.put(headerCell, "");
                        }
                    }
                    dataRows.add(row);
                }
            }
            return dataRows;
        } catch (IOException | EncryptedDocumentException | FieldTypeException ex) {
            log.warn("Cannot parse field in excel parser: {}", ex.getMessage());
            throw new ParserException("Cannot parse field in excel parser", ex);
        }
    }
}
