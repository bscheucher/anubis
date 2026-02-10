package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Dashboards;
import com.ibosng.dbservice.entities.Widget;
import com.ibosng.dbservice.services.impl.BenutzerServiceImpl;
import com.ibosng.dbservice.services.impl.WidgetServiceImpl;
import com.ibosng.gatewayservice.dtos.dashboards.AllDashboardsDto;
import com.ibosng.gatewayservice.dtos.dashboards.DashboardDto;
import com.ibosng.gatewayservice.dtos.dashboards.WidgetDto;
import com.ibosng.gatewayservice.services.impl.DashboardDetailsServiceImpl;
import com.ibosng.gatewayservice.services.impl.TokenValidatorServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


public class DashboardDetailsServiceImplTest extends BaseIntegrationTest {

    @Mock
    private com.ibosng.dbservice.services.impl.DashboardsServiceImpl dashboardsServiceIbosImpl;

    @Mock
    private BenutzerServiceImpl benutzerService;

    @Mock
    private WidgetServiceImpl widgetService;

    @Mock
    private TokenValidatorServiceImpl tokenValidatorServiceImpl;

    @Mock
    private Optional<Widget> widget;

    @InjectMocks
    private DashboardDetailsServiceImpl dashboardsServiceImpl;


    @Test
    void testDeleteDashboard() {

        Integer dashboardId = 1;

        String resultMessage = dashboardsServiceImpl.deleteDashboard(dashboardId);

        assertEquals(String.format("Dashboard with id %s successfully deleted!", dashboardId), resultMessage);
        verify(dashboardsServiceIbosImpl, times(1)).deleteById(dashboardId);
    }

    @Test
    public void testSetFavouriteDashboard_NotExists() {

        String fakeToken = "fakeToken";
        Integer dashboardId = 1;

        when(dashboardsServiceIbosImpl.findById(anyInt())).thenReturn(Optional.empty());

        String result = dashboardsServiceImpl.setFavouriteDashboard(fakeToken, dashboardId);

        assertEquals("Dashboard with id 1 does not exist", result);

    }

    @Test
    public void testSetFavouriteDashboard_success() {
        String token = "Bearer token";
        Integer dashboardId = 1;
        Dashboards dashboards = new Dashboards();
        dashboards.setId(1);
        Claims mockClaims = Mockito.mock(Claims.class);

        when(mockClaims.get("oid")).thenReturn("userAzureId");
        when(tokenValidatorServiceImpl.getClaimsFromJwt(token)).thenReturn(mockClaims);
        when(dashboardsServiceIbosImpl.findById(anyInt())).thenReturn(Optional.of(dashboards));
        when(dashboardsServiceIbosImpl.findDashboardByIdAndUserAzureId(anyInt(), anyString())).thenReturn(new Dashboards());

        String result = dashboardsServiceImpl.setFavouriteDashboard(token, dashboardId);

        assertEquals("Favourite dashboard successfully updated!", result);
    }

    @Test
    public void testSetFavouriteDashboard_NotBelongToUser() {

        String fakeToken = "fakeToken";
        Integer dashboardId = 1;

        Dashboards dashboards = new Dashboards();
        dashboards.setId(1);


        Claims mockClaims = Mockito.mock(Claims.class);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(fakeToken)).thenReturn(mockClaims);
        when(dashboardsServiceIbosImpl.findById(anyInt())).thenReturn(Optional.of(dashboards));
        when(dashboardsServiceIbosImpl.findDashboardByIdAndUserAzureId(anyInt(), anyString())).thenReturn(null);
        String result = dashboardsServiceImpl.setFavouriteDashboard(fakeToken, dashboardId);

        assertEquals("Dashboard with id 1 does not belong to user with azure id null", result);

    }

    @Test
    public void testGetAllDashboards() {

        String fakeToken = "fakeToken";

        Dashboards dashboards1 = new Dashboards();
        Dashboards dashboards2 = new Dashboards();
        Dashboards dashboards3 = new Dashboards();

        List<Dashboards> dashboards = List.of(dashboards1, dashboards2, dashboards3);

        Claims mockClaims = Mockito.mock(Claims.class);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(fakeToken)).thenReturn(mockClaims);

        when(dashboardsServiceIbosImpl.getAllDashboardsByAzureId(anyString())).thenReturn(dashboards);

        AllDashboardsDto result = dashboardsServiceImpl.getAllDashboards(fakeToken);

        assertEquals(result.getDashboards().size(), 0);
    }

    @Test
    void testSaveDashboard_NewDashboard() {

        String fakeToken = "fakeToken";
        DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setDashboardName("New Dashboard");
        dashboardDto.setDashboardId(1);
        dashboardDto.setWidgets(List.of(new WidgetDto(1, 0, 0), new WidgetDto(2, 1, 1)));

        Benutzer mockBenutzer = new Benutzer();
        mockBenutzer.setEmail("email@outlook.com");

        when(tokenValidatorServiceImpl.getClaimsFromJwt(fakeToken)).thenReturn(Mockito.mock(Claims.class));
        when(benutzerService.getBenutzerByAzureId(anyString())).thenReturn(mockBenutzer);

        Claims mockClaims = Mockito.mock(Claims.class);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(fakeToken)).thenReturn(mockClaims);
        when(mockClaims.get("oid")).thenReturn("userAzureId");
        when(benutzerService.getBenutzerByAzureId(anyString())).thenReturn(mockBenutzer);

        when(widgetService.findById((anyInt()))).thenReturn(widget);

        when(dashboardsServiceIbosImpl.saveUpdateDashboardWithWidgets(any())).thenReturn(new Dashboards());
        DashboardDto savedDashboard = dashboardsServiceImpl.saveDashboard(fakeToken, dashboardDto);

        verify(dashboardsServiceIbosImpl, times(1)).saveUpdateDashboardWithWidgets(any());
        assertNull(savedDashboard.getDashboardId());
    }

}

