package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import lombok.Data;

@Data
@JsonInclude()
public class PendlerpauschaleDto {
    private String anspruch;
    private GruppeNameKzDto art;
    private Integer kilometer;
}
