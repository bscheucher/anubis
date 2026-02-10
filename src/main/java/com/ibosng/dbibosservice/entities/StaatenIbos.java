package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "STAATEN",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "TLD"),
                @UniqueConstraint(columnNames = "alpha2"),
                @UniqueConstraint(columnNames = "alpha3"),
                @UniqueConstraint(columnNames = "bmd_id"),
                @UniqueConstraint(columnNames = "kennziffer"),
                @UniqueConstraint(columnNames = "reihung")
        },
        indexes = {
                @Index(name = "TLD_UNIQUE", columnList = "TLD", unique = true),
                @Index(name = "alpha2_UNIQUE", columnList = "alpha2", unique = true),
                @Index(name = "alpha3_UNIQUE", columnList = "alpha3", unique = true),
                @Index(name = "bmd_id_UNIQUE", columnList = "bmd_id", unique = true),
                @Index(name = "kennziffer_UNIQUE", columnList = "kennziffer", unique = true),
                @Index(name = "reihung_UNIQUE", columnList = "reihung", unique = true)
        })
@Data
public class StaatenIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "bmd_id", columnDefinition = "INT(6) UNSIGNED")
    private Integer bmdId;

    @Column(name = "reihung", nullable = false, columnDefinition = "INT(6) UNSIGNED")
    private Integer reihung;

    @Column(name = "bezeichnung", length = 60, columnDefinition = "VARCHAR(60) COLLATE latin1_german2_ci")
    private String bezeichnung;

    @Column(name = "f2w_bezeichnung", length = 60, columnDefinition = "VARCHAR(60) COLLATE latin1_german2_ci")
    private String f2wBezeichnung;

    @Column(name = "alpha2", length = 2, columnDefinition = "CHAR(2) COLLATE latin1_german2_ci")
    private String alpha2;

    @Column(name = "alpha3", length = 3, columnDefinition = "CHAR(3) COLLATE latin1_german2_ci")
    private String alpha3;

    @Column(name = "kennziffer", columnDefinition = "INT(4) UNSIGNED")
    private Integer kennziffer;

    @Column(name = "TLD", length = 2, columnDefinition = "CHAR(2) COLLATE latin1_german2_ci")
    private String tld;
}
