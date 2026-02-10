package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "ARBEITSVERTRAG_FREI")
@Data
public class ArbeitsvertragFreiIbos {

    @Id
    @Column(name = "ARBEITSVERTRAG_id", nullable = false, updatable = false)
    private Integer arbeitsvertragId;

    @Column(name = "kur", columnDefinition = "TINYINT(1)")
    private Boolean kur;

    @Column(name = "uid", length = 40, columnDefinition = "VARCHAR(40)")
    private String uid;

    @Column(name = "finanzamt", length = 40, columnDefinition = "varchar(40)")
    private String finanzamt;

    @Column(name = "alg", columnDefinition = "TINYINT(1)")
    private Boolean alg;

    @Column(name = "fba_kopie_uebergeben", columnDefinition = "TINYINT(1)")
    private Boolean fbaKopieUebergeben;

    @Column(name = "vtr", length = 100, columnDefinition = "VARCHAR(100)")
    private String vtr;

    @Column(name = "vnr", length = 11, columnDefinition = "VARCHAR(11)")
    private String vnr;

    @Column(name = "gewart", length = 100, columnDefinition = "VARCHAR(100)")
    private String gewart;

    @Column(name = "gewab", columnDefinition = "DATE")
    private LocalDate gewab;

    @Column(name = "gewbh", length = 40, columnDefinition = "VARCHAR(40)")
    private String gewbh;

    @Column(name = "gewnr", length = 40, columnDefinition = "VARCHAR(40)")
    private String gewnr;

    @Column(name = "PROJEKT_id", columnDefinition = "INT(6) UNSIGNED DEFAULT 0")
    private Integer projektId;

    @Column(name = "SEMINAR_id", columnDefinition = "INT(6) UNSIGNED DEFAULT 0")
    private Integer seminarId;
}
