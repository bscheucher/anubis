package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.GemeindeDto;
import lombok.Data;

@Data
@JsonInclude()
public class PflichtigkeitenDto {
    private Boolean db;
    private Boolean dz;
    private Boolean ubahn;
    private String kommunalSteuer;
    private GemeindeDto kommunalSteuerGemeinde;
}
