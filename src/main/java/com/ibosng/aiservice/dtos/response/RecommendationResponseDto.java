package com.ibosng.aiservice.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationResponseDto {
    private String reasoning;
    private List<RecommendationDto> recommendations;
    private List<OptimalCombinationDto> optimalCombination;
    private String detailedJustification;
}
