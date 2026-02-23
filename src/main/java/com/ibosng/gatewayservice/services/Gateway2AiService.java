package com.ibosng.gatewayservice.services;

import com.ibosng.aiservice.dtos.AIResponseDto;
import com.ibosng.gatewayservice.dtos.ai.FinalRecommendationResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface Gateway2AiService {

    ResponseEntity<String> sendGenericRequest(String request);
    ResponseEntity<String> sendSeminarVertretungRequest(String trainerVorname, String trainerNachname, String von, String bis);
    ResponseEntity<FinalRecommendationResponseDto> getSeminarVertretungRequest(String trainerId, String von, String bis);
    ResponseEntity<String> sendInformationsAggregatorRequest(String trainerVorname, String trainerNachname, String von, String bis);

    ResponseEntity<Map<String, String>> getAllTrainersRequest();

    ResponseEntity<AIResponseDto> sendChatRequest(AIResponseDto request);
    ResponseEntity<String> getAnweisungForAssistant(String assistantName);
    ResponseEntity<Void> setAnweisungForAssistant(String assistantName, String anweisung, String user);
    ResponseEntity<Void> deleteThread(String threadId);
}
