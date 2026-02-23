package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.dashboards.AllDashboardsDto;
import com.ibosng.gatewayservice.dtos.dashboards.DashboardDto;
import com.ibosng.gatewayservice.services.DashboardDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/dashboards")
@Tag(name = "Dashboards controller")
public class DashboardController {

    private final DashboardDetailsService dashboardDetailsService;

    public DashboardController(DashboardDetailsService dashboardDetailsService) {
        this.dashboardDetailsService = dashboardDetailsService;
    }

    @GetMapping("/get")
    @Operation(
            summary = "This method is used to get all dashboard informations.",
            description = "Returns a dto containing all dashboard informations and the favourite dashboard.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public ResponseEntity<AllDashboardsDto> getAllDashboards(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return new ResponseEntity<>(dashboardDetailsService.getAllDashboards(token), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used to save a dashboard.",
            description = "Returns the saved dashboard dto.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "201", description = "Success, dashboard saved"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/save")
    public ResponseEntity<DashboardDto> saveDashboard(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody DashboardDto dashboardDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return new ResponseEntity<>(dashboardDetailsService.saveDashboard(token, dashboardDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "This method is used to get delete a dashboard.",
            description = "Returns string containing the outcome.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDashboard( @RequestParam Integer dashboardId) {
        dashboardDetailsService.deleteDashboard(dashboardId);
        return new ResponseEntity<>(dashboardDetailsService.deleteDashboard(dashboardId), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used to set a favourite dashboard.",
            description = "Returns string containing the outcome.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "201", description = "Success, favourite dashboard set"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    @PostMapping("/favouriteDashboard")
    public ResponseEntity<String> setFavouriteDashboard(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam Integer dashboardId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        return new ResponseEntity<>(dashboardDetailsService.setFavouriteDashboard(token, dashboardId), HttpStatus.OK);
    }
}
