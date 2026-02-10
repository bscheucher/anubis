package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

@Data
public class OptimalCombinationDto {
    private String seminar;
    private String date;
    private String recommendedTrainer;
    private Integer recommendedTrainerId;
    private String reason;
}
