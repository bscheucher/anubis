package com.ibosng.aiservice.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationDto {
    private String seminar;
    private String date;
    private List<RankedTrainerDto> rankedTrainers;
}
