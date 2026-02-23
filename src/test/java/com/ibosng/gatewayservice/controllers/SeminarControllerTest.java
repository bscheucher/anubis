package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.SeminarResponseService;
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
public class SeminarControllerTest {

    public static final String TOKEN = "Bearer any";

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    private SeminarResponseService seminarResponseService;

    @InjectMocks
    private SeminarController seminarController;

    @Test
    public void testGetSeminarsByStatus_Success() {
        Boolean isActive = true;
        String projectName = "Project Alpha";
        PayloadResponse successfulResponse = PayloadResponse.builder().success(true).build();
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(seminarResponseService.getSeminarByStatusAndProjectName(isActive, null, projectName, null)).thenReturn(successfulResponse);

        ResponseEntity<PayloadResponse> response = seminarController.getSeminarsByStatus(TOKEN, isActive, null, projectName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());
        verify(seminarResponseService, times(1)).getSeminarByStatusAndProjectName(isActive, null, projectName, null);
    }

    @Test
    public void testGetSeminarsByStatus_NoContent() {
        Boolean isActive = false;
        String projectName = "NonExistentProject";

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(seminarResponseService.getSeminarByStatusAndProjectName(isActive, null, projectName, null)).thenReturn(null);

        ResponseEntity<PayloadResponse> response = seminarController.getSeminarsByStatus(TOKEN, isActive, null, projectName);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(seminarResponseService, times(1)).getSeminarByStatusAndProjectName(isActive,null, projectName, null);
    }

    @Test
    public void testGetSeminarsByStatus_BadRequest() {
        Boolean isActive = null;
        String projectName = "";

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(seminarResponseService.getSeminarByStatusAndProjectName(isActive, null, projectName, null)).thenThrow(new IllegalArgumentException("Invalid parameter"));

        try {
            seminarController.getSeminarsByStatus(TOKEN, isActive, null, projectName);
        } catch (Exception e) {
            assertEquals("Invalid parameter", e.getMessage());
            verify(seminarResponseService, times(1)).getSeminarByStatusAndProjectName(isActive, null, projectName, null);
        }
    }

    @Test
    public void testGetAllSeminars_Success() {
        String authorizationHeader = "Bearer mockToken";
        Boolean isUeba = true;
        int page = 1;
        int size = 10;
        PayloadResponse successfulResponse = PayloadResponse.builder().success(true).build();
        when(seminarResponseService.getAllSeminars(isUeba, page, size)).thenReturn(successfulResponse);

        ResponseEntity<PayloadResponse> response = seminarController.getSeminarsByStatus(authorizationHeader, isUeba, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());
        verify(seminarResponseService, times(1)).getAllSeminars(isUeba, page, size);
    }

    @Test
    public void testGetAllSeminars_NoContent() {
        String authorizationHeader = "Bearer mockToken";
        Boolean isUeba = false;
        int page = 0;
        int size = 5;
        when(seminarResponseService.getAllSeminars(isUeba, page, size)).thenReturn(null);

        ResponseEntity<PayloadResponse> response = seminarController.getSeminarsByStatus(authorizationHeader, isUeba, page, size);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(seminarResponseService, times(1)).getAllSeminars(isUeba, page, size);
    }

    @Test
    public void testGetAllSeminars_BadRequest() {
        String authorizationHeader = "Bearer mockToken";
        Boolean isUeba = null;
        int page = -1;
        int size = 0;
        when(seminarResponseService.getAllSeminars(isUeba, page, size)).thenThrow(new IllegalArgumentException("Invalid pagination parameters"));

        try {
            seminarController.getSeminarsByStatus(authorizationHeader, isUeba, page, size);
        } catch (Exception e) {
            assertEquals("Invalid pagination parameters", e.getMessage());
            verify(seminarResponseService, times(1)).getAllSeminars(isUeba, page, size);
        }
    }
}
