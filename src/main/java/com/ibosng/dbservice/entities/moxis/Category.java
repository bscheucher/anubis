package com.ibosng.dbservice.entities.moxis;

import lombok.Getter;

@Getter
public enum Category {

    QSIG(0, "QSIG"),
    EXTERNAL_QSIG(1, "EXTERNAL_QSIG"),
    EXTERNAL_APPROVAL(2, "EXTERNAL_APPROVAL"),
    APPROVAL(3, "APPROVAL"),
    APPROVAL_NOSIG(4, "APPROVAL_NOSIG");

    private final int code;
    private final String type;

    Category(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static Category fromType(String type) {
        for (Category category : Category.values()) {
            if (category.getType().equalsIgnoreCase(type)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + type);
    }
}
