package com.ibosng.dbservice.entities.zeitbuchung;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Leistungstyp {
    LEISTUNG("Leistung"), SPESEN("Spesen");

    private final String value;

    public static Leistungstyp fromValue(String value) {
        for (Leistungstyp request : Leistungstyp.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
