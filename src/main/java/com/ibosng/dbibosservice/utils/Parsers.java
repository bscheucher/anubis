package com.ibosng.dbibosservice.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.ibosng.dbibosservice.utils.Constants.DATE_PATTERNS;
import static com.ibosng.dbibosservice.utils.Helpers.isNullOrBlank;

@Slf4j
public class Parsers {

    public static LocalDate parseDate(String date) {
        if (!isNullOrBlank(date)) {
            for (String pattern : DATE_PATTERNS) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                    return LocalDate.parse(date, formatter);
                } catch (DateTimeParseException e) {
                    log.debug("Date expression {} did not match pattern {}", date, pattern);
                }
            }
        }
        return null;
    }

    public static LocalDate mapLocalDateRow(Object row) {
        if (row != null) {
            if (row instanceof java.sql.Date) {
                return ((java.sql.Date) row).toLocalDate();
            } else if (row instanceof String) {
                return parseDate((String) row);
            }
        }
        return null;
    }
}
