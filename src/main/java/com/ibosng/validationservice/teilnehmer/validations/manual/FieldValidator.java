package com.ibosng.validationservice.teilnehmer.validations.manual;

public interface FieldValidator<D> {
    void validate(D dto, Integer parentId);
}