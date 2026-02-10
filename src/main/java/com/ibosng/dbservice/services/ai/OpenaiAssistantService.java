package com.ibosng.dbservice.services.ai;


import com.ibosng.dbservice.entities.ai.OpenaiAssistant;
import com.ibosng.dbservice.services.BaseService;

public interface OpenaiAssistantService extends BaseService<OpenaiAssistant> {

    OpenaiAssistant findByAssistantId(String openaiAssistantId);

    OpenaiAssistant findByAssistantName(String name);
}

