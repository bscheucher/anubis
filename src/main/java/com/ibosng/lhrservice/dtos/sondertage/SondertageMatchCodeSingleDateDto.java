package com.ibosng.lhrservice.dtos.sondertage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude()
public class SondertageMatchCodeSingleDateDto {
    private DienstnehmerRefDto dienstnehmer;
    private SondertagDto sondertag;
}