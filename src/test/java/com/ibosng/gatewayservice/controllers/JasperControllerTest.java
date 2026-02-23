package com.ibosng.gatewayservice.controllers;


import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportType;
import com.ibosng.gatewayservice.services.impl.JasperReportServiceImpl;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JasperControllerTest {

    @Mock
    private JasperReportServiceImpl jasperReportService;

    @InjectMocks
    private JasperController jasperController;

    @Test
    public void testUploadJasperTemplate() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "report.jrxml", MediaType.TEXT_XML_VALUE, "template content".getBytes());
        String reportName = "Sample Report";

        jasperController.uploadJasperTemplate(file, reportName);

        verify(jasperReportService, times(1)).uploadReportTemplate(file, reportName);
    }

    @Test
    public void testUploadJasperTemplateThrowsIOException() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "report.jrxml", MediaType.TEXT_XML_VALUE, "template content".getBytes());
        String reportName = "Sample Report";
        doThrow(new IOException("IO Exception")).when(jasperReportService).uploadReportTemplate(file, reportName);

        assertThrows(IOException.class, () -> {
            jasperController.uploadJasperTemplate(file, reportName);
        });

        verify(jasperReportService, times(1)).uploadReportTemplate(file, reportName);
    }



    @Test
    @Disabled
    public void testGetReportsInternalServerError() {
        when(jasperReportService.getReports(ReportType.MANUAL)).thenThrow(new RuntimeException("Internal server error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jasperController.getReports(String.valueOf(ReportType.MANUAL));
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(jasperReportService, times(1)).getReports(ReportType.MANUAL);
    }


    private Report createReport(String reportName){
        Report report = new Report();
        report.setReportName(reportName);

        return report;
    }

}
