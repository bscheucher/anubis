package com.ibosng.dbservice.services.impl.ai;


import com.ibosng.dbservice.entities.ai.OpenaiAssistant;
import com.ibosng.dbservice.entities.ai.OpenaiAssistantAnweisung;
import com.ibosng.dbservice.repositories.ai.OpenaiAssistantAnweisungRepository;
import com.ibosng.dbservice.services.ai.OpenaiAssistantAnweisungService;
import com.ibosng.dbservice.services.ai.OpenaiAssistantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;


@Service
@RequiredArgsConstructor
@Slf4j
public class OpenaiAssistantAnweisungServiceImpl implements OpenaiAssistantAnweisungService {

    private final OpenaiAssistantAnweisungRepository openaiAssistantAnweisungRepository;
    private final OpenaiAssistantService openaiAssistantService;

    @Override
    public List<OpenaiAssistantAnweisung> findAll() {
        return openaiAssistantAnweisungRepository.findAll();
    }

    @Override
    public Optional<OpenaiAssistantAnweisung> findById(Integer id) {
        return openaiAssistantAnweisungRepository.findById(id);
    }

    @Override
    public OpenaiAssistantAnweisung save(OpenaiAssistantAnweisung object) {
        return openaiAssistantAnweisungRepository.save(object);
    }

    @Override
    public List<OpenaiAssistantAnweisung> saveAll(List<OpenaiAssistantAnweisung> objects) {
        return openaiAssistantAnweisungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        openaiAssistantAnweisungRepository.deleteById(id);
    }

    @Override
    public List<OpenaiAssistantAnweisung> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public void createNewAnweisung(String assistantName, String anweisung, String createdBy) {
        // Step 1: Find the Assistant by Name
        OpenaiAssistant assistant = openaiAssistantService.findByAssistantName(assistantName);
        if (assistant != null) {
            Integer latestVersion = openaiAssistantAnweisungRepository.findMaxVersionByOpenaiAssistantId_AssistantName(assistantName);
            latestVersion = (latestVersion == null) ? 0 : latestVersion; // Default to 0 if no version exists

            // Step 3: Create a new Anweisung entry with an incremented version
            OpenaiAssistantAnweisung newAnweisung = new OpenaiAssistantAnweisung();
            newAnweisung.setOpenaiAssistantId(assistant);
            newAnweisung.setAnweisung(anweisung);
            newAnweisung.setVersion(latestVersion + 1);
            newAnweisung.setCreatedBy(createdBy);
            openaiAssistantAnweisungRepository.save(newAnweisung);
        }
    }

    @Override
    public String getLatestAnweisung(String assistantName) {
        Integer latestVersion = openaiAssistantAnweisungRepository.findMaxVersionByOpenaiAssistantId_AssistantName(assistantName);
        if (latestVersion == null) {
            log.warn("No Anweisung found for assistant: {}", assistantName);
            return null;
        }

        OpenaiAssistantAnweisung anweisung = openaiAssistantAnweisungRepository.findByOpenaiAssistantId_AssistantNameAndVersion(assistantName, latestVersion);
        if (anweisung == null) {
            log.warn("Anweisung not found for assistant: {} with version: {}", assistantName, latestVersion);
        }
        if(!isNullOrBlank(anweisung.getAnweisung())) {
            return anweisung.getAnweisung();
        }
        return null;
    }
}
