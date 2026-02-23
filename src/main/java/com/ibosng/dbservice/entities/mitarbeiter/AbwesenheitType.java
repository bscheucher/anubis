package com.ibosng.dbservice.entities.mitarbeiter;

import lombok.Getter;

@Getter
public enum AbwesenheitType {
    ZEITAUSGLEICH("zeitausgleich"),
    URLAU("Urlaub"),
    SURL("Sonderurlaub"),
    UNURL("Unbezahlter urlaub");

    private final String value;

    public static AbwesenheitType fromValue(String value) {
        for (AbwesenheitType request : AbwesenheitType.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
    AbwesenheitType(String value) {
        this.value = value;
    }
}
