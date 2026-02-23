package com.ibosng.fileimportservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantVHSDto {

    @JsonSetter("INFO")
    private String info;

    @JsonSetter("Vorname")
    private String vorname;

    @JsonSetter("Familienname")
    private String nachname;

    @JsonSetter("Geschlecht")
    private String geschlecht;

    @JsonSetter("SVNr")
    private String svNummer;

    @JsonSetter("PLZ")
    private String plz;

    @JsonSetter("Ort")
    private String ort;

    @JsonSetter("Straße")
    private String strasse;

    @JsonSetter("Telefon")
    private String telefon;

    @JsonSetter("Nation")
    private String nation;

    @JsonSetter("RGS")
    private String rgs;

    @JsonSetter("Anmerkung")
    private String anmerkung;

    @JsonSetter("SeminarId")
    private String seminarId;
}
