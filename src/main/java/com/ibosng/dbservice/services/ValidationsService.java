package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.validations.ValidationStatus;
import com.ibosng.dbservice.entities.validations.ValidationType;
import com.ibosng.dbservice.entities.validations.Validations;

import java.util.List;

public interface ValidationsService extends BaseService<Validations> {
    List<Validations> findAllByTypeAndStatus(ValidationType type, ValidationStatus status);

    List<Validations> findAllByStatus(ValidationStatus status);

    void createInfoValidation(String message, String identifier, Integer entityId, String user);

    void createWarningValidation(String message, String identifier, Integer entityId, String user);

    void createErrorValidation(String message, String identifier, Integer entityId, String user);

    void resolveValidation(Validations validation, String user);

    List<Validations> findAllByType(ValidationType type);

    void createWarningValidation(String message, String identifier, String user);

    void createErrorValidation(String message, String identifier, String user);

    void createInfoValidation(String message, String identifier, String user);
}
