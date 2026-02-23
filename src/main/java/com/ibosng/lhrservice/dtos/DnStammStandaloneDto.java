package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@JsonInclude()
public class DnStammStandaloneDto {

    private DienstnehmerRefDto dienstnehmer;
    @Valid
    private DienstnehmerstammDto dienstnehmerstamm;
    private DienstnehmerRefDto primaryDienstnehmer;
}
