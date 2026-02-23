package com.ibosng.dbservice.entities.reports;

import lombok.Getter;

@Getter
public enum ReportParamType {
    STRING("string"),
    DATE("date"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    DECIMAL("decimal"),
    TIME("time");

    private final String value;

    public static ReportParamType fromValue(String value) {
        for (ReportParamType request : ReportParamType.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
    ReportParamType(String value) {
        this.value = value;
    }
}
