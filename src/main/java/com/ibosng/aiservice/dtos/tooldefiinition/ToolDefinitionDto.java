package com.ibosng.aiservice.dtos.tooldefiinition;

import lombok.Data;

@Data
public class ToolDefinitionDto {
    private String type = "function"; // Always "function" for this use case
    private ToolFunctionDto function;
}
