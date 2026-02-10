package com.ibosng.lhrservice.dtos.mitversicherte;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MitversicherteSingleDto {
    private DienstnehmerRefDto dienstnehmer;
    private MitversicherteSingleDataDto mitversicherte;
}
