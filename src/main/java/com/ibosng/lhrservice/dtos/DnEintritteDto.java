package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude()
public class DnEintritteDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<EintrittDto> eintritte;
}
