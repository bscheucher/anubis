package com.ibosng.dbmapperservice.utils.mappers;

import java.util.HashMap;
import java.util.Map;

public class BeschaeftigunsausmassMapper {

    // Output categories
    public static final String BESCHAEFTIGUNGSAUSMASS_VOLLVERSICHERUNG = "Vollversicherung";
    public static final String BESCHAEFTIGUNGSAUSMASS_GERINGFUEGIG = "Geringfügig";
    public static final String BESCHAEFTIGUNGSAUSMASS = "Unbekannte Kategorie";

    private static final Map<String, String> BESCHAEFTIGUNGSAUSMASS_MAPPING = new HashMap<>();

    static {
        BESCHAEFTIGUNGSAUSMASS_MAPPING.put("Vollversicherung", BESCHAEFTIGUNGSAUSMASS_VOLLVERSICHERUNG);
        BESCHAEFTIGUNGSAUSMASS_MAPPING.put("geringfügig Beschäftigt", BESCHAEFTIGUNGSAUSMASS_GERINGFUEGIG);
        BESCHAEFTIGUNGSAUSMASS_MAPPING.put("geringfügig Karenz", BESCHAEFTIGUNGSAUSMASS_GERINGFUEGIG);
    }

    public static String getMitarbeiterBeschaeftigunsausmass(String kategorie) {
        return BESCHAEFTIGUNGSAUSMASS_MAPPING.getOrDefault(kategorie, "Unknown Verwendungsgruppe");
    }
}