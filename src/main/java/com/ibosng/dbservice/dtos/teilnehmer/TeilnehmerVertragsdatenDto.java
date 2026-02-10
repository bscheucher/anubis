package com.ibosng.dbservice.dtos.teilnehmer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeilnehmerVertragsdatenDto {
    private Integer id;
    private String eintritt;
    private Boolean isBefristet;
    private String befristungBis;
    private String dienstort;
    private String kostenstelle;
    private String kategorie;
    private String taetigkeit;
    private String kollektivvertrag;
    private String notizAllgemein;
    private String dienstnehmergruppe;
    private String abrechnungsgruppe;
    private String wochenstunden;
    private String klasse;
    private Integer lehrjahr;
    private LocalDate naechsteVorrueckung;
}