package com.ibosng.validationservice.controllers;

import com.ibosng.dbservice.dtos.ZeitbuchungSyncRequestDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.validationservice.services.KVEinstufungService;
import com.ibosng.validationservice.services.MitarbeiterSyncService;
import com.ibosng.validationservice.services.ValidatorService;
import com.ibosng.validationservice.services.ZeitbuchungenValidatorService;
import com.ibosng.workflowservice.dtos.WorkflowPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

@Slf4j
@RestController
@RequestMapping("/validator")
@Tag(name = "Validator controller")
@RequiredArgsConstructor
public class ValidatorController {

    private final ValidatorService validatorService;
    private final KVEinstufungService kvEinstufungService;
    private final ZeitbuchungenValidatorService zeitbuchungenValidatorService;
    private final MitarbeiterSyncService mitarbeiterSyncService;

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @Operation(summary = "This method is used to get the participants and seminar details.",
            description = "Returns the participants and seminar details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/validateImportedTeilnehmer")
    public ResponseEntity<String> validateImportedParticipants(@RequestBody WorkflowPayload workflowPayload) {
        return validatorService.validateImportedParticipants(workflowPayload);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @Operation(summary = "This method is used to get the participants and seminar details.",
            description = "Returns the participants and seminar details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/validateMitarbeiterStammdaten")
    public ResponseEntity<StammdatenDto> validateMitarbeiterStammdaten(@RequestBody StammdatenDto stammdatenDto,
                                                                       @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
                                                                       @RequestParam String changedBy) {
        return validatorService.validateMitarbeiterStammdaten(stammdatenDto, isOnboarding, changedBy);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @Operation(summary = "This method is used to get the participants and seminar details.",
            description = "Returns the participants and seminar details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/validateMitarbeiterVertragsdaten")
    public ResponseEntity<VertragsdatenDto> validateMitarbeiterVertragsdaten(
            @RequestBody VertragsdatenDto vertragsdatenDto,
            @RequestParam(required = false, defaultValue = "false") Boolean isOnboarding,
            @RequestParam String changedBy) {
        return validatorService.validateMitarbeiterVertragsdaten(vertragsdatenDto, isOnboarding, changedBy);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @Operation(summary = "This method is used to save Vordienstzeiten",
            description = "Returns the saved Vordienstzeiten",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/validateMitarbeiterVordienstzeiten")
    public ResponseEntity<VordienstzeitenDto> validateMitarbeiterVordienstzeiten(@RequestBody VordienstzeitenDto vordienstzeitenDto, @RequestParam String changedBy) {
        return validatorService.validateMitarbeiterVordienstzeiten(vordienstzeitenDto, changedBy);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @Operation(summary = "This method is used to save Unterhaltsberechtigte",
            description = "Returns the saved Unterhaltsberechtigte",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/validateMitarbeiterUnterhaltsberechtigte")
    public ResponseEntity<UnterhaltsberechtigteDto> validateMitarbeiterUnterhaltsberechtigte(@RequestBody UnterhaltsberechtigteDto unterhaltsberechtigteDto, @RequestParam String changedBy) {
        return validatorService.validateMitarbeiterUnterhaltsberechtigte(unterhaltsberechtigteDto, changedBy);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @PostMapping("/validateZeitbuchung")
    public ResponseEntity<ZeitbuchungenDto> validateZeitbuchungenDto(
            @RequestBody ZeitbuchungenDto zeitbuchungenDto,
            @RequestParam String changedBy) {
        return validatorService.validateZeitbuchung(zeitbuchungenDto, changedBy);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @Operation(
            summary = "Get zeitbuchungen for mitarbeiter",
            description = "Get zeitbuchungen for mitarbeiter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "404", description = "Personalnummer not exist in ibosNG"),
                    @ApiResponse(responseCode = "500", description = "Internal service error")
            })
    @PostMapping("/validateZeitbuchungen")
    public ResponseEntity<List<ZeitbuchungenDto>> validateZeitbuchungen(@RequestBody ZeitbuchungSyncRequestDto requestDto) {
        return zeitbuchungenValidatorService.getZeitbuchungen(requestDto);
    }

    // NOTE: Seems to be unused. No calls from other services to here
    @PostMapping("/reconcileZeitbuchungen")
    public ResponseEntity<List<ZeitbuchungenDto>> reconcileZeitbuchungen(
            @RequestBody ZeitbuchungSyncRequestDto requestDto,
            @RequestParam(defaultValue = VALIDATION_SERVICE) String changedBy) {
        return zeitbuchungenValidatorService.updateZeitbuchungen(requestDto, changedBy);
    }

    // NOTE: Seems to be unused. No calls from other services to here
    @PostMapping("/validateSyncMitarbeiter")
    public ResponseEntity<String> validateSyncMitarbeiter(
            @RequestParam String email) {
        return mitarbeiterSyncService.syncMitarbeiterFromIbisacam(email, null, null);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @PostMapping("/validateSyncMitarbeiterWithUPN")
    public ResponseEntity<String> validateSyncMitarbeiterWithUPN(
            @RequestParam String upn) {
        return mitarbeiterSyncService.syncMitarbeiterFromIbisacamWithUPN(upn, null, null);
    }
}
