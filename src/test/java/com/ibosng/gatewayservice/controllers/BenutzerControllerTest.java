package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.services.impl.UserSessionServiceImpl;
import com.ibosng.gatewayservice.dtos.user.UserDetailsDto;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BenutzerControllerTest {

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    private UserSessionServiceImpl userSessionService;

    @InjectMocks
    private BenutzerController benutzerController;



    @Test
    public void testGetUserDetailsSuccess() {
        UserDetailsDto userDetailsDto = new UserDetailsDto("azureId", "Name", "LastName", List.of("test"), "");
        ResponseEntity<UserDetailsDto> expectedResponse = new ResponseEntity<>(userDetailsDto, HttpStatus.OK);
        when(benutzerDetailsService.getUserDetailsReponse(anyString())).thenReturn(expectedResponse);

        ResponseEntity<UserDetailsDto> response = benutzerController.getUserDetails("Bearer authorizationHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDetailsDto, response.getBody());
        verify(benutzerDetailsService, times(1)).getUserDetailsReponse(anyString());
    }

    @Test
    public void testLogoutUserSuccess() {
        when(userSessionService.killActiveSession(anyString())).thenReturn(true);

        ResponseEntity<Void> response = benutzerController.logoutUser("Bearer authorizationHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userSessionService, times(1)).killActiveSession(anyString());
    }

    @Test
    public void testLogoutUserFailed() {
        when(userSessionService.killActiveSession(anyString())).thenReturn(false);

        ResponseEntity<Void> response = benutzerController.logoutUser("Bearer authorizationHeader");

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        verify(userSessionService, times(1)).killActiveSession(anyString());
    }

    @Test
    public void testLogoutAllUserSessionsSuccess() {
        when(userSessionService.killAllUserSessions(anyString())).thenReturn(true);

        ResponseEntity<Void> response = benutzerController.logoutAllUserSessions("Bearer authorizationHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userSessionService, times(1)).killAllUserSessions(anyString());
    }

    @Test
    public void testLogoutAllUserSessionsFailed() {
        when(userSessionService.killAllUserSessions(anyString())).thenReturn(false);

        ResponseEntity<Void> response = benutzerController.logoutAllUserSessions("Bearer authorizationHeader");

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        verify(userSessionService, times(1)).killAllUserSessions(anyString());
    }
}

