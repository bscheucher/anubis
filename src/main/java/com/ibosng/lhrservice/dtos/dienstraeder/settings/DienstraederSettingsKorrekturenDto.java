package com.ibosng.lhrservice.dtos.dienstraeder.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsKorrekturenDto {
    private DienstraederSettingsStempelungDto vormittag = new DienstraederSettingsStempelungDto();
    private DienstraederSettingsStempelungDto nachmittag = new DienstraederSettingsStempelungDto();
    private boolean ignoreOnPublicHoliday;
}
