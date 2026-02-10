package com.ibosng.lhrservice.dtos.mitversicherte;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeschlechtDto {
    private String kz;
    private String description;
}
