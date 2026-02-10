package com.ibosng.workflowservice.enums;

import lombok.Getter;

@Getter
public enum SWorkflowItems {
    IMPORTING_TEILNEHMER("Importing Teilnehmer"),
    VALIDATING_TEILNEHMER("Validating Imported Teilnehmer"),

    STAMMDATEN_ERFASSEN("Stammdaten erfassen"),
    VERTRAGSDATEN_ERFASSEN("Vertragsdaten erfassen"),
    KV_EINSTUFUNG_BERECHNEN("KV-Einstufung berechnen"),

    LOHNVERRECHUNG_INFORMIEREN("Lohnverrechnung informieren"),
    MITARBEITERDATEN_PRUEFEN("Mitarbeitendedaten prüfen"),
    DIENSTVERTRAG_ERSTELLEN("Dienstvertrag erstellen"),

    UNTERSCHRIFTENLAUF_STARTEN("Unterschriftenlauf starten"),
    UNTERSCHRIFTENLAUF_DURCHFUEHREN("Unterschriftenlauf durchführen"),
    UNTERSCHRIEBENE_DOKUMENTE_SPEICHERN("Unterschriebene Dokumente speichern"),

    DATEN_AN_LHR_UEBERGEBEN("Daten an LHR übergeben"),

    NEUEN_MA_ANLEGEN("Neuen MA anlegen"),
    USER_ANLAGE_BEAUFTRAGEN("User Anlage beauftragen"),
    AD_UND_IBOS_USER_ANLEGEN("AD & ibos User anlegen"),
    IBOSNG_USER_ANLEGEN("IbosNG User anlegen"),

    TN_ONBOARDING_STAMMDATEN_ERFASSEN("Stammdaten erfassen (Teilnehmende)"),
    TN_ONBOARDING_VERTRAGSDATEN_ERFASSEN("Vertragsdaten erfassen (Teilnehmende)"),

    TN_ONBOARDING_LOHNVERRECHUNG_INFORMIEREN("Lohnverrechnung informieren (Teilnehmende)"),
    TN_ONBOARDING_MITARBEITERDATEN_PRUEFEN("Mitarbeitendedaten prüfen (Teilnehmende)"),

    TN_ONBOARDING_DATEN_AN_LHR_UEBERGEBEN("Daten an LHR übergeben (Teilnehmende)"),

    TN_ONBOARDING_STAKEHOLDER_INFORMIEREN("Stakeholder informieren (Teilnehmende)"),

    //WFI TN AN ABWWESENHEITEN TRANSFER
    TN_AN_ABWESENHEITEN_LHR_UEBERTRAGEN("Transfer An und Abwesenheiten to LHR"),
    TN_ZEITBUCHUNGSPERIODE_SPERREN("Lock time booking period"),
    TN_BETEILIGTEN_INFORMIEREN("Inform LV and Jugend Admin"),

    //WFI for TN TRITT AUS
    TN_TRITT_AUS_INFORM_LOHNVERRECHNUNG("Inform Lohnverrechnung for TN tritt aus"),
    TN_TRITT_AUS_SEND_AUSTRITT_TO_LHR("Send Austritt to LHR"),
    TN_TRITT_AUS_CHANGE_STATUS("Change status to Abgemeldet"),

    //WFI for MA_ZEITERFASSUNG
    MA_GET_MEHRSTUNDEN_FROM_LHR("Get Mehrstunden from LHR"),
    MA_MEHRSTUNDEN_UND_UEBERSTUNDEN_UEBERPRUEFEN("Mehrstunden und Ueberstunden ueberpruefen"),
    MA_AUSZAHLUNGSANTRAG_AN_LHR_UEBERMITTELN("Auszahlungsantrag an LHR uebermitteln"),
    MA_AUSZAHLUNGSANTRAG_ARCHIVIEREN("Auszahlungsantrag archivieren"),

    //MA_FK_VEREINBARUNG
    MA_FK_VEREINBARUNG_ERSTELLEN("Neue Vereinbarung erstellen zwischen MA und FK"),
    MA_FK_VEREINBARUNG_VERVOLLSTAENDIGEN("Vereinbarung vervollstaendigen zwischen MA und FK"),
    MA_FK_VEREINBARUNG_DOKUMENT_ERSTELLEN("Dokument erstellen fuer Vereinbarung zwischen MA und FK"),
    MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN("Unterschriftenlauf starten fuer Vereinbarung zwischen MA und FK"),
    MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_DURCHFUEHREN("Unterschriftenlauf durchfuehren fuer Vereinbarung zwischen MA und FK"),
    MA_FK_VEREINBARUNG_DOKUMENT_SPEICHERN("Unterschriebenes Dokument speichern fuer Vereinbarung zwischen MA und FK"),


    //MA_VD_VERAENDERUNG
    MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN("Daten vervollständigen (Vertragsänderung)"),
    MA_VD_VERTRAGSAENDERUNG_ZUSATZDOKUMENT("Zusatzdokument erstellen (Vertragsänderung)"),
    MA_VD_VERTRAGSAENDERUNG_PRUEFEN("Zur Prüfung vorlegen (Vertragsänderung)"),
    MA_VD_PEOPLE_INFORMIEREN("People informieren (Vertragsänderung)"),
    MA_VD_PEOPLE_PRUEFT("People prüft (Vertragsänderung)"),
    MA_VD_LOHNVERRECHNUNG_INFORMIEREN("Lohnverrechnung informieren (Vertragsänderung)"),
    MA_VD_LOHNVERRECHNUNG_PRUEFT("Lohnverrechnung prüft (Vertragsänderung)"),
    MA_VD_GENEHHMIGER_INFORMIEREN("Genehmiger informieren (Vertragsänderung)"),
    MA_VD_GENEHMIGER_PRUEFT("Genehmiger prüft (Vertragsänderung)"),
    MA_VD_UNTERSCHRIFTENLAUF_STARTEN("Unterschriftenlauf starten (Vertragsänderung)"),
    MA_VD_UNTERSCHRIFTENLAUF_DURCHFUEHREN("Unterschriftenlauf durchführen (Vertragsänderung)"),
    MA_VD_UNTERSCHRIEBENES_DOKUMENT_SPEICHERN("Unterschriebenes Dokument speichern (Vertragsänderung)"),
    MA_VD_ZUSATZ_IM_SYSTEM_EINTRAGEN("Zusatz im System eintragen (Vertragsänderung)"),
    MA_VD_DATEN_AN_LHR_UEBERMITTELN("Daten an LHR übermitteln (Vertragsänderung)"),
    MA_VD_BETEILIGTEN_INFORMIEREN("Beteiligten informieren (Vertragsänderung)");


    private final String value;


    SWorkflowItems(String value) {
        this.value = value;
    }

    public static SWorkflowItems fromValue(String value) {
        for (SWorkflowItems source : SWorkflowItems.values()) {
            if (source.getValue().equals(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}
