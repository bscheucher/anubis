package com.ibosng.dbibosservice.entities.pzleistung;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "PZLEISTUNG")
public class Pzleistung {

    @EmbeddedId
    private PzleistungId id;

    @Column(name = "LZtyp", columnDefinition = "enum('l', 's')")
    private String lztyp;

    @Column(name = "PZRECHNUNG_REnr", nullable = false)
    private Integer pzrechnungRenr;

    @Column(name = "LZdatum")
    private LocalDate lzdatum;

    @Column(name = "LZdatumt")
    private LocalTime lzdatumt;

    @Column(name = "LZbis")
    private LocalDate lzbis;

    @Column(name = "LZbist")
    private LocalTime lzbist;

    @Column(name = "LZpauseVon")
    private LocalTime lzpauseVon;

    @Column(name = "LZpauseBis")
    private LocalTime lzpauseBis;

    @Column(name = "LZdauer", precision = 4, scale = 2)
    private BigDecimal lzdauer;

    @Column(name = "LZtaetnr")
    private Integer lztaetnr;

    @Column(name = "LZtaettyp", columnDefinition = "enum('anw', 'abw')")
    private String lztaettyp;

    @Column(name = "PZ_TAETIGKEIT_ZUSATZ_id")
    private Integer pztaetigkeitZusatzId;

    @Column(name = "WORKSHOP_id")
    private Integer workshopId;

    @Column(name = "LZstundensatz", precision = 9, scale = 2)
    private BigDecimal lzstundensatz;

    @Column(name = "SM_AD_SMADnr")
    private Integer smAdSmadnr;

    @Column(name = "LZSMnr")
    private Integer lzsmnr;

    @Column(name = "LZKSTGR")
    private Integer lzkstgr;

    @Column(name = "LZDVnr")
    private Integer lzdvnr;

    @Column(name = "LZfahrtvonnach", columnDefinition = "text")
    private String lzfahrtvonnach;

    @Column(name = "LZkfz_kennzeichen", length = 20, columnDefinition = "char(20)")
    private String lzkfzKennzeichen;

    @Column(name = "LZkm", precision = 9, scale = 2)
    private BigDecimal lzkm;

    @Column(name = "LZkmgeld", precision = 9, scale = 2)
    private BigDecimal lzkmgeld;

    @Column(name = "LZspesen", precision = 9, scale = 2)
    private BigDecimal lzspesen;

    @Column(name = "LZtagesdiaeten", precision = 9, scale = 2)
    private BigDecimal lztagesdiaeten;

    @Column(name = "LZtagesdiaetensatz")
    private Integer lztagesdiaetensatz;

    @Column(name = "LZnaechtigung", precision = 9, scale = 2)
    private BigDecimal lznaechtigung;

    @Column(name = "LZspesensonstig", precision = 9, scale = 2)
    private BigDecimal lzspesensonstig;

    @Column(name = "LZbemerk", columnDefinition = "text")
    private String lzbemerk;

    @Column(name = "LZhomeoffice", length = 15)
    private String lzhomeoffice;

    @Column(name = "LZstatus")
    private Integer lzstatus;

    @Column(name = "LZloek", columnDefinition = "enum('n', 'y')")
    private String lzloek;

    @Column(name = "LZaeda")
    private LocalDateTime lzaeda;

    @Column(name = "LZaeuser", length = 35, columnDefinition = "char(35)")
    private String lzaeuser;

    @Column(name = "LZerda")
    private LocalDateTime lzerda;

    @Column(name = "LZeruser", length = 35, columnDefinition = "char(35)")
    private String lzeruser;
}
