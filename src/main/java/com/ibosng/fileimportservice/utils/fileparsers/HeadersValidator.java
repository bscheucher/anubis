package com.ibosng.fileimportservice.utils.fileparsers;

import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ibosng.fileimportservice.utils.Constants.INVALID_HEADER_ERROR_MESSAGE;

@Slf4j
@UtilityClass
public class HeadersValidator {

    public static void csvHeadersValidator(String[] headers,
                                           List<String> expectedHeaders,
                                           List<String> inactiveHeaders) throws FieldTypeException {
        inactiveHeaders = new ArrayList<>(inactiveHeaders);
        expectedHeaders = new ArrayList<>(expectedHeaders);
        if ((headers.length < expectedHeaders.size()) ||
                (headers.length > expectedHeaders.size() + inactiveHeaders.size())) {
            log.warn("Header count mismatch in csv file parser");
            throw new FieldTypeException("Header count mismatch in csv file parser");
        }

        for (int i = 0; i < headers.length; i++) {
            if (!isInHeadersList(expectedHeaders, headers[i])) {
                if (!isInHeadersList(inactiveHeaders, headers[i])) {
                    log.warn(INVALID_HEADER_ERROR_MESSAGE.formatted(headers[i].strip()));
                    throw new FieldTypeException(INVALID_HEADER_ERROR_MESSAGE.formatted(headers[i].strip()));
                }
            }
        }

        //are all expected headers present
        if (!expectedHeaders.isEmpty()) {
            log.warn("Expected header count mismatch in csv file parser");
            throw new FieldTypeException("Expected header count mismatch in csv file parser");
        }
    }

    public static void xlsxHeadersValidator(Row headerRow,
                                            List<String> expectedHeaders,
                                            List<String> inactiveHeaders) throws FieldTypeException {
        inactiveHeaders = new ArrayList<>(inactiveHeaders);
        expectedHeaders = new ArrayList<>(expectedHeaders);
        if (headerRow == null) {
            log.warn("Header row is missing in VHS file");
            throw new FieldTypeException("Header row is missing in VHS file");
        }

        if ((headerRow.getPhysicalNumberOfCells() < expectedHeaders.size())) {
            log.warn("Header count mismatch in xlsx file parser");
            throw new FieldTypeException("Header count mismatch in xlsx file parser");
        }

        for (int cellIndex = 0; (cellIndex < headerRow.getPhysicalNumberOfCells()) && (headerRow.getCell(cellIndex).getCellType() == CellType.STRING); cellIndex++) {
            Cell cell = headerRow.getCell(cellIndex);

            if (Objects.isNull(cell) || !Objects.equals(cell.getCellType(), CellType.STRING) ||
                    !isInHeadersList(expectedHeaders, cell.getStringCellValue().trim())) {

                if (Objects.equals(cell.getCellType(), CellType.STRING) && !isInHeadersList(inactiveHeaders, cell.getStringCellValue())) {
                    log.warn(String.format(INVALID_HEADER_ERROR_MESSAGE, (cell != null ? cell.getStringCellValue().trim() : "null")));
                    throw new FieldTypeException(String.format(INVALID_HEADER_ERROR_MESSAGE, (cell != null ? cell.getStringCellValue().trim() : "null")));
                }
            }
        }

        //are all expected headers present
        if (!expectedHeaders.isEmpty()) {
            log.warn("Expected header count mismatch in xlsx file parser");
            throw new FieldTypeException("Expected header count mismatch in xlsx file parser");
        }
    }

    private boolean isInHeadersList(List<String> headersList, String headerToCheck) {
        for (String header : headersList) {
            if (headerToCheck.strip().trim().equalsIgnoreCase(header.strip().trim())) {
                headersList.remove(header);
                return true;
            }
        }
        return false;
    }
}
