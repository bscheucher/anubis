package com.ibosng.lhrservice.dtos.kostenstellenaufteilung;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import com.ibosng.lhrservice.dtos.GruppeNameNrDto;
import lombok.Data;

@Data
@JsonInclude()
public class KostenstellenaufteilungDataDto {
    private GruppeNameNrDto kostenstelle;
    private GruppeNameKzDto kostentraeger;
    private double anteil;
    @JsonProperty("isStammKostenstelle")
    private boolean isStammKostenstelle;
}
