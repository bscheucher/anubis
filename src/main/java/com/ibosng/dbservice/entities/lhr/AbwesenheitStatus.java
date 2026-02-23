package com.ibosng.dbservice.entities.lhr;

import lombok.Getter;

@Getter
public enum AbwesenheitStatus {
    NEW(0),
    VALID(1),
    INVALID(2),
    ACCEPTED(3),
    REJECTED(4),
    CANCELED(5),
    ERROR(6),
    REQUEST_CANCELLATION(7),
    ACCEPTED_FINAL(8),
    USED(9);

    private final int code;

    AbwesenheitStatus(int code) {
        this.code = code;
    }
}
