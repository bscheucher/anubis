package com.ibosng.dbibosservice.utils;

public final class Constants {

    public static final String DATE_PATTERN = "dd.MM.yyyy";

    public static final String[] DATE_PATTERNS = {"dd.MM.yyyy", "yyyy-MM-dd", "ddMMyy"};

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate constant utility class");
    }
}

