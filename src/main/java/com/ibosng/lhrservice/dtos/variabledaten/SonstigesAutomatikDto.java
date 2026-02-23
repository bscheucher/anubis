package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class SonstigesAutomatikDto {
    private String betriebsvorsorge;
    private String pendlerpauschale;
    private String svgruppen;
}
