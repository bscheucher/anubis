package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.PLZOrtService;
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
public class PlzControllerTest {

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    private PLZOrtService plzOrtService;

    @InjectMocks
    PlzController plzController;


    @Test
    void testUploadFile(){
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(plzOrtService.addPLZAndOrtAssociation(anyString(), anyString(), anyString())).thenReturn(createSuccessfulPayload());
        ResponseEntity<PayloadResponse> responseEntity = plzController.uploadFile("Bearer dummyAuthorizationHeader", "1220", "Wien");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUploadFileForbidden(){
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);
        ResponseEntity<PayloadResponse> responseEntity = plzController.uploadFile("Bearer dummyAuthorizationHeader", "1220", "Wien");

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    private PayloadResponse createSuccessfulPayload () {
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        return payloadResponse;
    }

}