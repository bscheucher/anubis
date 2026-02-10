package com.ibosng.lhrservice.dtos.mitversicherte;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MitversicherteTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<MitversicherteDataDto> mitversicherte;
}
