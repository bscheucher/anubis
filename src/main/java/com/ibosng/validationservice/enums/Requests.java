package com.ibosng.validationservice.enums;

import lombok.Getter;

@Getter
public enum Requests {
    TEILNEHMER("teilnehmer"),
    FETCH_ALL_WIDGETS("fetchAllWidgets"),
    SAVE_DASHBOARD("saveDashboard");

    private final String value;

    public static Requests fromValue(String value) {
        for (Requests request : Requests.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    Requests(String value) {
        this.value = value;
    }
}
