package com.ibosng.dbservice.entities.masterdata;

import lombok.Getter;

@Getter
public enum TitelPosition {
    SUFFIX(0),
    PREFIX(1);

    private final int code;

    TitelPosition(int code) {
        this.code = code;
    }
}
