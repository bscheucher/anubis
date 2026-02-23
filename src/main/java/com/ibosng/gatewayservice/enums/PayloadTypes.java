package com.ibosng.gatewayservice.enums;

import lombok.Getter;

@Getter
public enum PayloadTypes {
    TEILNEHMER("teilnehmer"),
    SEMINARS("seminars"),
    STAMMDATEN("stammdaten"),
    VERTRAGSDATEN("vertragsdaten"),
    WORKFLOW("workflow"),
    MASSNAHMENUMMER("massnahmenummer"),
    MASTERDATA("masterdata"),
    PERSONALNUMMER("personalnummer"),
    FILE("file"),
    MITARBEITER_SUMMARY("mitarbeiterSummary"),
    LV_ACCEPTANCE("lvAcceptance"),
    VORDIENSTZEITEN("vordienstzeiten"),
    VORDIENSTZEIT("vordienstzeit"),
    UNTERHALTSBERECHTIGTE("unterhaltsberechtigte"),
    UNTERHALTSBERECHTIGT("unterhaltsberechtigt"),
    TRANSLATIONS("translations"),
    KOSTENSTELLEN("kostenstellen"),
    WORKFLOWGROUP("workflowgroup"),
    VERWENDUNGSGRUPPEN("verwendugsgruppen"),
    TEILNEHMER_SUMMARY("teilnehmerSummary"),
    MOXIS("moxis"),
    PROJEKT("projekt"),
    TEILNEHMER_SEMINARS("teilnehmerSeminars"),
    SEMINAR("seminar"),
    TEILNEHMER_NOTIZ("teilnehmerNotiz"),
    TEILNEHMER_AUSBILDUNG("teilnehmerAusbildung"),
    TEILNEHMER_BERUFSERFAHRUNG("teilnehmerBerufserfahrung"),
    SEMINAR_PRUEFUNG("seminarPruefung"),
    TEILNEHMER_SPRACHKENNTNIS("teilnehmerSprachkenntnis"),
    TEILNEHMER_ZERTIFIKAT("teilnehmerZertifikat"),
    MITARBEITER_TYPE("mitarbeiter_type"),
    ABWESENHEIT("abwesenheit"),
    TN_ZEITERFASSUNG_TRANSFER("teilnehmerZeiterfassungTransfer"),
    ZEITERFASSUNG_TRANSFER_LIST("zeiterfassungTransferList"),
    ZEITBUCHUNGEN("zeitbuchungen"),
    ZEITAUSGLEICH("zeitausgleich"),
    ZEITSPEICHER("zeitspeicher"),
    AUSZAHLUNGSANTRAG("auszahlungsantrag"),
    UEBAABMELDUNGEN("uebaAbmeldungen"),
    URLAUB_OVERVIEW("urlaubOverview"),
    REPORT("report"),
    REPORT_PARAMETERS("reportParameters"),
    VERTRETUNGSPLAN("vertretungsPlan"),
    FOLDER_STRUCTURE("folderStructure"),
    BERUFE("berufe"),
    MA_CHANGE_LOG("maChangeLog"),
    VERTRAGSAENDERUNGEN_CHANGE_LOG("vertragsenderungenChangeLog"),
    ABWESENHEITEN_YEAR_LIST("abwesenheitenYearList"),
    MA_FILTERED("maFiltered"),
    VERTRAGSAENDERUNG_FILTERED("vertragsenderungFiltered"),
    VERTRAGSAENDERUNG_STATUSES("vertragsenderungStatuses"),
    VERTRAGSAENDERUNG("vertragsaenderung"),
    VEREINBARUNEN("vereinbarungen"),
    PRUEFUNG_EXPORT("pruefungExport"),
    ZEITBUCHUNGMETADATA("zeitbuchungMetadata"),
    TRAINER_SEMINAR("trainerSeminar"),
    MANAGER_PROJEKT("managerProjekt"),
    TEILNAHME_OVERVIEW("teilnahmeOverview"),
    UMBUCHUNG("umbuchung"),
    ERROR("error");

    private final String value;

    public static PayloadTypes fromValue(String value) {
        for (PayloadTypes request : PayloadTypes.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
    PayloadTypes(String value) {
        this.value = value;
    }
}
