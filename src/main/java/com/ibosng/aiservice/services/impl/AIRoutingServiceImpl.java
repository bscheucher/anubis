package com.ibosng.aiservice.services.impl;

import com.ibosng.aiservice.dtos.AssistantRequestDTO;
import com.ibosng.aiservice.dtos.AssistantResponseDTO;
import com.ibosng.aiservice.dtos.TrainerReplacementDto;
import com.ibosng.aiservice.enums.RequestType;
import com.ibosng.aiservice.services.AssistantService;
import com.ibosng.aiservice.services.AIRoutingService;
import com.ibosng.aiservice.services.AIRequesterService;
import com.ibosng.dbservice.services.ai.OpenaiAssistantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.ibosng.aiservice.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIRoutingServiceImpl implements AIRoutingService {
    private final AssistantService assistantService;
    private final AIRequesterService aiRequesterService;
    private final OpenaiAssistantService openaiAssistantService;

    @Override
    public <T> ResponseEntity<String> sendRequest(T request) {
        String assistantId;
        RequestType requestType = findMatchingRoute(request);

        switch (requestType) {
            case CHAT -> {
                assistantId = openaiAssistantService.findByAssistantName(CHAT_ASSISTANT).getAssistantId();
                AssistantResponseDTO response = assistantService.processRequest(
                    AssistantRequestDTO.builder()
                        .request(request.toString())
                        .assistantId(assistantId)
                        .type(RequestType.CHAT)
                        .isNewThread(true)
                        .build()
                );
                return ResponseEntity.ok(response.getResponse());
            }
            case SEMINAR_VERTRETUNG -> {                
                if (request instanceof TrainerReplacementDto trainerReplacementDto) {
                    return aiRequesterService.processSeminarVertretungRequest(trainerReplacementDto);
                }

                return ResponseEntity.badRequest().body("Invalid request type for seminar vertretung");
            }
            default -> {
                assistantId = openaiAssistantService.findByAssistantName(CHAT_ASSISTANT).getAssistantId();
                AssistantResponseDTO response = assistantService.processRequest(
                    AssistantRequestDTO.builder()
                        .request(request.toString())
                        .assistantId(assistantId)
                        .type(RequestType.CHAT)
                        .isNewThread(true)
                        .build()
                );
                return ResponseEntity.ok(response.getResponse());
            }
        }
    }

    private <T> RequestType findMatchingRoute(T request) {
        log.info("Calling the Routing assistant");

        String assistantId = openaiAssistantService.findByAssistantName(ROUTING_ASSISTANT).getAssistantId();
        AssistantResponseDTO response = assistantService.processRequest(
            AssistantRequestDTO.builder()
                .request(request.toString())
                .assistantId(assistantId)
                .type(RequestType.ROUTING)
                .isNewThread(true)
                .build()
        );
        
        if (response != null) {
            RequestType requestType = RequestType.fromValue(response.getResponse());
            if (requestType == null) {
                log.warn("No route found for request {}", request);
            }
            return requestType;
        }
        return null;
    }
}
