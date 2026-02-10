package com.ibosng.aiservice.services.impl;

import com.azure.ai.openai.assistants.AssistantsClient;
import com.azure.ai.openai.assistants.models.ThreadDeletionStatus;
import com.ibosng.aiservice.dtos.AssistantRequestDTO;
import com.ibosng.aiservice.dtos.AssistantResponseDTO;
import com.ibosng.aiservice.dtos.AIResponseDto;
import com.ibosng.aiservice.dtos.TrainerReplacementDto;
import com.ibosng.aiservice.enums.RequestType;
import com.ibosng.aiservice.services.AIRequesterService;
import com.ibosng.aiservice.services.AIValidationService;
import com.ibosng.aiservice.services.AssistantService;
import com.ibosng.aiservice.utils.Helpers;
import com.ibosng.dbservice.services.ai.OpenaiAssistantAnweisungService;
import com.ibosng.dbservice.services.ai.OpenaiAssistantService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.ibosng.aiservice.utils.Constants.*;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIRequesterServiceImpl implements AIRequesterService {
    private final AssistantService assistantRequestService;
    private final OpenaiAssistantAnweisungService openaiAssistantAnweisungService;
    private final OpenaiAssistantService openaiAssistantService;
    private final AssistantsClient assistantsClient;
    private final AIValidationService aiValidationService;
    
    private static final int MAX_VALIDATION_ATTEMPTS = 2;

    @Getter
    @Value("${azureOpenAIChatAssistandId:#{null}}")
    private String azureOpenAIChatAssistandId;

    @Override
    public ResponseEntity<String> processSeminarVertretungRequest(TrainerReplacementDto trainerReplacementDto) {
        AssistantResponseDTO aggregationResponse = executeDataAggregationAssistant(trainerReplacementDto);
        AssistantResponseDTO decisionResponse = executeDecisionAssistant(aggregationResponse);
                
        return ResponseEntity.ok(decisionResponse.getResponse());
    }

    private AssistantResponseDTO executeDecisionAssistant(AssistantResponseDTO aggregationResponse) {
        AssistantResponseDTO decisionResponse = null;
        List<String> validationErrors = new ArrayList<String>();
        int attempts = 0;
        
        do {
            decisionResponse = createAndExecuteDecisionRequest(
                    aggregationResponse, 
                    validationErrors, 
                    decisionResponse != null ? decisionResponse.getResponse() : null);
            
            validationErrors = aiValidationService.validateResponseForSeminarAndTrainers(
                decisionResponse.getResponse()
            );
            
            if (!validationErrors.isEmpty()) {
                log.warn("Validation errors found in decision response (attempt {}): {}", attempts + 1, validationErrors);
                attempts++;
            }
        } while (!validationErrors.isEmpty() && attempts < MAX_VALIDATION_ATTEMPTS);

        return decisionResponse;
    }
    
    private AssistantResponseDTO createAndExecuteDecisionRequest(AssistantResponseDTO aggregationResponse, List<String> validationErrors, String originalResponse) {
        String requestContent = buildRequestContent(aggregationResponse.getResponse(), validationErrors, originalResponse);
        AssistantRequestDTO request = AssistantRequestDTO.builder()
                .request(requestContent)
                .assistantId(openaiAssistantService.findByAssistantName(TRAINER_ELECTION_ASSISTANT).getAssistantId())
                .type(RequestType.TRAINER_ELECTION)
                .isNewThread(true)
                .deleteThreadAfterProcessing(true)
                .toolCallResults(aggregationResponse.getToolCallResults())
                .build();
        
        return assistantRequestService.processRequest(request);
    }
    
    private String buildRequestContent(String aggregationResponseContent, List<String> validationErrors, String originalResponse) {
        if (originalResponse == null || validationErrors.isEmpty()) {
            return aggregationResponseContent;
        } 
        
        return "The previous response had the following errors, please correct them:\n" + 
            String.join("\n", validationErrors) + "\n\n" +
            "Original response: " + originalResponse;
    }

    private AssistantResponseDTO executeDataAggregationAssistant(TrainerReplacementDto trainerReplacementDto) {
        AssistantRequestDTO request = AssistantRequestDTO.builder()
                .request(Helpers.serializeObject(trainerReplacementDto))
                .assistantId(openaiAssistantService.findByAssistantName(INFORMATION_AGGREGATOR_ASSISTANT).getAssistantId())
                .type(RequestType.SEMINAR_VERTRETUNG)
                .isNewThread(true)
                .deleteThreadAfterProcessing(true)
                .build();
        
        return assistantRequestService.processRequest(request);
    }

    @Override
    public ResponseEntity<String> getAnweisung(String assistantName) {
        String anweisung = openaiAssistantAnweisungService.getLatestAnweisung(assistantName);

        if (anweisung == null || anweisung.isBlank()) {
            log.warn("No 'Anweisung' found for assistant: {}", assistantName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No Anweisung found for assistant: " + assistantName);
        }

        return ResponseEntity.ok(anweisung);
    }

    @Override
    public ResponseEntity<Void> setAnweisung(String assistantName, String anweisung, String user) {
        String decodedAnweisung = URLDecoder.decode(anweisung, StandardCharsets.UTF_8);
        String decodedAssistantName = URLDecoder.decode(assistantName, StandardCharsets.UTF_8);
        String decodedUser = URLDecoder.decode(user, StandardCharsets.UTF_8);
        
        try {
            openaiAssistantAnweisungService.createNewAnweisung(decodedAssistantName, decodedAnweisung, decodedUser);
            log.info("Successfully set 'Anweisung' for assistant: {}", assistantName);
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } catch (Exception e) {
            log.error("Error setting 'Anweisung' for assistant: {}", assistantName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @Override
    public ResponseEntity<Object> deleteThreadId(String threadId) {
        try {
            log.info("Deleting thread with ID: {}", threadId);
            ThreadDeletionStatus deletionStatus = assistantsClient.deleteThread(threadId);
            return ResponseEntity.ok(deletionStatus);
        } catch (Exception e) {
            log.error("Error deleting thread: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<AIResponseDto> processChatRequest(AIResponseDto chatRequest) {
        String assistantId = openaiAssistantService.findByAssistantName(CHAT_ASSISTANT).getAssistantId();
        AssistantRequestDTO assistantRequest = AssistantRequestDTO.builder()
                .request(chatRequest.getRequest())
                .assistantId(assistantId)
                .type(RequestType.CHAT)
                .isNewThread(chatRequest.getThreadId() == null)
                .deleteThreadAfterProcessing(false)
                .build();

        if (chatRequest == null || isNullOrBlank(chatRequest.getRequest())) {
            return ResponseEntity.notFound().build();
        }

        if (!isNullOrBlank(chatRequest.getThreadId())) {
            assistantRequest.setThreadId(chatRequest.getThreadId());
            assistantRequest.setNewThread(false);
        }

        AssistantResponseDTO response = assistantRequestService.processRequest(assistantRequest);
        chatRequest.setResponse(response.getResponse());
        chatRequest.setThreadId(response.getThreadId());

        return ResponseEntity.ok(chatRequest);
    }
}