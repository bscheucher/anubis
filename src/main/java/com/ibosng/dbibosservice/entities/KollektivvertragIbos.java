package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "KOLLEKTIVVERTRAG")
@Data
public class KollektivvertragIbos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "bezeichnung", length = 100, columnDefinition = "varchar(15)")
    private String bezeichnung;

    @Column(name = "erda", columnDefinition = "DATETIME")
    private LocalDateTime erda;

    @Column(name = "eruser", length = 35, columnDefinition = "char(35)")
    private String eruser;

    @Column(name = "aeda", columnDefinition = "DATETIME")
    private LocalDateTime aeda;

    @Column(name = "aeuser", length = 35, columnDefinition = "char(35)")
    private String aeuser;
}
