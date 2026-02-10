package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude()
public class DnEintrittDto {
    private DienstnehmerRefDto dienstnehmer;
    private EintrittDto eintritt;
}