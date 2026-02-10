package com.ibosng.fileimportservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantOEIFDto {

    @JsonSetter("Kennung")
    private String kennung;

    @JsonSetter("Nachname")
    private String nachname;

    @JsonSetter("Vorname")
    private String vorname;

    @JsonSetter("Geschlecht")
    private String geschlecht;

    @JsonSetter("Geburtsdatum")
    private String geburtsdatum;

    @JsonSetter("Mobiltelefon")
    private String mobiltelefon;

    @JsonSetter("Teilnahmebeginn")
    private String teilnahmebeginn;

    @JsonSetter("Anmeldestatus")
    private String anmeldestatus;

    @JsonSetter("Individuelles Niveau")
    private String individuellesNiveau;

    @JsonSetter("Email")
    private String email;

    @JsonSetter("Sozialversicherungsnummer")
    private String svn;

    @JsonSetter("AMS")
    private String ams;

    @JsonSetter("Abmeldegrund")
    private String abmeldegrund;

    @JsonSetter("Anwesenheit in UE")
    private String anwesenheitInUE;

    @JsonSetter("Entschuldigte Abwesenheit in UE")
    private String entschuldigteAnwesenheitInUE;

    @JsonSetter("Unentschuldigte Abwesenheit in UE")
    private String unentschuldigteAnwesenheitInUE;

    @JsonSetter("Summe erfasste An- und Abwesenheit")
    private String summeErfassteAnUndAbwesenheit;

    @JsonSetter("Anwesenheit erfasst bis (inkl.)")
    private String anwesenheitErfasstBis;

    @JsonSetter("Absolute Anwesenheit in %")
    private String absoluteAnwesenheit;

    @JsonSetter("Relative Anwesenheit in %")
    private String relativeAnwesenheit;

    @JsonSetter("Seminar ID")
    private String seminarId;
}
