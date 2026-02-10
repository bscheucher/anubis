package com.ibosng.lhrservice.dtos.sondertage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class SondertageTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<SondertageDto> sondertage;
}