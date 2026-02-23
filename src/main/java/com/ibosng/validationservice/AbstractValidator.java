package com.ibosng.validationservice;

import com.ibosng.dbservice.services.BaseService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public abstract class AbstractValidator<T, V> {

    @Getter
    @Setter
    protected String identifier;
    @Getter
    @Setter
    public List<T> objectsToValidate = new ArrayList<>();

    @Getter
    @Setter
    private String changedBy;

    @Getter
    @Setter
    protected List<Validation<T, V>> validations = new ArrayList<>();
    protected BaseService<T> baseServiceT;
    protected BaseService<V> baseServiceV;

    @Getter
    @Setter
    protected Map<ValidationObjectPair<T, V>, Boolean> validationResults = new HashMap<>();

    public AbstractValidator(BaseService<T> baseServiceT, BaseService<V> baseServiceV) {
        this.baseServiceT = baseServiceT;
        this.baseServiceV = baseServiceV;
    }

    protected void prepare() {
        this.objectsToValidate = baseServiceT.findAllByIdentifier(getIdentifier());
    }

    protected void addValidation(Validation<T, V> validation) {
        validations.add(validation);
    }

    protected void executeValidations() {
        for (T object : objectsToValidate) {
            V validatedObject = null;
            validationResults.put(new ValidationObjectPair<>(object, validatedObject), executeValidationsForOneObject(object, validatedObject));
        }
    }

    protected boolean executeValidationsForOneObject(T object, V validatedObject) {
        boolean allValid = true;
        for (Validation<T, V> validation : validations) {
            try {
                boolean isValid = executeSingleValidationForSingleObject(validation, object, validatedObject);
                if (!isValid) {
                    allValid = false;
                }
            } catch (Exception e) {
                log.error("Exception caught while executing validation {} with exception {} for object {}", validation.getClass().getSimpleName(), e.getMessage(), Objects.toString(object, "null value"));
            }
        }
        return allValid;
    }

    protected boolean executeSingleValidationForSingleObject(Validation<T, V> validation, T object, V validatedObject) {
        return validation.executeValidation(object, validatedObject);
    }

    protected abstract List<V> postProcess();

    public List<V> process() {
        prepare();
        executeValidations();
        return postProcess();
    }
}
