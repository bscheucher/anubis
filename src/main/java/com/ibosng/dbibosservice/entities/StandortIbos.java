package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "STANDORT")
public class StandortIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOstandortid", nullable = false)
    private Integer soStandortId;

    @Column(name = "SObezeichnung", columnDefinition = "char(60)")
    private String soBezeichnung;

    @Column(name = "SObezkurz", columnDefinition = "char(20)")
    private String soBezKurz;

    @Column(name = "SObundesland")
    private Integer soBundesland;

    @Column(name = "SOstrasse", columnDefinition = "char(50)")
    private String soStrasse;

    @Column(name = "SOlkz", columnDefinition = "char(6)")
    private String soLkz;

    @Column(name = "SOplz", columnDefinition = "char(6)")
    private String soPlz;

    @Column(name = "SOort", columnDefinition = "char(50)")
    private String soOrt;

    @Column(name = "SOtelefon", columnDefinition = "char(40)")
    private String soTelefon;

    @Column(name = "SOtelefax", columnDefinition = "char(40)")
    private String soTelefax;

    @Column(name = "SOemail", columnDefinition = "char(80)")
    private String soEmail;

    @Column(name = "SOgs")
    private Integer soGs;

    @Column(name = "SOvermieternr")
    private Integer soVermieterNr;

    @Column(name = "SOvermieternr_old")
    private Integer soVermieterNrOld;

    @Column(name = "SOhverw")
    private Integer soHverw;

    @Column(name = "SOhverw_old")
    private Integer soHverwOld;

    @Column(name = "SOverantwort")
    private Integer soVerantwort;

    @Column(name = "SObemerkung", columnDefinition = "TEXT")
    private String soBemerkung;

    @Column(name = "SOkstgrp")
    private Integer soKstGrp;

    @Column(name = "SOkstnr")
    private Integer soKstNr;

    @Column(name = "SOkstsub")
    private Integer soKstSub;

    @Column(name = "SOraumflaeche")
    private Integer soRaumflaeche;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOfinanzierung", columnDefinition = "ENUM('e', 'm', 'l', 's')")
    private SoFinanzierung soFinanzierung;

    @Column(name = "SOmietemonat", precision = 9, scale = 2)
    private BigDecimal soMietemonat;

    @Column(name = "SObetriebskosten", precision = 9, scale = 2)
    private BigDecimal soBetriebskosten;

    @Column(name = "SOreinigung", precision = 9, scale = 2)
    private BigDecimal soReinigung;

    @Column(name = "SOheizung", precision = 6, scale = 2)
    private BigDecimal soHeizung;

    @Column(name = "SOkostensonstige", precision = 9, scale = 2)
    private BigDecimal soKostenSonstige;

    @Column(name = "SOsonstbemerk", columnDefinition = "char(20)")
    private String soSonstBemerk;

    @Column(name = "SOverfuegbarvon")
    private LocalDate soVerfuegbarVon;

    @Column(name = "SOverfuegbarbis")
    private LocalDate soVerfuegbarBis;

    @Column(name = "SOpreisprostunde", precision = 9, scale = 2)
    private BigDecimal soPreisProStunde;

    @Column(name = "SOinternet")
    private Character soInternet;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOkatgs", columnDefinition = "ENUM('n', 'y')")
    private SoKatgs soKatgs = SoKatgs.n;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOkatsmort", columnDefinition = "ENUM('n', 'y')")
    private SoKatsmort soKatsmort = SoKatsmort.n;

    @Column(name = "SOpublishvon")
    private LocalDate soPublishVon;

    @Column(name = "SOpublishbis")
    private LocalDate soPublishBis;

    @Column(name = "SOmakler")
    private Integer soMakler;

    @Column(name = "SOmaklerprovision", precision = 9, scale = 2)
    private BigDecimal soMaklerprovision;

    @Column(name = "SOprovisionsdatum")
    private LocalDate soProvisionsdatum;

    @Column(name = "SOkaution", precision = 9, scale = 2)
    private BigDecimal soKaution;

    @Column(name = "SOkautionbeguenstigter")
    private Integer soKautionBeguenstigter;

    @Column(name = "SOkautionsart")
    private Integer soKautionsart;

    @Column(name = "SOkautionablaufdatum")
    private LocalDate soKautionAblaufdatum;

    @Column(name = "SOstromanmeldestand", columnDefinition = "char(12)")
    private String soStromanmeldestand;

    @Column(name = "SOstromanmeldedatum")
    private LocalDate soStromanmeldedatum;

    @Column(name = "SOstromanmeldungdurch", columnDefinition = "char(20)")
    private String soStromanmeldungdurch;

    @Column(name = "SOstromabmeldestand", columnDefinition = "char(12)")
    private String soStromabmeldestand;

    @Column(name = "SOstromabmeldedatum")
    private LocalDate soStromabmeldedatum;

    @Column(name = "SOstromabmeldungdurch", columnDefinition = "char(20)")
    private String soStromabmeldungdurch;

    @Column(name = "SOgasanmeldestand", columnDefinition = "char(12)")
    private String soGasAnmeldestand;

    @Column(name = "SOgasanmeldedatum")
    private LocalDate soGasAnmeldedatum;

    @Column(name = "SOgasanmeldungdurch", columnDefinition = "char(20)")
    private String soGasAnmeldungdurch;

    @Column(name = "SOgasabmeldestand", columnDefinition = "char(12)")
    private String soGasAbmeldestand;

    @Column(name = "SOgasabmeldedatum")
    private LocalDate soGasAbmeldedatum;

    @Column(name = "SOgasabmeldungdurch", columnDefinition = "char(20)")
    private String soGasAbmeldungdurch;

    @Column(name = "SOtelefonanmeldedatum")
    private LocalDate soTelefonAnmeldedatum;

    @Column(name = "SOtelefonanmeldungdurch", columnDefinition = "char(20)")
    private String soTelefonAnmeldungdurch;

    @Column(name = "SOtelefonabmeldedatum")
    private LocalDate soTelefonAbmeldedatum;

    @Column(name = "SOtelefonabmeldungdurch", columnDefinition = "char(20)")
    private String soTelefonAbmeldungdurch;

    @Column(name = "SOinternetanmeldedatum")
    private LocalDate soInternetAnmeldedatum;

    @Column(name = "SOinternetanmeldungdurch", columnDefinition = "char(20)")
    private String soInternetAnmeldungdurch;

    @Column(name = "SOinternetabmeldedatum")
    private LocalDate soInternetAbmeldedatum;

    @Column(name = "SOinternetabmeldungdurch", columnDefinition = "char(20)")
    private String soInternetAbmeldungdurch;

    @Column(name = "SOstatus")
    private Integer soStatus;

    @Column(name = "SOpopup", columnDefinition = "TEXT")
    private String soPopup;

    @Column(name = "SObildLink", columnDefinition = "TEXT")
    private String soBildLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOloek", columnDefinition = "ENUM('n', 'y')")
    private SoLoek soLoek = SoLoek.n;

    @Column(name = "SOaeda")
    private LocalDateTime soAeda;

    @Column(name = "SOaeuser", columnDefinition = "char(35)")
    private String soAeuser;

    @Column(name = "SOerda", nullable = false)
    private LocalDateTime soErda;

    @Column(name = "SOeruser", columnDefinition = "char(35)", nullable = false)
    private String soEruser;

    // Enums for the 'enum' columns
    public enum SoFinanzierung {
        e, m, l, s
    }

    public enum SoKatgs {
        n, y
    }

    public enum SoKatsmort {
        n, y
    }

    public enum SoLoek {
        n, y
    }

    // NoArgsConstructor, AllArgsConstructor, Getters, Setters, Equals, HashCode, ToString
}
