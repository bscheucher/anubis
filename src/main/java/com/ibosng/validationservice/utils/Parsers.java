package com.ibosng.validationservice.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibosng.dbservice.utils.Parsers.isValidDate;
import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
public class Parsers {

    private static final String DATE_PATTERN = "\\b(\\d{2}\\.\\d{2}\\.\\d{4})\\b";
    private static final String VHS_NB_DATE_PATTERN = "^NB(?:\\s+[^\\d]*)?\\s+(\\d{1,2}\\.\\d{1,2}\\.\\d{2,4})\\b"; //^NB(?:\\s+[^\\d]*)?\\s+(\\d{1,2}\\.\\d{1,2}\\.\\d{2,4})\\b
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static Long parseStringToLong(String numberStr) {
        Long number = null;
        if (!isNullOrBlank(numberStr)) {
            String trimmed = numberStr.replaceAll("\\s+", "");
            try {
                number = Long.parseLong(trimmed);
            } catch (NumberFormatException ex) {
                log.info("Failed to parse '{}' to a long number: {}", numberStr, ex.getMessage());
            }
        }
        return number;
    }

    public static boolean isLong(String numberStr) {
        if (!isNullOrBlank(numberStr)) {
            try {
                Long.parseLong(numberStr);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public static BigDecimal parseStringToBigDecimal(String numberStr) {
        BigDecimal number = null;
        if (!isNullOrBlank(numberStr)) {
            try {
                number = new BigDecimal(numberStr);
            } catch (NumberFormatException ex) {
                log.info("Failed to parse '{}' to a BigDecimal: {}", numberStr, ex.getMessage());
            }
        }
        return number;
    }

    public static Double parseStringToDouble(String numberStr) {
        Double number = null;
        if (!isNullOrBlank(numberStr)) {
            try {
                number = Double.parseDouble(numberStr);
            } catch (NumberFormatException ex) {
                log.info("Failed to parse '{}' to a double number: {}", numberStr, ex.getMessage());
            }
        }
        return number;
    }

    public static boolean isDouble(String numberStr) {
        if (!isNullOrBlank(numberStr)) {
            try {
                Double.parseDouble(numberStr);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }


    public static Integer parseStringToInteger(String numberStr) {
        Integer number = null;
        if (!isNullOrBlank(numberStr)) {
            try {
                number = Integer.parseInt(numberStr);
            } catch (NumberFormatException ex) {
                log.info("Failed to parse '{}' to an integer: {}", numberStr, ex.getMessage());
            }
        }
        return number;
    }

    public static LocalDate extractDate(String input, boolean isVHSFile) {
        Pattern pattern;
        if (isVHSFile) {
            pattern = Pattern.compile(VHS_NB_DATE_PATTERN);
        } else {
            pattern = Pattern.compile(DATE_PATTERN);
        }

        Matcher matcher = pattern.matcher(input.trim());

        if (matcher.find()) {
            String dateString = matcher.group(1); // Extract the date part
            if (isValidDate(dateString)) {
                return parseDate(dateString);
            }
        }
        return null;
    }
}
