package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.mitarbeiter.*;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.MAOnboardingService;
import com.ibosng.gatewayservice.services.MasterdataService;
import com.ibosng.gatewayservice.services.MitarbeiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.*;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@Validated
@RequestMapping("/mitarbeiter")
@Tag(name = "Mitarbeiter Onboarding controller")
public class MitarbeiterController {

    private final MAOnboardingService maOnboardingService;
    private final MitarbeiterService mitarbeiterService;
    private final MasterdataService masterdataService;
    private final BenutzerDetailsService benutzerDetailsService;

    public MitarbeiterController(@Qualifier("maOnboardingServiceImpl") MAOnboardingService maOnboardingService,
                                 @Qualifier("mitarbeiterServiceImpl") MitarbeiterService mitarbeiterService,
                                 MasterdataService masterdataService,
                                 BenutzerDetailsService benutzerDetailsService) {
        this.maOnboardingService = maOnboardingService;
        this.mitarbeiterService = mitarbeiterService;
        this.masterdataService = masterdataService;
        this.benutzerDetailsService = benutzerDetailsService;
    }

    @GetMapping("list")
    @Operation(
            summary = "This method is used to get all Mitarbeiter.",
            description = "Returns a dto containing all Mitarbeiter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getMitarbeiter(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                          @RequestParam(required = false) String sortProperty,
                                                          @RequestParam(required = false) String sortDirection,
                                                          @RequestParam(required = false) String mitarbeiterType,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {

        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);

        if (mitarbeiterType.equalsIgnoreCase(MitarbeiterType.MITARBEITER.getValue())) {
            if(benutzerDetailsService.isUserEligible(benutzer, List.of(FN_MA_ONBOARDING))){
                return checkResultIfNull(maOnboardingService.getAllMitarbeiterList(sortProperty, sortDirection, page, size, mitarbeiterType));

            }else if (benutzerDetailsService.isUserEligible(benutzer, List.of(FN_MA_ONBOARDING_EIGENE ))){
                return checkResultIfNull(maOnboardingService.getEigeneMitarbeiterList(sortProperty, sortDirection, page, size, mitarbeiterType, benutzer));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (mitarbeiterType.equalsIgnoreCase(MitarbeiterType.TEILNEHMER.getValue()) && !benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_TN_ONBOARDING))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(maOnboardingService.getAllMitarbeiterList(sortProperty, sortDirection, page, size, mitarbeiterType));
    }

    @GetMapping("stammdaten/edit/{personalnummer}")
    @Operation(
            summary = "This method is used to get all Mitarbeiter Stammdaten.",
            description = "Returns a dto containing all Mitarbeiter Stammdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getStammdaten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @PathVariable("personalnummer") String personalnummer) {
        return mitarbeiterService.getStammdaten(personalnummer, isOnboarding, authorizationHeader);
    }

    @PostMapping({"stammdaten/edit/{personalnummer}", "stammdaten/edit"})
    @Operation(
            summary = "This method is used to save Mitarbeiter Stammdaten.",
            description = "Saves the Mitarbeiter Stammdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> saveStammdaten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Integer workflowId,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @RequestBody StammdatenDto stammdatenDto) {
        return mitarbeiterService.saveStammdaten(stammdatenDto, workflowId, isOnboarding, authorizationHeader);
    }

    @GetMapping("vertragsdaten/edit/{personalnummer}")
    @Operation(
            summary = "This method is used to get all Mitarbeiter Vertragsdaten.",
            description = "Returns a dto containing all Mitarbeiter Vertragsdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getVertragsdaten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @PathVariable("personalnummer") String personalnummer) {
        return mitarbeiterService.getVertragsdaten(personalnummer, isOnboarding, authorizationHeader);
    }

    @PostMapping({"vertragsdaten/edit/{personalnummer}", "vertragsdaten/edit"})
    @Operation(
            summary = "This method is used to save Mitarbeiter Vertragsdaten.",
            description = "Saves the Mitarbeiter Vertragsdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> saveVertragsdaten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) Integer workflowId,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @RequestBody VertragsdatenDto vertragsDatenDto) {
        return mitarbeiterService.saveVertragsdaten(vertragsDatenDto, workflowId, isOnboarding, authorizationHeader);
    }

    @PostMapping({"vertragsdaten/vordienstzeiten/{personalnummer}", "vertragsdaten/vordienstzeiten"})
    @Operation(
            summary = "This method is used to save Mitarbeiter Vertragsdaten.",
            description = "Saves the Mitarbeiter Vertragsdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> saveVordienstzeiten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @RequestBody VordienstzeitenDto vordienstzeitenDto) {
        return mitarbeiterService.saveVordienstzeiten(vordienstzeitenDto, isOnboarding, authorizationHeader);
    }

    @PutMapping({"vertragsdaten/editVordienstzeiten"})
    @Operation(
            summary = "This method is used to save Mitarbeiter Vertragsdaten.",
            description = "Saves the Mitarbeiter Vertragsdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> editVordienstzeiten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @RequestBody VordienstzeitenDto vordienstzeitenDto) {
        return mitarbeiterService.saveVordienstzeiten(vordienstzeitenDto, isOnboarding, authorizationHeader);
    }

    @DeleteMapping({"vertragsdaten/vordienstzeiten/delete/{vordienstzeitenId}",})
    @Operation(
            summary = "This method is used to delete Mitarbeiter Vordienstzeiten.",
            description = "Deletes an specific entry for Mitarbeiter Vordienstzeiten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteVordienstzeiten(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "vordienstzeitenId is required") Integer vordienstzeitenId) {
        return mitarbeiterService.deleteVordienstzeiten(vordienstzeitenId, authorizationHeader);
    }

    @GetMapping("vertragsdaten/vordienstzeiten/{personalnummer}")
    @Operation(
            summary = "This method is used to get all Vordienstzeiten for a Mitarbeiter.",
            description = "Returns a dto containing all Vordienstzeiten for a Mitarbeiter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getVordienstzeiten(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                              @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
                                                              @PathVariable("personalnummer") String personalnummer) {
        return mitarbeiterService.getVordienstzeiten(personalnummer, isOnboarding, authorizationHeader);
    }

    @PostMapping({"vertragsdaten/unterhaltsberechtigte/{personalnummer}", "vertragsdaten/unterhaltsberechtigte"})
    @Operation(
            summary = "This method is used to save Mitarbeiter Unterhaltsberechtigte.",
            description = "Saves the Mitarbeiter Unterhaltsberechtigte.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> saveUnterhaltsberechtigte(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @RequestBody UnterhaltsberechtigteDto unterhaltsberechtigteDto) {
        return mitarbeiterService.saveUnterhaltsberechtigte(unterhaltsberechtigteDto, isOnboarding, authorizationHeader);
    }

    @PutMapping({"vertragsdaten/editUnterhaltsberechtigte"})
    @Operation(
            summary = "This method is used to save Mitarbeiter Unterhaltsberechtigte.",
            description = "Saves the Mitarbeiter Unterhaltsberechtigte.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> editUnterhaltsberechtigte(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @RequestBody UnterhaltsberechtigteDto unterhaltsberechtigteDto) {
        return mitarbeiterService.saveUnterhaltsberechtigte(unterhaltsberechtigteDto, isOnboarding, authorizationHeader);
    }

    @DeleteMapping({"vertragsdaten/unterhaltsberechtigte/delete/{unterhaltsberechtigteId}",})
    @Operation(
            summary = "This method is used to delete Mitarbeiter Unterhaltsberechtigte.",
            description = "Deletes an specific entry for Mitarbeiter Unterhaltsberechtigte.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> deleteUnterhaltsberechtigte(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable @NotNull(message = "unterhaltsberechtigteId is required") Integer unterhaltsberechtigteId) {
        return mitarbeiterService.deleteUnterhaltsberechtigte(unterhaltsberechtigteId, authorizationHeader);
    }

    @GetMapping("vertragsdaten/unterhaltsberechtigte/{personalnummer}")
    @Operation(
            summary = "This method is used to get all Unterhaltsberechtigte for a Mitarbeiter.",
            description = "Returns a dto containing all Unterhaltsberechtigte for a Mitarbeiter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getUnterhaltsberechtigte(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                    @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
                                                                    @PathVariable("personalnummer") String personalnummer) {
        return mitarbeiterService.getUnterhaltsberechtigte(personalnummer, isOnboarding, authorizationHeader);
    }

    @GetMapping("/masterdata")
    @Operation(
            summary = "This method is used to get all Masterdata.",
            description = "Returns a dto containing all Masterdata.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getMasterdata(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                         @RequestParam(required = false, defaultValue = "") String type) {
        return checkResultIfNull(masterdataService.getMitarbeiterPayload(type));
    }

    @GetMapping("/generatePersonalnummer")
    @Operation(
            summary = "This method is used to get all Mitarbeiter Stammdaten.",
            description = "Returns a dto containing all Mitarbeiter Stammdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> generatePersonalnummer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                  @RequestParam String firmenName) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Arrays.asList(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = maOnboardingService.generatePersonalnummerPayloadResponse(null, firmenName, token);
        return checkResultIfNull(response);
    }

    @GetMapping("/generatePersonalnummerForTeilnehmer")
    @Operation(
            summary = "This method is used to get all Mitarbeiter Stammdaten.",
            description = "Returns a dto containing all Mitarbeiter Stammdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> generatePersonalnummerForTeilnehmer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                               @RequestParam Integer teilnehmerID) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_TN_ONBOARDING))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = maOnboardingService.generatePersonalnummerPayloadResponse(teilnehmerID, null, token);
        return checkResultIfNull(response);
    }

    @PostMapping({"vertragsdaten/lv-acceptance"})
    @Operation(
            summary = "This method is used to save Mitarbeiter lv-acceptance.",
            description = "Saves the Mitarbeiter Vertragsdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> saveLvAcceptance(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody LvAcceptanceDto lvAcceptanceDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_UEBERPRUEFEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = maOnboardingService.saveLvAcceptance(lvAcceptanceDto, token);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @GetMapping({"vertragsdaten/lv-acceptance"})
    @Operation(
            summary = "This method is used to save Mitarbeiter lv-acceptance.",
            description = "Saves the Mitarbeiter Vertragsdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getLvAcceptance(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam String personalnummer) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_UEBERPRUEFEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = maOnboardingService.getLvAcceptance(personalnummer, token);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @GetMapping("/getKostenstelleFromPersonalnummer")
    @Operation(
            summary = "This method is used to get all Mitarbeiter Stammdaten.",
            description = "Returns a dto containing all Mitarbeiter Stammdaten.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> getKostenstelleFromPersonalnummer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                             @RequestParam String personalnummer) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN, FN_TN_ONBOARDING))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = masterdataService.getKostenstelleFromPersonalnummer(personalnummer);
        return checkResultIfNull(response);
    }

    @PostMapping("/sendMoxisSigningRequest")
    @Operation(
            summary = "This method is used to get start a signing process.",
            description = "Returns a payload containing the info of the workflowgroup process.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> sendMoxisSigningRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                   @RequestParam String personalnummer) {

        return mitarbeiterService.sendMoxisSigningRequest(personalnummer, authorizationHeader);
    }

    @PostMapping("/sendVereinbarungSigningRequest")
    @Operation(
            summary = "This method is used to get start a Vereinbarung signing process.",
            description = "Returns a payload containing the info of the workflowgroup process.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> sendVereinbarungSigningRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                          @RequestParam Integer vereinbarungId) {

        return mitarbeiterService.sendVereinbarungSigningRequest(authorizationHeader, vereinbarungId);
    }

    @PostMapping("/cancelMoxisSigningRequest")
    @Operation(
            summary = "This method is used to cancel the signing process.",
            description = "Returns a payload.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> cancelMoxisSigningRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                     @RequestParam String personalnummer) {

        return mitarbeiterService.cancelMoxisSigningRequest(personalnummer, authorizationHeader);
    }

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
                                                            @RequestParam String personalnummer) {
        if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(personalnummer, authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = maOnboardingService.getWorkflowgroup(personalnummer);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @GetMapping("/downloadZeitnachweis")
    public ResponseEntity<byte[]> downloadZeitnachweis(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(required = false) String date) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_ZEITEN_LESEN)) ||
                !benutzerDetailsService.isUserEligible(token, List.of(FN_EIGENE_ABWESENHEITEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return maOnboardingService.getZeitnachweisFile(token, date);
    }

    @PostMapping("/informLohnverrechnung")
    @Operation(
            summary = "This method is used to get start a signing process.",
            description = "Returns a payload containing the info of the workflowgroup process.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<PayloadResponse> informLohnverrechnung(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                 @RequestParam String personalnummer) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);

        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_ONBOARDING_LV_INFORMIEREN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = maOnboardingService.informLohnverrechnung(token, personalnummer);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @GetMapping("/seminars/{personalnummer}")
    public ResponseEntity<PayloadResponse> getSeminar(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                      @PathVariable String personalnummer) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_PROJEKTE_SEMINARE_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(mitarbeiterService.getSeminars(personalnummer));
    }

    @GetMapping("/projects/{personalnummer}")
    public ResponseEntity<PayloadResponse> getProjects(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                       @PathVariable String personalnummer) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_MA_PROJEKTE_SEMINARE_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return checkResultIfNull(mitarbeiterService.getProjekts(personalnummer));
    }
}
