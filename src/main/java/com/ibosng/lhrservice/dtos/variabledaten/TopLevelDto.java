package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class TopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<VariableDatenDto> variableDaten;
}