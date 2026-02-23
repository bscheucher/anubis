package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.ChangeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.FN_MA_LESEN;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/change-log")
@Tag(name = "Change Log controller")
@RequiredArgsConstructor
public class ChangeLogController {

    private final ChangeLogService changeLogService;
    private final BenutzerDetailsService benutzerDetailsService;

    @GetMapping("/mitarbeiter")
    @Operation(
            summary = "This method is used to retrieve Projekt based on isActive parameter",
            description = "Get Projekts based on isActive parameter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> getMAChangeLog(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                          @RequestParam String personalnummer) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_MA_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Get MA changeLog for personalnummer: {}", personalnummer);
        return checkResultIfNull(changeLogService.getMAChangeLog(personalnummer));
    }

    @GetMapping("/vertragsaenderungen")
    @Operation(
            summary = "This method is used to retrieve Projekt based on isActive parameter",
            description = "Get Projekts based on isActive parameter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> getVertragsaenderungenChangeLog(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                           @RequestParam String personalnummer) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_MA_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Get MA Vertragsaenderungen changeLog for personalnummer: {}", personalnummer);
        return checkResultIfNull(changeLogService.getVertragsaenderungenChangeLog(personalnummer));
    }
}
