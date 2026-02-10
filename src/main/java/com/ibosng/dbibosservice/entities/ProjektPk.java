package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "PROJEKT_PK")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjektPk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "PROJEKT_id", nullable = false)
    private Integer projektId;

    @Column(name = "ADRESSE_id", nullable = false)
    private Integer adresseId;

    @Column(name = "PROJEKT_PK_FUNKTIONEN_id", nullable = false)
    private Integer projektPkFunktionenId;

    @Column(name = "beginn")
    private LocalDate beginn;

    @Column(name = "ende")
    private LocalDate ende;

    @Column(name = "aeda")
    private LocalDateTime aeda;

    @Column(name = "aeuser", columnDefinition = "char(35)")
    private String aeuser;

    @Column(name = "erda")
    private LocalDateTime erda;

    @Column(name = "eruser", columnDefinition = "char(35)")
    private String eruser;

}
