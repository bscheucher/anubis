package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Seminar2TrainerDto {
    private Integer seminarId;
    private String bezeichnung;
    private String standort;
    private String masnahmennummer;
    private String startDate;
    private String endDate;
    private String role;
}
