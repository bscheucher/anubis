package com.ibosng.validationservice.utils;

public final class Constants {

    public static final String VALIDATION_SERVICE = "Validation Service";
    public static final String AUSTRIA_LAND_CODE = "AT";
    public static final String AUSTRIA_VORWAHL = "+43";
    public static final String TN_KOLLEKTIVVERTRAG = "AMS-Lehrteilnehmer";
    public static final Double DAYS_IN_YEAR = 365.0;
    public static final int MONTHS_IN_YEAR = 12;
    public static final String FIX_VERTRAGSART = "Fix";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate constant utility class");
    }
}
