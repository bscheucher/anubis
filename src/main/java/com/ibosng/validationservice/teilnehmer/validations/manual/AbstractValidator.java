package com.ibosng.validationservice.teilnehmer.validations.manual;

public abstract class AbstractValidator<T, D> {

    public D validateDto(D dto, String parentId, String changedBy, String secondParentId) {
        T entity = validateEntity(dto, parentId, changedBy, secondParentId);
        return mapToDto(entity, dto);
    }

    protected abstract T validateEntity(D dto, String parentId, String changedBy, String secondParentId);

    protected abstract D mapToDto(T entity, D dto);
}
