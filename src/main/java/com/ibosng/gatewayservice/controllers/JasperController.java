package com.ibosng.gatewayservice.controllers;

import com.ibosng.dbservice.dtos.ReportDto;
import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.reports.ReportType;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.JasperReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.FN_REPORTS;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@RestController
@RequestMapping("/jasper")
@Tag(name = "Jasper controller")
public class JasperController {

    private final JasperReportService jasperReportService;
    private final BenutzerDetailsService benutzerDetailsService;


    public JasperController(JasperReportService jasperReportService, BenutzerDetailsService benutzerDetailsService) {
        this.jasperReportService = jasperReportService;
        this.benutzerDetailsService = benutzerDetailsService;
    }

    @PostMapping(value = "/uploadTemplate")
    @Operation(
            summary = "Uploads a Jasper template file.",
            description = "This endpoint allows uploading a Jasper template file along with the report name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public void uploadJasperTemplate(@RequestPart(name = "file", required = false) MultipartFile file, String reportName) throws IOException {
        jasperReportService.uploadReportTemplate(file, reportName);
    }

    @PostMapping(value = "/uploadTemplates")
    @Operation(
            summary = "Uploads a Jasper report with multiple files.",
            description = "This endpoint allows uploading a Jasper template consisting of multiple files",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public void uploadJasperTemplates(
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @ModelAttribute(name = "params") List<ReportParameterDto> reportParams,
            @RequestPart(name = "reportName", required = true) String reportName) throws IOException {
        for (MultipartFile file : files) {
            jasperReportService.uploadReportTemplate(file, reportName);
        }
    }

    @PostMapping(value = "/saveReport")
    @Operation(
            summary = "Creates or updates a Jasper report in the DB.",
            description = "This endpoint allows uploading a Jasper template consisting of multiple files",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PayloadResponse> saveReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody ReportDto reportDto) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        PayloadResponse response = jasperReportService.createReport(reportDto, benutzerDetailsService.getUserFromToken(token).getEmail());
        return checkResultIfNull(response);
    }


    @GetMapping("/reports")
    @Operation(
            summary = "Retrieve a list of reports.",
            description = "This endpoint returns a list of reports.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PayloadResponse> getReports(@RequestParam(required = true) String reportType) {
        return ResponseEntity.ok(jasperReportService.getReports(ReportType.fromValue(reportType)));
    }


    @PostMapping("/generate/report")
    @Operation(
            summary = "Generate a PDF report for Benutzer.",
            description = "This endpoint generates a PDF report for Benutzer and returns the PDF file as bytes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity generateReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody ReportRequestDto reportRequestDto) {
        log.info("Received request to generate report: {}", reportRequestDto);
        //TODO Check for correct permissions
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, List.of(FN_REPORTS))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Fetch and set benutzer info
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        reportRequestDto.setCreatedBy(benutzerDetailsService != null ? benutzer.getEmail() : null);

        ReportResponse reportResponse = jasperReportService.generateReport(reportRequestDto);

        if (reportResponse == null) {
            log.error("Report generation failed: jasperReportService returned null.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Report generation failed.");
        }

        log.info("Report generated successfully: {}", reportResponse.getReportName());

        // Set Media Type for FE
        MediaType mediaType;
        switch (reportResponse.getOutputFormat()) {
            case PDF:
                mediaType = MediaType.APPLICATION_PDF;
                break;
            case XLS:
            case XLSX:
                mediaType = MediaType.parseMediaType("application/vnd.ms-excel");
                break;
            case CSV:
                mediaType = MediaType.TEXT_PLAIN;
                break;
            default:
                log.warn("Unsupported report format requested: {}", reportResponse.getOutputFormat());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Unsupported report format: " + reportResponse.getOutputFormat());
        }

        log.info("Report format determined: {}", mediaType);

        // Set the HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("filename", reportResponse.getReportName() + "." + reportResponse.getOutputFormat().getValue());

        log.info("Returning report response with headers: {}", headers);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(reportResponse.getReportBytes());
    }

    /*@PostMapping("/generate/dvtest")
    @Operation(
            summary = "Generate a PDF report for Benutzer.",
            description = "This endpoint generates a PDF report for Benutzer and returns the PDF file as bytes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<byte[]> generateDvTest(@RequestParam(value = "personalnummer")String personalnummer,
                                                 @RequestBody ReportRequestDto reportRequestDto) throws Exception {

        byte[] pdfBytes = jasperReportService.generateDv(personalnummer, reportRequestDto);

        // Set the HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "generatedReport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }*/

    @GetMapping("/report/parameters")
    @Operation(
            summary = "Get the parameters for a report.",
            description = "This endpoint returns a list of all the parameters and their types required for a report",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<PayloadResponse> getReportParameters(@RequestParam(value = "reportName") String reportName) {
        PayloadResponse reportParameters = jasperReportService.getReportParameters(reportName);
        return ResponseEntity
                .ok()
                .body(reportParameters);
    }

}
