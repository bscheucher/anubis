package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

import java.util.List;

@Data
public class AISeminarDto {
    private String seminarTitle;
    private Integer seminarId;
    private List<WeekDataDto> weekData;
}
