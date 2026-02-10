package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.teilnahme.TeilnahmeCreationDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.SeminarResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.*;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/seminar")
@Tag(name = "Seminar controller")
@RequiredArgsConstructor
public class SeminarController {

    private final SeminarResponseService seminarResponseService;

    private final BenutzerDetailsService benutzerDetailsService;

    @GetMapping("massnahmenummer")
    @Operation(
            summary = "This method is used to retrieve massnahmenummer",
            description = "Get massnahmenummer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> getAllMassnahmenummer() {
        return checkResultIfNull(seminarResponseService.getAllMassnahmenummer());
    }

    @GetMapping("/getSeminarsByStatus")
    @Operation(
            summary = "This method is used to retrieve Seminars based on isActive parameter",
            description = "Get Seminars based on isActive parameter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> getSeminarsByStatus(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isKorrigieren,
            @RequestParam(required = false, defaultValue = "") String projectName) {
        log.info("Get seminars by status: {}, project name: '{}'", isActive, projectName);
        if (benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_TR_LESEN_ALLE))) {
            return checkResultIfNull(seminarResponseService.getSeminarByStatusAndProjectName(isActive, isKorrigieren, projectName, null));
        }
        if (benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            Benutzer benutzer = benutzerDetailsService.getUserFromToken(getTokenFromAuthorizationHeader(authorizationHeader));
            return checkResultIfNull(seminarResponseService.getSeminarByStatusAndProjectName(isActive, isKorrigieren, projectName, benutzer));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("allSeminars")
    @Operation(
            summary = "This method is used to retrieve all Seminars ",
            description = "Get All Seminars ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> getSeminarsByStatus(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Boolean isUeba,
            @RequestParam int page,
            @RequestParam int size) {
        return checkResultIfNull(seminarResponseService.getAllSeminars(isUeba, page, size));
    }

    @GetMapping("/{id}/teilnahme")
    public ResponseEntity<PayloadResponse> getTeilnahme(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable int id,
            @RequestParam String date) {
        return checkResultIfNull(seminarResponseService.getTeilnahme(id, date));
    }

    @PostMapping("/teilnahme")
    public ResponseEntity<PayloadResponse> getTeilnahme(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody TeilnahmeCreationDto teilnahmeCreationDto) {


        Benutzer benutzer = benutzerDetailsService.getUserFromToken(getTokenFromAuthorizationHeader(authorizationHeader));
        return checkResultIfNull(seminarResponseService.postTeilnahme(benutzer, teilnahmeCreationDto));
    }

    @GetMapping("/getSeminarAnAbwesenheitDto")
    @Operation(
            summary = "This method is used to retrieve all Seminar An und Abwesenheiten",
            description = "Get all SeminarAnAbwesenheit depending on the user rights. If the user is an admin, " +
                    "all seminars will be returned; if the user is a trainer, " +
                    "only seminars hosted by the trainer will be returned.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            })
    public ResponseEntity<PayloadResponse> getSeminarsByStatus(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String project,
            @RequestParam(required = false) String seminar,
            @RequestParam(required = false) String kursEndeFrom, //kursEndeSpaeterAls
            @RequestParam(required = false) String kursEndeTo, //kursEndeFrueherAls
            @RequestParam(required = false) Boolean verzoegerung,
            @RequestParam(required = false, defaultValue = "seminar") String sortProperty,
            @RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
            @RequestParam int page,
            @RequestParam int size
    ) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);

        boolean isAdmin = benutzerDetailsService.isUserEligible(token,
                List.of(FN_TN_ADMIN_ANWESENHEITEN_LESEN, FN_TN_ADMIN_ANWESENHEITEN_VERWALTEN, FN_TN_ADMIN_ABWESENHEITEN_LESEN, FN_TN_ADMIN_ABWESENHEITEN_VERWALTEN));

        boolean isTrainer = benutzerDetailsService.isUserEligible(token,
                List.of(FN_TN_TR_ANWESENHEITEN_LESEN, FN_TN_TR_ANWESENHEITEN_VERWALTEN, FN_TN_TR_ABWESENHEITEN_LESEN, FN_TN_TR_ABWESENHEITEN_VERWALTEN));

        if (!isTrainer && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return checkResultIfNull(seminarResponseService.getSeminarAnAbwesenheitDto(
                token, isAdmin, isActive, project, seminar, kursEndeFrom,
                kursEndeTo, verzoegerung, sortProperty, sortDirection, page, size));

    }
}
