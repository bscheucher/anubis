package com.ibosng.lhrservice.dtos.persoenlicheSaetze;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class PersoenlicheSaetzeTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<PersoenlicheSaetzeDto> persoenlicheSaetze;
}