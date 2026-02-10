package com.ibosng.dbservice.services.impl.ai;

import com.ibosng.dbservice.entities.ai.OpenaiAssistant;
import com.ibosng.dbservice.repositories.ai.OpenaiAssistantRepository;
import com.ibosng.dbservice.services.ai.OpenaiAssistantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpenaiAssistantServiceImpl implements OpenaiAssistantService {

    private final OpenaiAssistantRepository openaiAssistantRepository;

    public OpenaiAssistantServiceImpl(OpenaiAssistantRepository openaiAssistantRepository) {
        this.openaiAssistantRepository = openaiAssistantRepository;
    }

    @Override
    public List<OpenaiAssistant> findAll() {
        return openaiAssistantRepository.findAll();
    }

    @Override
    public Optional<OpenaiAssistant> findById(Integer id) {
        return openaiAssistantRepository.findById(id);
    }

    @Override
    public OpenaiAssistant save(OpenaiAssistant object) {
        return openaiAssistantRepository.save(object);
    }

    @Override
    public List<OpenaiAssistant> saveAll(List<OpenaiAssistant> objects) {
        return openaiAssistantRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        openaiAssistantRepository.deleteById(id);
    }

    @Override
    public List<OpenaiAssistant> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public OpenaiAssistant findByAssistantId(String openaiAssistantId) {
        return openaiAssistantRepository.findByAssistantId(openaiAssistantId);
    }

    @Override
    public OpenaiAssistant findByAssistantName(String name) {
        return openaiAssistantRepository.findByAssistantName(name);
    }
}
