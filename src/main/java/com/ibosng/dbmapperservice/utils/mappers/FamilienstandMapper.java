package com.ibosng.dbmapperservice.utils.mappers;

import java.util.HashMap;
import java.util.Map;

public class FamilienstandMapper {

    private static final String FAMILIENSTAND_LEDIG = "Ledig";
    private static final String FAMILIENSTAND_VERHEIRATET = "Verheiratet";
    private static final String FAMILIENSTAND_VERWITWET = "Verwitwet";
    private static final String FAMILIENSTAND_PARTNERSCHAFT = "Partnerschaft";
    private static final String FAMILIENSTAND_PARTNERSCHAFT_AUGELOEST = "Partnerschaft aufgelöst";
    private static final String FAMILIENSTAND_GERICHT = "Gericht";
    private static final String FAMILIENSTAND_TOD = "Tod";
    private static final String FAMILIENSTAND_IBOS_LEBENSGEMEINSCHAFT = "Lebensgemeinschaft";
    private static final String FAMILIENSTAND_IBOS_LEBENSGEMEINSCHAFT_AUFGEHOBEN = "Lebensgemeinschaft aufgehoben";
    private static final String FAMILIENSTAND_IBOS_LEBENSPARTNER_VERSTORBEN = "Lebenspartner verstorben";
    private static final String FAMILIENSTAND_IBOS_GESCHIEDEN = "geschieden";
    private static final String FAMILIENSTAND_IBOS_KEINE_ANGABEN = "keine Angabe";

    private static final Map<String, String> FAMILIENSTAND_MAPPING = new HashMap<>();

    static {
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_LEDIG.toLowerCase(), FAMILIENSTAND_LEDIG);
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_VERHEIRATET.toLowerCase(), FAMILIENSTAND_VERHEIRATET);
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_VERWITWET.toLowerCase(), FAMILIENSTAND_VERWITWET);
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_IBOS_LEBENSGEMEINSCHAFT, FAMILIENSTAND_PARTNERSCHAFT);
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_IBOS_LEBENSGEMEINSCHAFT_AUFGEHOBEN, FAMILIENSTAND_PARTNERSCHAFT_AUGELOEST);
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_IBOS_LEBENSPARTNER_VERSTORBEN, FAMILIENSTAND_TOD);
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_IBOS_GESCHIEDEN, FAMILIENSTAND_GERICHT);
        FAMILIENSTAND_MAPPING.put(FAMILIENSTAND_IBOS_KEINE_ANGABEN, "");
    }

    public static String getIbosFamilienstand(String familienstand) {
        return FAMILIENSTAND_MAPPING.getOrDefault(familienstand, "");
    }
}
