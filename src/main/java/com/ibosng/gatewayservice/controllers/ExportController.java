package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.FileExportService;
import com.ibosng.gatewayservice.services.Teilnehmerservice;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/export")
@Tag(name = "Teilnehmer controller")
@AllArgsConstructor
public class ExportController {

    private static final String FN_TEILNEHMERINNEN_LESEN = "FN_TEILNEHMERINNEN_LESEN";
    private final BenutzerDetailsService benutzerDetailsService;
    private final Teilnehmerservice teilnehmerservice;
    private final FileExportService fileExportService;

    @GetMapping("/getPruefungExport")
    public ResponseEntity getTeilnehmerPruefungCsv(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                   @RequestParam(required = false) String identifiersString,
                                                   @RequestParam(required = false) String seminarName,
                                                   @RequestParam(required = false) String projectName,
                                                   @RequestParam(required = false) boolean isActive,
                                                   @RequestParam(required = false) Boolean isUebaTeilnehmer,
                                                   @RequestParam(defaultValue = "true") boolean isAngemeldet,
                                                   @RequestParam(required = false) String geschlecht,
                                                   @RequestParam(required = false) String sortProperty,
                                                   @RequestParam(required = false) String sortDirection,
                                                   @RequestParam(required = false) String outputFormat) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Collections.singletonList(FN_TEILNEHMERINNEN_LESEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

//        log.info("Received request to generate pruefung export for: {}", reportRequestDto);

        ReportResponse reportResponse = null;
        if(outputFormat.equalsIgnoreCase("CSV")){
            List<PruefungCsvDto> pruefungTeilnehmer = teilnehmerservice.getTeilnehmerPruefungListCsv(identifiersString, seminarName, projectName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, sortProperty, sortDirection);
            if(pruefungTeilnehmer.isEmpty()){
                log.error("No Teilnehmers found");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No Teilnehmers found for request.");
            }
            reportResponse = fileExportService.exportToCsv(pruefungTeilnehmer);
        } else if(outputFormat.equalsIgnoreCase("XLSX")){
            List<PruefungXlsxDto> pruefungTeilnehmer = teilnehmerservice.getTeilnehmerPruefungListXlsx(identifiersString, seminarName, projectName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, sortProperty, sortDirection);
            if(pruefungTeilnehmer.isEmpty()){
                log.error("No Teilnehmers found");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No Teilnehmers found for request.");
            }
            reportResponse = fileExportService.exportToXlsx(pruefungTeilnehmer);
        }

        if (reportResponse == null) {
            log.error("CSV Export failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CSV Export generation failed.");
        }


        // Set Media Type for FE
        MediaType mediaType;
        switch (reportResponse.getOutputFormat()) {
            case CSV:
                mediaType = MediaType.TEXT_PLAIN;
                break;
            case XLSX:
                mediaType = MediaType.parseMediaType("application/vnd.ms-excel");
                break;
            default:
                log.warn("Unsupported Export format requested: {}", reportResponse.getOutputFormat());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Unsupported Export format: " + reportResponse.getOutputFormat());
        }

        log.info("Export format determined: {}", mediaType);

        // Set the HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        //TODO Set proper name
        headers.setContentDispositionFormData("filename", reportResponse.getReportName() + "." + reportResponse.getOutputFormat().getValue());

        log.info("Returning export response with headers: {}", headers);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(reportResponse.getReportBytes());


    }
}
