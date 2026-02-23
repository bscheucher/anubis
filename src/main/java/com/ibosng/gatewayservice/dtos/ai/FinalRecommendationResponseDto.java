package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

import java.util.List;

@Data
public class FinalRecommendationResponseDto {
    private String reasoning;
    private List<RecommendationDto> recommendations;
    private List<OptimalCombinationDto> optimalCombination;
    private String detailedJustification;
    private TrainerReplacementDto replacement;
}
