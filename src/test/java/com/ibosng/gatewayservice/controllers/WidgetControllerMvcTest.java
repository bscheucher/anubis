package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.services.impl.TokenValidatorServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WidgetController.class)
@AutoConfigureMockMvc
@Disabled
public class WidgetControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TokenValidatorServiceImpl tokenValidatorServiceImpl;

    @MockBean
    WidgetController widgetController;


    @Test
    void testGetMeineSeminareUnauthorized() throws Exception {

        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        RequestBuilder requestBuilder = get("/getMeineSeminare")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetFehlerhafteTeilnehmerUnauthorized() throws Exception{

        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        RequestBuilder requestBuilder = get("/getFehlerhafteTeilnehmer")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetMeinePersoenlichenDatenWidgetDataUnauthorized() throws Exception{

        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        RequestBuilder requestBuilder = get("/getMeinePersoenlichenDaten")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetProjectsForUserUnauthorized() throws Exception{

        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        RequestBuilder requestBuilder = get("/controlling/getProjectsForUser")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetProjectsForUserWithParametersUnauthorized() throws Exception {
        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        boolean isActive = true;
        Optional<Boolean> isInTheFuture = Optional.of(true);

        RequestBuilder requestBuilder = get("/controlling/getProjectsForUser")
                .param("isActive", String.valueOf(isActive))
                .param("isInTheFuture", String.valueOf(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetRevenueForProjectUnauthorized() throws Exception{

        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        RequestBuilder requestBuilder = get("/controlling/getRevenueForProject")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetRevenueForProjectWithParamsUnauthorized() throws Exception {
        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        // Provide sample values for the parameters
        int projectNumber = 123;
        LocalDate von = LocalDate.now();
        LocalDate bis = LocalDate.now().plusDays(7);
        boolean isProjectStartToToday = true;
        boolean isTodayToProjectEnd = true;

        RequestBuilder requestBuilder = get("/controlling/getRevenueForProject")
                .param("projectNumber", String.valueOf(projectNumber))
                .param("von", von.toString())
                .param("bis", bis.toString())
                .param("isProjectStartToToday", String.valueOf(isProjectStartToToday))
                .param("isTodayToProjectEnd", String.valueOf(isTodayToProjectEnd))
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetForecastForProjectUnauthorized() throws Exception{

        when(tokenValidatorServiceImpl.isTokenValid(anyString())).thenReturn(false);

        RequestBuilder requestBuilder = get("/controlling/getForecastForProject")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

}
