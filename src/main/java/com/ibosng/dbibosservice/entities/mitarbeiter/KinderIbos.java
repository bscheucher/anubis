package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "KINDER")
@Data
public class KinderIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "ADRESSE_adnr", columnDefinition = "INT(6) UNSIGNED")
    private Integer adresseAdnr;

    @Column(name = "vorname", length = 100, columnDefinition = "varchar(100)")
    private String vorname;

    @Column(name = "nachname", length = 100, columnDefinition = "varchar(100)")
    private String nachname;

    @Column(name = "svnr", length = 50, columnDefinition = "varchar(50)")
    private String svnr;

    @Column(name = "gebdatum")
    private LocalDate gebdatum;

    @Enumerated(EnumType.STRING)
    @Column(name = "volljaehrig")
    private Volljaehrig volljaehrig = Volljaehrig.n;

    // Enum for the 'volljaehrig' field
    public enum Volljaehrig {
        n,
        y
    }
}
