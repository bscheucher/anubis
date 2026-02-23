package com.ibosng.workflowservice.enums;

import lombok.Getter;

@Getter
public enum SWorkflows {
    //WFG IMPORT
    IMPORT_TEILNEHMER("Import Teilnehmer"),
    //WFG MA ONBOARDING
    ERFASSEN_STAMMDATEN_VERTRAGSDATEN("Collect Data for New Mitarbeiter"),
    LOHNVERRECHNUNG("Inform the Lohnverrechnung for new Mitarbeiter"),
    UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG("Start the signing process of the contract for new Mitarbeiter"),
    LHR_DATEN_UEBERGEBEN("Send data to LHR for the new MA"),
    NEUEN_MA_ANLEGEN_IM_SYSTEM("Create new user in AD and DB"),
    //WFG TN ONBOARDING
    TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN("Collect Data for TN onboarding"),
    TN_ONBOARDING_LOHNVERRECHNUNG("Inform the Lohnverrechnung for TN onboarding"),
    TN_ONBOARDING_LHR_DATEN_UEBERGEBEN("Send data to LHR for TN onboarding"),
    TN_ONBOARDING_STAKEHOLDER("Inform the Stakeholder for TN onboarding"),
    //WFG TN AN ABWWESENHEITEN TRANSFER
    TN_AN_ABWESENHEITEN_TRANSFER_LHR("Transfer AN und Abwesenheiten for TN on LHR"),
    //WF FOR TN_TRITT AUS
    TN_TRITT_SICHERLICH_AUS("TN tritt sicherlich aus"),

    //WF FOR MA_ZEITERFASSUNG
    MA_ZEITERFASSUNG("MA Zeiterfassung"),

    //MA_FK_VEREINBARUNG
    MA_FK_VEREINBARUNG_STARTEN("Neue Vereinbarung zwischen MA und FK"),
    MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF("Unterschriftenlauf fuer Vereinbarung zwischen MA und FK"),


    //MA_VD_VERAENDERUNG
    MA_VD_VEREINBARUNG_STARTEN("Vertragsaenderung Starten"),
    MA_VD_PEOPLE("Vertragsdaten an People informieren und ueberpruefen"),
    MA_VD_LOHNVERRECHNUNG("Vertragsdaten an Lohnverrechnung informieren und ueberpruefen"),
    MA_VD_GEHNEHMIGEN("Vertragsdaten an Gehnehmiger informieren und ueberpruefen"),
    MA_VD_UNTERSCHRIFTENLAUF("Unterschriftenlauf fuer Vertragsaenderung"),
    MA_VD_ENDE("Vertragsaenderung finalisieren");

    private final String value;


    SWorkflows(String value) {
        this.value = value;
    }

    public static SWorkflows fromValue(String value) {
        for (SWorkflows source : SWorkflows.values()) {
            if (source.getValue().equals(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}
