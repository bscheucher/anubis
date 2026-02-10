package com.ibosng.gatewayservice.utils;

public final class Constants {
    public static final String GATEWAY_SERVICE = "Gateway Service";

    public static final String FN_STAMMDATEN_ERFASSEN = "FN_STAMMDATEN_ERFASSEN";
    public static final String FN_VERTRAGSDATEN_ERFASSEN = "FN_VERTRAGSDATEN_ERFASSEN";
    public static final String FN_MA_UEBERPRUEFEN = "FN_MA_UEBERPRUEFEN";
    public static final String FN_MA_ONBOARDING_LV_INFORMIEREN = "FN_MA_ONBOARDING_LV_INFORMIEREN";
    public static final String FN_MA_LESEN = "FN_MA_LESEN";
    public static final String FN_MA_ONBOARDING  = "FN_MA_ONBOARDING";
    public static final String FN_MA_ONBOARDING_EIGENE  = "FN_MA_ONBOARDING_EIGENE";
    public static final String FN_MA_ZEITEN_LESEN = "FN_MA_ZEITEN_LESEN";
    public static final String FN_TN_ZEITBUCHUNGEN_BEARBEITEN = "FN_TN_ZEITBUCHUNGEN_BEARBEITEN";
    public static final String FN_EIGENE_ABWESENHEITEN_LESEN = "FN_EIGENE_ABWESENHEITEN_LESEN";
    public static final String FN_ABWESENHEITEN_GENEHMIGEN = "FN_ABWESENHEITEN_GENEHMIGEN";
    public static final String FN_ABWESENHEITEN_EDITIEREN = "FN_ABWESENHEITEN_EDITIEREN";
    public static final String FN_UEBERSTUNDEN_AUSZAHLUNG = "FN_UEBERSTUNDEN_AUSZAHLUNG";
    public static final String FN_UNTERLAGEN_LESEN = "FN_UNTERLAGEN_LESEN";
    public static final String FN_TN_ONBOARDING = "FN_TN_ONBOARDING";
    public static final String FN_VERTRAGSAENDERUNG_DATENVERVOLLSTAENDIGEN = "FN_VERTRAGSAENDERUNG_DATENVERVOLLSTAENDIGEN";
    public static final String FN_VERTRAGSAENDERUNG_STARTEN = "FN_VERTRAGSAENDERUNG_STARTEN";
    public static final String FN_VERTRAGSAENDERUNG_PRUEFEN_PEOPLE = "FN_VERTRAGSAENDERUNG_PRUEFEN_PEOPLE";
    public static final String FN_VERTRAGSAENDERUNG_PRUEFEN_LOHNVERRECHNUNG = "FN_VERTRAGSAENDERUNG_PRUEFEN_LOHNVERRECHNUNG";
    public static final String FN_VERTRAGSAENDERUNG_PRUEFEN_GENEHMIGENDEN = "FN_VERTRAGSAENDERUNG_PRUEFEN_GENEHMIGENDEN";
    public static final String FN_VERTRAGSAENDERUNG_ABBRECHEN = "FN_VERTRAGSAENDERUNG_ABBRECHEN";
    public static final String FN_VERTRAGSAENDERUNG_UEBERSICHT = "FN_VERTRAGSAENDERUNG_UEBERSICHT";
    public static final String FN_TN_UPLOAD_PROFIL = "FN_TN_UPLOAD_PROFIL";
    public static final String FN_VEREINBARUNGEN_EINSEHEN = "FN_VEREINBARUNGEN_EINSEHEN";
    public static final String FN_VEREINBARUNGEN_ERSTELLEN = "FN_VEREINBARUNGEN_ERSTELLEN";
    public static final String FN_ALLE_VEREINBARUNGEN_EINSEHEN = "FN_ALLE_VEREINBARUNGEN_EINSEHEN";
    public static final String FN_UNEINGESCHRAENKTE_VEREINBARUNGEN_ERSTELLEN = "FN_UNEINGESCHRAENKTE_VEREINBARUNGEN_ERSTELLEN";
    public static final String FN_TEILNEHMERINNEN_TR_LESEN_ALLE = "FN_TEILNEHMERINNEN_TR_LESEN_ALLE";
    public static final String VERTRAGSAENDERUNG_PRUFUNG_LINK = "%s/mitarbeiter/vertragsaenderungen/%s?wfi=%s";
    public static final String ABWESENHEIT_GENEHMIGEN_LINK = "%s/meine-mitarbeiter/abwesenheiten";
    public static final String MEINE_ABWESENHEITEN_LINK = "%s/mein-bereich/meine-abwesenheiten";
    public static final String FN_TN_TR_ANWESENHEITEN_LESEN = "FN_TN_TR_ANWESENHEITEN_LESEN";
    public static final String FN_TN_TR_ANWESENHEITEN_VERWALTEN = "FN_TN_TR_ANWESENHEITEN_VERWALTEN";
    public static final String FN_TN_ADMIN_ANWESENHEITEN_LESEN = "FN_TN_ADMIN_ANWESENHEITEN_LESEN";
    public static final String FN_TN_ADMIN_ANWESENHEITEN_VERWALTEN = "FN_TN_ADMIN_ANWESENHEITEN_VERWALTEN";
    public static final String FN_TN_TR_ABWESENHEITEN_LESEN = "FN_TN_TR_ABWESENHEITEN_LESEN";
    public static final String FN_TN_TR_ABWESENHEITEN_VERWALTEN = "FN_TN_TR_ABWESENHEITEN_VERWALTEN";
    public static final String FN_TN_ADMIN_ABWESENHEITEN_LESEN = "FN_TN_ADMIN_ABWESENHEITEN_LESEN";
    public static final String FN_TN_ADMIN_ABWESENHEITEN_VERWALTEN = "FN_TN_ADMIN_ABWESENHEITEN_VERWALTEN";
    public static final String FN_WIDGET_MT_FEHLER_TN = "FN_WIDGET_MT_FEHLER_TN";
    public static final String FN_TEILNEHMERINNEN_BEARBEITEN = "FN_TEILNEHMERINNEN_BEARBEITEN";
    public static final String FN_TEILNEHMERINNEN_LESEN = "FN_TEILNEHMERINNEN_LESEN";
    public static final String FN_TN_ABMELDEN = "FN_TN_ABMELDEN";
    public static final String FN_REPORTS =  "FN_REPORTS";
    public static final String FN_MA_PROJEKTE_SEMINARE_LESEN = "FN_MA_PROJEKTE_SEMINARE_LESEN";
    public static final String TEST_TENANT_UPN_PREFIX = "ibosng.";


    public final static String OID = "oid";
    public final static String FIRST_NAME = "given_name";
    public final static String LAST_NAME = "family_name";
    public final static String EMAIL = "email";
    public final static String UPN = "upn";

    private Constants() {
        throw new AssertionError("Cannot instantiate constant utility class");
    }
}
