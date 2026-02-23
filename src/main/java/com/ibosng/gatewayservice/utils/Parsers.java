package com.ibosng.gatewayservice.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class Parsers {

    private static final String[] DATE_PATTERNS = {"dd.MM.yyyy", "yyyy-MM-dd", "ddMMyy"};

    public static boolean isValidDate(String dateStr) {
        for (String pattern : DATE_PATTERNS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDate.parse(dateStr, formatter);
                return true;
            } catch (DateTimeParseException e) {
                log.debug("Time expression {} did not match pattern {}", dateStr, pattern);
            }
        }
        return false;
    }

    public static LocalDate parseDate(String date) {
        for (String pattern : DATE_PATTERNS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                log.debug("Time expression {} did not match pattern {}", date, pattern);
            }
        }
        return null;
    }

    public static Integer parseStringToInteger(String numberStr) {
        Integer number = null;
        try {
            number = Integer.parseInt(numberStr);
        } catch (NumberFormatException ex) {
            log.error("Failed to parse '{}' to an integer: {}", numberStr, ex.getMessage());
        }
        return number;
    }
}
