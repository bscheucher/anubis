package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.dashboards.AllDashboardsDto;
import com.ibosng.gatewayservice.dtos.dashboards.DashboardDto;
import com.ibosng.gatewayservice.services.impl.DashboardDetailsServiceImpl;
import com.ibosng.gatewayservice.services.impl.TokenValidatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardDetailsServiceImpl dashboardsServiceImpl;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void testGetAllDashboards() {
        when(dashboardsServiceImpl.getAllDashboards(anyString())).thenReturn(new AllDashboardsDto());

        ResponseEntity<AllDashboardsDto> responseEntity = dashboardController.getAllDashboards("Bearer validToken");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new AllDashboardsDto(), responseEntity.getBody());
    }

    @Test
    void testSaveDashboard() {
        when(dashboardsServiceImpl.saveDashboard(anyString(), any())).thenReturn(new DashboardDto());

        ResponseEntity<DashboardDto> responseEntity = dashboardController.saveDashboard("Bearer validToken", new DashboardDto());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(new DashboardDto(), responseEntity.getBody());
    }

    @Test
    void testDeleteDashboard() {
        when(dashboardsServiceImpl.deleteDashboard(anyInt())).thenReturn("Deleted");

        ResponseEntity<String> responseEntity = dashboardController.deleteDashboard( 1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted", responseEntity.getBody());
    }

    @Test
    void testSetFavouriteDashboard() {
        when(dashboardsServiceImpl.setFavouriteDashboard(anyString(), anyInt())).thenReturn("Favourite Set");

        ResponseEntity<String> responseEntity = dashboardController.setFavouriteDashboard("Bearer validToken", 1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Favourite Set", responseEntity.getBody());
    }

}
