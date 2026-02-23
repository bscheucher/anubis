package com.ibosng.lhrservice.dtos.urlaube;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class UrlaubswertDailyAnspruchsverlaufRestAliquotDto {
    private Double unrounded;
    private Double rounded;
}
