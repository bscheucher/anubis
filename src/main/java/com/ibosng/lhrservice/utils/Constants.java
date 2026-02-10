package com.ibosng.lhrservice.utils;

import java.time.LocalTime;

public final class Constants {

    public static final String LHR_SERVICE = "LHR Service";
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_PATTERN_ZEITNACHWEIS = "dd.MM.yy";
    public static final String DATE_PATTERN_ABWESENHEIT_ERROR = "dd.MM.yyyy";
    public static final String URLAUB_ART = "H";
    public static final String URLAUB_GRUND = "URLAU";
    public static final String EINTRITT_GRUND_ANFANG = "EINTR";
    public static final String EINTRITT_ART_ANFANG = "E";
    public static final String AUSTRITT_GRUND_EINVN = "EINVN";
    public static final String AUSTRITT_ART_EINVN = "A";
    public static final String AUSTRITT_BESCHREIBUNG_EINVN = "Einvernehmliche Lösung";
    public static final String BUCHUNGANFRAGE_I = "I";
    public static final String BUCHUNGANFRAGE_D = "D";
    public static final String BUCHUNGANFRAGE_ZEITAUSGLEICH = "12";
    public static final String BUCHUNGANFRAGE_BILDUNGSFREISTELLUNG = "39";
    public static final String BUCHUNGANFRAGE_PAUSE = "33";
    public static final String ORIGINAL_JA = "J";
    public static final String TERMINAL = "IbosNG";
    public static final LocalTime TIME_VON = LocalTime.of(0, 0);
    public static final String REGEX_ZEITNACHWEIS = "*/Zeitnachweis";
    public static final String REGEX_GEHALTSZETTEL = "*/Gehaltszettel";
    public static final String REGEX_L16 = "*/L16";
    public static final String REGEX_ELDA = "*/ELDA";
    public static final String ZEITSPEICHER_UNKNOWN = "unknown";
    public static final String TEST_TENANT_UPN_PREFIX = "ibosng.";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate constant utility class");
    }
}
