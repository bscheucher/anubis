package com.ibosng.lhrservice.dtos.kostenstellenaufteilung;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude()
public class KostenstellenaufteilungSingleTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private KostenstellenaufteilungDto kostenstellenaufteilung;
}