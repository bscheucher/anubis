package com.ibosng.dbservice.entities.lhr;

import lombok.Getter;

@Getter
public enum AbmeldungStatus {

    NEW(0),
    ABMELDUNG_BEI_LV(1),
    ABGEMELDET(2),
    ERROR(3);

    private final int code;

    AbmeldungStatus(int code) {
        this.code = code;
    }


}
