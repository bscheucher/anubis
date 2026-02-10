package com.ibosng.dbibosservice.converters;

import com.ibosng.dbibosservice.enums.BooleanStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanStatusConverter implements AttributeConverter<BooleanStatus, String> {

    @Override
    public String convertToDatabaseColumn(BooleanStatus status) {
        return (status == null) ? null : status.name();
    }

    @Override
    public BooleanStatus convertToEntityAttribute(String value) {
        return BooleanStatus.fromValue(value);
    }
}