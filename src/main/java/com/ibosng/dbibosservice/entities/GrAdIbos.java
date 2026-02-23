package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "GR_AD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GrAdId.class)
public class GrAdIbos {

    @Id
    @Column(name = "GRUPPE_GRnr", nullable = false)
    private Integer gruppeGrnr;

    @Id
    @Column(name = "ADRESSE_ADadnr", nullable = false)
    private Integer adresseAdadnr;

    @Column(name = "GRADaeda")
    private LocalDateTime aenderungsdatum;

    @Column(name = "GRADaeuser", columnDefinition = "char(35)")
    private String aenderungsUser;

    @Column(name = "GRADerda")
    private LocalDateTime erstellungsdatum;

    @Column(name = "GRADeruser", columnDefinition = "char(35)")
    private String erstellungsUser;
}
