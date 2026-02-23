package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public abstract class OptionalFieldValidator<T, V> extends AbstractValidation<T, V> {
    private final AbstractValidation<T, V> validator;

    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean executeValidation(T objectT, V objectV) {
        if (isOptional() && setFieldsEmpty(objectT, objectV)) {
            return true;
        }
        return validator.executeValidation(objectT, objectV);
    }

    @Override
    public Set<TeilnehmerSource> getSources() {
        return validator.getSources();
    }

    @Override
    public void setSources(Set<TeilnehmerSource> sources) {
        validator.setSources(sources);
    }

    @Override
    public boolean shouldValidationRun() {
        return validator.shouldValidationRun();
    }

    public abstract boolean setFieldsEmpty(T objectT, V objectV);
}