package com.ibosng.dbservice.services.ai;


import com.ibosng.dbservice.entities.ai.OpenaiAssistantAnweisung;
import com.ibosng.dbservice.services.BaseService;

public interface OpenaiAssistantAnweisungService extends BaseService<OpenaiAssistantAnweisung> {

    void createNewAnweisung(String assistantName, String anweisung, String createdBy);

    String getLatestAnweisung(String assistantName);
}

