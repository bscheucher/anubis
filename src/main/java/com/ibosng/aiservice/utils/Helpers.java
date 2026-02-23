package com.ibosng.aiservice.utils;


import com.azure.ai.openai.assistants.models.MessageTextContent;
import com.azure.ai.openai.assistants.models.ThreadMessage;
import com.azure.json.JsonProviders;
import com.azure.json.JsonReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibosng.aiservice.dtos.response.RecommendationResponseDto;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Helpers {

    public static String convertToMessageContent(Object request) {
        if (request instanceof String) {
            return (String) request; // Use the plain string as-is
        } else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(request);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize request: {}", e.getMessage());
                return request.toString();
            }
        }
    }

    public static boolean isExtraneousContent(String content) {
        // Example: Detect lines that resemble the original prompt or repeated instructions
        return content.startsWith("###") || content.contains("Anweisung") || content.contains("Ausgabenformat");
    }

    public static <T> String serializeObject(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object: {}", e.getMessage());
            return "{}";
        }
    }

    public static Map<String, Object> parseJsonToMap(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error("Failed to parse JSON arguments: {}", json, e);
            return new HashMap<>(); // Return empty map if parsing fails
        }
    }

    public static RecommendationResponseDto parseResponseToDto(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, RecommendationResponseDto.class);
        } catch (Exception e) {
            log.error("Failed to parse JSON arguments: {}", json, e);
            return null;
        }
    }

    public static String extractMessageContent(ThreadMessage message) {
        return message.getContent().stream()
                .filter(MessageTextContent.class::isInstance)
                .map(content -> ((MessageTextContent) content).getText().getValue())
                .findFirst()
                .orElse(""); // Default to empty string if no content
    }

    public static JsonReader createJsonReaderFromDto(Object dto) {
        try {
            // Serialize the DTO to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(dto);

            // Create and return a JsonReader from the JSON string
            return JsonProviders.createReader(jsonString);
        } catch (JsonProcessingException e) {
            // Handle serialization failure
            log.error("Error serializing DTO to JSON: ", e);
        } catch (IllegalArgumentException | IOException e) {
            // Handle invalid JSON input to JsonProviders.createReader
            log.error("Invalid input for JsonReader creation: ", e);
        }
        // Return null if an exception occurred
        return null;
    }

    public static Object convertValue(Object value, Class<?> targetType) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(value, targetType);
    }

    public static String mapJavaTypeToOpenAI(String javaType) {
        return switch (javaType) {
            case "String" -> "string";
            case "int", "Integer" -> "integer";
            case "double", "Double" -> "number";
            case "boolean", "Boolean" -> "boolean";
            default -> "string"; // Default case
        };
    }

    public static String normalizeFilename(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFC);
    }
}
