package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "IBIS_FIRMA")
public class IbisFirmaIbos {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", length = 50, columnDefinition = "char(50)")
    private String name;

    @Column(name = "kurzname", nullable = false, length = 15, columnDefinition = "char(15)")
    private String kurzname;

    @Column(name = "cognos_code", length = 20, columnDefinition = "VARCHAR(20)")
    private String cognosCode;

    @Column(name = "bmd_klient_id", columnDefinition = "INT(6)'")
    private Integer bmdKlientId;

    @Column(name = "lhr_kz", length = 15, columnDefinition = "CHAR(15)")
    private String lhrKz;

    @Column(name = "lhr_nr", columnDefinition = "INT(6)")
    private Integer lhrNr;

    @Column(name = "saveable_for_new_items", columnDefinition = "TINYINT(1)")
    private Boolean saveableForNewItems;

    @Column(name = "fusszeiletxt", columnDefinition = "TEXT")
    private String fusszeileTxt;

    @Column(name = "reihung", columnDefinition = "INT(2) UNSIGNED")
    private Integer reihung;

    @Column(name = "erda")
    private LocalDateTime erda;

    @Column(name = "eruser", length = 35, columnDefinition = "char(35)")
    private String eruser;

    @Column(name = "aeda")
    private LocalDateTime aeda;

    @Column(name = "aeuser", length = 35, columnDefinition = "CHAR(35)")
    private String aeuser;
}
