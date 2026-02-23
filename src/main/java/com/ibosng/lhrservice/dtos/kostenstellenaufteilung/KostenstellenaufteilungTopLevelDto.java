package com.ibosng.lhrservice.dtos.kostenstellenaufteilung;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class KostenstellenaufteilungTopLevelDto {
    private DienstnehmerRefDto dienstnehmer;
    private List<KostenstellenaufteilungDto> kostenstellenaufteilung;
}