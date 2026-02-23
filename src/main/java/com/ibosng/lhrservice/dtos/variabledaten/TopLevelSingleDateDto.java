package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude()
public class TopLevelSingleDateDto {
    private DienstnehmerRefDto dienstnehmer;
    private VariableDatenDto variableDaten;
}