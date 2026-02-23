package com.ibosng.lhrservice.dtos.dienstraeder.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsErsatzzeitDto {
    private DienstraederSettingsTimeFrameDto timeFrame = new DienstraederSettingsTimeFrameDto();
    private String timeForCreatedBuchung;
}
