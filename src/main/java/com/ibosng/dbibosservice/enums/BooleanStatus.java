package com.ibosng.dbibosservice.enums;

public enum BooleanStatus {
    n,
    y;

    public static BooleanStatus fromValue(String value) {
        if (value == null) {
            return null; // Handle null values
        }
        try {
            return BooleanStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null; // Handle invalid values
        }
    }

    /**
     * Converts the BooleanStatus to a boolean value.
     *
     * @return true for "y", false for "n"
     */
    public boolean toBoolean() {
        return this == y;
    }
}
