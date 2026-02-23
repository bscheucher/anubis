package com.ibosng.lhrservice.dtos.sondertage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude()
public class SondertageMatchCodeDto {
    private DienstnehmerRefDto dienstnehmer;
    private SondertageDto sondertage;
}