package com.ibosng.dbservice.entities.validations;

import lombok.Getter;

@Getter
public enum ValidationStatus {

    RESOLVED(0),
    UNRESOLVED(1),
    OVERWRITTEN(2);

    private final int code;

    ValidationStatus(int code) {
        this.code = code;
    }


}
