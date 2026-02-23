package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungMultiRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.enums.Action;
import com.ibosng.gatewayservice.enums.UserType;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.MAVerwaltenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.ibosng.gatewayservice.enums.ReportOutputFormat.*;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_ABBRECHEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_DATENVERVOLLSTAENDIGEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_PRUEFEN_GENEHMIGENDEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_PRUEFEN_LOHNVERRECHNUNG;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_PRUEFEN_PEOPLE;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_STARTEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSAENDERUNG_UEBERSICHT;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/ma-verwalten")
@Tag(name = "Mitarbeiter Verwalten controller")
@RequiredArgsConstructor
public class MAVerwaltenController {

    private final MAVerwaltenService maVerwaltenService;

    private final BenutzerDetailsService benutzerDetailsService;


    @GetMapping("/search")
    @Operation(
            summary = "This method is used to filter all onboarded Mitarbeiter.",
            description = "Returns a dto containing all Mitarbeiter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> filterMitarbeiter(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                             @RequestParam(required = false) String searchTerm,
                                                             @RequestParam(required = false) String wohnort,
                                                             @RequestParam(required = false) List<String> firma,
                                                             @RequestParam(required = false) List<String> kostenstelle,
                                                             @RequestParam(required = false) List<String> beschaeftigungsstatus,
                                                             @RequestParam(required = false) List<String> jobbezeichnung,
                                                             @RequestParam(required = false) List<String> kategorie,
                                                             @RequestParam(required = false) String sortProperty,
                                                             @RequestParam(required = false) String sortDirection,
                                                             @RequestParam int page,
                                                             @RequestParam int size) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_UEBERSICHT))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(maVerwaltenService.findMAByCriteria(searchTerm, wohnort, firma, kostenstelle, beschaeftigungsstatus, jobbezeichnung, kategorie, sortProperty, sortDirection, page, size));
    }

    @GetMapping("/search-vertragaenderungen")
    @Operation(
            summary = "This method is used to filter all onboarded Mitarbeiter.",
            description = "Returns a dto containing all Mitarbeiter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> filterVertragaenderungen(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                    @RequestParam(required = false) String searchTerm,
                                                                    @RequestParam(required = false) List<String> statuses,
                                                                    @RequestParam(required = false) String sortProperty,
                                                                    @RequestParam(required = false) String sortDirection,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_VERTRAGSAENDERUNG_UEBERSICHT))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(maVerwaltenService.findVertragsaenderungenByCriteria(searchTerm, statuses, sortProperty, sortDirection, page, size));
    }

    @PostMapping(value = "/vertragsaenderung")
    @Operation(
            summary = "This method is used to do Vertragsaenderung of a Mitarbeiter",
            description = "Returns a dto with Vetragsaenderung",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> postVertragsaenderung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto
    ) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader),
                List.of(FN_VERTRAGSAENDERUNG_STARTEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return checkResultIfNull(maVerwaltenService.performVertragsaenderung(vertragsaenderungMultiRequestDto, token));
    }

    @GetMapping(value = "/vertragsaenderung")
    @Operation(
            summary = "This method is used to do Vertragsaenderung of a Mitarbeiter",
            description = "Returns a dto with Vetragsaenderung",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getVertragsaenderung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam Integer vertragsaenderungId
    ) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader),
                List.of(FN_VERTRAGSAENDERUNG_UEBERSICHT))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(maVerwaltenService.getVertragsaenderung(vertragsaenderungId));
    }

    @GetMapping(value = "/downloadDocument")
    @Operation(
            summary = "This method is used to download the Vertragsaenderung Document of a Mitarbeiter",
            description = "Returns a Vetragsaenderung PDF Document",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity downloadVertragsaenderungDocument(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam String personalnummer
    ) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader),
                List.of(FN_VERTRAGSAENDERUNG_UEBERSICHT))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ReportResponse reportResponse = maVerwaltenService.downloadVertragsaenderungFile(personalnummer);
        // Set Media Type for FE
        MediaType mediaType;
        switch (reportResponse.getOutputFormat()) {
            case PDF:
                mediaType = MediaType.APPLICATION_PDF;
                break;
            case XLS:
            case XLSX:
                mediaType = MediaType.parseMediaType("application/vnd.ms-excel");
                break;
            case CSV:
                mediaType = MediaType.TEXT_PLAIN;
                break;
            default:
                log.warn("Unsupported report format requested: {}", reportResponse.getOutputFormat());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Unsupported report format: " + reportResponse.getOutputFormat());
        }

        log.info("Report format determined: {}", mediaType);

        // Set the HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("filename", reportResponse.getReportName());

        log.info("Returning report response with headers: {}", headers);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(reportResponse.getReportBytes());
       
    }

    @PostMapping({"/prufung/{id}"})
    public ResponseEntity<PayloadResponse> acceptVertragsaenderung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam Action action,
            @RequestParam UserType userType,
            @PathVariable Integer id,
            @RequestBody(required = false) VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto) {
        return maVerwaltenService.acceptVertragsaenderung(action, userType, id, vertragsaenderungMultiRequestDto, authorizationHeader);
    }
}
