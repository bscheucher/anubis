package com.ibosng.aiservice.dtos;

import com.ibosng.aiservice.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantRequestDTO {
    private String request;
    private String assistantId;
    private String threadId;
    private RequestType type;
    private boolean isNewThread;
    private boolean deleteThreadAfterProcessing;
    private List<AssistantResponseDTO.ToolCallResultDTO> toolCallResults;

    public boolean hasToolCallResults() {
        return toolCallResults != null && !toolCallResults.isEmpty();
    }

    public boolean deleteThreadAfterProcessing() {
        return deleteThreadAfterProcessing;
    }
} 