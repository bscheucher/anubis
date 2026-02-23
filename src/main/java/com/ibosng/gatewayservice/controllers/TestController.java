package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.moxisservice.clients.MoxisClient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Profile({"localdev","test"})
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    private final MoxisClient moxisClient;
    private final MailService mailService;

    public TestController(MoxisClient moxisClient, MailService mailService) {
        this.moxisClient = moxisClient;
        this.mailService = mailService;
    }

    @GetMapping("/moxis")
    @Operation(
            summary = "Test Moxis Connection",
            description = "To be used for testing the connection to Moxis during local devlopment." +
                    "As Moxis doesn't offer a 'status endpoint' or similar, the .lookupJobStates operation is used.")
    public ResponseEntity<List<MoxisJobStateDto>> testMoxisConnection() {
        return moxisClient.testConnection();
    }

    @GetMapping("/mail")
    @Operation(
            summary = "Test Moxis Connection",
            description = "To be used for testing the connection to Moxis during local devlopment." +
                    "As Moxis doesn't offer a 'status endpoint' or similar, the .lookupJobStates operation is used.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void testSendingMail() {
        mailService.sendEmail(
                "gateway-service.hr.neuer-ma-abgelehnt",
                "german",
                null,
                new String[0],
                toObjectArray(),
                toObjectArray("Test Name")
        );
    }

}
