package com.ibosng.dbservice.entities.zeitbuchung;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Leistungsort {

    OFFICE("Office"), HOMEOFFICE("Homeoffice"), MOBILE_WORKING("Mobile_Working");

    private final String value;

    public static Leistungsort fromValue(String value) {
        for (Leistungsort request : Leistungsort.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
