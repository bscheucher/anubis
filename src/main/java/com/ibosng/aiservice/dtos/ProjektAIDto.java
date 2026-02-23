package com.ibosng.aiservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjektAIDto {
    private Integer id;
    private Integer projektNummer;
    private Integer auftragNummer;
    private String bezeichnung;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    private Integer kostenstelleGruppe;
    private Integer kostenstelle;
    private Integer kostentraeger;
    private String projektTypeName;
}
