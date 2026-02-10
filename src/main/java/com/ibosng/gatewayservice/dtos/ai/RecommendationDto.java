package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationDto {
    private String seminar;
    private Integer seminarId;
    private String date;
    private List<RankedTrainerDto> rankedTrainers;
}
