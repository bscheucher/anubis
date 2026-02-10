package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class SonstigesUrlaubDto {
    private SonstigesUrlaubAnspruchDto anspruch;
    private Integer zusaetzlicherUrlaub;
}
