package com.ibosng.dbservice.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AbwesenheitTaetigkeitenType {
    ZEITAUSGLEICH("Zeitausgleich"),
    URLAUB("Urlaub");

    private final String label;
}

