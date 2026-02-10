package com.ibosng.dbservice.entities.moxis;

import lombok.Getter;

@Getter
public enum PositionType {

    ON_PLACEHOLDER(0, "ON_PLACEHOLDER"),
    INVISIBLE(1, "INVISIBLE"),
    SIGNATURE_PAGE(2, "SIGNATURE_PAGE"),
    DIRECT_POSITION(3, "DIRECT_POSITION");

    private final int code;
    private final String type;

    PositionType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static PositionType fromType(String type) {
        for (PositionType positionType : PositionType.values()) {
            if (positionType.getType().equalsIgnoreCase(type)) {
                return positionType;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + type);
    }
}
