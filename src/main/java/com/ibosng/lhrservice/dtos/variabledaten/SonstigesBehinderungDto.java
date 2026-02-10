package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class SonstigesBehinderungDto {
    private String description;
    private String kz;
    private Integer percentage;
}
