package com.ibosng.lhrservice.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
public class Parsers {

    public static Integer parseStringToInteger(String numberStr) {
        Integer number = null;
        if (!isNullOrBlank(numberStr)) {
            try {
                number = Integer.parseInt(numberStr);
            } catch (NumberFormatException ex) {
                log.error("Failed to parse '{}' to an integer: {}", numberStr, ex.getMessage());
            }
        }
        return number;
    }

    public static Double parseStringToDouble(String numberStr) {
        Double number = null;
        try {
            number = Double.parseDouble(numberStr);
        } catch (NumberFormatException ex) {
            log.info("Failed to parse '{}' to a double number: {}", numberStr, ex.getMessage());
        }
        return number;
    }

    public static String mergeLocalDateTime(LocalDate localDate, LocalTime localTime) {
        if (localDate == null || localTime == null) {
            return null;
        }
        return LocalDateTime.of(localDate, localTime).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
