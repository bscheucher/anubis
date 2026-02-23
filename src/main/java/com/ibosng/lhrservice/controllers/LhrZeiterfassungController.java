package com.ibosng.lhrservice.controllers;

import com.ibosng.lhrservice.services.LHRUrlaubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/zeiterfassung")
@Tag(name = "Zeiterfassung controller")
@RequiredArgsConstructor
public class LhrZeiterfassungController {
    private final LHRUrlaubService lhrUrlaubService;

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @PostMapping("/syncLeistungserfassung")
    @Operation(
            summary = "Send leisterfassung for mitarbeiter",
            description = "Send leisterfassung for mitarbeiter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not Found personalnummer or leisterfassung"),
                    @ApiResponse(responseCode = "422", description = "Leisterfassung already processed/ User was absent on specified date"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    public ResponseEntity<?> sendLeistungerfassung(@RequestParam Integer personalnummerId,
                                                   @RequestParam String leistungDatum) {
        return lhrUrlaubService.sendLeistungsdatumToLhr(personalnummerId, leistungDatum);
    }

    /*
     * IMPORTANT: Do not remove this endpoint. It is still used by the backward sync service
     */
    @DeleteMapping("/syncLeistungserfassung")
    @Operation(
            summary = "Delete leisterfassung for mitarbeiter",
            description = "Delete leisterfassung for mitarbeiter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not Found personalnummer or leisterfassung"),
                    @ApiResponse(responseCode = "422", description = "Leisterfassung not synced"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    public ResponseEntity<?> deleteLeistungerfassung(@RequestParam Integer personalnummerId,
                                                     @RequestParam String leistungDatum) {
        return lhrUrlaubService.deleteLeistungsdatumToLhr(personalnummerId, leistungDatum);
    }
}
