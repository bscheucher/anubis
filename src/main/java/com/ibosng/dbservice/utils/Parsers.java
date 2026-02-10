package com.ibosng.dbservice.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ibosng.dbservice.utils.Constants.EUROPE_VIENNA;
import static com.ibosng.dbservice.utils.Constants.UTC;

@Slf4j
@UtilityClass
public class Parsers {
    private static final String[] TIME_PATTERNS = {"hh:mm", "HH:mm", "h:mm a", "hh:mm:ss a", "H:mm:ss"};
    private static final String[] DATE_TIME_PATTERNS = {"dd.MM.yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss"};

    private static final DateTimeFormatter CUSTOM_2_DIGIT_YEAR_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("ddMM")
                    .appendValueReduced(ChronoField.YEAR, 2, 2, 1950) // <- pivot year here
                    .toFormatter();


    public static boolean isNullOrBlank(String string) {
        return string == null || string.isEmpty() || string.isBlank() || "null".equals(string);
    }

    public static List<String> parseCsvString(String csvString) {
        return !isNullOrBlank(csvString) ?
                Arrays.stream(csvString.split(",")).toList() :
                new ArrayList<>();
    }

    public static boolean isValidDate(String dateStr) {
        if(isNullOrBlank(dateStr)) return false;
        LocalDate parsedDate = parseDate(dateStr);
        return parsedDate != null;
    }

    public static LocalDate parseDate(String dateString) {
        if (dateString == null) return null;
        dateString = dateString.trim();

        // Normalize dashes to dots
        String normalized = dateString.replaceAll("[-]", ".");

        // 1. Try yyyy.MM.dd (or yyyy-MM-dd → normalized)
        try {
            DateTimeFormatter isoLike = new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.YEAR, 4)
                    .appendLiteral('.')
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral('.')
                    .appendValue(ChronoField.DAY_OF_MONTH)
                    .toFormatter();
            return LocalDate.parse(normalized, isoLike);
        } catch (DateTimeParseException ignored) {}

        // 2. Try dd.MM.yyyy
        try {
            DateTimeFormatter longYear = new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.DAY_OF_MONTH)
                    .appendLiteral('.')
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral('.')
                    .appendValue(ChronoField.YEAR, 4)
                    .toFormatter();
            return LocalDate.parse(normalized, longYear);
        } catch (DateTimeParseException ignored) {}

        // 3. Try dd.MM.yy with pivot at 1950
        try {
            DateTimeFormatter shortYear = new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.DAY_OF_MONTH)
                    .appendLiteral('.')
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral('.')
                    .appendValueReduced(ChronoField.YEAR, 2, 2, 1950)
                    .toFormatter();
            return LocalDate.parse(normalized, shortYear);
        } catch (DateTimeParseException e) {}



        // 4. Try yy.MM.dd with pivot at 1950
        try {
            DateTimeFormatter shortYearFirst = new DateTimeFormatterBuilder()
                    .appendValueReduced(ChronoField.YEAR, 2, 2, 1950)
                    .appendLiteral('.')
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral('.')
                    .appendValue(ChronoField.DAY_OF_MONTH)
                    .toFormatter();
            return LocalDate.parse(normalized, shortYearFirst);
        } catch (DateTimeParseException e) {}

        // Special handling for ddMMyy using custom pivot logic
        try {
            return LocalDate.parse(dateString, CUSTOM_2_DIGIT_YEAR_FORMATTER);
        } catch (DateTimeParseException e) {
            log.debug("Time expression {} did not match custom ddMMyy formatter", dateString);
        }
        return null;
    }

    public static LocalTime parseTime(String time) {
        if (time == null) return null;
        for (String pattern : TIME_PATTERNS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalTime.parse(time, formatter);
            } catch (DateTimeParseException e) {
                log.debug("Time expression {} did not match pattern {}", time, pattern);
            }
        }
        return null;
    }
    public static boolean isValidTime(String dateTimeStr) {
        if(isNullOrBlank(dateTimeStr)) return false;
        for (String pattern : TIME_PATTERNS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalTime.parse(dateTimeStr, formatter);
                return true;
            } catch (DateTimeParseException e) {
                log.debug("Time expression {} did not match pattern {}", dateTimeStr, pattern);
            }
        }
        return false;
    }

    public static boolean isValidDateTime(String dateTimeStr) {
        if(isNullOrBlank(dateTimeStr)) return false;
        for (String pattern : DATE_TIME_PATTERNS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDateTime.parse(dateTimeStr, formatter);
                return true;
            } catch (DateTimeParseException e) {
                log.debug("Date time expression {} did not match pattern {}", dateTimeStr, pattern);
            }
        }
        return false;
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        for (String pattern : DATE_TIME_PATTERNS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

                return LocalDateTime.parse(dateTimeStr, formatter);
            } catch (DateTimeParseException e) {
                log.debug("Date time expression {} did not match pattern {}", dateTimeStr, pattern);
            }
        }
        return null;
    }

    public static LocalDateTime getLocalDateNow() {
        return LocalDateTime.now(Clock.system(ZoneId.of("Europe/Vienna")));
    }

    public static LocalDate parseTimestampSqlToLocalDate(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    public static LocalDateTime parseTimestampSqlToLocalDateTime(Timestamp timestamp) {
        return timestamp.toInstant() // Convert to Instant (UTC)
                .atZone(ZoneId.of(UTC)) // Ensure it's read as UTC
                .withZoneSameInstant(ZoneId.of(EUROPE_VIENNA)) // Convert to Vienna time
                .toLocalDateTime();
    }

    public static Integer parseStringToInteger(String numberStr) {
        Integer number = null;
        try {
            number = Integer.parseInt(numberStr);
        } catch (NumberFormatException ex) {
            log.debug("Failed to parse '{}' to an integer: {}", numberStr, ex.getMessage());
        }
        return number;
    }
}
