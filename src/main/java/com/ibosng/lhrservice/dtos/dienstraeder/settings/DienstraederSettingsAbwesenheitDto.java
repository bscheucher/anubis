package com.ibosng.lhrservice.dtos.dienstraeder.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsAbwesenheitDto {
    private DienstraederSettingsZeitspeicherDto zeitspeicher = new DienstraederSettingsZeitspeicherDto();
    private String duration;
    private boolean ignoreOnPublicHoliday;
}
