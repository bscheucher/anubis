package com.ibosng.dbibosservice.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class YesNoConverter implements
        AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return attribute != null && attribute ? "y" : "n";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "y".equalsIgnoreCase(dbData);
    }
}
