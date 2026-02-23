package com.ibosng.aiservice.dtos.tooldefiinition;

import lombok.Data;

@Data
public class ToolFunctionDto {
    private String name; // Name of the function (e.g., method name)
    private String description; // Description of the function
    private ToolParametersDto parameters; // Parameters object
}
