package com.ibosng.gatewayservice.enums;

import lombok.Getter;

@Getter
public enum BerufNodeTypes {
    KATEGORIE("Kategorie"),
    BERUFSFELD("Berufsfeld"),
    BERUF("Beruf"),
    SPEZIALISIERUNG("Spezialisierung");

    private final String value;

    public static BerufNodeTypes fromValue(String value) {
        for (BerufNodeTypes request : BerufNodeTypes.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    BerufNodeTypes(String value) {
        this.value = value;
    }
}
