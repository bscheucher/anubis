package com.ibosng.aiservice.services;

import com.ibosng.aiservice.dtos.AssistantRequestDTO;
import com.ibosng.aiservice.dtos.AssistantResponseDTO;

public interface AssistantService {
    
    /**
     * Process an assistant request synchronously using AssistantRequestDTO
     * 
     * @param assistantRequest Request parameters
     * @return Response entity containing the assistant's response with tool call results
     */
    AssistantResponseDTO processRequest(AssistantRequestDTO assistantRequest);
} 