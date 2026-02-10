package com.ibosng.dbservice.entities.mitarbeiter.vereinbarung;

import lombok.Getter;

@Getter
public enum VereinbarungStatus {

    NEW("new"),
    GENEHMIGT("genehmigt"),
    ABGELEHNT("abgelehnt"),
    COMPLETED("completed"),
    ERROR("error");

    private final String value;

    public static VereinbarungStatus fromValue(String value) {
        for (VereinbarungStatus status : VereinbarungStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    VereinbarungStatus(String value) {
        this.value = value;
    }
}

