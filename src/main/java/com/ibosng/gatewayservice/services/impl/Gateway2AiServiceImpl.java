package com.ibosng.gatewayservice.services.impl;

import com.ibosng.aiservice.services.AIRequesterService;
import com.ibosng.aiservice.services.AIRoutingService;
import com.ibosng.aiservice.dtos.AIResponseDto;
import com.ibosng.gatewayservice.dtos.ai.FinalRecommendationResponseDto;
import com.ibosng.gatewayservice.services.Gateway2AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class Gateway2AiServiceImpl implements Gateway2AiService {
    private final AIRoutingService aiRoutingService;
    private final AIRequesterService aiRequesterService;

    public Gateway2AiServiceImpl(AIRoutingService aiRoutingService, AIRequesterService aiRequesterService) {
        this.aiRoutingService = aiRoutingService;
        this.aiRequesterService = aiRequesterService;
    }

    @Override
    public ResponseEntity<String> sendGenericRequest(String request) {
        log.info("Calling ai service for generic request: {}", request);
        return aiRoutingService.sendRequest(request);
    }

    @Override
    public ResponseEntity<String> sendSeminarVertretungRequest(String trainerVorname, String trainerNachname, String von, String bis) {
        log.info("Calling ai service for seminar vertretung request with params {}, {}, {}, {}", trainerVorname, trainerNachname, von, bis);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<FinalRecommendationResponseDto> getSeminarVertretungRequest(String trainerId, String von, String bis) {
        log.info("Calling ai service for seminar vertretung request with params {}, {}, {}, {}", trainerId, von, bis);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<String> sendInformationsAggregatorRequest(String trainerVorname, String trainerNachname, String von, String bis) {
        log.info("Calling ai service for informationsaggregator request with params {}, {}, {}, {}", trainerVorname, trainerNachname, von, bis);
        return  new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Map<String, String>> getAllTrainersRequest() {
        log.info("Processing get all trainers request");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<AIResponseDto> sendChatRequest(AIResponseDto request) {
        log.info("Calling ai service for chat request: {}", request);
        return aiRequesterService.processChatRequest(request);
    }

    @Override
    public ResponseEntity<String> getAnweisungForAssistant(String assistantName) {
        log.info("Calling ai service for getting anweisung for assistant: {}", assistantName);
        return aiRequesterService.getAnweisung(assistantName);
    }

    @Override
    public ResponseEntity<Void> setAnweisungForAssistant(String assistantName, String anweisung, String user) {
        log.info("Calling ai service for setting anweisung {} for assistant: {}", anweisung, assistantName);
        return aiRequesterService.setAnweisung(assistantName, anweisung, user);
    }

    @Override
    public ResponseEntity<Void> deleteThread(String threadId) {
        log.info("Calling ai service for deleting thread: {}", threadId);
        ResponseEntity<Object> result = aiRequesterService.deleteThreadId(threadId);

        if (result.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(result.getStatusCode()).build();
        }
    }

}
