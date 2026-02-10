package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.PermissionService;
import com.ibosng.gatewayservice.services.VereinbarungenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.*;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/vereinbarung")
@RequiredArgsConstructor
@Tag(name = "Vereinbarung controller")
public class VereinbarungController {

    private final VereinbarungenService vereinbarungenService;

    private final BenutzerDetailsService benutzerDetailsService;

    private final PermissionService permissionService;


    @GetMapping("/getWorkflowgroup")
    @Operation(
            summary = "This method is used to get all the info of the workflowgroup.",
            description = "Returns a payload containing all the info of the workflowgroup.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getWorkflowgroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                            @RequestParam Integer vereinbarungId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_ALLE_VEREINBARUNGEN_EINSEHEN)) && !permissionService.canReadVereinbarung(benutzer, vereinbarungId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = vereinbarungenService.getWorkflowgroup(vereinbarungId);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @GetMapping("/list")
    @Operation(
            summary = "Retrieve a list of Vereinbarungen.",
            description = "This endpoint returns a list of Vereinbarungen.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PayloadResponse> getVereinbarungen(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                      @RequestParam int page,
                                                      @RequestParam int size) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_ALLE_VEREINBARUNGEN_EINSEHEN))){
            return ResponseEntity.ok(vereinbarungenService.listAll(page, size));
        }
        if (benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_VEREINBARUNGEN_EINSEHEN))) {
            return checkResultIfNull(vereinbarungenService.listVereinbarungenForBenutzer(benutzer, page, size));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "This method is used to get filtered Vereinbarungen.",
            description = "Returns the filtered Vereinbarungen.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/search")
    public ResponseEntity<PayloadResponse> searchVereinbarungen(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String firma,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortDirection,
            @RequestParam int page, @RequestParam int size) {

        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_ALLE_VEREINBARUNGEN_EINSEHEN))) {
            return checkResultIfNull(vereinbarungenService.getFilteredVereinbarungen(
                    status != null ? List.of(VereinbarungStatus.fromValue(status)) : List.of(),
                    firma, searchTerm, sortProperty, sortDirection, page, size));
        }

        if (benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_VEREINBARUNGEN_EINSEHEN))) {
            return checkResultIfNull(vereinbarungenService.getFilteredVereinbarungenForBenutzer(
                    benutzer, status != null ? List.of(VereinbarungStatus.fromValue(status)) : List.of(),
                    firma, searchTerm, sortProperty, sortDirection, page, size));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @Operation(summary = "This method is used edit an existing Vereinbarung.",
            description = "Returns the edited Vereinbarung.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/edit")
    public ResponseEntity<PayloadResponse> saveVereinbarung(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                            @RequestBody VereinbarungDto vereinbarungDto){
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_UNEINGESCHRAENKTE_VEREINBARUNGEN_ERSTELLEN)) && !permissionService.canCreateVereinbarung(benutzer, vereinbarungDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(vereinbarungenService.updateVereinbarung(vereinbarungDto));
    }

    @GetMapping("/fetch/{id}")
    @Operation(
            summary = "Retrieve Vereinbarung based on ID.",
            description = "Retrieve Vereinbarung based on ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PayloadResponse> getVereinbarungById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                      @PathVariable("id") Integer id) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_ALLE_VEREINBARUNGEN_EINSEHEN)) && !permissionService.canReadVereinbarung(benutzer, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(vereinbarungenService.getVereinbarung(id));
    }

    @PostMapping("/create")
    @Operation(
            summary = "Generate a new Vereinbarung Entry (NO PDF FILE YET).",
            description = "This endpoint generates a new Vereinbarung Entry (NO PDF FILE YET).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity generateVereinbarung(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody ReportRequestDto reportRequestDto) {
        log.info("Received request to generate Vereinbarung: {}", reportRequestDto);

        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_UNEINGESCHRAENKTE_VEREINBARUNGEN_ERSTELLEN)) && !permissionService.canCreateVereinbarung(benutzer, reportRequestDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reportRequestDto.setCreatedBy(benutzerDetailsService != null ? benutzer.getEmail() : null);

        PayloadResponse createdVereinbarung = vereinbarungenService.createVereinbarung(reportRequestDto);

        return ResponseEntity
                .ok()
                .body(createdVereinbarung);
    }

    @PostMapping("/generateFile")
    @Operation(
            summary = "Generate a new Vereinbarung File (PDF FILE).",
            description = "This endpoint generates a new Vereinbarung File (PDF FILE).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity generateVereinbarungFile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody VereinbarungDto vereinbarungDto) {
        log.info("Received request to persist file for Vereinbarung: {}", vereinbarungDto);
        //TODO Check for correct permissions
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_UNEINGESCHRAENKTE_VEREINBARUNGEN_ERSTELLEN)) && !permissionService.canCreateVereinbarung(benutzer, vereinbarungDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        vereinbarungDto.setCreatedBy(benutzerDetailsService != null ? benutzer.getEmail() : null);

        PayloadResponse createdVereinbarung = vereinbarungenService.persistVereinbarungFile(vereinbarungDto);

        return ResponseEntity
                .ok()
                .body(createdVereinbarung);
    }


    @PostMapping("/generatePreview")
    @Operation(
            summary = "Generate Preview of a Vereinbarung PDF (DOES NOT SAVE THE FILE)",
            description = "Generate Preview of a Vereinbarung PDF (DOES NOT SAVE THE FILE)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity generateVereinbarungPreview(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody VereinbarungDto vereinbarungDto) {
        log.info("Received Preview request for Vereinbarung: {}", vereinbarungDto);
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_UNEINGESCHRAENKTE_VEREINBARUNGEN_ERSTELLEN)) && !permissionService.canCreateVereinbarung(benutzer, vereinbarungDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        ReportResponse vereinbarungPreview = vereinbarungenService.generateVereinbarungPreview(vereinbarungDto);


        // Set the HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", vereinbarungPreview.getReportName() + "." + vereinbarungPreview.getOutputFormat().getValue());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(vereinbarungPreview.getReportBytes());
    }



}
