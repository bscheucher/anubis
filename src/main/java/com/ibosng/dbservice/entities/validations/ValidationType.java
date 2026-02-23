package com.ibosng.dbservice.entities.validations;

import lombok.Getter;

@Getter
public enum ValidationType {

    INFO(0),
    WARNING(1),
    ERROR(2);


    private final int code;

    ValidationType(int code) {
        this.code = code;
    }


}
