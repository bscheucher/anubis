package com.ibosng.lhrservice.dtos.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class DnZeitdatenPeriodensummenPeriodensummenDto {
    private List<TagZeitspeicherwerteDto> vortrag;
    private List<TagZeitspeicherwerteDto> periodenschluss;
    private List<TagZeitspeicherwerteDto> abrechnung;
    private List<TagZeitspeicherwerteDto> uebertrag;
}
