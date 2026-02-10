package com.ibosng.lhrservice.dtos.dienstraeder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import com.ibosng.lhrservice.dtos.dienstraeder.settings.DienstraederSettingsDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DienstraederSingleTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private DienstraederDataDto dienstrad;
    private List<DienstraederSettingsDto> settings;
}