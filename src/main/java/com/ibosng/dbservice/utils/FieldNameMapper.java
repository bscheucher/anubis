package com.ibosng.dbservice.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class FieldNameMapper {

    private static final Map<String, String> fieldNameMap = new HashMap<>();

    static {
        fieldNameMap.put("adresse.plz", "plz");
        fieldNameMap.put("adresse.land.land_name", "land");
        fieldNameMap.put("geburtsdatum", "geburtsDatum");
        fieldNameMap.put("lebensalter", "alter");
        fieldNameMap.put("abweichende_adresse.strasse", "aStrasse");
        fieldNameMap.put("abweichende_adresse.land.land_name", "aLand");
        fieldNameMap.put("abweichende_adresse.plz", "aPlz");
        fieldNameMap.put("abweichende_adresse.ort", "aOrt");
        fieldNameMap.put("land.land_name", "land");
        fieldNameMap.put("land.telefonvorwahl", "telefonvorwahl");
        fieldNameMap.put("bank.bank", "bank");
        fieldNameMap.put("bank.iban", "iban");
        fieldNameMap.put("bank.bic", "bic");
        fieldNameMap.put("bank.card", "bankcard");
        fieldNameMap.put("zusatz_info.gueltigBis", "gueltigBis");
        fieldNameMap.put("zusatz_info.arbeitsgenehmigung", "arbeitsgenehmigung");
        fieldNameMap.put("dienstort.name", "dienstort");
        fieldNameMap.put("dienstort.short_name", "dienstortShortName");
        fieldNameMap.put("kategorie.name", "kategorie");
        fieldNameMap.put("taetigkeit.name", "taetigkeit");
        fieldNameMap.put("kostenstelle.bezeichnung", "kostenstelle");
        fieldNameMap.put("job_bezeichnung.name", "jobBezeichnung");
        fieldNameMap.put("abrechnungsgruppe.name", "abrechnungsgruppe");
        fieldNameMap.put("dienstnehmergruppe.bezeichnung", "dienstnehmergruppe");
        fieldNameMap.put("abrechnungsgruppe.bezeichnung", "abrechnungsgruppeBezeichnung");
        fieldNameMap.put("verwendungsgruppe.name", "verwendungsgruppe");
        fieldNameMap.put("jobbeschreibung.name", "jobBezeichnung");
        fieldNameMap.put("arbeitszeitmodell.name", "arbeitszeitmodell");
        fieldNameMap.put("titel.name", "titel");
    }

    public static boolean isMappingExists(String fieldName) {
        return fieldNameMap.containsKey(fieldName);
    }

    public static String getDtoFieldName(String fieldName) {
        return fieldNameMap.getOrDefault(fieldName, fieldName);
    }
}