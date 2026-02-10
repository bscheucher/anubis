package com.ibosng.dbservice.entities.mitarbeiter;

import lombok.Getter;

@Getter

public enum VertragsaenderungStatus {
    NEW,
    IN_PROGRESS,
    CLOSED,
    CANCELED,
    ERROR
}
