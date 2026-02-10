package com.ibosng.lhrservice.dtos.persoenlicheSaetze;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude()
public class PersoenlicheSaetzeSingleTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private PersoenlicheSaetzeDto persoenlicherSatz;
}