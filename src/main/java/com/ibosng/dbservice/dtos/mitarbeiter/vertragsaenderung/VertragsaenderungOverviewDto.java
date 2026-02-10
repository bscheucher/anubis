package com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VertragsaenderungOverviewDto {
    private Integer id;
    private String personalnummer;
    private String nachname;
    private String vorname;
    private String svnr;
    private LocalDate gueltigAb;
    private String kostenstelle;
    private String status;
    private String interneAnmerkung;
}
