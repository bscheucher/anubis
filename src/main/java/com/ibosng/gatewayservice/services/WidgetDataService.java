package com.ibosng.gatewayservice.services;

import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WidgetDataService {
    Object getMeineSeminare(String token);
    Object getFehlerhafteTeilnehmer(LocalDate date);
    Object getMeinePersoenlichenDatenWidgetData(String token);
    List<BasicProjectDto> findProjectsForUser(String token, Boolean isActive, Optional<Boolean> isInTheFuture);
    ProjectRevenueDataDto findRevenueForKPI(Integer projectNumber, LocalDate von, LocalDate bis, Boolean isProjectStartToToday, Boolean isTodayToProjectEnd);
    ProjectForecastDataDto findForecastForKPI(Integer projectNumber);
}
