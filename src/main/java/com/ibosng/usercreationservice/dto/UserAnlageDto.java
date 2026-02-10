package com.ibosng.usercreationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Constants.ISO_DATE_PATTERN;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserAnlageDto {
    private String personalnummer;
    private String vorname;
    private String nachname;
    private String kostenstelle;
    private DienstortDto dienstort;
    private String fuehrungskraft;
    private String firma;
    private String startcoach;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate eintrittAm;
    private String kategorie;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate befristungBis;
}
