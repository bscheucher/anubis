package com.ibosng.aiservice.services;

import com.ibosng.aiservice.dtos.AIResponseDto;
import com.ibosng.aiservice.dtos.TrainerReplacementDto;
import org.springframework.http.ResponseEntity;

public interface AIRequesterService {

    ResponseEntity<String> processSeminarVertretungRequest(TrainerReplacementDto trainerReplacementDto);

    ResponseEntity<String> getAnweisung(String assistantName);

    ResponseEntity<Void> setAnweisung(String assistantName, String anweisung, String user);

    ResponseEntity<Object> deleteThreadId(String threadId);

    ResponseEntity<AIResponseDto> processChatRequest(AIResponseDto chatRequest);
}
