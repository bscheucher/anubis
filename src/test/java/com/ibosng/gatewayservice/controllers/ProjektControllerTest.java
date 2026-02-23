package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.ProjektResponseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjektControllerTest {

    public static final String TOKEN = "Bearer any";

    @Mock
    private ProjektResponseService projektResponseService;

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @InjectMocks
    private ProjektController projektController;

    @Test
    public void testGetProjektsByStatus_Success() {
        Boolean isActive = true;
        PayloadResponse successfulResponse = PayloadResponse.builder().success(true).build();

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(projektResponseService.getProjektByStatus(isActive, null, null)).thenReturn(successfulResponse);

        ResponseEntity<PayloadResponse> response = projektController.getProjektsByStatus(TOKEN, isActive, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());
        verify(projektResponseService, times(1)).getProjektByStatus(isActive, null, null);
    }

    @Test
    public void testGetProjektsByStatus_NoContent() {
        Boolean isActive = false;

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(projektResponseService.getProjektByStatus(isActive, null, null)).thenReturn(null);

        ResponseEntity<PayloadResponse> response = projektController.getProjektsByStatus(TOKEN, isActive, null);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(projektResponseService, times(1)).getProjektByStatus(isActive, null, null);
    }

    @Test
    public void testGetProjektsByStatus_BadRequest() {
        Boolean isActive = null;

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(projektResponseService.getProjektByStatus(isActive, null, null)).thenThrow(new IllegalArgumentException("Invalid parameter"));

        try {
            projektController.getProjektsByStatus(TOKEN, isActive, null);
        } catch (Exception e) {
            assertEquals("Invalid parameter", e.getMessage());
            verify(projektResponseService, times(1)).getProjektByStatus(isActive, null, null);
        }
    }

}
