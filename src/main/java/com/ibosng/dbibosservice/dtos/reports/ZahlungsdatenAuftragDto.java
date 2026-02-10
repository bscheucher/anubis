package com.ibosng.dbibosservice.dtos.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ZahlungsdatenAuftragDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Bezeichnung")
    private String bezeichnung;

    @JsonProperty("Zielgruppe")
    private String zielgruppe;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("P Nummer")
    private String pNummer;

    @JsonProperty("Abgabedatum")
    private LocalDate abgabedatum;

    @JsonProperty("von")
    private LocalDate von;

    @JsonProperty("bis")
    private LocalDate bis;

    @JsonProperty("Geschäftstelle")
    private Integer geschaeftsstelle;

    @JsonProperty("Bundesland")
    private String bundesland;

    @JsonProperty("Abrechnungsmodus")
    private String abrechnungsmodus;

    @JsonProperty("Datum geplant")
    private LocalDate datumGeplant;

    @JsonProperty("Betrag geplant")
    private BigDecimal betragGeplant;

    @JsonProperty("Datum eingang")
    private LocalDate datumEingang;

    @JsonProperty("Betrag eingang")
    private BigDecimal betragEingang;

    @JsonProperty("Zahlungspflichtiger")
    private String zahlungspflichtiger;

    @JsonProperty("Empfänger 1")
    private String empfaenger1;

    @JsonProperty("Empfänger 2")
    private String empfaenger2;

    @JsonProperty("Zahlungstyp")
    private String zahlungstyp;

    @JsonProperty("Status Zahlung")
    private String statusZahlung;

    @JsonProperty("Datum_im_Abfragezeitraum")
    private Boolean datumImAbfragezeitraum;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("Kontakt")
    private LocalDate kontakt;

    @JsonProperty("vorgezogen Projektstart")
    private LocalDate vorgezogenProjektstart;

    @JsonProperty("ProjektdauerTage")
    private Integer projektdauerTage;

    @JsonProperty("ProjektdauerTageDrittel")
    private Integer projektdauerTageDrittel;

    @JsonProperty("Laufzeitdrittel")
    private LocalDate laufzeitdrittel;

    @JsonProperty("vorgezogenDrittel")
    private LocalDate vorgezogenDrittel;

    @JsonProperty("KontaktDrittel")
    private LocalDate kontaktDrittel;

    @JsonProperty("Projektendevorgezogen")
    private LocalDate projektendeVorgezogen;

    @JsonProperty("Sum")
    private BigDecimal sum;

    @JsonProperty("Sum Abfragejahr")
    private BigDecimal sumAbfragejahr;

    @JsonProperty("SumTZ")
    private BigDecimal sumTZ;

    @JsonProperty("SumAkonto")
    private BigDecimal sumAkonto;

    @JsonProperty("SumRestzahlung")
    private BigDecimal sumRestzahlung;

    @JsonProperty("Prozent")
    private BigDecimal prozent;
}

