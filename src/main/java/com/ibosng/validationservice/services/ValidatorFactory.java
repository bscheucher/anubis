package com.ibosng.validationservice.services;

import com.ibosng.validationservice.AbstractValidator;
import com.ibosng.validationservice.SingleEntityAbstractValidator;

public interface ValidatorFactory {
    <T, V> AbstractValidator<T, V> createValidator(String identifier, Class<T> typeT, Class<V> typeV);
    <T, V> SingleEntityAbstractValidator<T, V> createSingleEntityValidator(Class<T> typeT, Class<V> typeV);
}

