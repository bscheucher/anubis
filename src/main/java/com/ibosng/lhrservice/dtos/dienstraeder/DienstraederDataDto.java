package com.ibosng.lhrservice.dtos.dienstraeder;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class DienstraederDataDto {
    private String bezeichnung;
    private String beschreibung;
    private DienstraederTimeframeDto timeframe;
    private Integer weeks;
}
