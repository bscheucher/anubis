package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.GruppeNameNrDto;
import lombok.Data;

@Data
@JsonInclude()
public class SonstigesDto {
    private SonstigesAutomatikDto automatik;
    private SonstigesBehinderungDto behinderung;
    private GruppeNameNrDto beruf;
    private Boolean betriebsratsumlage;
    private SonstigesUrlaubDto urlaub;
}
