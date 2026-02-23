package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Label;
import com.ibosng.dbservice.repositories.LabelRepository;
import com.ibosng.dbservice.services.LabelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public List<Label> findAll() {
        return labelRepository.findAll();
    }

    @Override
    public List<Label> findAllByLanguage(String language) {
        return labelRepository.findAllByLanguage(language);
    }

    @Override
    public Optional<Label> findById(Integer id) {
        return labelRepository.findById(id);
    }

    @Override
    public Label save(Label object) {
        return labelRepository.save(object);
    }

    @Override
    public List<Label> saveAll(List<Label> objects) {
        return labelRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        labelRepository.deleteById(id);
    }

    @Override
    public Optional<Label> findByLabelKey(String key) {
        return labelRepository.findByLabelKey(key);
    }

    @Override
    public List<Label> findAllByIdentifier(String identifier) {
        return null;
    }
}
