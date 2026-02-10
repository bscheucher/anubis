package com.ibosng.dbservice.dtos.teilnahme;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class TeilnahmeCreationDto {
    private int seminarId;
    private LocalDate date;
    private List<TeilnahmerTeilnahmeCreationDto> teilnehmers;
}
