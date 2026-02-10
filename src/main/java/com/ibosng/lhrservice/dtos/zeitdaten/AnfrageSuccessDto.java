package com.ibosng.lhrservice.dtos.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude()
public class AnfrageSuccessDto {
    private DienstnehmerRefDto dienstnehmer;
    private Integer anfrage;
    private String status;
}
