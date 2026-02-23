package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.dtos.BasicPersonDto;
import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.dbibosservice.entities.BenutzerIbos;

import java.time.LocalDate;
import java.util.List;

public interface BenutzerIbosService extends BaseService<BenutzerIbos>{
    List<String> findRolesForUser(String user);
    List<BasicProjectDto> findActiveProjectForUser(String user);
    List<BasicProjectDto> findPastProjectForUser(String user);
    List<BasicProjectDto> findFutureProjectForUser(String user);
    ProjectRevenueDataDto findRevenueAndHoursFromProjectAndDates(Integer projectNumber, LocalDate von, LocalDate bis, boolean isProjectStartToToday, boolean isTodayToProjectEnd);
    ProjectForecastDataDto findProjectForecast(Integer projectNumber);
    List<BasicPersonDto> getAllFuehrungskraftForMitarbeiter();
    String getEmailForFuehrungskraftForMitarbeiter(Long adresseId);
    String getEmailForStartcoachForMitarbeiter(Long adresseId);
    List<BasicPersonDto> getAllStartcoachForMitarbeiter();
    Integer getTitelId(String titel);
    Integer saveTitel(String titel);
    String getTitelFromId(Integer id);
    List<String> getSigneesFromKostenStelle(Integer kostenstelle);
    BenutzerIbos findBenutzerByBnadnr(Integer bnadrn);
}
