package com.ibosng.workflowservice.enums;

import lombok.Getter;

@Getter
public enum SWorkflowGroups {
    IMPORT_TEILNEHMER_FILES("Import Teilnehmer Files"),
    NEW_MITARBEITER("Create New Mitarbeiter"),
    TN_ONBOARDING("Teilnehmer Onboarding"),
    TN_AN_ABWESENHEITEN_TRANSFER("TN An und Abwesenheiten Transfer"),
    TN_TRITT_AUS("TN tritt aus"),
    MA_ZEITERFASSUNG("MA Zeiterfassung zum Monatsende"),
    MA_FK_VEREINBARUNG("Vereinbarung zwischen MA und FK"),
    MA_VD_VERAENDERUNG("Vertragsaenderung starten und vervollstaendigen");

    private final String value;


    SWorkflowGroups(String value) {
        this.value = value;
    }

    public static SWorkflowGroups fromValue(String value) {
        for (SWorkflowGroups source : SWorkflowGroups.values()) {
            if (source.getValue().equals(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}
