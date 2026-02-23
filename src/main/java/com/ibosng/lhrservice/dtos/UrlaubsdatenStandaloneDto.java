package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.urlaube.UrlaubswertDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class UrlaubsdatenStandaloneDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<UrlaubswertDto> urlaubsdaten;
}
