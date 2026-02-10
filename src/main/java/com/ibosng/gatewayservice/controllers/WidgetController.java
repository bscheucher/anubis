package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.gatewayservice.services.WidgetDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/widget")
@Tag(name = "Widget controller")
public class WidgetController {

    private final WidgetDataService widgetDataService;

    public WidgetController(WidgetDataService widgetDataService) {
        this.widgetDataService = widgetDataService;
    }

    @Operation(
            summary = "This method is used to get data for Meine Seminare widget.",
            description = "Returns an object containing the data for the Meine Seminare widget.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/getMeineSeminare")
    public ResponseEntity<Object> getMeineSeminare(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return new ResponseEntity<>(widgetDataService.getMeineSeminare(token), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used to get data for Fehlerhafte Teilnehmer widget.",
            description = "Returns an object containing the data for the Fehlerhafte Teilnehmer widget.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/getFehlerhafteTeilnehmer")
    public ResponseEntity<Object> getFehlerhafteTeilnehmer(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam LocalDate date) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return new ResponseEntity<>(widgetDataService.getFehlerhafteTeilnehmer(date), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used to get data for Meine Persoenlichen Daten widget.",
            description = "Returns an object containing the data for the Meine Persoenlichen Daten widget.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/getMeinePersoenlichenDaten")
    public ResponseEntity<Object> getMeinePersoenlichenDatenWidgetData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return new ResponseEntity<>(widgetDataService.getMeinePersoenlichenDatenWidgetData(token), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used to get data for the project controlling widget.",
            description = "Returns an object containing the data for the project controlling widget.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/controlling/getProjectsForUser")
    public ResponseEntity<List<BasicProjectDto>> getProjectsForUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam Boolean isActive,
            @RequestParam Optional<Boolean> isInTheFuture) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return new ResponseEntity<>(widgetDataService.findProjectsForUser(token, isActive, isInTheFuture), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used to get data for the project controlling widget.",
            description = "Returns an object containing the data for the project controlling widget.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/controlling/getRevenueForProject")
    public ResponseEntity<ProjectRevenueDataDto> getRevenueForProject(
            @RequestParam Integer projectNumber,
            @RequestParam LocalDate von,
            @RequestParam LocalDate bis,
            @RequestParam Boolean isProjectStartToToday,
            @RequestParam Boolean isTodayToProjectEnd) {
        ProjectRevenueDataDto result = widgetDataService.findRevenueForKPI(projectNumber, von, bis, isProjectStartToToday, isTodayToProjectEnd);
        return checkResultIfNull(result);
    }

    @Operation(
            summary = "This method is used to get data for the project controlling widget.",
            description = "Returns an object containing the data for the project controlling widget.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @GetMapping("/controlling/getForecastForProject")
    public ResponseEntity<ProjectForecastDataDto> getForecastForProject(
            @RequestParam Integer projectNumber) {
        ProjectForecastDataDto result = widgetDataService.findForecastForKPI(projectNumber);
        return checkResultIfNull(result);
    }
}
