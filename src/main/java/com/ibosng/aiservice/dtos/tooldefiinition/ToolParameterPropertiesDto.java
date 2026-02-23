package com.ibosng.aiservice.dtos.tooldefiinition;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ToolParameterPropertiesDto {
    private String type; // Data type of the parameter (e.g., "string", "integer")
    private String description; // Description of the parameter
    private List<String> enumValues; // Optional: Enum values for parameters if applicable
    private Map<String, String> items;
}
