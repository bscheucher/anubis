package com.ibosng.lhrservice.dtos.dienstraeder.settings;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederSettingsDto {
    private DienstraederSettingsValidForDto validFor = new DienstraederSettingsValidForDto();
    private String note;
    private DienstraederSettingsZeitmodellDto zeitmodell = new DienstraederSettingsZeitmodellDto();
    private DienstraederSettingsSollzeitDto sollzeit = new DienstraederSettingsSollzeitDto();
    private DienstraederSettingsKorrekturenDto korrekturen = new DienstraederSettingsKorrekturenDto();
    private DienstraederSettingsArbeitszeitDto normalarbeitszeit = new DienstraederSettingsArbeitszeitDto();
    private DienstraederSettingsArbeitszeitDto kernzeit = new DienstraederSettingsArbeitszeitDto();
    private DienstraederSettingsErsatzBeginnEndzeitDto ersatzBeginnEndzeit = new DienstraederSettingsErsatzBeginnEndzeitDto();
    private DienstraederSettingsPauseDto pause = new DienstraederSettingsPauseDto();
    private DienstraederSettingsAbwesenheitDto abwesenheit = new DienstraederSettingsAbwesenheitDto();

}