package com.ibosng.gatewayservice.controllers;

import com.ibosng.aiservice.dtos.AIResponseDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.AIEngineService;
import com.ibosng.gatewayservice.services.Gateway2AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/ai-engine")
@Tag(name = "AI controller")
@RequiredArgsConstructor
public class AIController {

    private final Gateway2AiService gateway2AiService;
    private final AIEngineService aiEngineService;

    @PostMapping("generic-request")
    @Operation(
            summary = "This method is used to send a request to the AI engine",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<String> sendGenericRequest(@RequestBody String request) {
        log.info("Send request to the AI engine: {}", request);
        return gateway2AiService.sendGenericRequest(request);
    }

    @GetMapping("seminar-vertretung-request")
    @Operation(
            summary = "This method is used to send a request to the AI engine",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<String> sendSeminarVertretungRequest(@RequestParam String trainerVorname,
                                                                @RequestParam String trainerNachname,
                                                               @RequestParam String von,
                                                               @RequestParam String bis) {
        log.info("Send request to the AI engine with params: {}, {}, {}, {}", trainerVorname, trainerNachname, von, bis);
        return gateway2AiService.sendSeminarVertretungRequest(trainerVorname, trainerNachname, von, bis);
    }

    @GetMapping("information-aggregator-request")
    @Operation(
            summary = "This method is used to send a request to the AI engine",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<String> sendInformationsAggregatorRequest(@RequestParam String trainerVorname,
                                                               @RequestParam String trainerNachname,
                                                               @RequestParam String von,
                                                               @RequestParam String bis) {
        log.info("Send request to the AI engine with params: {}, {}, {}, {}", trainerVorname, trainerNachname, von, bis);
        return gateway2AiService.sendInformationsAggregatorRequest(trainerVorname, trainerNachname, von, bis);
    }

    @GetMapping("seminar-vertretung")
    @Operation(
            summary = "This method is used to send a request to the AI engine",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> sendSeminarVertretung(@RequestParam String trainerId,
                                                                        @RequestParam String von,
                                                                        @RequestParam String bis) {
        log.info("Send request to the AI engine with params: {}, {}, {}", trainerId, von, bis);
        return aiEngineService.getSeminarVertretungRequest(trainerId, von, bis);
    }

    @PostMapping("chat-request")
    @Operation(
            summary = "This method is used to send a request to the AI engine",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<AIResponseDto> sendChatRequest(@RequestBody AIResponseDto request) {
        log.info("Send request to the AI engine: {}", request);
        return gateway2AiService.sendChatRequest(request);
    }

    @PostMapping(value = "set-anweisung")
    @Operation(
            summary = "This method is used set anweisung for an assistant",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<Void> setAnweisungForAssistant(@RequestParam String assistantName,
                                                         @RequestBody String anweisung,
                                                         @RequestParam String user) {
        log.info("Received request for new anweisung: {} for assistant {}", anweisung, assistantName);
        return gateway2AiService.setAnweisungForAssistant(assistantName, anweisung, user);
    }

    @GetMapping("get-anweisung")
    @Operation(
            summary = "This method is used set anweisung for an assistant",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<String> getAnweisungForAssistant(@RequestParam String assistantName) {
        log.info("Received request for getting anweisung for assistant {}", assistantName);
        return gateway2AiService.getAnweisungForAssistant(assistantName);
    }

    @DeleteMapping("thread")
    @Operation(
            summary = "This method is used set anweisung for an assistant",
            description = "Sends request to the AI engine",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<Void> deleteThread(@RequestParam String threadId) {
        log.info("Received request deleting thread {}", threadId);
        return gateway2AiService.deleteThread(threadId);
    }
}
