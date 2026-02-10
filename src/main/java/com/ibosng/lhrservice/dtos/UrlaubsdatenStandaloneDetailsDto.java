package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.urlaube.UrlaubswertDailyDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude
public class UrlaubsdatenStandaloneDetailsDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<UrlaubswertDailyDto> urlaubsdaten;
}
