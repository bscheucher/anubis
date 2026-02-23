package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

import static com.ibosng.lhrservice.utils.Constants.LOCAL_DATE_FORMAT;

@Data
@JsonInclude()
public class KollektivvertragDto {
    private KollektivvertragEinstufungDto einstufung;
    private String kollektivvertragName;
    private Integer kollektivvertragNr;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_FORMAT)
    private LocalDate letzteVorrueckung;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_FORMAT)
    private LocalDate naechsteVorrueckung;
}
