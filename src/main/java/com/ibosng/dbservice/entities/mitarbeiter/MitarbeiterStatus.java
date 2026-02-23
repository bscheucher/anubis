package com.ibosng.dbservice.entities.mitarbeiter;

import lombok.Getter;

@Getter
public enum MitarbeiterStatus {

    NEW(0),
    ACTIVE(1),
    INACTIVE(2),
    NOT_VALIDATED(3),
    VALIDATED(4);

    private final int code;

    MitarbeiterStatus(int code) {
        this.code = code;
    }


}
