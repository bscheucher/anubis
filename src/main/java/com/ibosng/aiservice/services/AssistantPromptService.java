package com.ibosng.aiservice.services;

import com.ibosng.aiservice.enums.RequestType;

public interface AssistantPromptService {
    String getPrompt(RequestType type);
}
