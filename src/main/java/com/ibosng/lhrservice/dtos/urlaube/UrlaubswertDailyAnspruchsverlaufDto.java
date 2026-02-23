package com.ibosng.lhrservice.dtos.urlaube;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class UrlaubswertDailyAnspruchsverlaufDto {
    private Double kuerzung;
    private Double verjaehrung;
    private Double konsum;
    private UrlaubswertDailyAnspruchsverlaufRestAliquotDto restAliquot;
}
