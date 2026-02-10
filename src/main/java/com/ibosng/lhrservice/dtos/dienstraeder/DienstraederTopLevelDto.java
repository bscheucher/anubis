package com.ibosng.lhrservice.dtos.dienstraeder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class DienstraederTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<DienstraederDataDto> dienstraeder;
}