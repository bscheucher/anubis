package com.ibosng.lhrservice.dtos.dienstraeder.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsErsatzBeginnEndzeitDto {
    private DienstraederSettingsErsatzzeitDto vormittag = new DienstraederSettingsErsatzzeitDto();
    private DienstraederSettingsErsatzzeitDto nachmittag = new DienstraederSettingsErsatzzeitDto();
    private boolean ignoreOnPublicHoliday;
}
