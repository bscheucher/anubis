package com.ibosng.gatewayservice.services.impl;


import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.services.impl.ReportServiceImpl;
import com.ibosng.dbservice.utils.Mappers;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.enums.ReportOutputFormat;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JasperReportServicePrototype {

    @Qualifier("mariaDbDataSource")
    private final DataSource mariaDbDataSource;

    @Qualifier("postgresDataSource")
    private final DataSource postgresDataSource;


    private final ReportServiceImpl reportService;

    private final FileShareService fileShareService;


    public JasperReportServicePrototype(@Qualifier("mariaDbDataSource") DataSource mariaDbDataSource, @Qualifier("postgresDataSource") DataSource postgresDataSource, ReportServiceImpl reportService, FileShareService fileShareService) {
        this.mariaDbDataSource = mariaDbDataSource;
        this.postgresDataSource = postgresDataSource;
        this.reportService = reportService;
        this.fileShareService = fileShareService;
    }


    public ReportResponse generateReport(ReportRequestDto reportRequestDto) throws Exception {
        // Step 1: Select the correct DataSource
        String dataSourceName = "mariaDb";


        // Step 2: Extract & Validate SQL Query (Optional Security Check)
        /*String query = JasperReportUtils.extractSqlQuery(reportPath);
        if (query != null && !JasperQueryValidator.isQuerySafe(query)) {
            throw new SecurityException("Unsafe SQL detected in report!");
        }*/


        Report report = reportService.findByReportName(reportRequestDto.getReportName());
        if (report == null) {
            log.error("No report found for name: " + reportRequestDto.getReportName());
            return null;
        }

        DataSource selectedDataSource = selectDataSource(report.getDataSource().getValue());

        // Get the requested output format
        ReportOutputFormat outputFormat;
        try {
            outputFormat = ReportOutputFormat.fromValue(reportRequestDto.getOutputFormat());
        } catch (IllegalArgumentException e) {
            log.error("Invalid report output format: " + reportRequestDto.getOutputFormat());
            return null;
        }


        // TODO: Replace hardcoded values
        String shareName = "reports";
        String remoteDirectory = report.getSourcePath();
        Path tempDir;
        // Get a connection from the selected DataSource
        Connection connection = DataSourceUtils.getConnection(selectedDataSource);
        try {
            tempDir = Files.createTempDirectory("jasperReports");

            fileShareService.downloadFiles(remoteDirectory, tempDir, shareName);

            // Fetch original class loader
            ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();

            // New class loader to access downloaded report templates
            ClassLoader newClassLoader = addFolderToClasspath(Thread.currentThread().getContextClassLoader(), tempDir);

            // Replace original class loader with the new one
            Thread.currentThread().setContextClassLoader(newClassLoader);
            Path reportFile = tempDir.resolve(report.getMainReportFile());
            InputStream inputStream = Files.newInputStream(reportFile);

            // Compile the Jasper Report
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // Extract and validate the query
            //TODO update validation
            String reportSqlQuery = jasperReport.getQuery().getText();
            /*if (!JasperQueryValidator.isQuerySafe(reportSqlQuery)) {
                throw new SecurityException("Unsafe SQL detected in report!");
            }*/

            //Set report Params
            Map<String, Object> parameters = new HashMap<>();
            if (reportRequestDto.getReportParameters() != null) {
                for (ReportParameterDto param : reportRequestDto.getReportParameters()) {
                    parameters.put(param.getName(), Mappers.mapReportParameter(param));
                }
            }


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Export based on format
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            switch (outputFormat) {
                case PDF -> JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                case XLS, XLSX -> {
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    exporter.exportReport();
                }
                case CSV -> {
                    JRCsvExporter exporter = new JRCsvExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                    exporter.exportReport();
                }
            }

            // Reset class loader and cleanup
            Thread.currentThread().setContextClassLoader(originalClassLoader);
            cleanUp(tempDir);

            byte[] reportBytes = outputStream.toByteArray();

            // Set response data
            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setReportBytes(reportBytes);
            reportResponse.setReportName(reportRequestDto.getReportName());
            reportResponse.setOutputFormat(outputFormat);
            return reportResponse;

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            // Step 7: Release the database connection
            DataSourceUtils.releaseConnection(connection, selectedDataSource);
        }
        return null;
    }

    /**
     * Selects the appropriate DataSource based on the input name.
     */
    private DataSource selectDataSource(String dataSourceName) {
        if ("mariaDb".equalsIgnoreCase(dataSourceName)) {
            return mariaDbDataSource;
        } else if ("postgres".equalsIgnoreCase(dataSourceName)) {
            return postgresDataSource;
        } else {
            throw new IllegalArgumentException("Unknown DataSource: " + dataSourceName);
        }
    }

    public static ClassLoader addFolderToClasspath(ClassLoader parent, Path folder) throws Exception {
        URL folderUrl = folder.toUri().toURL();
        return new URLClassLoader(new URL[]{folderUrl}, parent);
    }


    private void storePDF(byte[] pdfBytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
    }

    private static void cleanUp(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public java.nio.file.FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws java.io.IOException {
                Files.delete(file);
                return java.nio.file.FileVisitResult.CONTINUE;
            }

            @Override
            public java.nio.file.FileVisitResult postVisitDirectory(Path dir, java.io.IOException exc) throws java.io.IOException {
                Files.delete(dir);
                return java.nio.file.FileVisitResult.CONTINUE;
            }
        });
    }
}
