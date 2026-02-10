package com.ibosng.dbservice.entities.teilnehmer;

import lombok.Getter;

@Getter
public enum TeilnehmerStatus {
    NEW(0),
    VALID(1),
    INVALID(2);

    private final int code;

    TeilnehmerStatus(int code) {
        this.code = code;
    }

    public static String getNameByCode(int code) {
        for (TeilnehmerStatus status : values()) {
            if (status.getCode() == code) {
                return status.name();
            }
        }
        return null; // or throw an exception if preferred
    }
}
