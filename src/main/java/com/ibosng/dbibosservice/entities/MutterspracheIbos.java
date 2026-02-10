package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "MUTTERSPRACHE")
public class MutterspracheIbos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "kennzeichen_iso_639_1", length = 5, columnDefinition = "VARCHAR(5)")
    private String kennzeichenIso6391;

    @Column(name = "kennzeichen_iso_639_3", length = 5, columnDefinition = "VARCHAR(5)")
    private String kennzeichenIso6393;

    @Column(name = "kennzeichen_iso_639_2", length = 5, columnDefinition = "VARCHAR(5)")
    private String kennzeichenIso6392;

    @Column(name = "name", length = 100, columnDefinition = "VARCHAR(100)")
    private String name;
}
