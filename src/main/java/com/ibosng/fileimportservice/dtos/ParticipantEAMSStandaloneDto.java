package com.ibosng.fileimportservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ibosng.fileimportservice.config.DateToStringDeserializer;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantEAMSStandaloneDto {

    @JsonSetter("Titel")
    private String titel;

    @JsonSetter("Familien-/Nachname")
    private String nachname;

    @JsonSetter("Vorname")
    private String vorname;

    @JsonSetter("Geschlecht")
    private String geschlecht;

    @JsonSetter("SV-Nummer")
    private String svNummer;

    @JsonSetter("Geburtsdatum")
    @JsonDeserialize(using = DateToStringDeserializer.class)
    private String geburtsdatum;

    @JsonSetter("PLZ")
    private String plz;

    @JsonSetter("Ort")
    private String ort;

    @JsonSetter("Straße")
    private String strasse;

    @JsonSetter("Landesvorwahl")
    private String landesvorwahl;

    @JsonSetter("Vorwahl")
    private String vorwahl;

    @JsonSetter("Telefon-Nr")
    private String telefonNr;

    @JsonSetter("Buchungsstatus")
    private String buchungsstatus;

    @JsonSetter("Anmerkung")
    private String anmerkung;

    @JsonSetter("Zubuchung")
    @JsonDeserialize(using = DateToStringDeserializer.class)
    private String zubuchung;

    @JsonSetter("geplant")
    @JsonDeserialize(using = DateToStringDeserializer.class)
    private String geplant;

    @JsonSetter("Eintritt")
    @JsonDeserialize(using = DateToStringDeserializer.class)
    private String eintritt;

    @JsonSetter("Austritt")
    @JsonDeserialize(using = DateToStringDeserializer.class)
    private String austritt;

    @JsonSetter("RGS")
    private String rgs;

    @JsonSetter("Titel Betreuer")
    private String titelBetreuer;

    @JsonSetter("Familien-/Nachname Betreuer")
    private String nachnameBetreuer;

    @JsonSetter("Vorname Betreuer")
    private String vornameBetreuer;

    @JsonSetter("Maßnahmennummer")
    private String massnahmennummer;

    @JsonSetter("Veranstaltungsnummer")
    private String veranstaltungsnummer;

    @JsonSetter("eMail-Adresse")
    private String emailAdresse;

    @JsonSetter("Seminar ID")
    private String seminarId;
}
