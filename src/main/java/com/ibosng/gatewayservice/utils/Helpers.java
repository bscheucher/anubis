package com.ibosng.gatewayservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.gatewayservice.dtos.response.Pagination;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.ibosng.dbservice.enums.MimeTypeMapping.getExtensionForMimeType;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@UtilityClass
public class Helpers {

    private static final String AUTHORIZATION = "Authorization";
    public static final String DATE_TIME_EMAIL_FORMAT = "dd.MM.yyyy HH:mm";
    private static final Map<String, String> fieldToPropertyMap = new HashMap<>();

    static {
        fieldToPropertyMap.put("startDate", "von");
        fieldToPropertyMap.put("endDate", "bis");
        fieldToPropertyMap.put("days", "tage");
        fieldToPropertyMap.put("saldo", "saldo");
        fieldToPropertyMap.put("comment", "kommentar");
        fieldToPropertyMap.put("urlaubType", "beschreibung");
        fieldToPropertyMap.put("status", "status");
    }

    public static <T> ResponseEntity<T> checkResultIfNull(T result) {
        if (areAllFieldsNull(result)) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static String getSortPropertyForAbwesenheitOverwiev(String property) {
        return fieldToPropertyMap.get(property);
    }

    public static boolean areAllFieldsNull(Object dto) {
        if (dto == null) {
            return true;
        }

        Class<?> currentClass = dto.getClass();
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true); // You might need this line if your fields are private
                try {
                    if (field.get(dto) != null) {
                        return false; // Found a non-null field
                    }
                } catch (IllegalAccessException e) {
                    log.error("Error accessing field", e);
                }
            }
            currentClass = currentClass.getSuperclass(); // Move to the superclass
        }

        return true; // All fields are null
    }

    public static <T> Pagination createPagination(Page<T> page) {
        return new Pagination(
                (int) page.getTotalElements(),
                page.getSize(),
                page.getNumber()
        );
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public static String getTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public static String extractErrorMessage(String responseBody) {
        try {
            if (!isValidJson(responseBody)) {
                return responseBody;
            }
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
        try {
            T body = (T) message;
        } catch (ClassCastException classCastException) {
            log.debug("During error message cann`t be converted: {}", message);
            return ResponseEntity.status(status).build();
        }
        return ResponseEntity.status(status).body((T) message);
    }

    public static Sort.Direction getSortDirection(String sortDirectionString) {
        if (!isNullOrBlank(sortDirectionString) && sortDirectionString.equals("DESC")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public static Pageable createPageable(String sortProperty, String sortDirection, int page, int size) {
        if (isNullOrBlank(sortProperty)) {
            sortProperty = "createdOn";
        }
        Pageable pageable;
        if (getSortDirection(sortDirection).equals(Sort.Direction.ASC)) {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).descending());
        }
        return pageable;
    }

    public static void updateStammdatenFromTeilnehmer(Teilnehmer teilnehmer, Stammdaten stammdaten) {
        stammdaten.setVorname(teilnehmer.getVorname());
        stammdaten.setNachname(teilnehmer.getNachname());
        stammdaten.setGeburtsdatum(teilnehmer.getGeburtsdatum());
        stammdaten.setEmail(teilnehmer.getEmail());
        stammdaten.setGeschlecht(teilnehmer.getGeschlecht());
        stammdaten.setSvnr(teilnehmer.getSvNummer());
        stammdaten.setAdresse(teilnehmer.getAdresse());
        stammdaten.setAnrede(teilnehmer.getAnrede());
        if (!teilnehmer.getTelefons().isEmpty()) {
            stammdaten.setMobilnummer(teilnehmer.getTelefons().get(0));
        }
        if (!teilnehmer.getNation().isEmpty()) {
            stammdaten.setStaatsbuergerschaft(teilnehmer.getNation().iterator().next());
        }
    }

    public static String getFileExtension(InputStream inputStream, String filename) {
        String mimeType = getMimeType(inputStream, filename); // Reuse getMimeType logic
        if ("application/octet-stream".equals(mimeType)) {
            log.warn("MIME type could not be determined for file '{}'.", filename);
            return ""; // Return empty string if MIME type is unknown
        }
        String extension = getExtensionForMimeType(mimeType);
        if (extension.isEmpty()) {
            log.warn("No matching file extension found for MIME type '{}' of file '{}'.", mimeType, filename);
        }
        return extension;
    }

    public static String getMimeType(InputStream inputStream, String filename) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            // Use BufferedInputStream to ensure efficient reading
            Tika tika = new Tika();
            return tika.detect(bufferedInputStream);
        } catch (IOException e) {
            log.error("Could not detect MIME type of file '{}': {}", filename, e.getMessage());
            return "application/octet-stream"; // Fallback MIME type
        }
    }

    public static String getFileName(String identifier, String additionalIdentifier, FileUploadTypes type, boolean isUpload) {
        StringBuilder fileName = new StringBuilder();
        if (isUpload) {
            String datum = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            fileName.append(datum)
                    .append("_");
        }
        fileName.append(type.name());

        if (!isNullOrBlank(additionalIdentifier)) {
            fileName.append("_").append(additionalIdentifier);
        }

        fileName.append("_").append(identifier);
        return fileName.toString();
    }

    public static String createDienstvertragVorschauLUrl(String personalnummer, String url) {
        return url + "/mitarbeiter/erfassen/ " + personalnummer + "?wfi=10";
    }

    @SuppressWarnings("unchecked")
    public static Comparable<?> getSortPropertyForAbwesenheitDto(AbwesenheitDto dto, String sortBy) {
        return switch (sortBy) {
            case "personalnummer" -> dto.getPersonalnummerId();
            case "startDate" -> dto.getStartDate();
            case "endDate" -> dto.getEndDate();
            case "type" -> dto.getType();
            case "comment" -> dto.getComment();
            case "changedOn" -> dto.getChangedOn();
            case "durationInDays" ->
                    isNullOrBlank(dto.getDurationInDays()) ? 0d : Double.parseDouble(dto.getDurationInDays());
            case "status" -> dto.getStatus();
            case "fullName" -> dto.getFullName();
            case "commentFuehrungskraft" -> dto.getCommentFuehrungskraft();
            default -> throw new IllegalArgumentException("Invalid sort property: " + sortBy);
        };
    }

    public static String getDateAndTimeInEmailFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_EMAIL_FORMAT);
        return dateTime.format(formatter);
    }

    public static boolean isValidJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

}
