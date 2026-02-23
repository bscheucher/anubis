package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.validations.ValidationStatus;
import com.ibosng.dbservice.entities.validations.ValidationType;
import com.ibosng.dbservice.entities.validations.Validations;
import com.ibosng.dbservice.repositories.ValidationsRepository;
import com.ibosng.dbservice.services.ValidationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidationsServiceImpl implements ValidationsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationsServiceImpl.class);

    private final ValidationsRepository validationsRepository;

    public ValidationsServiceImpl(ValidationsRepository validationsRepository) {
        this.validationsRepository = validationsRepository;
    }

    @Override
    public List<Validations> findAll() {
        return validationsRepository.findAll();
    }

    @Override
    public Optional<Validations> findById(Integer id) {
        return validationsRepository.findById(id);
    }

    @Override
    public Validations save(Validations object) {
        return validationsRepository.save(object);
    }

    @Override
    public List<Validations> saveAll(List<Validations> validations) {
        return validationsRepository.saveAll(validations);
    }

    @Override
    public void deleteById(Integer id) {
        validationsRepository.deleteById(id);
    }

    @Override
    public List<Validations> findAllByIdentifier(String identifier) {
        return validationsRepository.findAllByIdentifier(identifier);
    }

    @Override
    public List<Validations> findAllByTypeAndStatus(ValidationType type, ValidationStatus status) {
        return validationsRepository.findAllByTypeAndStatus(type, status);
    }

    @Override
    public List<Validations> findAllByStatus(ValidationStatus status) {
        return validationsRepository.findAllByStatus(status);
    }


    @Override
    public void createInfoValidation(String message, String identifier, Integer entityId, String user) {
        validationsRepository.save(new Validations(ValidationType.INFO, ValidationStatus.RESOLVED, message, identifier, entityId, user));
    }

    @Override
    public void createWarningValidation(String message, String identifier, Integer entityId, String user) {
        validationsRepository.save(new Validations(ValidationType.WARNING, ValidationStatus.UNRESOLVED, message, identifier, entityId, user));
    }


    @Override
    public void createErrorValidation(String message, String identifier, Integer entityId, String user) {
        validationsRepository.save(new Validations(ValidationType.ERROR, ValidationStatus.UNRESOLVED, message, identifier, entityId, user));
    }

    @Override
    public void resolveValidation(Validations validation, String user) {
        validation.setStatus(ValidationStatus.RESOLVED);
        validation.setChangedBy(user);
        validationsRepository.save(validation);
    }

    @Override
    public List<Validations> findAllByType(ValidationType type) {
        return validationsRepository.findAllByType(type);
    }

    @Override
    public void createWarningValidation(String message, String identifier, String user) {
        createWarningValidation(message, identifier, null, user);
    }

    @Override
    public void createErrorValidation(String message, String identifier, String user) {
        createErrorValidation(message, identifier, null, user);
    }

    @Override
    public void createInfoValidation(String message, String identifier, String user) {
        createInfoValidation(message, identifier, null, user);
    }
}
