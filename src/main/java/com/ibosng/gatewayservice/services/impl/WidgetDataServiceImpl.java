package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbibosservice.services.impl.SeminarIbosServiceImpl;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragIbosService;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.WidgetDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WidgetDataServiceImpl implements WidgetDataService {

    private final SeminarIbosServiceImpl seminarService;

    private final ArbeitsvertragIbosService arbeitsvertragIbosService;

    private final TeilnehmerService teilnehmerService;

    private final BenutzerDetailsService benutzerDetailsService;

    private final BenutzerIbosService benutzerIbosService;

    public WidgetDataServiceImpl(SeminarIbosServiceImpl seminarService,
                                 ArbeitsvertragIbosService arbeitsvertragIbosService,
                                 TeilnehmerService teilnehmerService,
                                 BenutzerDetailsService benutzerDetailsService,
                                 BenutzerIbosService benutzerIbosService) {
        this.seminarService = seminarService;
        this.arbeitsvertragIbosService = arbeitsvertragIbosService;
        this.teilnehmerService = teilnehmerService;
        this.benutzerDetailsService = benutzerDetailsService;
        this.benutzerIbosService = benutzerIbosService;
    }

    @Override
    public Object getMeineSeminare(String token) {
        Benutzer user = benutzerDetailsService.getUserFromToken(token);
        String firstAndLastName = user.getFirstName() + "." + user.getLastName();

        log.info("Processing data for Mein Tag for user: " + firstAndLastName);
        return seminarService.getSeminarDataRaw(firstAndLastName);
    }

    @Override
    public Object getFehlerhafteTeilnehmer(LocalDate date) {
        return teilnehmerService.getSummaryImportedTeilnehmer(date);
    }

    @Override
    public Object getMeinePersoenlichenDatenWidgetData(String token) {
        Benutzer user = benutzerDetailsService.getUserFromToken(token);
        String firstAndLastName = user.getFirstName() + "." + user.getLastName();

        log.info("Processing data for Meine Persönliche Daten for user: " + firstAndLastName);
        return arbeitsvertragIbosService.getAllContracts(firstAndLastName);
    }

    @Override
    public List<BasicProjectDto> findProjectsForUser(String token, Boolean isActive, Optional<Boolean> isInTheFuture) {
        Benutzer user = benutzerDetailsService.getUserFromToken(token);
        String firstAndLastName = user.getFirstName() + "." + user.getLastName();
        if (isActive) {
            if (isInTheFuture != null && isInTheFuture.isPresent() && isInTheFuture.get()) {
                return benutzerIbosService.findFutureProjectForUser(firstAndLastName);
            }
            return benutzerIbosService.findActiveProjectForUser(firstAndLastName);
        } else {
            return benutzerIbosService.findPastProjectForUser(firstAndLastName);
        }
    }

    @Override
    public ProjectRevenueDataDto findRevenueForKPI(Integer projectNumber, LocalDate von, LocalDate bis, Boolean isProjectStartToToday, Boolean isTodayToProjectEnd) {
        return benutzerIbosService.findRevenueAndHoursFromProjectAndDates(projectNumber, von, bis, isProjectStartToToday, isTodayToProjectEnd);
    }

    @Override
    public ProjectForecastDataDto findForecastForKPI(Integer projectNumber) {
        return benutzerIbosService.findProjectForecast(projectNumber);
    }
}
