package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import com.ibosng.lhrservice.dtos.GruppeNameNrDto;
import lombok.Data;

@Data
@JsonInclude()
public class BuchhaltungDto {
    private GruppeNameNrDto buchungsbeleg;
    private GruppeNameKzDto kontoklasse;
}
