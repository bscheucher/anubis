package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

import java.util.List;

@Data
public class WeekDataDto {
    private String date;
    private List<TrainerDto> list;
    private Integer selection;
}