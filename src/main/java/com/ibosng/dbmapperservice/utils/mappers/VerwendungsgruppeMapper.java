package com.ibosng.dbmapperservice.utils.mappers;

import java.util.HashMap;
import java.util.Map;

public class VerwendungsgruppeMapper {

    private static final Map<String, String> VERWENDUNGSGRUPPE_MAP = new HashMap<>();

    static {
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 1", "VB 1");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 2", "VB 2");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 3", "VB 3");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 4", "VB 4");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 4a", "VB 4a");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 5", "VB 5");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 6", "VB 6");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 7", "VB 7");
        VERWENDUNGSGRUPPE_MAP.put("Verwendungsgruppe 8", "VB 8");
    }

    public static String getVerwendungsgruppe(String vb) {
        return VERWENDUNGSGRUPPE_MAP.getOrDefault(vb, "Unknown Verwendungsgruppe");
    }
}
