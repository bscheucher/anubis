package com.ibosng.dbservice.entities.moxis;

import lombok.Getter;

@Getter
public enum UserClassifier {

    EMAIL(0, "EMAIL"),
    ID(1, "ID"),
    UPN(2, "UPN");

    private final int code;
    private final String type;

    UserClassifier(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static UserClassifier fromType(String type) {
        for (UserClassifier classifier : UserClassifier.values()) {
            if (classifier.getType().equalsIgnoreCase(type)) {
                return classifier;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + type);
    }
}
