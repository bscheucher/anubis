package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.SeminarResponseService;
import com.ibosng.gatewayservice.services.Teilnehmerservice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static com.ibosng.gatewayservice.utils.Constants.FN_TEILNEHMERINNEN_LESEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_TEILNEHMERINNEN_TR_LESEN_ALLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeilnehmerControllerTest {

    @Mock
    private SeminarResponseService seminarResponseService;

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    Teilnehmerservice teilnehmerservice;

    @InjectMocks
    private TeilnehmerController teilnehmerController;

    @Test
    public void testSearchTeilnehmerSuccess() {
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        when(teilnehmerservice.getFilteredTeilnehmer(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(payloadResponse);

        ResponseEntity<PayloadResponse> response = teilnehmerController.searchTeilnehmer("Max", "Mustermann", "test", 1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payloadResponse, response.getBody());
        verify(teilnehmerservice, times(1)).getFilteredTeilnehmer(anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetTeilnehmerByIdSuccess() {
        String authorizationHeader = "Bearer valid_token";
        Integer id = 1;
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TEILNEHMERINNEN_LESEN")))).thenReturn(true);
        when(teilnehmerservice.getTeilnehmerById(id, false, null)).thenReturn(payloadResponse);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getTeilnehmerById(authorizationHeader, false, null, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payloadResponse, response.getBody());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, times(1)).getTeilnehmerById(id, false, null);
    }

    @Test
    public void testGetTeilnehmerByIdForbidden() {
        String authorizationHeader = "Bearer invalid_token";
        Integer id = 1;

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TEILNEHMERINNEN_LESEN")))).thenReturn(false);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getTeilnehmerById(authorizationHeader, false, null, id);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, never()).getTeilnehmerById(anyInt(), anyBoolean(), anyString());
    }

    @Test
    void testGetZeiterfassungUebermittlungen_UserNotEligible_ShouldReturnForbidden() {
        String authorizationHeader = "Bearer mockToken";
        int page = 0;
        int size = 10;

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getZeiterfassungUebermittlungen(authorizationHeader, null, null, page, size);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetZeiterfassungUebermittlungen_UserEligible_NonNullCheckResult_ShouldReturnOk() {
        String authorizationHeader = "Bearer mockToken";
        int page = 0;
        int size = 10;

        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(teilnehmerservice.getZeiterfassungTransfers(null, null, page, size)).thenReturn(payloadResponse);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getZeiterfassungUebermittlungen(authorizationHeader, null, null, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetTeilnehmerFilterSummaryDto_UserNotEligible_ShouldReturnForbidden() {
        String authorizationHeader = "Bearer mockToken";
        int page = 0;
        int size = 10;

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getTeilnehmerFilterSummaryDto(authorizationHeader, null, null, null, false, null, true, null, null, null, null, null, page, size);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetTeilnehmerFilterSummaryDto_UserEligible_NonNullCheckResult_ShouldReturnOk() {
        Benutzer benutzer = new Benutzer();
        benutzer.setId(5);
        String authorizationHeader = "Bearer mockToken";
        int page = 0;
        int size = 10;

        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);
        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList(FN_TEILNEHMERINNEN_LESEN)))).thenReturn(true);
        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList(FN_TEILNEHMERINNEN_TR_LESEN_ALLE)))).thenReturn(false);
        when(teilnehmerservice.getTeilnehmerFilterSummaryDto(null, null, null, false, null, true, null, null, null, benutzer.getId(), null, null,  page, size)).thenReturn(payloadResponse);
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(teilnehmerservice.getTeilnehmerFilterSummaryDto(null, null, null, false, null, true, null, null, null,  benutzer.getId(), null, null, page, size)).thenReturn(payloadResponse);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getTeilnehmerFilterSummaryDto(authorizationHeader, null, null, null, false, null, true, null, null, null, null, null, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetTeilnehmersZeiterfassung_UserNotEligible_ShouldReturnForbidden() {
        String authorizationHeader = "Bearer mockToken";
        ZeiterfassungTransferDto zeiterfassungDto = new ZeiterfassungTransferDto();
        Boolean shouldSubmit = true;

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getTeilnehmersZeiterfassung(authorizationHeader, shouldSubmit, zeiterfassungDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetTeilnehmersZeiterfassung_UserEligible_NonNullCheckResult_ShouldReturnOk() {
        String authorizationHeader = "Bearer mockToken";
        ZeiterfassungTransferDto zeiterfassungDto = new ZeiterfassungTransferDto();
        Boolean shouldSubmit = true;

        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        Benutzer mockUser = new Benutzer();
        mockUser.setEmail("test@example.com");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(mockUser);

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(teilnehmerservice.getTeilnehmersZeiterfassung(eq(zeiterfassungDto), eq(shouldSubmit), anyString()))
                .thenReturn(payloadResponse);

        ResponseEntity<PayloadResponse> response = teilnehmerController.getTeilnehmersZeiterfassung(authorizationHeader, shouldSubmit, zeiterfassungDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testPostAbmeldung_UserNotEligible_ShouldReturnForbidden() {
        String authorizationHeader = "Bearer invalid_token";
        AbmeldungDto abmeldungDto = new AbmeldungDto();

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TN_ABMELDEN")))).thenReturn(false);

        ResponseEntity<?> response = teilnehmerController.postAbmeldung(authorizationHeader, abmeldungDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, never()).postUebaAbmeldung(anyString(), any(AbmeldungDto.class));
    }

    @Test
    void testPostAbmeldung_UserEligible_NonNullResult_ShouldReturnOk() {
        String authorizationHeader = "Bearer valid_token";
        AbmeldungDto abmeldungDto = new AbmeldungDto();
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TN_ABMELDEN")))).thenReturn(true);
        when(teilnehmerservice.postUebaAbmeldung(anyString(), any(AbmeldungDto.class))).thenReturn(payloadResponse);

        ResponseEntity<?> response = teilnehmerController.postAbmeldung(authorizationHeader, abmeldungDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payloadResponse, response.getBody());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, times(1)).postUebaAbmeldung(anyString(), any(AbmeldungDto.class));
    }

    @Test
    void testGetUebaAbmeldung_UserNotEligible_ShouldReturnForbidden() {
        String authorizationHeader = "Bearer invalid_token";
        int page = 1;
        int size = 10;

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TN_ABMELDEN")))).thenReturn(false);

        ResponseEntity<?> response = teilnehmerController.getUebaAbmeldung(authorizationHeader, page, size);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, never()).getUebaAbmeldung(anyInt(), anyInt());
    }

    @Test
    void testGetUebaAbmeldung_UserEligible_NullResult_ShouldReturnNoContent() {
        String authorizationHeader = "Bearer valid_token";
        int page = 1;
        int size = 10;

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TN_ABMELDEN")))).thenReturn(true);
        when(teilnehmerservice.getUebaAbmeldung(page, size)).thenReturn(null);

        ResponseEntity<?> response = teilnehmerController.getUebaAbmeldung(authorizationHeader, page, size);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, times(1)).getUebaAbmeldung(page, size);
    }

    @Test
    void testEditUebaAbmeldung_UserNotEligible_ShouldReturnForbidden() {
        String authorizationHeader = "Bearer invalid_token";
        Integer id = 1;

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TN_ABMELDEN")))).thenReturn(false);

        ResponseEntity<?> response = teilnehmerController.editUebaAbmeldung(authorizationHeader, id);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, never()).getUebaAbmeldungById(anyInt());
    }

    @Test
    void testEditUebaAbmeldung_UserEligible_InvalidId_ShouldReturnNoContent() {
        String authorizationHeader = "Bearer valid_token";
        Integer id = 999;

        when(benutzerDetailsService.isUserEligible(anyString(), eq(Collections.singletonList("FN_TN_ABMELDEN")))).thenReturn(true);
        when(teilnehmerservice.getUebaAbmeldungById(id)).thenReturn(null);

        ResponseEntity<?> response = teilnehmerController.editUebaAbmeldung(authorizationHeader, id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, times(1)).getUebaAbmeldungById(id);
    }

    @Test
    public void testValidateTeilnehmerWithSuccess() {
        TeilnehmerSeminarDto teilnehmerDto = new TeilnehmerSeminarDto();
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(teilnehmerservice.validateTeilnehmer(any(TeilnehmerSeminarDto.class), anyString())).thenReturn(payloadResponse);

        ResponseEntity<PayloadResponse> response = teilnehmerController.validateTeilnehmer(
                "Bearer authorizationHeader", false, teilnehmerDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payloadResponse, response.getBody());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, times(1)).validateTeilnehmer(any(TeilnehmerSeminarDto.class), anyString());
    }

    @Test
    public void testValidateTeilnehmerWithForbidden() {
        TeilnehmerSeminarDto teilnehmerDto = new TeilnehmerSeminarDto();

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);

        ResponseEntity<PayloadResponse> response = teilnehmerController.validateTeilnehmer(
                "Bearer authorizationHeader", false, teilnehmerDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(teilnehmerservice, never()).validateTeilnehmer(any(TeilnehmerSeminarDto.class), anyString());
    }

}