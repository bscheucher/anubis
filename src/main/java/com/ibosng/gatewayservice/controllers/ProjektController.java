package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.ProjektResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.ibosng.gatewayservice.utils.Constants.FN_TEILNEHMERINNEN_LESEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_TEILNEHMERINNEN_TR_LESEN_ALLE;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/projekt")
@Tag(name = "Projekt controller")
@RequiredArgsConstructor
public class ProjektController {

    private final ProjektResponseService projektResponseService;
    private final BenutzerDetailsService benutzerDetailsService;

    @GetMapping("/getProjektsByStatus")
    @Operation(
            summary = "This method is used to retrieve Projekt based on isActive parameter",
            description = "Get Projekts based on isActive parameter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> getProjektsByStatus(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isKorrigieren) {
        log.info("Get projekt by status: {}", isActive);
        if (benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_TR_LESEN_ALLE))) {
            return checkResultIfNull(projektResponseService.getProjektByStatus(isActive, isKorrigieren, null));
        }
        if (benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            Benutzer benutzer = benutzerDetailsService.getUserFromToken(getTokenFromAuthorizationHeader(authorizationHeader));
            return checkResultIfNull(projektResponseService.getProjektByStatus(isActive, isKorrigieren, benutzer));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
