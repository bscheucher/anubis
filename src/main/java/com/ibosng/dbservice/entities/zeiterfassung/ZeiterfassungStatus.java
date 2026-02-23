package com.ibosng.dbservice.entities.zeiterfassung;

import lombok.Getter;

@Getter
public enum ZeiterfassungStatus {
    NEW(0),
    COMPLETED(1),
    REJECTED(2),
    ERROR(3),
    CANCELED(4),
    VALID(5),
    INVALID(6),
    IN_PROGRESS(7),
    PARTIALLY_COMPLETED(8);

    private final int code;

    ZeiterfassungStatus(int code) {
        this.code = code;
    }
}
