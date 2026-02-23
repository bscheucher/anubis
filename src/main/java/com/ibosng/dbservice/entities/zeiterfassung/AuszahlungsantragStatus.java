package com.ibosng.dbservice.entities.zeiterfassung;

import lombok.Getter;

@Getter
public enum AuszahlungsantragStatus {
    NEW("NEW"),
    WAITING("WAITING"),
    DONE("DONE"),
    ERROR("ERROR");

    private final String value;

    public static AuszahlungsantragStatus fromValue(String value) {
        for (AuszahlungsantragStatus request : AuszahlungsantragStatus.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    AuszahlungsantragStatus(String value) {
        this.value = value;
    }
}
