package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.dbibosservice.services.impl.BenutzerIbosServiceImpl;
import com.ibosng.dbibosservice.services.impl.SeminarIbosServiceImpl;
import com.ibosng.dbibosservice.services.impl.mitarbeiter.ArbeitsvertragIbosServiceImpl;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.services.impl.TeilnehmerServiceImpl;
import com.ibosng.gatewayservice.services.impl.WidgetDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class WidgetDataServiceImplTest extends BaseIntegrationTest {

    @Mock
    private ArbeitsvertragIbosServiceImpl arbeitsvertragIbosService;

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    private BenutzerIbosServiceImpl benutzerIbosService;

    @Mock
    private TeilnehmerServiceImpl teilnehmerService;

    @Mock
    private SeminarIbosServiceImpl seminarService;

    @InjectMocks
    private WidgetDataServiceImpl widgetDataServiceImpl;

    @Test
    public void testGetMeineSeminare() {
        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("Test");
        benutzer.setLastName("Test");

        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(seminarService.getSeminarDataRaw(anyString())).thenReturn(new ArrayList<>());

        Object result = widgetDataServiceImpl.getMeineSeminare("Bearer dummyToken");

        assertNotNull(result);
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testGetMeinePersoenlichenDatenWidgetData() {

        String token = "sampleToken";
        Optional<String> additionalInfo = Optional.of("additionalInfo");

        Benutzer mockUser = new Benutzer();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        when(benutzerDetailsService.getUserFromToken(token)).thenReturn(mockUser);
        when(arbeitsvertragIbosService.getAllContracts(anyString())).thenReturn(new ArrayList<>());

        Object result = widgetDataServiceImpl.getMeinePersoenlichenDatenWidgetData(token);

        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testFindActiveProjectsForUser() {
        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findActiveProjectForUser(anyString())).thenReturn(createProjects());
        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", true, Optional.of(false));
        assertNotNull(result);
        assertEquals(result.size(), 3);
    }

    @Test
    public void testFindPastProjectsForUser() {
        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findPastProjectForUser(anyString())).thenReturn(createProjects());
        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", false, Optional.of(false));
        assertNotNull(result);
        assertEquals(result.size(), 3);
    }

    @Test
    public void testFindFutureProjectsForUser() {
        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findFutureProjectForUser(anyString())).thenReturn(createProjects());
        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", true, Optional.of(true));
        assertNotNull(result);
        assertEquals(result.size(), 3);
    }

    @Test
    public void testFindRevenueForKPI() {
        ProjectRevenueDataDto projectRevenueDataDto = new ProjectRevenueDataDto();
        when(benutzerIbosService.findRevenueAndHoursFromProjectAndDates(anyInt(), any(LocalDate.class), any(LocalDate.class), any(Boolean.class), any(Boolean.class))).thenReturn(projectRevenueDataDto);
        ProjectRevenueDataDto result = widgetDataServiceImpl.findRevenueForKPI(3, LocalDate.now(), LocalDate.now(), true, false);
        assertNotNull(result);
    }

    @Test
    public void testFindForecastForKPI() {
        ProjectForecastDataDto projectRevenueDataDto = new ProjectForecastDataDto();
        when(benutzerIbosService.findProjectForecast(anyInt())).thenReturn(projectRevenueDataDto);
        ProjectForecastDataDto result = widgetDataServiceImpl.findForecastForKPI(3);
        assertNotNull(result);
    }

    @Test
    public void testGetFehlerhafteTeilnehmer() {

        when(teilnehmerService.getSummaryImportedTeilnehmer(any())).thenReturn(new ArrayList<>());

        Object result = widgetDataServiceImpl.getFehlerhafteTeilnehmer(LocalDate.now());

        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(0, ((List<?>) result).size());
    }

    @Test
    public void testFindProjectsForUser_NoFutureFlag() {

        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findActiveProjectForUser(anyString())).thenReturn(createProjects());

        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", true, Optional.empty());

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindRevenueForKPI_NullResult() {

        when(benutzerIbosService.findRevenueAndHoursFromProjectAndDates(anyInt(), any(LocalDate.class), any(LocalDate.class), any(Boolean.class), any(Boolean.class))).thenReturn(null);

        ProjectRevenueDataDto result = widgetDataServiceImpl.findRevenueForKPI(3, LocalDate.now(), LocalDate.now(), true, false);

        assertNull(result);
    }

    @Test
    public void testFindProjectsForUser_NoActiveProjects() {

        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findActiveProjectForUser(anyString())).thenReturn(new ArrayList<>());

        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", true, Optional.of(false));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindProjectsForUser_NoPastProjects() {

        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findPastProjectForUser(anyString())).thenReturn(new ArrayList<>());

        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", false, Optional.of(false));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindProjectsForUser_NoFutureProjects() {

        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findFutureProjectForUser(anyString())).thenReturn(new ArrayList<>());

        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", true, Optional.of(true));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindRevenueForKPI_NullInputs() {

        ProjectRevenueDataDto result = widgetDataServiceImpl.findRevenueForKPI(null, null, null, true, true);

        assertNull(result);
    }

    @Test
    public void testFindRevenueForKPI_ValidInputs() {

        ProjectRevenueDataDto revenueDataDto = new ProjectRevenueDataDto();
        when(benutzerIbosService.findRevenueAndHoursFromProjectAndDates(anyInt(), any(LocalDate.class), any(LocalDate.class), anyBoolean(), anyBoolean())).thenReturn(revenueDataDto);

        ProjectRevenueDataDto result = widgetDataServiceImpl.findRevenueForKPI(123, LocalDate.now(), LocalDate.now(), true, false);

        assertNotNull(result);
        assertEquals(revenueDataDto, result);
    }

    @Test
    public void testFindForecastForKPI_ValidInput() {

        ProjectForecastDataDto forecastDataDto = new ProjectForecastDataDto();
        when(benutzerIbosService.findProjectForecast(anyInt())).thenReturn(forecastDataDto);

        ProjectForecastDataDto result = widgetDataServiceImpl.findForecastForKPI(456);

        assertNotNull(result);
        assertEquals(forecastDataDto, result);
    }

    @Test
    public void testFindRevenueForKPI_InvalidProjectNumber() {

        ProjectRevenueDataDto result = widgetDataServiceImpl.findRevenueForKPI(-1, LocalDate.now(), LocalDate.now(), true, false);

        assertNull(result);
    }

    @Test
    public void testFindProjectsForUser_NoProjects() {

        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("firstName");
        benutzer.setLastName("lastName");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(benutzerIbosService.findActiveProjectForUser(anyString())).thenReturn(new ArrayList<>());

        List<BasicProjectDto> result = widgetDataServiceImpl.findProjectsForUser("token", true, Optional.of(true));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindRevenueForKPI_NullDates() {

        ProjectRevenueDataDto result = widgetDataServiceImpl.findRevenueForKPI(123, null, null, true, false);

        assertNull(result);
    }

    @Test
    public void testFindForecastForKPI_NullProjectNumber() {

        ProjectForecastDataDto result = widgetDataServiceImpl.findForecastForKPI(null);

        assertNull(result);
    }

    private List<BasicProjectDto> createProjects() {
        BasicProjectDto project1 = new BasicProjectDto(1, "test1");
        BasicProjectDto project2 = new BasicProjectDto(2, "test2");
        BasicProjectDto project3 = new BasicProjectDto(3, "test3");
        return Arrays.asList(project1, project2, project3);
    }

}
