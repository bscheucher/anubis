package com.ibosng.dbibosservice.entities.mitarbeiter;

import com.ibosng.dbibosservice.converters.BooleanStatusConverter;
import com.ibosng.dbibosservice.enums.BooleanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERSONALBOGEN")
@Data
public class PersonalbogenIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PBid", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "PB_ADadnr", nullable = false, columnDefinition = "INT(6) UNSIGNED")
    private Integer adresseAdnr;

    @Enumerated(EnumType.STRING)
    @Column(name = "PBart", nullable = false)
    private PersonalbogenArt pbArt;

    @Column(name = "PBtitel", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbTitel;

    @Column(name = "PBznf1", length = 50, columnDefinition = "varchar(50)")
    private String pbZnf1;

    @Column(name = "PBvnf1", length = 50, columnDefinition = "varchar(50)")
    private String pbVnf1;

    @Column(name = "PBfirma", length = 100, columnDefinition = "varchar(100)")
    private String pbFirma;

    @Column(name = "PBstrasse", length = 50, columnDefinition = "varchar(50)")
    private String pbStrasse;

    @Column(name = "PBplz", length = 6, columnDefinition = "varchar(6)")
    private String pbPlz;

    @Column(name = "PBort", length = 50, columnDefinition = "varchar(50)")
    private String pbOrt;

    @Column(name = "PBsvnr", length = 11, columnDefinition = "varchar(11)")
    private String pbSvnr;

    @Column(name = "PBgebdat")
    private LocalDate pbGebdat;

    @Column(name = "PBgetdatf", columnDefinition = "INT(1) UNSIGNED DEFAULT 0")
    private Integer pbGetdatf;

    @Column(name = "PBstaatsb", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbStaatsb;

    @Column(name = "PBfamstand", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbFamstand;

    @Column(name = "PBkinder", columnDefinition = "INT(2) UNSIGNED")
    private Integer pbKinder;

    @Column(name = "PBunterhaltspflichtige", columnDefinition = "INT(6) DEFAULT 0")
    private Integer pbUnterhaltspflichtige;

    @Column(name = "PBemail", length = 80, columnDefinition = "varchar(80)")
    private String pbEmail;

    @Column(name = "PBkonto", columnDefinition = "INT(1) DEFAULT 0")
    private Integer pbKonto;

    @Column(name = "PBbank", length = 40, columnDefinition = "varchar(40)")
    private String pbBank;

    @Column(name = "PBblz", length = 11, columnDefinition = "varchar(11)")
    private String pbBlz;

    @Column(name = "PBkontonr", length = 34, columnDefinition = "varchar(34)")
    private String pbKontonr;

    @Column(name = "PBiban", length = 34, columnDefinition = "varchar(34)")
    private String pbIban;

    @Column(name = "PBbic", length = 34, columnDefinition = "varchar(34)")
    private String pbBic;

    @Column(name = "PBdienstgeber", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbDienstgeber;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBavab")
    private BooleanStatus pbAvab;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBpendlerp")
    private BooleanStatus pbPendlerp;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBkopie_ueberg")
    private BooleanStatus pbKopieUeberg;

    @Column(name = "PBagen", length = 40, columnDefinition = "varchar(40)")
    private String pbAgen;

    @Column(name = "PBagenbis", length = 5, columnDefinition = "varchar(5)")
    private String pbAgenbis;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBagenkopie_ueberg")
    private BooleanStatus pbAgenkopieUeberg;

    @Column(name = "PBagenbh", length = 40, columnDefinition = "varchar(40)")
    private String pbAgenbh;

    @Column(name = "PBvtr", length = 100, columnDefinition = "varchar(100)")
    private String pbVtr;

    @Column(name = "PBvnr", length = 11, columnDefinition = "varchar(11)")
    private String pbVnr;

    @Column(name = "PBgewart", length = 40, columnDefinition = "varchar(40)")
    private String pbGewart;

    @Column(name = "PBgewab")
    private LocalDate pbGewab;

    @Column(name = "PBgewbh", length = 40, columnDefinition = "varchar(40)")
    private String pbGewbh;

    @Column(name = "PBgewnr", length = 40, columnDefinition = "varchar(40)")
    private String pbGewnr;

    @Column(name = "STANDORT_SOstandortid", columnDefinition = "INT(6) UNSIGNED")
    private Integer standortSoStandortId;

    @Column(name = "PBbereich", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbBereich;

    @Column(name = "PBtaetart", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbTaetart;

    @Column(name = "PBtaetmasz", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbTaetmasz;

    @Column(name = "PBstatus", columnDefinition = "INT(1) UNSIGNED DEFAULT 0")
    private Integer pbStatus;

    @Column(name = "PBsmorpj", columnDefinition = "INT(1) UNSIGNED DEFAULT 0")
    private Integer pbSmorpj;

    @Column(name = "PBzuweisung", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbZuweisung;

    @Column(name = "PBbegin")
    private LocalDate pbBegin;

    @Column(name = "PBende")
    private LocalDate pbEnde;

    @Column(name = "PBtaetstatus", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbTaetstatus;

    @Column(name = "PBtage", length = 30)
    private String pbTage;

    @Column(name = "PBwochenstd", precision = 5, scale = 2, columnDefinition = "DECIMAL(5, 2) UNSIGNED")
    private BigDecimal pbWochenstd;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBgleitzeit")
    private BooleanStatus pbGleitzeit;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBgleitzeit_light")
    private BooleanStatus pbGleitzeitLight;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBmobileworking")
    private BooleanStatus pbMobileWorking;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBvdgeprueft")
    private BooleanStatus pbVdGeprueft;

    @Column(name = "PBvstufe", columnDefinition = "INT UNSIGNED")
    private Integer pbVstufe;

    @Column(name = "PBvbereich_old", columnDefinition = "INT UNSIGNED")
    private Integer pbVbereichOld;

    @Column(name = "PBvstufe_old", columnDefinition = "INT UNSIGNED")
    private Integer pbVstufeOld;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBbetriebsvereinbarung")
    private BooleanStatus pbBetriebsvereinbarung;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBreisekostenrichtlinie")
    private BooleanStatus pbReisekostenrichtlinie;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBgleitzeitvereinbarung")
    private BooleanStatus pbGleitzeitvereinbarung;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBgleitzeitvereinbarung_light")
    private BooleanStatus pbGleitzeitvereinbarungLight;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBmobileworkingvereinbarung")
    private BooleanStatus pbMobileWorkingVereinbarung;

    @Column(name = "PBansaessbesch", columnDefinition = "TINYINT(1)")
    private Boolean pbAnsaessBesch;

    @Column(name = "PBformblattA1", columnDefinition = "TINYINT(1)")
    private Boolean pbFormblattA1;

    @Column(name = "PBgehalt", precision = 9, scale = 2, columnDefinition = "DECIMAL(9, 2) UNSIGNED")
    private BigDecimal pbGehalt;

    @Column(name = "PBzulage", precision = 9, scale = 2, columnDefinition = "DECIMAL(9, 2) UNSIGNED")
    private BigDecimal pbZulage;

    @Column(name = "PB_SCid", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbScId;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBalg")
    private BooleanStatus pbAlg;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBkur")
    private BooleanStatus pbKur;

    @Column(name = "PBuid", length = 40, columnDefinition = "varchar(40)")
    private String pbUid;

    @Column(name = "PBfinanzamt", length = 40, columnDefinition = "varchar(40)")
    private String pbFinanzamt;

    @Column(name = "PBbemerkung", columnDefinition = "TEXT")
    private String pbBemerkung;

    @Column(name = "PBvdbemerk", columnDefinition = "TEXT")
    private String pbVdBemerk;

    @Column(name = "PBfahrp", length = 128, columnDefinition = "char(128)")
    private String pbFahrp;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "PBloek")
    private BooleanStatus pbLoek;

    @Column(name = "PBaeda", columnDefinition = "DATETIME")
    private LocalDateTime pbAeda;

    @Column(name = "PBaeuser", length = 35, columnDefinition = "char(35)")
    private String pbAeuser;

    @Column(name = "PBerda", columnDefinition = "DATETIME")
    private LocalDateTime pbErda;

    @Column(name = "PBeruser", length = 35, columnDefinition = "char(35)")
    private String pbEruser;

    public enum PersonalbogenArt {
        fix,
        freio,
        freim
    }
}
