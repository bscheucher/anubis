package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.BerufService;
import com.ibosng.gatewayservice.services.SeminarResponseService;
import com.ibosng.gatewayservice.services.Teilnehmerservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ibosng.gatewayservice.utils.Constants.*;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@Validated
@RequestMapping("/teilnehmer")
@Tag(name = "Teilnehmer controller")
public class TeilnehmerController {
    private final SeminarResponseService seminarResponseService;
    private final BenutzerDetailsService benutzerDetailsService;
    private final Teilnehmerservice teilnehmerservice;
    private final BerufService berufService;

    public TeilnehmerController(SeminarResponseService seminarResponseService,
                                BenutzerDetailsService benutzerDetailsService,
                                Teilnehmerservice teilnehmerservice,
                                BerufService berufService) {
        this.seminarResponseService = seminarResponseService;
        this.benutzerDetailsService = benutzerDetailsService;
        this.teilnehmerservice = teilnehmerservice;
        this.berufService = berufService;
    }

    @Operation(summary = "This method is used to get filtered Teilnehmers.",
            description = "Returns the filtered Teilnehmer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/search")
    public ResponseEntity<PayloadResponse> searchTeilnehmer(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortDirection,
            @RequestParam int page, @RequestParam int size) {

        return checkResultIfNull(teilnehmerservice.getFilteredTeilnehmer(searchTerm, sortProperty, sortDirection, page, size));
    }

    @GetMapping("/getTeilnehmerFilterSummaryDto")
    public ResponseEntity<PayloadResponse> getTeilnehmerFilterSummaryDto(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                         @RequestParam(required = false) String identifiersString,
                                                                         @RequestParam(required = false) String seminarName,
                                                                         @RequestParam(required = false) String projectName,
                                                                         @RequestParam(required = false) Boolean isActive,
                                                                         @RequestParam(required = false) Boolean isUebaTeilnehmer,
                                                                         @RequestParam(required = false) Boolean isAngemeldet,
                                                                         @RequestParam(required = false) String geschlecht,
                                                                         @RequestParam(required = false) Boolean isFehlerhaft,
                                                                         @RequestParam(required = false) String massnahmennummer,
                                                                         @RequestParam(required = false) String sortProperty,
                                                                         @RequestParam(required = false) String sortDirection,
                                                                         @RequestParam int page,
                                                                         @RequestParam int size) {
        if (benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_TR_LESEN_ALLE))) {
            return checkResultIfNull(teilnehmerservice.getTeilnehmerFilterSummaryDto(identifiersString, seminarName, projectName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer,  null, sortProperty, sortDirection, page, size));
        }
        if (benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            Benutzer benutzer = benutzerDetailsService.getUserFromToken(getTokenFromAuthorizationHeader(authorizationHeader));
            return checkResultIfNull(teilnehmerservice.getTeilnehmerFilterSummaryDto(identifiersString, seminarName, projectName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer,  benutzer != null ? benutzer.getId() : null, sortProperty, sortDirection, page, size));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<PayloadResponse> getTeilnehmerById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Boolean isKorrigieren,
            @RequestParam(required = false) String seminarName,
            @PathVariable Integer id) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //We need the seminarName for isKorrigieren, in order to know which data to load
        return checkResultIfNull(teilnehmerservice.getTeilnehmerById(id, isKorrigieren, seminarName));
    }


    @PostMapping({"/edit/{id}", "/edit"})
    public ResponseEntity<PayloadResponse> validateTeilnehmer(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Boolean isKorrigieren,
            @RequestBody TeilnehmerSeminarDto teilnehmerDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_WIDGET_MT_FEHLER_TN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(isKorrigieren != null && isKorrigieren) {
            return checkResultIfNull(seminarResponseService.validateTeilnehmer(teilnehmerDto.getTeilnehmerDto(), benutzerDetailsService.getUserFromToken(token).getEmail(), Optional.empty()));
        }
        return checkResultIfNull(teilnehmerservice.validateTeilnehmer(teilnehmerDto, token));
    }

    @PostMapping({"/edit/{teilnehmerId}/seminar"})
    public ResponseEntity<PayloadResponse> validateSeminar(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer teilnehmerId,
            @RequestBody SeminarDto seminarDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.validateSeminarByTeilnehmer(seminarDto, token, teilnehmerId));
    }

    @GetMapping("/{teilnehmerId}/notizen")
    @Operation(
            summary = "This method is used to get all Notizen for a Teilnehmer.",
            description = "Returns a dto containing all Notizen for a Teilnehmer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getTeilnehmerNotizen(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "teilnehmerId is required") Integer teilnehmerId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return teilnehmerservice.getTeilnehmerNotizenByTeilnehmerId(teilnehmerId);
    }

    @PostMapping({"/edit/{teilnehmerId}/notiz"})
    public ResponseEntity<PayloadResponse> validateNotiz(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer teilnehmerId,
            @RequestBody TeilnehmerNotizDto teilnehmerNotizDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.validateTeilnehmerNotiz(teilnehmerNotizDto, token, teilnehmerId));
    }

    @DeleteMapping({"/delete/notiz/{notizId}"})
    @Operation(
            summary = "This method is used to delete Teilnehmer Notizen.",
            description = "Deletes an specific entry for Teilnehmer Notizen.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteTeilnehmerNotiz(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer notizId) {
        return teilnehmerservice.deleteTeilnehmerNotiz(notizId, authorizationHeader);
    }

    @GetMapping("/{teilnehmerId}/ausbildungen")
    @Operation(
            summary = "This method is used to get all Ausbildungen for a Teilnehmer.",
            description = "Returns a dto containing all Ausbildungen for a Teilnehmer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getTeilnehmerAusbildungen(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "teilnehmerId is required") Integer teilnehmerId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return teilnehmerservice.getTeilnehmerAusbildungenByTeilnehmerId(teilnehmerId);
    }

    @PostMapping({"/edit/{teilnehmerId}/ausbildung"})
    public ResponseEntity<PayloadResponse> validateAusbildung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer teilnehmerId,
            @RequestBody TnAusbildungDto tnAusbildungDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.validateTeilnehmerAusbildung(tnAusbildungDto, token, teilnehmerId));
    }

    @DeleteMapping({"/delete/ausbildung/{ausbildungId}"})
    @Operation(
            summary = "This method is used to delete Teilnehmer Ausbildung.",
            description = "Deletes an specific entry for Teilnehmer Ausbildung.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteTeilnehmerAusbildung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer ausbildungId) {
        return teilnehmerservice.deleteTeilnehmerAusbildung(ausbildungId, authorizationHeader);
    }

    @GetMapping("/{teilnehmerId}/seminar/{seminarId}/pruefungen")
    @Operation(
            summary = "This method is used to get all Pruefungen for a TeilnehmerSeminar.",
            description = "Returns a dto containing all Pruefungen for a TeilnehmerSeminar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getTeilnehmerSeminarPruefungen(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "teilnehmerId is required") Integer teilnehmerId,
            @PathVariable @NotNull(message = "seminarId is required") Integer seminarId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return teilnehmerservice.getSeminarPruefungenByTeilnehmerIdAndSeminarId(teilnehmerId, seminarId);
    }

    @PostMapping({"/edit/{teilnehmerId}/seminar/{seminarId}/pruefung"})
    public ResponseEntity<PayloadResponse> validateSeminarPruefung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "teilnehmerId is required") Integer teilnehmerId,
            @PathVariable @NotNull(message = "seminarId is required") Integer seminarId,
            @RequestBody SeminarPruefungDto seminarPruefungDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.validateSeminarPruefung(seminarPruefungDto, token, teilnehmerId, seminarId));
    }

    @DeleteMapping({"/delete/pruefung/{pruefungId}"})
    @Operation(
            summary = "This method is used to delete TeilnehmerSeminar Pruefung.",
            description = "Deletes an specific entry for TeilnehmerSeminar Pruefung.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteSeminarPruefung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer pruefungId) {
        return teilnehmerservice.deleteSeminarPruefung(pruefungId, authorizationHeader);
    }


    @PostMapping({"/edit/{teilnehmerId}/zertifikat"})
    public ResponseEntity<PayloadResponse> validateZertifikat(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer teilnehmerId,
            @RequestBody TnZertifikatDto tnZertifikatDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.validateTeilnehmerZertifikat(tnZertifikatDto, token, teilnehmerId));
    }

    @GetMapping("/{teilnehmerId}/zertifikate")
    @Operation(
            summary = "This method is used to get all Zertifikate for a Teilnehmer.",
            description = "Returns a dto containing all Zertifikate for a Teilnehmer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getTeilnehmerZertifikate(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "teilnehmerId is required") Integer teilnehmerId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return teilnehmerservice.getTeilnehmerZertifikateByTeilnehmerId(teilnehmerId);
    }

    @DeleteMapping({"/delete/zertifikat/{zertifikatId}"})
    @Operation(
            summary = "This method is used to delete Teilnehmer Zertifikat.",
            description = "Deletes an specific entry for Teilnehmer Zertifikat.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteTeilnehmerZertifikat(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer zertifikatId) {
        return teilnehmerservice.deleteTeilnehmerZertifikat(zertifikatId, authorizationHeader);
    }

    @GetMapping("/{teilnehmerId}/sprachkenntnisse")
    @Operation(
            summary = "This method is used to get all Sprachkenntnisse for a Teilnehmer.",
            description = "Returns a dto containing all Sprachkenntnisse for a Teilnehmer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getTeilnehmerSprachkenntnisse(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "teilnehmerId is required") Integer teilnehmerId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return teilnehmerservice.getTeilnehmerSprachkenntnisseByTeilnehmerId(teilnehmerId);
    }

    @PostMapping({"/edit/{teilnehmerId}/sprachkenntnis"})
    public ResponseEntity<PayloadResponse> validateSprachkenntnis(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer teilnehmerId,
            @RequestBody SprachkenntnisDto sprachkenntnisDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.validateTeilnehmerSprachkenntnis(sprachkenntnisDto, token, teilnehmerId));
    }

    @DeleteMapping({"/delete/sprachkenntnis/{sprachkenntnisId}"})
    @Operation(
            summary = "This method is used to delete Teilnehmer Sprachkenntnis.",
            description = "Deletes an specific entry for Teilnehmer Sprachkenntnis.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteTeilnehmerSprachkenntnis(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer sprachkenntnisId) {
        return teilnehmerservice.deleteTeilnehmerSprachkenntnis(sprachkenntnisId, authorizationHeader);
    }

    @GetMapping("/{teilnehmerId}/berufserfahrungen")
    @Operation(
            summary = "This method is used to get all Berufserfahrungen for a Teilnehmer.",
            description = "Returns a dto containing all Berufserfahrungen for a Teilnehmer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getTeilnehmerBerufserfahrungen(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "teilnehmerId is required") Integer teilnehmerId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return teilnehmerservice.getTeilnehmerBerufserfahrungenByTeilnehmerId(teilnehmerId);
    }

    @PostMapping({"/edit/{teilnehmerId}/berufserfahrung"})
    public ResponseEntity<PayloadResponse> validateBerufserfahrung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer teilnehmerId,
            @RequestBody TnBerufserfahrungDto tnBerufserfahrungDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.validateTeilnehmerBerufserfahrung(tnBerufserfahrungDto, token, teilnehmerId));
    }

    @DeleteMapping({"/delete/berufserfahrung/{berufserfahrungId}"})
    @Operation(
            summary = "This method is used to delete Teilnehmer Berufserfahrung.",
            description = "Deletes an specific entry for Teilnehmer Berufserfahrung.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteTeilnehmerBerufserfahrung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer berufserfahrungId) {
        return teilnehmerservice.deleteTeilnehmerBerufserfahrung(berufserfahrungId, authorizationHeader);
    }

    @PostMapping("/submitTeilnehmersZeiterfassung")
    public ResponseEntity<PayloadResponse> getTeilnehmersZeiterfassung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Boolean shouldSubmit,
            @RequestBody ZeiterfassungTransferDto zeiterfassungDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TN_ZEITBUCHUNGEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.getTeilnehmersZeiterfassung(zeiterfassungDto, shouldSubmit, benutzerDetailsService.getUserFromToken(token).getEmail()));
    }

    @GetMapping("/getZeiterfassungUebermittlungen")
    public ResponseEntity<PayloadResponse> getZeiterfassungUebermittlungen(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortDirection,
            @RequestParam int page,
            @RequestParam int size) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TN_ZEITBUCHUNGEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.getZeiterfassungTransfers(sortProperty, sortDirection, page, size));
    }

    @Operation(
            summary = "Send UEBA abmelden",
            description = "Send abmeldungsformular to LHR to with austrittsgrund",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping("/submitUebaAbmeldung")
    public ResponseEntity<?> postAbmeldung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody AbmeldungDto abmeldungDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TN_ABMELDEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.postUebaAbmeldung(token, abmeldungDto));
    }

    @Operation
            (
                    summary = "Get UEBA abmeldungen",
                    description = "Get abmeldungen for mitarbeiter that were abgemeldet and added into the abmeldung table",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "404", description = "Not Found"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error")
                    }
            )
    @GetMapping("/getUebaAbmeldung")
    public ResponseEntity<?> getUebaAbmeldung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam int page,
            @RequestParam int size) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TN_ABMELDEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.getUebaAbmeldung(page, size));
    }

    @Operation
            (
                    summary = "Get a single UEBA Abmeldung",
                    description = "Get single abmeldungen for mitarbeiter that were abgemeldet and added into the abmeldung table",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "404", description = "Not Found"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error")
                    }
            )
    @GetMapping("/getUebaAbmeldung/{id}")
    public ResponseEntity<?> editUebaAbmeldung(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer id
    ) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TN_ABMELDEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(teilnehmerservice.getUebaAbmeldungById(id));
    }


    @Operation
            (
                    summary = "Post Kompetenzen übersicht",
                    description = "Post Kompetenzenübersicht to natif.ai through natif-service",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "404", description = "Not Found"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error")
                    }
            )
    @PostMapping("/postKompetenzenUebersicht/{id}")
    public ResponseEntity<?> postKompetenzenUebersicht(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam(required = false, name = "document_type") String documentType,
            @RequestParam(required = false) String language,
            @RequestParam(required = false, name = "generate_pdf") String generatePdf,
            @RequestParam(required = false, name = "process_definition_key") String process_definition_key,
            @PathVariable Integer id
    ) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TN_UPLOAD_PROFIL))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("sending document to natif-service");
        return checkResultIfNull(teilnehmerservice.postKompetenzUebersicht(file, documentType, language, generatePdf,
                process_definition_key, id));
    }

    @Operation
            (
                    summary = "Get AMS Berufe",
                    description = "Get AMS Berufe converted from XML to Json",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "404", description = "Not Found"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error")
                    }
            )
    @GetMapping("/berufe")
    public ResponseEntity<PayloadResponse> getTreeStructureBerufe(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                  @RequestParam(required = false) String searchTerm) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(berufService.fetchBerufe(searchTerm));
    }

}
