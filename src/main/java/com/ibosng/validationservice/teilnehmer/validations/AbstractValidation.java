package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.validationservice.Validation;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public abstract class AbstractValidation<T, V> implements Validation<T, V> {
    @Getter
    @Setter
    protected Set<TeilnehmerSource> sources;

    public abstract boolean executeValidation(T objectT, V objectV);

    public boolean shouldValidationRun() {
        return true;
    }
}
