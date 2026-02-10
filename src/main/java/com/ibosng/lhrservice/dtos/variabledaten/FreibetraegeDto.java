package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class FreibetraegeDto {
    private Integer p63_103;
    private Integer p35_3;
    private Integer p63_105;
}
