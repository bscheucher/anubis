package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.dbibosservice.repositories.BenutzerIbosRepository;
import com.ibosng.dbibosservice.services.impl.BenutzerIbosServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Transactional("mariaDbTransactionManager")
public class BenutzerIbosServiceMockTest extends BaseIntegrationTest {

    private static final String PROJECT_NAME = "TEST_PROJECT";
    private static final Integer PROJECT_NUMBER = 3;
    private static final String ROLE1 = "admin";
    private static final String ROLE2 = "PK";
    @Mock
    private BenutzerIbosRepository benutzerIbosRepository;

    @InjectMocks
    private BenutzerIbosServiceImpl benutzerService;

    @Test
    public void findActiveProjectForUser() {
        when(benutzerIbosRepository.findActiveProjectsForUser(anyString())).thenReturn(Collections.singletonList(createRawProjectData()));

        List<BasicProjectDto> result = benutzerService.findActiveProjectForUser("test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(result.get(0).getProjektNumber(), PROJECT_NUMBER);
        assertEquals(result.get(0).getProject(), PROJECT_NAME);
    }

    @Test
    public void findPastProjectForUser() {
        when(benutzerIbosRepository.findPastProjectsForUser(anyString())).thenReturn(Collections.singletonList(createRawProjectData()));

        List<BasicProjectDto> result = benutzerService.findPastProjectForUser("test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(result.get(0).getProjektNumber(), PROJECT_NUMBER);
        assertEquals(result.get(0).getProject(), PROJECT_NAME);
    }

    @Test
    public void findFutureProjectForUser() {
        when(benutzerIbosRepository.findFutureProjectsForUser(anyString())).thenReturn(Collections.singletonList(createRawProjectData()));

        List<BasicProjectDto> result = benutzerService.findFutureProjectForUser("test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(result.get(0).getProjektNumber(), PROJECT_NUMBER);
        assertEquals(result.get(0).getProject(), PROJECT_NAME);
    }

    @Test
    public void findRevenueAndHoursFromProjectAndDates() {
        when(benutzerIbosRepository.findIsAndSollRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawIsSollData()));
        when(benutzerIbosRepository.findPlanRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawPlanData()));
        ProjectRevenueDataDto projectRevenueDataDto = benutzerService.findRevenueAndHoursFromProjectAndDates(3, LocalDate.now(), LocalDate.now(), false, false);
        assertNotNull(projectRevenueDataDto);
    }

    @Test
    public void findRevenueAndHoursFromProjectStart() {
        when(benutzerIbosRepository.findIsAndSollRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawIsSollData()));
        when(benutzerIbosRepository.findPlanRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawPlanData()));
        ProjectRevenueDataDto projectRevenueDataDto = benutzerService.findRevenueAndHoursFromProjectAndDates(3, LocalDate.now(), LocalDate.now(), true, false);
        assertNotNull(projectRevenueDataDto);
    }

    @Test
    public void findRevenueAndHoursFromTodayToProjectEnd() {
        when(benutzerIbosRepository.findIsAndSollRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawIsSollData()));
        when(benutzerIbosRepository.findPlanRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawPlanData()));
        when(benutzerIbosRepository.findProjectSeminarGreatestEndDate(anyInt())).thenReturn(LocalDate.now().plusMonths(3));
        ProjectRevenueDataDto projectRevenueDataDto = benutzerService.findRevenueAndHoursFromProjectAndDates(3, LocalDate.now(), LocalDate.now(), false, true);
        assertNotNull(projectRevenueDataDto);
    }

    @Test
    public void findProjectForecast() {
        when(benutzerIbosRepository.findIsAndSollRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawIsSollData()));
        when(benutzerIbosRepository.findPlanRevenueBetweenDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.singletonList(createRawPlanData()));
        when(benutzerIbosRepository.findForecastRevenue(anyInt())).thenReturn(Collections.singletonList(createRawForecastData()));
        ProjectForecastDataDto projectForecastDataDto = benutzerService.findProjectForecast(3);
        assertNotNull(projectForecastDataDto);
    }

    @Test
    public void findRolesForUser() {
        when(benutzerIbosRepository.findRolesForUser(anyString())).thenReturn(Stream.of(ROLE1, ROLE2).collect(Collectors.toList()));

        List<String> roles = benutzerService.findRolesForUser("test");

        assertNotNull(roles);
        assertFalse(roles.isEmpty());
        assertEquals(2, roles.size());
        assertEquals(roles.get(0), ROLE1);
        assertEquals(roles.get(1), ROLE2);
    }

    private Object[] createRawProjectData() {
        return new Object[]{
                PROJECT_NUMBER,
                PROJECT_NAME
        };
    }

    private Object[] createRawIsSollData() {
        return new Object[]{
                PROJECT_NUMBER,
                "2023-07-01",
                "2023-11-30",
                BigDecimal.valueOf(564.3),
                BigDecimal.valueOf(5634.3),
                BigDecimal.valueOf(54364.3),
                BigDecimal.valueOf(11564.3),
                BigDecimal.valueOf(55564.3),
        };
    }

    private Object[] createRawPlanData() {
        return new Object[]{
                BigDecimal.valueOf(8425.7),
                BigDecimal.valueOf(579.1)
        };
    }

    private Object[] createRawForecastData() {
        return new Object[]{
                BigDecimal.valueOf(8764.3),
                BigDecimal.valueOf(634.3),
                BigDecimal.valueOf(364.3),
                BigDecimal.valueOf(117564.3),
        };
    }

}
