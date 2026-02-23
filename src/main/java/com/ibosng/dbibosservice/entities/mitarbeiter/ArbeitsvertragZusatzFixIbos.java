package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "ARBEITSVERTRAG_ZUSATZ_FIX")
public class ArbeitsvertragZusatzFixIbos {

    @Id
    @Column(name = "ARBEITSVERTRAG_ZUSATZ_id", nullable = false, updatable = false)
    private Integer arbeitsvertragZusatzId;

    @Column(name = "taetstatus", columnDefinition = "INT(6) UNSIGNED")
    private Integer taetstatus;

    @Column(name = "stdmo", precision = 5, scale = 2, columnDefinition = "DECIMAL(5, 2)")
    private BigDecimal stdmo;

    @Column(name = "stdmovon", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stdmovon;

    @Column(name = "stdmobis", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stdmobis;

    @Column(name = "stddi", precision = 5, scale = 2, columnDefinition = "DECIMAL(5, 2)")
    private BigDecimal stddi;

    @Column(name = "stddivon", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stddivon;

    @Column(name = "stddibis", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stddibis;

    @Column(name = "stdmi", precision = 5, scale = 2, columnDefinition = "DECIMAL(5, 2)")
    private BigDecimal stdmi;

    @Column(name = "stdmivon", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stdmivon;

    @Column(name = "stdmibis", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stdmibis;

    @Column(name = "stddo", precision = 5, scale = 2, columnDefinition = "DECIMAL(5, 2)")
    private BigDecimal stddo;

    @Column(name = "stddovon", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stddovon;

    @Column(name = "stddobis", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stddobis;

    @Column(name = "stdfr", precision = 5, scale = 2, columnDefinition = "DECIMAL(5, 2)")
    private BigDecimal stdfr;

    @Column(name = "stdfrvon", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stdfrvon;

    @Column(name = "stdfrbis", columnDefinition = "TIME DEFAULT '00:00:00'")
    private LocalTime stdfrbis;

    @Column(name = "stufe", columnDefinition = "INT(6) UNSIGNED")
    private Integer stufe;

    @Column(name = "gehalt", precision = 9, scale = 2, columnDefinition = "DECIMAL(9, 2)")
    private BigDecimal gehalt;

    @Column(name = "gehaltszulage", precision = 9, scale = 2)
    private BigDecimal gehaltszulage;

    @Column(name = "gleitzeit", columnDefinition = "TINYINT(1)")
    private Boolean gleitzeit;

    @Column(name = "gleitzeit_light", columnDefinition = "TINYINT(1)")
    private Boolean gleitzeitLight;

    @Column(name = "mobile_working", columnDefinition = "TINYINT(1)")
    private Boolean mobileWorking;

    @Column(name = "gehaltskorrektur", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean gehaltskorrektur;

    @Column(name = "mittagspause_ignorieren", columnDefinition = "TINYINT(1)")
    private Boolean mittagspauseIgnorieren;

    @Column(name = "aenderung_in_befristung", columnDefinition = "TINYINT(1)")
    private Boolean aenderungInBefristung;

    @Enumerated(EnumType.STRING)
    @Column(name = "av_dauer")
    private AvDauer avDauer;

    @Column(name = "kategorie", columnDefinition = "INT(6) UNSIGNED")
    private Integer kategorie;

    @Column(name = "sicherheitsunterweisung_uebergeben_am")
    private LocalDate sicherheitsunterweisungUebergebenAm;

    @Column(name = "Arbeitsplanmodell")
    private Integer arbeitsplanmodell;

    @Column(name = "Umbuchungsmodell")
    private Integer umbuchungsmodell;

    @Column(name = "Plangruppe")
    private Integer plangruppe;

    // Enum type for 'av_dauer'
    public enum AvDauer {
        befristung1,
        befristung2,
        unbefristet
    }
}