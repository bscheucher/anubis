package com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VertragsaenderungBasicDto {
    private Integer id;
    private String personalnummer;
    private String antragssteller;
    private LocalDate gueltigAb;
}
