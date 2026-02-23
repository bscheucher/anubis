package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.ZeiterfassungGatewayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GatewayLhrZeiterfassungControllerTest {

    //TODO FIX TESTS

    @Mock
    private ZeiterfassungGatewayService zeiterfassungGatewayService;

    @Mock
    private BenutzerDetailsService benutzerDetailsService;
    ;

    @InjectMocks
    private GatewayZeiterfassungController gatewayZeiterfassungController;

    private PayloadResponse createPayloadResponse(boolean success) {
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(success);
        return payloadResponse;
    }

    @Test
    public void testDeleteAbwesenheit_Success() {
        Integer abwesenheitId = 1;
        String authorizationHeader = "Bearer mockToken";
        String token = "mockToken";
        PayloadResponse successfulResponse = createPayloadResponse(true);

        when(zeiterfassungGatewayService.deleteAbwesenheit(anyInt())).thenReturn(successfulResponse);
        when(benutzerDetailsService.isUserEligible(token, List.of(FN_ABWESENHEITEN_EDITIEREN))).thenReturn(true);

        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.deleteAbwesenheit(authorizationHeader, abwesenheitId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());
        verify(zeiterfassungGatewayService, times(1)).deleteAbwesenheit(abwesenheitId);
    }

    @Test
    public void testSaveAbwesenheit_Success() {
        AbwesenheitDto abwesenheitDto = new AbwesenheitDto();
        String authorizationHeader = "Bearer mockToken";

        String token = "mockToken";
        when(benutzerDetailsService.isUserEligible(token, List.of(FN_ABWESENHEITEN_EDITIEREN))).thenReturn(true);

        PayloadResponse successfulResponse = createPayloadResponse(true);
        when(zeiterfassungGatewayService.saveAbwesenheit(any(AbwesenheitDto.class), anyString())).thenReturn(successfulResponse);

        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.saveAbwesenheit(authorizationHeader, abwesenheitDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());
        verify(zeiterfassungGatewayService, times(1)).saveAbwesenheit(any(AbwesenheitDto.class), anyString());
    }

    @Test
    public void testGetAbwesenheit_Success() {
        Integer abwesenheitId = 1;
        String authorizationHeader = "Bearer mockToken";
        PayloadResponse successfulResponse = createPayloadResponse(true);

        String token = "mockToken";
        when(benutzerDetailsService.isUserEligible(token, List.of(FN_EIGENE_ABWESENHEITEN_LESEN))).thenReturn(true);
        when(zeiterfassungGatewayService.getAbwesenheit(anyInt())).thenReturn(successfulResponse);

        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.getAbwesenheit(authorizationHeader, abwesenheitId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());
        verify(zeiterfassungGatewayService, times(1)).getAbwesenheit(abwesenheitId);
    }

    @Test
    public void testGetAbwesenheiten_Success() {
        String authorizationHeader = "Bearer mockToken";
        Boolean isPersonal = true;
        String sortProperty = "startDate";
        String sortDirection = "ASC";
        int page = 0;
        int size = 100;
        PayloadResponse successfulResponse = createPayloadResponse(true);

        String token = "mockToken";
        when(benutzerDetailsService.isUserEligible(token, List.of(FN_EIGENE_ABWESENHEITEN_LESEN))).thenReturn(true);

        when(zeiterfassungGatewayService.getAbwesenheitenList(eq(token), eq(isPersonal), anyString(), eq(0), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(successfulResponse);

        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.getAbwesenheiten(authorizationHeader, isPersonal, "", 0, sortProperty, sortDirection, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());

        verify(zeiterfassungGatewayService, times(1)).getAbwesenheitenList(eq(token), eq(isPersonal), anyString(), eq(0), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testDeleteAbwesenheit_NoContent() {
        Integer abwesenheitId = 2;
        String authorizationHeader = "Bearer mockToken";

        when(zeiterfassungGatewayService.deleteAbwesenheit(anyInt())).thenReturn(null);
        String token = "mockToken";
        when(benutzerDetailsService.isUserEligible(token, List.of(FN_ABWESENHEITEN_EDITIEREN))).thenReturn(true);
        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.deleteAbwesenheit(authorizationHeader, abwesenheitId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(zeiterfassungGatewayService, times(1)).deleteAbwesenheit(abwesenheitId);
    }

    @Test
    public void testSaveAbwesenheit_BadRequest() {
        AbwesenheitDto abwesenheitDto = new AbwesenheitDto();
        String authorizationHeader = "Bearer invalidToken";

        when(zeiterfassungGatewayService.saveAbwesenheit(any(AbwesenheitDto.class), anyString())).thenThrow(new IllegalArgumentException("Invalid token"));
        String token = "mockToken";
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);

        try {
            gatewayZeiterfassungController.saveAbwesenheit(authorizationHeader, abwesenheitDto);
        } catch (Exception e) {
            assertEquals("Invalid token", e.getMessage());
            verify(zeiterfassungGatewayService, times(1)).saveAbwesenheit(any(AbwesenheitDto.class), anyString());
        }
    }

    @Test
    public void testGetAbwesenheiten_NoContent() {
        String authorizationHeader = "Bearer mockToken";
        String sortProperty = "startDate";
        String sortDirection = "ASC";
        int page = 0;
        int size = 100;

        String token = "mockToken";
        when(benutzerDetailsService.isUserEligible(token, List.of(FN_EIGENE_ABWESENHEITEN_LESEN))).thenReturn(true);
        when(zeiterfassungGatewayService.getAbwesenheitenList(eq(token), eq(true), anyString(), eq(0), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(null);

        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.getAbwesenheiten(authorizationHeader, true, "", 0, sortProperty, sortDirection, page, size);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(zeiterfassungGatewayService, times(1)).getAbwesenheitenList(eq(token), eq(true), anyString(), eq(0), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetZeitbuchungenList_Success() {
        String authorizationHeader = "Bearer mockToken";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";
        boolean shouldSync = true;
        String token = "mockToken";
        when(benutzerDetailsService.isUserEligible(token, List.of(FN_MA_ZEITEN_LESEN))).thenReturn(true);
        PayloadResponse successfulResponse = createPayloadResponse(true);

        when(zeiterfassungGatewayService.getMitarbeiterZeitbuchungen(token, startDate, endDate, shouldSync)).thenReturn(successfulResponse);

        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.getZeitbuchungenList(authorizationHeader, startDate, endDate, shouldSync);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successfulResponse, response.getBody());
        verify(zeiterfassungGatewayService, times(1)).getMitarbeiterZeitbuchungen(token, startDate, endDate, shouldSync);
    }

    @Test
    public void testGetZeitbuchungenList_Forbidden() {
        String authorizationHeader = "Bearer mockToken";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";
        Boolean shouldSync = true;
        String token = "mockToken";

        when(benutzerDetailsService.isUserEligible(token, List.of(FN_MA_ZEITEN_LESEN))).thenReturn(false);

        ResponseEntity<PayloadResponse> response = gatewayZeiterfassungController.getZeitbuchungenList(authorizationHeader, startDate, endDate, shouldSync);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(zeiterfassungGatewayService, never()).getMitarbeiterZeitbuchungen(anyString(), anyString(), anyString(), anyBoolean());
    }

}
