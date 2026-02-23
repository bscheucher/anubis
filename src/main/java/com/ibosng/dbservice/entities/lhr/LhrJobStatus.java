package com.ibosng.dbservice.entities.lhr;

import lombok.Getter;

@Getter
public enum LhrJobStatus {

    NEW(0),
    PENDING(1),
    ACTIVE(2),
    COMPLETED(3),
    ERROR(4);

    private final int code;

    LhrJobStatus(int code) {
        this.code = code;
    }


}
