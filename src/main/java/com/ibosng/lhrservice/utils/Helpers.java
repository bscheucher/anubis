package com.ibosng.lhrservice.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.lhrservice.dtos.exceptions.ErrorResponseDto;
import com.ibosng.lhrservice.enums.LhrDocuments;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@UtilityClass
public class Helpers {

    public static final String DATE_TIME_EMAIL_FORMAT = "dd.MM.yyyy HH:mm";
    public static final String DATE_POST_FORMAT = "yyyy-MM-dd";

    public static <T> T getBodyIfStatusOk(ResponseEntity<T> response) {
        if (response != null) {
            if (response.getStatusCode() == HttpStatus.OK) {
                if (response.getBody() != null) {
                    return response.getBody();
                }

            } else if (response.getStatusCode() != HttpStatus.NOT_FOUND) {
                log.error("Request returned non-OK status: {}, body: {}", response.getStatusCode(), response.getBody());
            }
            return null;
        }
        log.warn("Request returned null");
        return null;
    }

    public static String extractErrorMessage(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("detail").asText();
        } catch (Exception e) {
            log.error("Failed to parse error response: " + e.getMessage());
            return "An error occurred";
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ResponseEntity<T> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body((T) message);
    }

    public static String removeLeadingZeros(String str) {
        if (str != null) {
            return str.replaceFirst("^0+(?!$)", "");
        }
        return str;
    }

    public static String getDateAndTimeInEmailFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_EMAIL_FORMAT);
        return dateTime.format(formatter);
    }

    public static String getDateInPostRequestFormat(LocalDate dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_POST_FORMAT);
        return dateTime.format(formatter);
    }

    public static ResponseEntity<String> handleLHRWebClientException(LHRWebClientException e, String personalnummer, String lhrProperty) throws LHRException {
        if (e.getHttpStatus().equals(HttpStatus.NOT_FOUND)) {
            log.warn("LHR client returned NOT FOUND for getting {} for personalnummer: {}", lhrProperty, personalnummer);
            throw new LHRException(String.format("LHR client returned NOT FOUND for getting %s for personalnummer: %s", lhrProperty, personalnummer));
        }
        if (e.getHttpStatus().equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
            log.warn("LHR client returned UNPROCESSABLE_ENTITY for getting {} for personalnummer: {}", lhrProperty, personalnummer);
            throw new LHRException(String.format("LHR client returned UNPROCESSABLE_ENTITY for getting %s for personalnummer: %s with message %s", lhrProperty, personalnummer, e.getMessage()));
        } else {
            log.error("LHR client returned error for getting {} for personalnummer: {} with message {}", lhrProperty, personalnummer, e.getMessage());
            throw new LHRException(String.format("LHR client returned error for getting %s for personalnummer: %s with message %s", lhrProperty, personalnummer, e.getMessage()));
        }
    }

    public static ResponseEntity<?> handleLHRExceptions(String personalnummer, String operationName, Supplier<ResponseEntity<?>> action) {
        try {
            return action.get();  // `get()` returns the result of the action
        } catch (LHRWebClientException e) {
            if (e.getHttpStatus().equals(HttpStatus.NOT_FOUND)) {
                log.warn("LHR client returned NOT FOUND for getting {} for personalnummer: {}", operationName, personalnummer);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                log.error("LHR client returned error for getting {} for personalnummer: {} with message {}", operationName, personalnummer, e.getMessage());
                return ResponseEntity.status(e.getHttpStatus()).body(String.format("LHR client exception occurred with exception %s", e.getMessage()));
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while calling LHR client for getting {} for personalnummer: {} with error {}", operationName, personalnummer, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static String extractDigitFromStringEnd(String input) {
        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(input);
        String result = null;
        if (matcher.find()) {
            result = matcher.group();
        } else {
            log.warn("No digits found in: {}", input);
        }
        return result;
    }

    public static String extractReasonFromErrorBody(String errorBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponseDto errorResponse = objectMapper.readValue(errorBody, ErrorResponseDto.class);

            // Return the first reason, if present
            if (errorResponse.getErrors() != null && !errorResponse.getErrors().isEmpty()) {
                return errorResponse.getErrors().get(0).getReason();
            }
        } catch (Exception e) {
            log.error("Failed to parse error body: {}", errorBody, e);
        }

        return "Unknown error";
    }

    //yyyyMMdd_[type]_[additional]_[identifier] = 20250101_Gehaltszettel_0010000001_name_surname
    public static String getFileName(LocalDate date, String identifier, String additionalIdentifier, String type, LhrDocuments documentType, String oldName) {
        StringBuilder fileName = new StringBuilder();
        String datum;
        if (LhrDocuments.NETTOZETTEL.equals(documentType) && !isNullOrBlank(oldName)) {
            String regex = "(\\d{2})\\.(\\d{4})"; //in order to get the month and the year of the Nettozettel
            datum = oldName.replaceAll(".*" + regex + ".*", "$2$1");
        } else {
            datum = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        fileName.append(datum).append("_");
        fileName.append(type);

        if (!isNullOrBlank(additionalIdentifier)) {
            fileName.append("_").append(additionalIdentifier);
        }

        fileName.append("_").append(identifier);
        return fileName.toString();
    }

    public static String getFileName(String identifier, String additionalIdentifier, String type, LhrDocuments documentType, String oldName) {
        return getFileName(LocalDate.now(), identifier, additionalIdentifier, type, documentType, oldName);
    }

    public static String findKeyByValue(Map<String, List<String>> map, String value) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getValue().contains(value)) {
                return entry.getKey(); // Return the first matching key
            }
        }
        return null; // Not found
    }
}