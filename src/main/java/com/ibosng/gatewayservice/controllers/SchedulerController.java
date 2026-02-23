package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.services.ZeiterfassungGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sync")
@Tag(name = "Scheduler controller")
@RequiredArgsConstructor
@Profile({"development", "QA", "preProd"})
public class SchedulerController {

    private final ZeiterfassungGatewayService zeiterfassungGatewayService;

    @GetMapping("/abwesenheiten")
    @Operation(summary = "This method is used to trigger scheduler in lhr-service! it sync tage and saldo for existing abwesenheiten")
    public void syncAbwesenheiten() {
        zeiterfassungGatewayService.syncAbwesenheitData();
    }

    @GetMapping("/document")
    @Operation(summary = "This method is used to trigger scheduler in lhr-service! It saves documents from lhr into our share-point")
    public void syncDokument() {
        zeiterfassungGatewayService.syncDocumentData();
    }

    @GetMapping("/close-monaten")
    @Operation(summary = "This method is used to trigger scheduler in lhr-service! It saves documents from lhr into our share-point")
    public void closeMonaten() {
        zeiterfassungGatewayService.closeMonaten();
    }
}
