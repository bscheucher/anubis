package com.ibosng.dbibosservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

import static com.ibosng.dbibosservice.utils.Constants.DATE_PATTERN;

@Data
@AllArgsConstructor
public class SeminarDto {


    private Integer projektNummer;

    private Integer seminarNummer;

    private String ort;

    private String seminar;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate von;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDate bis;

    private String uhrzeit;
}
