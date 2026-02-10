package com.ibosng.moxisservice.utils;

import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.moxisservice.exceptions.MoxisException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.ibosng.moxisservice.utils.Constants.DATE_TIME_EMAIL_FORMAT;

@Slf4j
public class Helpers {

    private static final String PDF_EXTENSION = ".pdf";

    public static File createFile(String fileName, byte[] file) {
        String sanitizedFileName = sanitizeFileName(fileName);
        File document = new File(sanitizedFileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(document)) {
            fileOutputStream.write(Objects.requireNonNull(file));
            return document;
        } catch (IOException e) {
            log.error("Exception caught during creating file: {}", e.getMessage());
        }
        return null;
    }

    private static String sanitizeFileName(String fileName) {
        // Replace invalid characters with an underscore
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    public static String extractFilename(String contentDisposition) {
        if (contentDisposition == null || !contentDisposition.contains("filename=")) {
            return "default_filename";
        }
        String[] parts = contentDisposition.split("filename=");
        if (parts.length > 1) {
            String filenamePart = parts[1];
            return filenamePart.replace("\"", "").trim();
        }
        return "default_filename";
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

    // Method to capitalize the first letter of a string
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String getDateAndTimeInEmailFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_EMAIL_FORMAT);
        return dateTime.format(formatter);
    }

    @NotNull
    public static FileInputStream createFileInputStream(File data) {
        try {
            return new FileInputStream(data);
        } catch (FileNotFoundException e) {
            log.warn("Uploading file has been failed, because the file is not found!");
            throw new MoxisException(e);
        }
    }

    public static ByteArrayResource convertInputStreamToByteArrayResource(InputStream inputStream, String filename) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return new ByteArrayResource(byteArrayOutputStream.toByteArray()) {
                @Override
                public String getFilename() {
                    return filename + ".pdf";
                }
            };
        }
    }

    public static String getPartFilenameForDV(Stammdaten stammdaten) {
        return "_DIENSTVERTRAG_" + stammdaten.getPersonalnummer().getPersonalnummer() + "_" + stammdaten.getVorname().toUpperCase() + "_" + stammdaten.getNachname().toUpperCase() + PDF_EXTENSION;
    }
}
