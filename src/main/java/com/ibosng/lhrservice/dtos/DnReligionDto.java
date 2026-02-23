package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnReligionDto {

    private String designator;
    private String name;
}
