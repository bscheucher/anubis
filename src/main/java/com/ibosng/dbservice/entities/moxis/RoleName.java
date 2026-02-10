package com.ibosng.dbservice.entities.moxis;

import lombok.Getter;

@Getter
public enum RoleName {

    INTERN(0, "INTERN"),
    EXTERN(1, "Extern"),
    EXTERNAL(2, "External");

    private final int code;
    private final String type;

    RoleName(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static RoleName fromType(String type) {
        for (RoleName userClass : RoleName.values()) {
            if (userClass.getType().equals(type)) {
                return userClass;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + type);
    }
}
