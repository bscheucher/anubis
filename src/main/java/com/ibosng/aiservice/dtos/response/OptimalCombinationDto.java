package com.ibosng.aiservice.dtos.response;

import lombok.Data;

@Data
public class OptimalCombinationDto {
    private String seminar;
    private String date;
    private String recommendedTrainer;
    private String reason;
}
