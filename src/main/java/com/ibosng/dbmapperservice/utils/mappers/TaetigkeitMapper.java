package com.ibosng.dbmapperservice.utils.mappers;

import java.util.HashMap;
import java.util.Map;

public class TaetigkeitMapper {

    // Output categories
    public static final String KATEGORIE_SACHBEARBEITUNG = "Sachbearbeitung";
    public static final String KATEGORIE_TRAINING_COACHING = "Training&Coaching";
    public static final String KATEGORIE_LEHRLING = "Lehrling";
    public static final String KATEGORIE_ASSISTENZ = "Assistenz";
    public static final String KATEGORIE_PROJEKTLEITUNG = "Projektleitung";
    public static final String KATEGORIE_GESCHAEFTSFUEHRUNG = "Geschäftsführung";
    public static final String KATEGORIE_STREAMLEITUNG = "Streamleitung";
    public static final String KATEGORIE_TEAMLEITUNG = "Teamleitung";
    public static final String KATEGORIE_GESCHAEFTSFELDLEITUNG = "Geschäftsfeldleitung";
    public static final String KATEGORIE_AMS_LEHRTEILNEHMER = "AMS-Lehrteilnehmer";
    public static final String KATEGORIE_UNKNOWN = "Unbekannte Kategorie";

    private static final Map<String, String> KATEGORIE_MAPPING = new HashMap<>();

    static {
        KATEGORIE_MAPPING.put("Abteilungsleitung", KATEGORIE_TEAMLEITUNG);
        KATEGORIE_MAPPING.put("Bereichsleitungsassistenz", KATEGORIE_ASSISTENZ);
        KATEGORIE_MAPPING.put("Case Manager", KATEGORIE_SACHBEARBEITUNG);
        KATEGORIE_MAPPING.put("Bereichsleitung", KATEGORIE_GESCHAEFTSFELDLEITUNG);
        KATEGORIE_MAPPING.put("stellvertretende Bereichsleitung", KATEGORIE_STREAMLEITUNG);
        KATEGORIE_MAPPING.put("Projektkoordination", KATEGORIE_PROJEKTLEITUNG);
        KATEGORIE_MAPPING.put("Reinigungskraft", KATEGORIE_UNKNOWN);
        KATEGORIE_MAPPING.put("Aushilfe", KATEGORIE_UNKNOWN);
        KATEGORIE_MAPPING.put("TechnikerIn", KATEGORIE_SACHBEARBEITUNG);
        KATEGORIE_MAPPING.put("Assistenz (VB 3)", KATEGORIE_ASSISTENZ);
        KATEGORIE_MAPPING.put("Assistenz (VB 4)", KATEGORIE_ASSISTENZ);
        KATEGORIE_MAPPING.put("Assistenz (VB 5)", KATEGORIE_ASSISTENZ);
        KATEGORIE_MAPPING.put("Assistenz", KATEGORIE_ASSISTENZ);
        KATEGORIE_MAPPING.put("SachbearbeiterIn (VB 3)", KATEGORIE_SACHBEARBEITUNG);
        KATEGORIE_MAPPING.put("SachbearbeiterIn (VB 4)", KATEGORIE_SACHBEARBEITUNG);
        KATEGORIE_MAPPING.put("SachbearbeiterIn (VB 5)", KATEGORIE_SACHBEARBEITUNG);
        KATEGORIE_MAPPING.put("SachbearbeiterIn", KATEGORIE_SACHBEARBEITUNG);
        KATEGORIE_MAPPING.put("HausarbeiterIn", KATEGORIE_SACHBEARBEITUNG);
        KATEGORIE_MAPPING.put("Lehrling", KATEGORIE_LEHRLING);
        KATEGORIE_MAPPING.put("RegionalleiterIn", KATEGORIE_TEAMLEITUNG);
        KATEGORIE_MAPPING.put("VertriebsleiterIn", KATEGORIE_TEAMLEITUNG);
        KATEGORIE_MAPPING.put("GeschäftsführerIn", KATEGORIE_GESCHAEFTSFUEHRUNG);
        KATEGORIE_MAPPING.put("Betriebkontakter_in", KATEGORIE_TRAINING_COACHING);
    }

    public static String getMitarbeiterKategorie(String kategorie) {
        return KATEGORIE_MAPPING.getOrDefault(kategorie, "Unknown Verwendungsgruppe");
    }
}