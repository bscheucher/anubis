package com.ibosng.dbservice.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ForeignKeyResolver {
    private static final Map<String, String> foreignKeyToTableMap = new HashMap<>();

    static {
        //From stammdaten
        foreignKeyToTableMap.put("abweichende_adresse", "adresse");
        foreignKeyToTableMap.put("adresse", "adresse");
        foreignKeyToTableMap.put("anrede", "anrede");
        foreignKeyToTableMap.put("bank", "bank_daten");
        foreignKeyToTableMap.put("familienstand", "familienstand");
        foreignKeyToTableMap.put("geschlecht", "geschlecht");
        foreignKeyToTableMap.put("mobilnummer", "telefon");
        foreignKeyToTableMap.put("muttersprache", "muttersprache");
        foreignKeyToTableMap.put("personalnummer", "personalnummer");
        foreignKeyToTableMap.put("staatsbuergerschaft", "land");
        foreignKeyToTableMap.put("titel", "titel");
        foreignKeyToTableMap.put("titel2", "titel");
        foreignKeyToTableMap.put("zusatz_info", "zusatz_info");
        foreignKeyToTableMap.put("dienstort", "dienstort");
        foreignKeyToTableMap.put("dienstnehmergruppe", "dienstnehmergruppe");
        foreignKeyToTableMap.put("kategorie", "kategorie");
        foreignKeyToTableMap.put("taetigkeit", "taetigkeit");
        foreignKeyToTableMap.put("kostenstelle", "kostenstelle");
        foreignKeyToTableMap.put("job_bezeichnung", "jobbeschreibung");
        foreignKeyToTableMap.put("abrechnungsgruppe", "abrechnungsgruppe");
        foreignKeyToTableMap.put("verwendungsgruppe", "verwendungsgruppe");
        foreignKeyToTableMap.put("arbeitszeitmodell", "arbeitszeitmodell");
        foreignKeyToTableMap.put("fuehrungskraft", "benutzer");
        foreignKeyToTableMap.put("startcoach", "benutzer");
        //From adresse
        foreignKeyToTableMap.put("land", "land");
    }

    public static String getReferencedTable(String fieldName) {
        return foreignKeyToTableMap.get(fieldName);
    }
}