package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class SonstigesUrlaubAnspruchDto {
    private String description;
    private Integer nr;
}
