package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.ReportDto;
import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungReportParameter;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportDataSource;
import com.ibosng.dbservice.entities.reports.ReportParameter;
import com.ibosng.dbservice.entities.reports.ReportType;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.services.ReportParameterService;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.impl.ReportServiceImpl;
import com.ibosng.dbservice.services.jasper.DienstvertragDataService;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.utils.Mappers;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.enums.ReportOutputFormat;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.JasperReportService;
import com.ibosng.gatewayservice.services.WorkflowHelperService;
import com.ibosng.gatewayservice.utils.Parsers;
import com.ibosng.microsoftgraphservice.services.BlobStorageService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFStartService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JasperReportServiceImpl implements JasperReportService {

    @Getter
    @Value("${storageContainerJasperReports:#{null}}")
    private String jasperContainer;


    private final DataSource mariaDbDataSource;

    private final DataSource postgresDataSource;

    private final DataSource lhrDataSource;

    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    private static final String ZUSATZVEREINBARUNGEN = "zusatzvereinbarungen";
    private static final String UNSIGNED = "Nicht unterschrieben";


    private final BlobStorageService blobStorageService;

    private final ReportServiceImpl reportService;

    private final ReportParameterService reportParameterService;

    private final DienstvertragDataService dienstvertragDataService;
    private final FileShareService fileShareService;

    private final VereinbarungService vereinbarungService;

    private final VertragsdatenService vertragsdatenService;

    private final IbisFirmaService ibisFirmaService;

    private final PersonalnummerService personalnummerService;

    private final StammdatenService stammdatenService;

    private final ManageWFsService manageWFsService;
    private final WorkflowHelperService workflowHelperService;
    private final ManageWFItemsService manageWFItemsService;
    private final BenutzerDetailsService benutzerDetailsService;

    private final WFStartService wfStartService;

    public JasperReportServiceImpl(@Qualifier("mariaDbDataSource") DataSource mariaDbDataSource,
                                   @Qualifier("postgresDataSource") DataSource postgresDataSource,
                                   @Qualifier("lhrDataSource") DataSource lhrDataSource,
                                   BlobStorageService blobStorageService,
                                   ReportServiceImpl reportService,
                                   ReportParameterService reportParameterService,
                                   DienstvertragDataService dienstvertragDataService,
                                   FileShareService fileShareService,
                                   VereinbarungService vereinbarungService,
                                   VertragsdatenService vertragsdatenService,
                                   IbisFirmaService ibisFirmaService,
                                   PersonalnummerService personalnummerService,
                                   StammdatenService stammdatenService,
                                   ManageWFsService manageWFsService,
                                   WorkflowHelperService workflowHelperService,
                                   ManageWFItemsService manageWFItemsService,
                                   BenutzerDetailsService benutzerDetailsService,
                                   WFStartService wfStartService) {
        this.mariaDbDataSource = mariaDbDataSource;
        this.postgresDataSource = postgresDataSource;
        this.lhrDataSource = lhrDataSource;
        this.blobStorageService = blobStorageService;
        this.reportService = reportService;
        this.reportParameterService = reportParameterService;
        this.dienstvertragDataService = dienstvertragDataService;
        this.fileShareService = fileShareService;
        this.vereinbarungService = vereinbarungService;
        this.vertragsdatenService = vertragsdatenService;
        this.ibisFirmaService = ibisFirmaService;
        this.personalnummerService = personalnummerService;
        this.stammdatenService = stammdatenService;
        this.manageWFsService = manageWFsService;
        this.workflowHelperService = workflowHelperService;
        this.manageWFItemsService = manageWFItemsService;
        this.benutzerDetailsService = benutzerDetailsService;
        this.wfStartService = wfStartService;
    }

    @Override
    public void uploadReportTemplate(MultipartFile file, String reportName) throws IOException {
        if (file != null && !file.isEmpty()) {
            String blobName = reportName.replace(' ', '_') + '.' + StringUtils.getFilenameExtension(file.getOriginalFilename());
            InputStream data = file.getInputStream();
            long length = file.getSize();
            blobStorageService.uploadOrReplaceJasperReport(blobName, data, length);
            Report report = new Report();
            report.setSourcePath(blobName);
            report.setReportName(reportName);
            reportService.save(report);
        }
    }

    @Override
    public PayloadResponse getReports(ReportType reportType) {
        List<Report> reports = reportService.findAllByReportType(reportType);
        // Clear blob name before returning
        for (Report report : reports) {
            report.setMainReportFile(null);
            report.setId(null);
            report.setSourcePath(null);
        }

        PayloadResponse response = new PayloadResponse();

        PayloadTypeList<ReportDto> reportDtoPayloadType = new PayloadTypeList<>(PayloadTypes.REPORT.getValue());
        List<ReportDto> reportDtos = reports.stream()
                .map(this::mapReportToDto)
                .collect(Collectors.toList());

        reportDtoPayloadType.setAttributes(reportDtos);
        response.setSuccess(true);
        response.setData(Arrays.asList(reportDtoPayloadType));
        return response;
    }


    public static void cleanUp(Path directory) throws IOException {
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


    public static ClassLoader addFolderToClasspath(ClassLoader parent, Path folder) throws Exception {
        URL folderUrl = folder.toUri().toURL();
        return new URLClassLoader(new URL[]{folderUrl}, parent);
    }

    private Vereinbarung saveVereinbarung(ReportRequestDto reportRequestDto, ReportResponse reportResponse, WWorkflow workflowMoxis) {
        //TODO Set createdBy
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(reportRequestDto.getPersonalnummer());
        List<Vertragsdaten> vertragsdaten = vertragsdatenService.findByPersonalnummerString(reportRequestDto.getPersonalnummer());
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(reportRequestDto.getPersonalnummer());
        if (personalnummer == null) {
            log.error("No Personalnummer found for: " + reportRequestDto.getPersonalnummer());
            return null;
        }
        if (stammdaten == null) {
            log.error("No Stammdaten found for: " + reportRequestDto.getPersonalnummer());
            return null;
        }
        Vereinbarung vereinbarung = new Vereinbarung();
        vereinbarung.setPersonalnummer(personalnummer);
        vereinbarung.setFirma(personalnummer.getFirma());
        vereinbarung.setVereinbarungName(reportRequestDto.getReportName());
        vereinbarung.setStatus(VereinbarungStatus.NEW);
        vereinbarung.setWorkflow(workflowMoxis);
        if (!vertragsdaten.isEmpty()) {
            Benutzer fuehrungskraft = vertragsdaten.get(0).getFuehrungskraft();
            vereinbarung.setFuehrungskraft(fuehrungskraft.getFirstName() + " " + fuehrungskraft.getLastName());
        } else {
            log.error("No Vertragsdaten found for Personalnummer: " + reportRequestDto.getPersonalnummer());
        }

        // Create and add report parameters
        List<VereinbarungReportParameter> parameters = new ArrayList<>();
        for (ReportParameterDto paramDto : reportRequestDto.getReportParameters()) {
            VereinbarungReportParameter param = new VereinbarungReportParameter();
            param.setVereinbarung(vereinbarung);
            param.setName(paramDto.getName());
            param.setValue(paramDto.getValue());
            param.setType(paramDto.getType());
            parameters.add(param);
        }
        // Associate parameters with Vereinbarung
        vereinbarung.setParameters(parameters);
        if (reportRequestDto.getGueltigAb() != null) {
            vereinbarung.setGueltigAb(Parsers.parseDate(reportRequestDto.getGueltigAb()));
        }
        String filename = reportResponse.getReportName() + "." + reportResponse.getOutputFormat().getValue();
        vereinbarung.setVereinbarungFile(filename);
        vereinbarung = vereinbarungService.save(vereinbarung);
        saveVereinbarungFile(vereinbarung, stammdaten, reportResponse);
        return vereinbarung;
    }


    private void saveVereinbarungFile(Vereinbarung vereinbarung, Stammdaten stammdaten, ReportResponse reportResponse) {
        String directoryPath = fileShareService.getVereinbarungenDirectory(vereinbarung.getPersonalnummer().getPersonalnummer(), stammdaten);
        // Use "/" as separator since we are refering to the AZ file share
        directoryPath += "/" + UNSIGNED;

        String filename = reportResponse.getReportName() + "." + reportResponse.getOutputFormat().getValue();

        InputStream pdfInputStream = new ByteArrayInputStream(reportResponse.getReportBytes());
        fileShareService.uploadOrReplaceInFileShare(
                getFileSharePersonalunterlagen(),
                directoryPath,
                filename,
                pdfInputStream,
                (long) reportResponse.getReportBytes().length
        );
    }


    @Override
    public ReportResponse generateReport(ReportRequestDto reportRequestDto) {
        log.info("Generating report: {}", reportRequestDto.getReportName());

        Report report = reportService.findByReportName(reportRequestDto.getReportName());
        if (report == null) {
            log.error("No report found for name: " + reportRequestDto.getReportName());
            return null;
        }
        log.debug("Report found: {}", report.getReportName());


        DataSource selectedDataSource = selectDataSource(report.getDataSource().getValue());
        log.debug("Selected data source: {}", report.getDataSource().getValue());

        // Get the requested output format
        ReportOutputFormat outputFormat;
        try {
            outputFormat = ReportOutputFormat.fromValue(reportRequestDto.getOutputFormat());
        } catch (IllegalArgumentException e) {
            log.error("Invalid report output format: " + reportRequestDto.getOutputFormat(), e);
            return null;
        }

        log.debug("Report output format: {}", outputFormat);

        String shareName = "reports";
        String remoteDirectory = report.getSourcePath();
        Path tempDir;
        Connection connection = DataSourceUtils.getConnection(selectedDataSource);
        log.debug("Database connection established.");

        try {

            tempDir = Files.createTempDirectory("jasperReports");
            log.debug("Temporary directory created: {}", tempDir);

            fileShareService.downloadFiles(remoteDirectory, tempDir, shareName);
            log.debug("Report files downloaded from remote directory: {}", remoteDirectory);

            ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader newClassLoader = addFolderToClasspath(Thread.currentThread().getContextClassLoader(), tempDir);
            Thread.currentThread().setContextClassLoader(newClassLoader);
            log.debug("ClassLoader updated for report compilation.");

            Path reportFile = tempDir.resolve(report.getMainReportFile());
            InputStream inputStream = Files.newInputStream(reportFile);
            log.debug("Report file loaded: {}", reportFile);

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            log.debug("Jasper report compiled successfully.");

            Map<String, Object> parameters = new HashMap<>();
            if (reportRequestDto.getReportParameters() != null) {
                for (ReportParameterDto param : reportRequestDto.getReportParameters()) {
                    parameters.put(param.getName(), Mappers.mapReportParameter(param));
                }
                log.debug("Report parameters mapped: {}", parameters.keySet());
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            log.debug("Jasper report filled with data.");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            switch (outputFormat) {
                case PDF -> {
                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                    log.debug("Report exported to PDF.");
                }
                case XLS, XLSX -> {
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    exporter.exportReport();
                    log.debug("Report exported to Excel.");
                }
                case CSV -> {
                    JRCsvExporter exporter = new JRCsvExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                    exporter.exportReport();
                    log.debug("Report exported to CSV.");
                }
            }

            Thread.currentThread().setContextClassLoader(originalClassLoader);
            cleanUp(tempDir);
            log.debug("Cleaned up temporary files.");

            byte[] reportBytes = outputStream.toByteArray();
            log.info("Report generation successful. Report size: {} bytes", reportBytes.length);

            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setReportBytes(reportBytes);
            reportResponse.setReportName(reportRequestDto.getReportName());
            reportResponse.setOutputFormat(outputFormat);


            return reportResponse;

        } catch (Exception e) {
            log.error("Error generating report: " + e.getMessage(), e);
        } finally {
            DataSourceUtils.releaseConnection(connection, selectedDataSource);
            log.debug("Database connection released.");
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
        } else if ("lhrDb".equalsIgnoreCase(dataSourceName)) {
            return lhrDataSource;
        } else {
            throw new IllegalArgumentException("Unknown DataSource: " + dataSourceName);
        }
    }


    @Override
    public PayloadResponse getReportParameters(String reportName) {
        log.info("Fetching parameters for report: " + reportName);
        Report report = reportService.findByReportName(reportName);
        if (report == null) {
            log.error("No report found for name: " + reportName);
            return null;
        }
        List<ReportParameter> reportParams = reportParameterService.findAllByReport(report);
        if (reportParams.isEmpty()) {
            log.warn("No report params found for report: " + reportName);
            return null;
        }
        List<ReportParameterDto> reportParameterDtos = new ArrayList<>();
        for (ReportParameter reportParameter : reportParams) {
            reportParameterDtos.add(mapReportParameterToDto(reportParameter));
        }
        PayloadResponse response = new PayloadResponse();

        PayloadTypeList<ReportParameterDto> reportDtoPayloadType = new PayloadTypeList<>(PayloadTypes.REPORT_PARAMETERS.getValue());


        reportDtoPayloadType.setAttributes(reportParameterDtos);
        response.setSuccess(true);
        response.setData(Arrays.asList(reportDtoPayloadType));
        return response;
    }

    @Override
    public PayloadResponse createReport(ReportDto reportDto, String createdBy) {
        PayloadResponse response = new PayloadResponse();
        List<ReportParameter> reportParameters = new ArrayList<>();

        // Create the Report object
        Report report = new Report();
        report.setReportName(reportDto.getReportName());
        report.setSourcePath(reportDto.getSourcePath());
        report.setMainReportFile(reportDto.getMainReportFile());
        report.setDataSource(ReportDataSource.fromValue(reportDto.getDataSource()));
        report.setCreatedOn(LocalDateTime.now());
        report.setCreatedBy(createdBy);

        for (ReportParameterDto reportParameterDto : reportDto.getReportParameters()) {
            ReportParameter reportParameter = mapDtoToReportParameter(reportParameterDto);
            reportParameter.setReport(report);
            // Associate each ReportParameter with the Report
            reportParameters.add(reportParameter);
        }
        // Set the report parameters
        report.setReportParameters(reportParameters);

        // Save the Report object
        report = reportService.save(report);
        PayloadTypeList<ReportDto> reportDtoPayloadType = new PayloadTypeList<>(PayloadTypes.REPORT.getValue());

        reportDtoPayloadType.setAttributes(Collections.singletonList(mapReportToDto(report)));
        response.setSuccess(true);
        response.setData(Arrays.asList(reportDtoPayloadType));
        return response;
    }

    private void storePDF(byte[] pdfBytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
    }

    private ReportParameterDto mapReportParameterToDto(ReportParameter reportParameter) {
        if (reportParameter == null) {
            return null;
        }
        ReportParameterDto reportParameterDto = new ReportParameterDto();
        reportParameterDto.setName(reportParameter.getName());
        reportParameterDto.setType(reportParameter.getType());
        reportParameterDto.setDescription(reportParameter.getDescription());
        reportParameterDto.setLabel(reportParameter.getLabel());
        reportParameterDto.setRequired(reportParameter.getRequired());
        reportParameterDto.setValue(reportParameter.getDefaultValue());

        return reportParameterDto;
    }

    private ReportParameter mapDtoToReportParameter(ReportParameterDto reportParameterDto) {
        if (reportParameterDto == null) {
            return null;
        }
        ReportParameter reportParameter = new ReportParameter();
        reportParameter.setName(reportParameterDto.getName());
        reportParameter.setType(reportParameterDto.getType());
        reportParameter.setDescription(reportParameterDto.getDescription());
        reportParameter.setLabel(reportParameterDto.getLabel());
        reportParameter.setRequired(reportParameterDto.getRequired());

        return reportParameter;
    }

    private ReportDto mapReportToDto(Report report) {
        if (report == null) {
            return null;
        }
        List<ReportParameterDto> reportParameterDtos = report.getReportParameters().stream()
                .map(this::mapReportParameterToDto)
                .collect(Collectors.toList());

        ReportDto reportDto = new ReportDto();
        reportDto.setId(report.getId());
        reportDto.setReportName(report.getReportName());
        reportDto.setSourcePath(report.getSourcePath());
        reportDto.setMainReportFile(report.getMainReportFile());
        reportDto.setReportParameters(reportParameterDtos);

        return reportDto;
    }

    private Report mapDtoToReport(ReportDto reportDto) {
        if (reportDto == null) {
            return null;
        }
        List<ReportParameter> reportParameters = reportDto.getReportParameters().stream()
                .map(this::mapDtoToReportParameter)
                .collect(Collectors.toList());

        Report report = new Report();
        report.setId(reportDto.getId());
        report.setReportName(reportDto.getReportName());
        report.setSourcePath(reportDto.getSourcePath());
        report.setMainReportFile(reportDto.getMainReportFile());
        report.setReportParameters(reportParameters);

        // Set bidirectional relationship if necessary
        reportParameters.forEach(param -> param.setReport(report));

        return report;
    }


}
