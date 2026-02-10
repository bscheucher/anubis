package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.PLZOrtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static com.ibosng.gatewayservice.utils.Constants.FN_STAMMDATEN_ERFASSEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSDATEN_ERFASSEN;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/plz")
@Tag(name = "PLZ & Ort controller")
@RequiredArgsConstructor
public class PlzController {

    private final BenutzerDetailsService benutzerDetailsService;

    private final PLZOrtService plzOrtService;

    @PostMapping(value = "/savePlzAndOrtRelation")
    @Operation(
            summary = "Upload a file.",
            description = "This endpoint allows users to save a relation between an austrian plz and ort.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PayloadResponse> uploadFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam String plz,
            @RequestParam String ort) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Arrays.asList(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(plzOrtService.addPLZAndOrtAssociation(plz, ort, token));
    }
}
