package com.ibosng.lhrservice.dtos.dienstraeder.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsStempelungDto {
    private DienstraederSettingsTimeFrameDto timeFrame = new DienstraederSettingsTimeFrameDto();
    private String timeToRoundTo;
}
