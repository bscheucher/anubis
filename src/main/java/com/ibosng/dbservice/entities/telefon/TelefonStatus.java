package com.ibosng.dbservice.entities.telefon;

import lombok.Getter;

@Getter
public enum TelefonStatus {

    ACTIVE(0),
    INACTIVE(1);

    private final int code;

    TelefonStatus(int code) {
        this.code = code;
    }


}
