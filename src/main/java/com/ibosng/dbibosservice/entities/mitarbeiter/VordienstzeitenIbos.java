package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "VORDIENSTZEITEN")
@Data
public class VordienstzeitenIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "PERSONALBOGEN_id", nullable = false, columnDefinition = "INT(6) UNSIGNED DEFAULT 0")
    private Integer personalbogenId;

    @Column(name = "ARBEITSVERTRAG_id", columnDefinition = "INT(6) UNSIGNED")
    private Integer arbeitsvertragId;

    @Column(name = "firma", length = 100, columnDefinition = "varchar(100)")
    private String firma;

    @Column(name = "taetigkeit", length = 100, columnDefinition = "varchar(100)")
    private String taetigkeit;

    @Column(name = "nachweis", columnDefinition = "INT(1)")
    private Integer nachweis;

    @Column(name = "wochenstd", precision = 10, scale = 2, columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal wochenstd;

    @Column(name = "von")
    private LocalDate von;

    @Column(name = "bis")
    private LocalDate bis;

    @Enumerated(EnumType.STRING)
    @Column(name = "anstellungsart")
    private Anstellungsart anstellungsart;

    @Enumerated(EnumType.STRING)
    @Column(name = "erfahrungsart")
    private Erfahrungsart erfahrungsart;

    // Enums for the ENUM columns
    public enum Anstellungsart {
        fix,
        frei
    }

    public enum Erfahrungsart {
        facheinschl,
        allgemein
    }
}
