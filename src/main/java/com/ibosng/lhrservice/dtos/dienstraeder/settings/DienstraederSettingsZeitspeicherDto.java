package com.ibosng.lhrservice.dtos.dienstraeder.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsZeitspeicherDto {
    private int zeitspeicherNummer;
    private String zeitspeicherName;
}
