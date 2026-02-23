package com.ibosng.gatewayservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home controller")
public class HomeController {

    @GetMapping("/probe")
    @Operation(
            summary = "This method is used to test the availability of the endpoint.",
            description = "This endpoint is used to verify if the ibosng-backend is reachable.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "401", description = "Token expired"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            })
    public String home() {
        return "This is the test home for the ibosng-backend!!!";
    }
}
