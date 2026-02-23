package com.ibosng.gatewayservice.controllers;


import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.gatewayservice.services.TokenValidatorService;
import com.ibosng.gatewayservice.services.WidgetDataService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
class WidgetControllerTest extends BaseIntegrationTest {

    @Mock
    private TokenValidatorService tokenValidatorService;

    @Mock
    private WidgetDataService widgetDataService;

    @InjectMocks
    private WidgetController widgetController;

    @Test
    void testGetFehlerhafteTeilnehmer() {

        when(tokenValidatorService.isTokenValid(anyString())).thenReturn(true);

        Object sampleWidgetData = new Object();

        when(widgetDataService.getFehlerhafteTeilnehmer(any())).thenReturn(sampleWidgetData);

        ResponseEntity<Object> responseEntity = widgetController.getFehlerhafteTeilnehmer("Bearer validToken", LocalDate.now());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(sampleWidgetData, responseEntity.getBody());
    }

    @Test
    void testGetMeineSeminare() {

        Object sampleWidgetData = new Object();

        when(tokenValidatorService.isTokenValid(anyString())).thenReturn(true);
        when(widgetDataService.getMeineSeminare(anyString())).thenReturn(sampleWidgetData);

        ResponseEntity<Object> responseEntity = widgetController.getMeineSeminare("Bearer validToken");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sampleWidgetData, responseEntity.getBody());
    }


    @Test
    void testGetMeinePersoenlichenDatenWidgetData() {
        when(tokenValidatorService.isTokenValid(anyString())).thenReturn(true);

        Object sampleWidgetData = new Object();

        when(widgetDataService.getMeinePersoenlichenDatenWidgetData(anyString())).thenReturn(sampleWidgetData);

        ResponseEntity<Object> responseEntity = widgetController.getMeinePersoenlichenDatenWidgetData("Bearer validToken");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sampleWidgetData, responseEntity.getBody());
    }


    @Test
    void testGetProjectsForUser() {
        when(tokenValidatorService.isTokenValid(anyString())).thenReturn(true);

        List<BasicProjectDto> sampleWidgetData = new ArrayList<>();
        sampleWidgetData.add(new BasicProjectDto(1, "test"));

        when(widgetDataService.findProjectsForUser(anyString(), any(Boolean.class), any(Optional.class))).thenReturn(sampleWidgetData);

        ResponseEntity<List<BasicProjectDto>> responseEntity = widgetController.getProjectsForUser("Bearer validToken", true, Optional.of(true));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sampleWidgetData, responseEntity.getBody());
    }

    @Test
    void testGetRevenueForProjectFromStartToToday() {
        when(tokenValidatorService.isTokenValid(anyString())).thenReturn(true);
        LocalDate von = LocalDate.now().minusDays(10);
        LocalDate bis = LocalDate.now().plusDays(10);

        ProjectRevenueDataDto sampleWidgetData = new ProjectRevenueDataDto();
        sampleWidgetData.setSollStunden(new BigDecimal(31));

        when(widgetDataService.findRevenueForKPI(anyInt(), any(LocalDate.class), any(LocalDate.class), any(Boolean.class), any(Boolean.class))).thenReturn(sampleWidgetData);

        ResponseEntity<ProjectRevenueDataDto> responseEntity = widgetController.getRevenueForProject(3, von, bis, true, false);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sampleWidgetData, responseEntity.getBody());
    }

    @Test
    void testGetRevenueForProjectFromTodayToProjectEnd() {
        when(tokenValidatorService.isTokenValid(anyString())).thenReturn(true);
        LocalDate von = LocalDate.now().minusDays(10);
        LocalDate bis = LocalDate.now().plusDays(10);

        ProjectRevenueDataDto sampleWidgetData = new ProjectRevenueDataDto();
        sampleWidgetData.setSollStunden(new BigDecimal(31));

        when(widgetDataService.findRevenueForKPI(anyInt(), any(LocalDate.class), any(LocalDate.class), any(Boolean.class), any(Boolean.class))).thenReturn(sampleWidgetData);

        ResponseEntity<ProjectRevenueDataDto> responseEntity = widgetController.getRevenueForProject(3, von, bis, false, true);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sampleWidgetData, responseEntity.getBody());
    }


    @Test
    void testGetForecastForProject() {
        when(tokenValidatorService.isTokenValid(anyString())).thenReturn(true);

        ProjectForecastDataDto sampleWidgetData = new ProjectForecastDataDto();
        sampleWidgetData.setSollStunden(new BigDecimal(31));

        when(widgetDataService.findForecastForKPI(anyInt())).thenReturn(sampleWidgetData);

        ResponseEntity<ProjectForecastDataDto> responseEntity = widgetController.getForecastForProject(3);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sampleWidgetData, responseEntity.getBody());
    }

}
