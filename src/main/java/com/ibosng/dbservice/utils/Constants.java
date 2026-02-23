package com.ibosng.dbservice.utils;

public final class Constants {

    public static final String DATE_PATTERN = "dd.MM.yyyy";
    public static final String ISO_DATE_PATTERN = "yyyy-MM-dd";
    public static final String AUSTRIA_AT = "at";
    public static final String EUROPE_VIENNA = "Europe/Vienna";
    public static final String UTC = "UTC";
    public static final String URLAUB = "URLAU";
    public static final String ARZT = "ARZT";
    public static final String KRANK = "KRANK";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate constant utility class");
    }
}

