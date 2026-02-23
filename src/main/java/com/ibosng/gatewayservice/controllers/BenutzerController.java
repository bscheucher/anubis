package com.ibosng.gatewayservice.controllers;


import com.ibosng.dbservice.services.impl.UserSessionServiceImpl;
import com.ibosng.gatewayservice.dtos.user.UserDetailsDto;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/benutzer")
@Tag(name = "User controller")
public class BenutzerController {

    private final BenutzerDetailsService benutzerDetailsService;

    private final UserSessionServiceImpl userSessionService;

    public BenutzerController(BenutzerDetailsService benutzerDetailsService, UserSessionServiceImpl userSessionService) {
        this.benutzerDetailsService = benutzerDetailsService;
        this.userSessionService = userSessionService;
    }

    @Operation(
            summary = "This method is used to get the user details.",
            description = "Returns the user details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/get")
    public ResponseEntity<UserDetailsDto> getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return benutzerDetailsService.getUserDetailsReponse(token);
    }


    @Operation(
            summary = "This method is used logout the user",
            description = "Kills the active session",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "417", description = "Unable to kill active session"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if(userSessionService.killActiveSession(token)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    @GetMapping("/logoutAllDevices")
    @Operation(
            summary = "This method is used to logout all devices.",
            description = "Logout from all devices.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout from all devices successful."),
                    @ApiResponse(responseCode = "401", description = "The provided token has expired. Please obtain a new token and try again."),
                    @ApiResponse(responseCode = "403", description = "Forbidden: The user is not authorized to access this endpoint.")
            })
    public ResponseEntity<Void> logoutAllUserSessions(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if(userSessionService.killAllUserSessions(token)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }
}
