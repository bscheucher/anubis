package com.ibosng.dbservice.dtos.urlaubsdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude()
public class AbwesenheitOverviewDto {
    private String nextAnspruch;
    private String month;
    private long rest;
    private long konsum;
    private long verbraucht;
    @Builder.Default
    private List<AbwesenheitDetailedDto> urlaube = new ArrayList<>();
}
