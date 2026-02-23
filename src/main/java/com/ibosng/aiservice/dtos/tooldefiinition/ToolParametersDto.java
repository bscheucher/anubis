package com.ibosng.aiservice.dtos.tooldefiinition;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ToolParametersDto {
    private String type = "object"; // Always "object" for parameters
    private Map<String, ToolParameterPropertiesDto> properties; // Key-value pairs of parameter name and details
    private List<String> required; // List of required parameter names
}
