package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ARBEITSVERTRAG_ZUSATZ")
public class ArbeitsvertragZusatzIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "ARBEITSVERTRAG_id", columnDefinition = "INT(6) UNSIGNED")
    private Integer arbeitsvertragId;

    @Column(name = "datum_von")
    private LocalDate datumVon;

    @Column(name = "datum_bis")
    private LocalDate datumBis;

    @Column(name = "geschaeftsbereich", columnDefinition = "INT(6) UNSIGNED")
    private Integer geschaeftsbereich;

    @Column(name = "dienstgeber", columnDefinition = "INT(6) UNSIGNED")
    private Integer dienstgeber;

    @Column(name = "persnr", length = 10, columnDefinition = "VARCHAR(10)")
    private String persnr;

    @Column(name = "taetbezeichnung", columnDefinition = "INT(6) UNSIGNED")
    private Integer taetbezeichnung;

    @Column(name = "taetausmass", columnDefinition = "INT(6) UNSIGNED")
    private Integer taetausmass;

    @Column(name = "dienstort", columnDefinition = "INT(6) UNSIGNED")
    private Integer dienstort;

    @Column(name = "fahrkostenpauschale", length = 128, columnDefinition = "VARCHAR(128)")
    private String fahrkostenpauschale;

    @Column(name = "bemerkung", columnDefinition = "TEXT")
    private String bemerkung;

    @Column(name = "bemerkung_drucken", columnDefinition = "TINYINT(1)")
    private Boolean bemerkungDrucken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "unterschrieben_am", columnDefinition = "DATETIME")
    private LocalDateTime unterschriebenAm;

    @Column(name = "DZid_old", columnDefinition = "INT(6) UNSIGNED")
    private Integer dzidOld;

    @Enumerated(EnumType.STRING)
    @Column(name = "loek")
    private Loek loek;

    @Column(name = "aeda", columnDefinition = "DATETIME")
    private LocalDateTime aeda;

    @Column(name = "aeuser", length = 35, columnDefinition = "CHAR(35)")
    private String aeuser;

    @Column(name = "erda", columnDefinition = "DATETIME")
    private LocalDateTime erda;

    @Column(name = "eruser", length = 35, columnDefinition = "CHAR(35)")
    private String eruser;

    // Enum types for the enum fields
    public enum Status {
        neu,
        freigegeben,
        unterschrieben,
        gesperrt
    }

    public enum Loek {
        n,
        y
    }
}