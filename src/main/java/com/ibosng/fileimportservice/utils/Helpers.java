package com.ibosng.fileimportservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Slf4j
public class Helpers {

    private static final String[] DATE_PATTERNS = {"dd.MM.yyyy", "yyyy-MM-dd", "ddMMyy"};

    public static boolean isNullOrBlank(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

    public static String getFilenameWithDate(String file) {
        String filename = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "_" + file;
        filename = filename.replaceAll(":", "_");
        return filename.replaceAll("-", "_");
    }


    public static String createVhsEamsFilename(String vhsFilename, String eAmsFilename) {
        return vhsFilename + "_" + eAmsFilename;
    }

    public static boolean deleteLocalFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public static String safelyConvertToString(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            for (String pattern : DATE_PATTERNS) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                    return dateFormat.format((Date) value);
                } catch (Exception e) {
                    log.debug("Error formatting date: " + e.getMessage());
                }
            }
            log.debug(String.format("Date expression %s did not match patterns", value));
        } else {
            return value.toString();
        }
        return null;
    }

    public static boolean isAnyValueEmptyOrBlank(Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String value = entry.getKey();
            if (value == null || value.trim().isEmpty() || value.trim().isBlank()) {
                return true;
            }
        }
        return false;
    }

    public static String getTimeWithZoneNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH:mm:ss");
        return ZonedDateTime.now(ZoneId.of("Europe/Paris")).truncatedTo(ChronoUnit.SECONDS).format(formatter);
    }
}
