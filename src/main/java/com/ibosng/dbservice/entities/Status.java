package com.ibosng.dbservice.entities;

import lombok.Getter;

@Getter
public enum Status {

    NEW(0),
    ACTIVE(1),
    INACTIVE(2);

    private final int code;

    Status(int code) {
        this.code = code;
    }


}
