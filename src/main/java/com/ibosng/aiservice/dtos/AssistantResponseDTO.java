package com.ibosng.aiservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantResponseDTO {
    private String threadId;
    private String response;
    
    private List<ToolCallResultDTO> toolCallResults;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToolCallResultDTO {
        private String toolId;
        private String toolName;
        private Map<String, Object> arguments;
        private Object result;
    }
} 