package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "KV_STUFE")
@Data
public class KvStufeIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "kv_verwendungsgruppe_id", columnDefinition = "INT(6) UNSIGNED")
    private Integer kvVerwendungsgruppeId;

    @Column(name = "bezeichnung", length = 100, columnDefinition = "varchar(100)")
    private String bezeichnung;

    @Column(name = "jahre_von", columnDefinition = "INT(6) UNSIGNED")
    private Integer jahreVon;

    @Column(name = "jahre_bis", columnDefinition = "INT(6) UNSIGNED")
    private Integer jahreBis;

    @Column(name = "gueltig_von")
    private LocalDate gueltigVon;

    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

    @Column(name = "erda", columnDefinition = "DATETIME")
    private LocalDateTime erda;

    @Column(name = "eruser", length = 35, columnDefinition = "char(35)")
    private String eruser;

    @Column(name = "aeda", columnDefinition = "DATETIME")
    private LocalDateTime aeda;

    @Column(name = "aeuser", length = 35, columnDefinition = "char(35)")
    private String aeuser;
}
