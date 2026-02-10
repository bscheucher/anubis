package com.ibosng.dbibosservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.ibosng.dbibosservice.utils.Constants.DATE_PATTERN;

@Data
@AllArgsConstructor
public class ContractDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate datumVon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate datumBis;

    private String verwendungsgruppe;

    private String stufe;

    private String funktion;

    private Long kst;

    private String personalNummer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate eintrittsdatum;

    private BigDecimal wochenstunden;

    private String arbeitszeitmodel;

    private String dienstort;

    private String nichtLeistungen;
}
