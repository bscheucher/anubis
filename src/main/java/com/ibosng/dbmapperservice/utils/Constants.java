package com.ibosng.dbmapperservice.utils;

public final class Constants {

    public static final String BW_SYNC_SERVICE = "Backward Sync Service";
    public static final String FW_SYNC_SERVICE = "DB_FW_SYNC_SERVICE";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd.MM.yyyy";
    public static final String ZONE_UTC = "UTC";
    public static final String ZONE_EUROPE_PARIS = "Europe/Paris";
    public static final String ADTYP_TEILNEHMER = "tn";
    public static final String ADTYP_STAMMDATEN = "tr";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate constant utility class");
    }
}
