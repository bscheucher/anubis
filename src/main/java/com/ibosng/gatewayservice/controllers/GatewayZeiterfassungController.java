package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.zeiterfassung.umbuchung.UmbuchungDto;
import com.ibosng.gatewayservice.dtos.GenehmigungDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.ZeiterfassungGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.*;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/zeiterfassung")
@Tag(name = "Zeiterfassung controller")
@RequiredArgsConstructor
public class GatewayZeiterfassungController {
    private final ZeiterfassungGatewayService zeiterfassungGatewayService;
    private final BenutzerDetailsService benutzerDetailsService;

    @DeleteMapping({"/abwesenheiten/delete/{abwesenheitId}"})
    @Operation(
            summary = "This method is used to delete Mitarbeiter Abwesenheiten.",
            description = "Deletes the Mitarbeiter Abwesenheit.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteAbwesenheit(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer abwesenheitId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_ABWESENHEITEN_EDITIEREN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(zeiterfassungGatewayService.deleteAbwesenheit(abwesenheitId));
    }

    @PostMapping("/abwesenheiten/{id}")
    @Operation(
            summary = "Resends INVALID abwesenheit with specified id. Will not work if user is not creator of abwesenheit"
    )
    public ResponseEntity<PayloadResponse> resend(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer id) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_ABWESENHEITEN_EDITIEREN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(zeiterfassungGatewayService.resendAbwesenheit(id, token));
    }

    @PostMapping({"/abwesenheiten/edit"})
    @Operation(
            summary = "This method is used to save Mitarbeiter Abwesenheiten.",
            description = "Saves the Mitarbeiter Abwesenheiten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> saveAbwesenheit(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody AbwesenheitDto abwesenheitDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_ABWESENHEITEN_EDITIEREN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(zeiterfassungGatewayService.saveAbwesenheit(abwesenheitDto, token));
    }

    @GetMapping({"/abwesenheiten/edit/{abwesenheitId}"})
    @Operation(
            summary = "This method is used to get a specific Mitarbeiter Abwesenheit by id.",
            description = "Fetches the Mitarbeiter Abwesenheit by id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getAbwesenheit(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer abwesenheitId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_EIGENE_ABWESENHEITEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(zeiterfassungGatewayService.getAbwesenheit(abwesenheitId));
    }

    @GetMapping({"/abwesenheiten/list"})
    @Operation(
            summary = "This method is used to fetch Mitarbeiter Abwesenheiten.",
            description = "Fetches the Mitarbeiter Abwesenheiten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getAbwesenheiten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(defaultValue = "true") Boolean isPersonal,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortDirection,
            @RequestParam int page,
            @RequestParam int size
    ) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_EIGENE_ABWESENHEITEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(zeiterfassungGatewayService.getAbwesenheitenList(token, isPersonal, status, year, sortProperty, sortDirection, page, size));
    }

    @PostMapping("/abwesenheiten/genehmigung/{abwesenheitId}")
    public ResponseEntity<PayloadResponse> genehmigenAbwesenheit(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer abwesenheitId,
            @RequestBody GenehmigungDto genehmigungDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_ABWESENHEITEN_GENEHMIGEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(zeiterfassungGatewayService.genehmigungAbwesenheit(token, abwesenheitId, genehmigungDto));
    }

    @GetMapping("/abwesenheiten/overview")
    public ResponseEntity<PayloadResponse> getAbwesenheitOverview(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) String personalnummer,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "startDate") String sortProperty,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_EIGENE_ABWESENHEITEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(zeiterfassungGatewayService
                .getAbwesenheitOverview(token, startDate, endDate, sortProperty, sortDirection, page, size));
    }

    @GetMapping("/zeitbuchungen/list")
    public ResponseEntity<PayloadResponse> getZeitbuchungenList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                @RequestParam String startDate,
                                                                @RequestParam String endDate,
                                                                @RequestParam(defaultValue = "true") Boolean shouldSync) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_ZEITEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = zeiterfassungGatewayService.getMitarbeiterZeitbuchungen(token, startDate, endDate, shouldSync);
        return checkResultIfNull(response);
    }

    /**
     * The implementation of this endpoint by msg-plaut has been removed as the feature hadn't been ready for release
     */
    @GetMapping("/umbuchung")
    public ResponseEntity<PayloadResponse> getUmbuchung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam String date) {

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * The implementation of this endpoint by msg-plaut has been removed as the feature hadn't been ready for release
     */
    @PostMapping("/umbuchung")
    public ResponseEntity<PayloadResponse> postUmbuchung(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                         @RequestParam String date,
                                                         @RequestBody @Valid UmbuchungDto umbuchungDto) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
