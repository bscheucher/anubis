package com.ibosng.lhrservice.dtos.dienstraeder.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsArbeitszeitDto {
    private DienstraederSettingsTimeFrameDto vormittag = new DienstraederSettingsTimeFrameDto();
    private DienstraederSettingsTimeFrameDto nachmittag = new DienstraederSettingsTimeFrameDto();
    private boolean ignoreOnPublicHoliday;
}
