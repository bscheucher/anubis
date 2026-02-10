package com.ibosng.dbservice.services.impl.ai;

import com.ibosng.dbservice.entities.ai.AiUiShortcut;
import com.ibosng.dbservice.repositories.ai.AiUiShortcutRepository;
import com.ibosng.dbservice.services.ai.AiUiShortcutService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AiUiShortcutServiceImpl implements AiUiShortcutService {

    private final AiUiShortcutRepository aiUiShortcutRepository;

    public AiUiShortcutServiceImpl(AiUiShortcutRepository aiUiShortcutRepository) {
        this.aiUiShortcutRepository = aiUiShortcutRepository;
    }

    @Override
    public List<AiUiShortcut> findAll() {
        return aiUiShortcutRepository.findAll();
    }

    @Override
    public Optional<AiUiShortcut> findById(Integer id) {
        return aiUiShortcutRepository.findById(id);
    }

    @Override
    public AiUiShortcut save(AiUiShortcut object) {
        return aiUiShortcutRepository.save(object);
    }

    @Override
    public List<AiUiShortcut> saveAll(List<AiUiShortcut> objects) {
        return aiUiShortcutRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        aiUiShortcutRepository.deleteById(id);
    }

    @Override
    public List<AiUiShortcut> findAllByIdentifier(String identifier) {
        return null;
    }
}
