package com.ibosng.dbservice.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static com.ibosng.dbservice.utils.Constants.DATE_PATTERN;

@Slf4j
public class Helpers {

    public static <T> T findFirstObject(List<T> objects, Set<String> identifiers, String object) {
        if (objects.size() > 1) {
            log.info("Multiple objects found for identifiers: {} for the object {}", identifiers, object);
        } else if (objects.isEmpty()) {
            log.info("No object found for identifiers: {} for the object {}", identifiers, object);
            return null;
        }
        return objects.get(0);
    }

    public static String getSvnForMitarbeiter(String svnString) {
        if (svnString != null) {
            if (svnString.length() == 6) {
                return "0000" + svnString;
            }
            return svnString;
        }
        return null;
    }

    public static String localDateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return localDate.format(formatter);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }


    public static boolean isDateBetween(LocalDate dateToCheck, LocalDate datumVon, LocalDate datumBis) {
        if (dateToCheck == null) {
            dateToCheck = LocalDate.now();
        }
        return (dateToCheck.isEqual(datumVon) || dateToCheck.isAfter(datumVon)) &&
                (datumBis == null || dateToCheck.isEqual(datumBis) || dateToCheck.isBefore(datumBis));
    }

    public static String truncateToSeconds(String timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
            return dateTime.withNano(0).toString();
        } catch (Exception e) {
            log.error("Failed to parse timestamp for truncation: {}", timestamp, e);
            return timestamp; // Return the original value if parsing fails
        }
    }

    public static Boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
