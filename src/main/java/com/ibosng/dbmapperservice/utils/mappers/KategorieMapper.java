package com.ibosng.dbmapperservice.utils.mappers;

import java.util.HashMap;
import java.util.Map;

public class KategorieMapper {

    private static final String KATEGORIE_TRAINING = "Training";
    private static final String KATEGORIE_EXTERN = "Extern";
    private static final String KATEGORIE_SCHLUESSELKRAFT = "Schlüsselkraft";
    private static final String KATEGORIE_MITARBEITER = "Mitarbeiter";
    private static final String KATEGORIE_IBOS_ANGESTELLTE_TR = "Angestellte TR";
    public static final String KATEGORIE_IBOS_ANGESTELLTE_SK = "Angestellte SK";
    private static final String KATEGORIE_IBOS_EXTERN = "Extern";
    private static final String KATEGORIE_IBOS_ARBEITER = "Arbeiter";
    private static final String KATEGORIE_LEHRLING = "Lehrling";
    public static final String KATEGORIE_UNKNOWN = "Unknown Verwendungsgruppe";

    private static final Map<String, String> KATEGORIE_MAPPING = new HashMap<>();

    static {
        KATEGORIE_MAPPING.put(KATEGORIE_IBOS_ANGESTELLTE_TR, KATEGORIE_TRAINING);
        KATEGORIE_MAPPING.put(KATEGORIE_IBOS_EXTERN, KATEGORIE_EXTERN);
        KATEGORIE_MAPPING.put(KATEGORIE_IBOS_ANGESTELLTE_SK, KATEGORIE_SCHLUESSELKRAFT);
        KATEGORIE_MAPPING.put(KATEGORIE_IBOS_ARBEITER, KATEGORIE_MITARBEITER);
        KATEGORIE_MAPPING.put(KATEGORIE_LEHRLING, KATEGORIE_LEHRLING);
    }

    public static String getMitarbeiterKategorie(String kategorie) {
        return KATEGORIE_MAPPING.getOrDefault(kategorie, KATEGORIE_UNKNOWN);
    }
}