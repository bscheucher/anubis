package com.ibosng.validationservice;

public interface Validation<T, V> {

    boolean executeValidation(T objectT, V objectV);

}
