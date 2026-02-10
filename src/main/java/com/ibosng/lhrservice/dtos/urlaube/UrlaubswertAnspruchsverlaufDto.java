package com.ibosng.lhrservice.dtos.urlaube;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class UrlaubswertAnspruchsverlaufDto {
    private Double kuerzung;
    private Double verjaehrung;
    private Double anspruch;
    private Double konsum;
    private Double rest;
}
