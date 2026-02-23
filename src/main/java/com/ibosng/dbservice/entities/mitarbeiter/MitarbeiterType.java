package com.ibosng.dbservice.entities.mitarbeiter;

import lombok.Getter;

@Getter
public enum MitarbeiterType {
    MITARBEITER("mitarbeiter"),
    TEILNEHMER("teilnehmer");

    private final String value;

    public static MitarbeiterType fromValue(String value) {
        for (MitarbeiterType request : MitarbeiterType.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
    MitarbeiterType(String value) {
        this.value = value;
    }
}
