package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NewParticipantsSummaryDto {
    private LocalDate date;
    private Integer projektNummer;
    private Integer seminarNummer;
    private String seminar;
    private String massnahmenummer;
    private String filename;
    private Long gesamt;
    private Long valid;
    private Long invalid;
}
