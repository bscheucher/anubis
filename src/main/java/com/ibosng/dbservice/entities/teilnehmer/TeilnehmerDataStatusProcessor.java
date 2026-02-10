package com.ibosng.dbservice.entities.teilnehmer;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

public class TeilnehmerDataStatusProcessor {

    public void setErrorFields(Teilnehmer teilnehmer, TeilnehmerStaging teilnehmerStaging, TeilnehmerDto dto) {
        for (TeilnehmerDataStatus dataStatus : teilnehmer.getErrors()) {
            String error = dataStatus.getError();

            switch (error) {
                case "titel":
                    if (!isNullOrBlank(teilnehmerStaging.getTitel())) {
                        dto.setTitel(teilnehmerStaging.getTitel());
                    }
                    break;
                case "vorname":
                    if (!isNullOrBlank(teilnehmerStaging.getVorname())) {
                        dto.setVorname(teilnehmerStaging.getVorname());
                    }
                    break;
                case "nachname":
                    if (!isNullOrBlank(teilnehmerStaging.getNachname())) {
                        dto.setNachname(teilnehmerStaging.getNachname());
                    }
                    break;
                case "geschlecht":
                    if (!isNullOrBlank(teilnehmerStaging.getGeschlecht())) {
                        dto.setGeschlecht(teilnehmerStaging.getGeschlecht());
                    }
                    break;
                case "sv_nummer":
                    if (!isNullOrBlank(teilnehmerStaging.getSvNummer())) {
                        dto.setSvNummer(teilnehmerStaging.getSvNummer());
                    }
                    break;
                case "geburtsdatum":
                    if (!isNullOrBlank(teilnehmerStaging.getGeburtsdatum())) {
                        dto.setGeburtsdatum(teilnehmerStaging.getGeburtsdatum());
                    }
                    break;
                case "buchungsstatus":
                    if (!isNullOrBlank(teilnehmerStaging.getBuchungsstatus())) {
                        dto.setBuchungsstatus(teilnehmerStaging.getBuchungsstatus());
                    }
                    break;
                case "anmerkung":
                    if (!isNullOrBlank(teilnehmerStaging.getAnmerkung())) {
                        dto.setAnmerkung(teilnehmerStaging.getAnmerkung());
                    }
                    break;
                case "zubuchung":
                    if (!isNullOrBlank(teilnehmerStaging.getZubuchung())) {
                        dto.setZubuchung(teilnehmerStaging.getZubuchung());
                    }
                    break;
                case "geplant":
                    if (!isNullOrBlank(teilnehmerStaging.getGeplant())) {
                        dto.setGeplant(teilnehmerStaging.getGeplant());
                    }
                    break;
                case "eintritt":
                    if (!isNullOrBlank(teilnehmerStaging.getEintritt())) {
                        dto.setEintritt(teilnehmerStaging.getEintritt());
                    }
                    break;
                case "austritt":
                    if (!isNullOrBlank(teilnehmerStaging.getAustritt())) {
                        dto.setAustritt(teilnehmerStaging.getAustritt());
                    }
                    break;
                case "rgs":
                    if (!isNullOrBlank(teilnehmerStaging.getRgs())) {
                        dto.setRgs(teilnehmerStaging.getRgs());
                    }
                    break;
                case "massnahmennummer":
                    if (!isNullOrBlank(teilnehmerStaging.getMassnahmennummer())) {
                        dto.setMassnahmennummer(teilnehmerStaging.getMassnahmennummer());
                    }
                    break;
                case "veranstaltungsnummer":
                    if (!isNullOrBlank(teilnehmerStaging.getVeranstaltungsnummer())) {
                        dto.setVeranstaltungsnummer(teilnehmerStaging.getVeranstaltungsnummer());
                    }
                    break;
                case "email":
                    if (!isNullOrBlank(teilnehmerStaging.getEmail())) {
                        dto.setEmail(teilnehmerStaging.getEmail());
                    }
                    break;
                case "betreuer_titel":
                    if (!isNullOrBlank(teilnehmerStaging.getBetreuerTitel())) {
                        dto.setBetreuerTitel(teilnehmerStaging.getBetreuerTitel());
                    }
                    break;
                case "betreuer_vorname":
                    if (!isNullOrBlank(teilnehmerStaging.getBetreuerVorname())) {
                        dto.setBetreuerVorname(teilnehmerStaging.getBetreuerVorname());
                    }
                    break;
                case "betreuer_nachname":
                    if (!isNullOrBlank(teilnehmerStaging.getBetreuerNachname())) {
                        dto.setBetreuerNachname(teilnehmerStaging.getBetreuerNachname());
                    }
                    break;
                case "plz":
                    if (!isNullOrBlank(teilnehmerStaging.getPlz())) {
                        dto.setPlz(teilnehmerStaging.getPlz());
                    }
                    break;
                case "ort":
                    if (!isNullOrBlank(teilnehmerStaging.getOrt())) {
                        dto.setOrt(teilnehmerStaging.getOrt());
                    }
                    break;
                case "strasse":
                    if (!isNullOrBlank(teilnehmerStaging.getStrasse())) {
                        dto.setStrasse(teilnehmerStaging.getStrasse());
                    }
                    break;
                case "nation":
                    if (!isNullOrBlank(teilnehmerStaging.getNation())) {
                        dto.setNation(teilnehmerStaging.getNation());
                    }
                    break;
                case "anrede":
                    if (!isNullOrBlank(teilnehmerStaging.getAnrede())) {
                        dto.setAnrede(teilnehmerStaging.getAnrede());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

