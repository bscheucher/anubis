package com.ibosng.aiservice.services.impl;

import com.azure.ai.openai.assistants.AssistantsClient;
import com.azure.ai.openai.assistants.models.*;
import com.azure.json.JsonReader;
import com.ibosng.aiservice.dtos.AssistantRequestDTO;
import com.ibosng.aiservice.dtos.AssistantResponseDTO;
import com.ibosng.aiservice.dtos.AssistantResponseDTO.ToolCallResultDTO;
import com.ibosng.aiservice.dtos.tooldefiinition.ToolDefinitionDto;
import com.ibosng.aiservice.enums.RequestType;
import com.ibosng.aiservice.services.AssistantPromptService;
import com.ibosng.aiservice.services.AssistantService;
import com.ibosng.aiservice.services.ServiceMethodRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ibosng.aiservice.utils.Helpers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssistantServiceImpl implements AssistantService {
    private final static Integer NUMBER_OF_RETRIES = 150;
    private final AssistantsClient assistantsClient;

    private final ServiceMethodRegistryService serviceMethodRegistryService;
    private final AssistantPromptService assistantPromptService;

    @Override
    public AssistantResponseDTO processRequest(AssistantRequestDTO assistantRequest) {
        long startTime = System.currentTimeMillis();
        String uniqueId = UUID.randomUUID().toString();
        String modifiedPrompt = assistantPromptService.getPrompt(assistantRequest.getType()) + " [Request ID: " + uniqueId + "]";

        ThreadRun run = createAndRunThread(assistantRequest, uniqueId, modifiedPrompt);

        if (run == null) {
            return createErrorResponse(assistantRequest.getThreadId());
        }

        String runningThreadId = run.getThreadId();
        ThreadRun completedRun = pollRunStatus(run, NUMBER_OF_RETRIES);

        List<ToolCallResultDTO> toolCallResults = new ArrayList<>();
        processRunWithToolCallCollection(completedRun, runningThreadId, assistantRequest.getType(), 5, toolCallResults);

        PageableList<ThreadMessage> messagesList = assistantsClient.listMessages(runningThreadId);
        List<ThreadMessage> messages = messagesList != null ? messagesList.getData() : List.of();
        String response = processMessages(messages, run.getId(), assistantRequest.getType());

        log.info("Request completed in {} ms", System.currentTimeMillis() - startTime);

        if (assistantRequest.deleteThreadAfterProcessing()) {
            deleteThread(runningThreadId);
        }

        return AssistantResponseDTO.builder()
                .threadId(runningThreadId)
                .response(response)
                .toolCallResults(toolCallResults)
                .build();
    }

    private String processMessages(List<ThreadMessage> messages, String runId, RequestType type) {
        StringBuilder responseBuilder = new StringBuilder();

        for (ThreadMessage dataMessage : messages) {
            if (dataMessage.getRole() == MessageRole.ASSISTANT && dataMessage.getContent() != null) {
                for (MessageContent messageContent : dataMessage.getContent()) {
                    if (messageContent instanceof MessageTextContent) {
                        String responsePart = ((MessageTextContent) messageContent).getText().getValue();

                        if (isExtraneousContent(responsePart)) {
                            continue;
                        }

                        if (!responseBuilder.toString().contains(responsePart)) {
                            if (RequestType.CHAT.equals(type)) {
                                if (runId.equals(dataMessage.getRunId())) {
                                    responseBuilder.append(responsePart).append("\n");
                                }
                            } else {
                                responseBuilder.append(responsePart).append("\n");
                            }
                        }
                    }
                }
            }
        }

        return responseBuilder.toString().replaceAll(".*†source.*", "").trim();
    }

    private boolean isExtraneousContent(String content) {
        return content == null || content.trim().isEmpty() ||
                content.contains("Request ID:") ||
                content.contains("Previous tool call results:");
    }

    private ToolCallResultDTO executeToolCall(RequiredToolCall toolCall, RequestType requestType) {
        var builder = ToolCallResultDTO.builder()
                .toolId(toolCall.getId());

        if (toolCall instanceof RequiredFunctionToolCall functionToolCall) {
            RequiredFunctionToolCallDetails functionDetails = functionToolCall.getFunction();

            if (functionDetails == null) {
                log.warn("No function details found in toolCall: {}", toolCall);
                return builder.toolName("unknown")
                        .arguments(Map.of())
                        .result("Error: No function call details available")
                        .build();
            }

            String toolName = functionDetails.getName();
            String toolArguments = functionDetails.getArguments();

            log.info("Tool Name: {}", toolName);
            log.info("Tool Arguments: {}", toolArguments);

            builder.toolName(toolName);

            Map<String, Object> params = parseJsonToMap(toolArguments);
            builder.arguments(params);

            try {
                Object output = serviceMethodRegistryService.callServiceMethod(toolName, params, requestType);
                builder.result(output);
            } catch (Exception e) {
                log.error("Error executing service method for tool '{}': {}", toolName, e.getMessage());
                builder.result("Error executing tool: " + toolName + ", " + e.getMessage());
            }
        } else {
            log.error("toolCall is NOT an instance of RequiredFunctionToolCall. Actual class: {}", toolCall.getClass().getName());
            builder.toolName("unknown")
                    .arguments(Map.of())
                    .result("Error: toolCall is not a function tool call.");
        }

        return builder.build();
    }

    private ThreadRun createAndRunThread(AssistantRequestDTO assistantRequest, String uniqueId, String modifiedPrompt) {
        if (assistantRequest.isNewThread()) {
            return createNewThread(assistantRequest, uniqueId, modifiedPrompt);
        } else {
            return continueExistingThread(assistantRequest, uniqueId);
        }
    }

    private ThreadRun createNewThread(AssistantRequestDTO assistantRequest, String uniqueId, String modifiedPrompt) {
        List<ThreadMessageOptions> messages = new ArrayList<>();
        messages.add(new ThreadMessageOptions(MessageRole.USER, convertToMessageContent(assistantRequest.getRequest()) + " (Request ID: " + uniqueId + ")"));
        messages.add(new ThreadMessageOptions(MessageRole.ASSISTANT, modifiedPrompt));

        if (assistantRequest.hasToolCallResults()) {
            messages.add(createToolCallResultsMessage(assistantRequest.getToolCallResults()));
        }

        CreateAndRunThreadOptions createAndRunThreadOptions = new CreateAndRunThreadOptions(assistantRequest.getAssistantId())
                .setThread(new AssistantThreadCreationOptions().setMessages(messages));

        setTools(createAndRunThreadOptions, assistantRequest.getType());
        return assistantsClient.createThreadAndRun(createAndRunThreadOptions);
    }

    private ThreadMessageOptions createToolCallResultsMessage(List<ToolCallResultDTO> toolCallResults) {
        StringBuilder toolCallMessage = new StringBuilder("Previous tool call results:\n");
        for (ToolCallResultDTO result : toolCallResults) {
            toolCallMessage.append(String.format("- %s: %s\n", result.getToolName(), serializeObject(result.getResult())));
        }
        return new ThreadMessageOptions(MessageRole.ASSISTANT, toolCallMessage.toString());
    }

    private ThreadRun continueExistingThread(AssistantRequestDTO assistantRequest, String uniqueId) {
        checkAndWaitForActiveRuns(assistantRequest.getThreadId());

        ThreadMessageOptions userMessage = new ThreadMessageOptions(MessageRole.USER,
                convertToMessageContent(assistantRequest.getRequest()) + " (Request ID: " + uniqueId + ")");
        assistantsClient.createMessage(assistantRequest.getThreadId(), userMessage);

        CreateRunOptions createRunOptions = new CreateRunOptions(assistantRequest.getAssistantId());
        createRunOptions.setAdditionalInstructions("Treat this as a new, independent request. Request ID: " + uniqueId);

        return assistantsClient.createRun(assistantRequest.getThreadId(), createRunOptions);
    }

    private void checkAndWaitForActiveRuns(String threadId) {
        PageableList<ThreadRun> runs = assistantsClient.listRuns(threadId);
        if (runs != null && runs.getData() != null) {
            for (ThreadRun existingRun : runs.getData()) {
                if (existingRun.getStatus() == RunStatus.IN_PROGRESS ||
                        existingRun.getStatus() == RunStatus.QUEUED ||
                        existingRun.getStatus() == RunStatus.REQUIRES_ACTION) {

                    log.warn("Active run detected on thread {}. Waiting for it to complete...", threadId);
                    pollRunStatus(existingRun, NUMBER_OF_RETRIES);
                }
            }
        }
    }

    private void processRunWithToolCallCollection(ThreadRun run, String threadId, RequestType type, int maxIterations,
                                                  List<ToolCallResultDTO> toolCallResults) {
        ThreadRun currentRun = run;
        int currentIteration = 0;

        log.info("Starting processRunWithToolCallCollection with maxIterations={}", maxIterations);

        while (currentIteration < maxIterations) {
            log.info("Iteration {}/{}: Run status is {}", currentIteration + 1, maxIterations, currentRun.getStatus());

            if (currentRun.getStatus().equals(RunStatus.COMPLETED)) {
                log.info("Run is COMPLETED. Exiting loop.");
                break;
            }

            if (currentRun.getStatus().equals(RunStatus.FAILED)) {
                log.error("Run FAILED with error: {}",
                        currentRun.getLastError() != null ? currentRun.getLastError().getMessage() : "Unknown error");
                break;
            }

            if (currentRun.getStatus().equals(RunStatus.REQUIRES_ACTION) &&
                    currentRun.getRequiredAction() instanceof SubmitToolOutputsAction requiredAction) {

                List<ToolOutput> toolOutputs = new ArrayList<>();

                for (RequiredToolCall toolCall : requiredAction.getSubmitToolOutputs().getToolCalls()) {
                    var toolCallResult = executeToolCall(toolCall, type);
                    toolCallResults.add(toolCallResult);

                    toolOutputs.add(new ToolOutput()
                            .setToolCallId(toolCall.getId())
                            .setOutput(serializeObject(toolCallResult.getResult())));
                }

                log.info("Submitting tool outputs for iteration {}/{}", currentIteration + 1, maxIterations);
                currentRun = assistantsClient.submitToolOutputsToRun(threadId, currentRun.getId(), toolOutputs);
                log.info("Successfully submitted tool outputs. Updated Run ID: {}, Status: {}",
                        currentRun.getId(), currentRun.getStatus());

                currentRun = pollRunStatus(currentRun, NUMBER_OF_RETRIES);
                currentIteration++;
            } else {
                log.info("Run is in state {} which doesn't require action. Exiting loop.", currentRun.getStatus());
                break;
            }
        }

        if (currentIteration >= maxIterations) {
            log.warn("Reached maximum number of iterations ({}). Stopping processing.", maxIterations);
        }
    }

    private void setTools(CreateAndRunThreadOptions createAndRunThreadOptions, RequestType type) {
        List<ToolDefinitionDto> tools = serviceMethodRegistryService.buildToolDefinitions(type);

        if (tools != null && !tools.isEmpty()) {
            List<ToolDefinition> mappedTools = tools.stream()
                    .map(tool -> {
                        try {
                            JsonReader jsonReader = createJsonReaderFromDto(tool);
                            if (jsonReader != null) {
                                return ToolDefinition.fromJson(jsonReader);
                            }
                        } catch (IOException e) {
                            log.error("Error while trying to create ToolDefinition: ", e);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!mappedTools.isEmpty()) {
                createAndRunThreadOptions.setTools(mappedTools);
            }
        }

/*        // 💡 Always add file_search for chat
        if (RequestType.CHAT.equals(type)) {
            String vectorStoreId = aiFilesService.getOrCreateVectorStore();
            aiFilesService.logVectorStoreFileStatuses(vectorStoreId);
            createAndRunThreadOptions.setToolResources(aiFilesService.getUpdateToolResources(vectorStoreId));
        }*/
    }

    private ThreadRun pollRunStatus(ThreadRun run, int retries) {
        if (retries <= 0) {
            throw new RuntimeException("Max retries exceeded while polling status.");
        }
        while (retries > 0) {
            run = assistantsClient.getRun(run.getThreadId(), run.getId());

            if (run.getStatus() != RunStatus.QUEUED && run.getStatus() != RunStatus.IN_PROGRESS) {
                log.info("Final status: {}, Thread ID: {}, Run ID: {}", run.getStatus(), run.getThreadId(), run.getId());
                return run;
            }
            if (run.getStatus() == RunStatus.FAILED) {
                log.warn("Request failed, last error message {}", run.getLastError().getMessage());
            }

            log.info("Status still pending: {}, Thread ID: {}, Run ID: {}", run.getStatus(), run.getThreadId(), run.getId());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for run status update", e);
            }
            retries--;
        }
        throw new RuntimeException("Max retries exceeded while polling status.");
    }

    private AssistantResponseDTO createErrorResponse(String threadId) {
        return AssistantResponseDTO.builder()
                .threadId(threadId)
                .response("Error: Failed to process assistant request.")
                .build();
    }

    private void deleteThread(String threadId) {
        try {
            log.info("Deleting thread {} after processing", threadId);
            assistantsClient.deleteThread(threadId);
        } catch (Exception e) {
            log.error("Failed to delete thread {}: {}", threadId, e.getMessage());
        }
    }
}