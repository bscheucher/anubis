package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Language;
import com.ibosng.dbservice.repositories.LanguageRepository;
import com.ibosng.dbservice.services.LanguageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    @Override
    public Optional<Language> findById(Integer id) {
        return languageRepository.findById(id);
    }

    @Override
    public Language save(Language object) {
        return languageRepository.save(object);
    }

    @Override
    public List<Language> saveAll(List<Language> objects) {
        return languageRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        languageRepository.deleteById(id);
    }

    @Override
    public Optional<Language> findByName(String name) {
        return languageRepository.findByName(name);
    }

    @Override
    public List<Language> findAllByIdentifier(String identifier) {
        return null;
    }
}
