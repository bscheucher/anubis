package com.ibosng.fileimportservice.services.impl;

import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.fileimportservice.services.FileParserService;
import com.ibosng.fileimportservice.services.WFsFileImportService;
import com.ibosng.fileimportservice.services.fileservices.EAMSStandaloneService;
import com.ibosng.fileimportservice.services.fileservices.OEIFService;
import com.ibosng.fileimportservice.services.fileservices.TeilnehmerCsvService;
import com.ibosng.fileimportservice.services.fileservices.VHSService;
import com.ibosng.fileimportservice.services.impl.fileservices.CsvServiceImpl;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.ibosng.validationservice.services.ValidatorService;
import com.ibosng.workflowservice.dtos.WorkflowPayload;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.fileimportservice.utils.Constants.CSV_EXTENSION;
import static com.ibosng.fileimportservice.utils.Constants.EXCEL_EXTENSION;
import static com.ibosng.fileimportservice.utils.Helpers.*;
import static com.ibosng.microsoftgraphservice.utils.Helpers.deleteLocalFiles;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Profile("!(localdev | test)")
@Slf4j
@Service
@RequiredArgsConstructor
public class FileParserServiceImpl implements FileParserService {
    private final VHSService vhsService;
    private final CsvServiceImpl csvServiceImpl;
    private final OEIFService oeifService;
    private final EAMSStandaloneService eamsStandaloneService;
    private final SharePointService sharePointService;
    private final SharePointProperties sharePointProperties;
    private final MailService mailService;
    private final ValidatorService validatorService;
    private final WFsFileImportService WFsFileImportService;
    private final TeilnehmerCsvService teilnehmerCsvService;
    private final AzureSSOService azureSSOService;

    @Getter
    @Setter
    WWorkflowGroup workflowGroup;


    @Getter
    @Setter
    WorkflowPayload workflowPayload;

    @Override
    public void manageFiles(DriveItemCollectionPage items) {
        List<FileDetails> files = new ArrayList<>();
        for (DriveItem item : items.getCurrentPage()) {
            log.info("Startiing to manage the files.");
            String filename = getFilenameWithDate(item.name);
            sharePointService.moveFile(item.id, filename, sharePointProperties.getProcessing());
            File file = null;
            try {
                file = sharePointService.downloadFile(item.id, filename);
            } catch (MSGraphServiceException e) {
                log.error("Exception occurred while downloading the file with filename {}", filename, e);
            }
            files.add(new FileDetails(file, filename, file.getAbsolutePath(), item.id, FilenameUtils.getExtension(item.name).equals(EXCEL_EXTENSION)));
        }
        if (!files.isEmpty()) {
            setWorkflowGroup(WFsFileImportService.createWWGroup(files));
        }
        readFiles(files);
        deleteLocalFiles(files);
    }


    private void readFiles(List<FileDetails> files) {
        List<FileDetails> excelFiles = files.stream().filter(FileDetails::isExcel).toList();
        List<FileDetails> csvFiles = new ArrayList<>(files.stream().filter(fileDetails -> FilenameUtils.getExtension(fileDetails.getFilename()).equals(CSV_EXTENSION)).toList());
        handleWrongExtensions(files, excelFiles, csvFiles);

        for (FileDetails excelFile : excelFiles) {
            log.info("Reading excel files.");
            WWorkflow workflow = WFsFileImportService.startWFWithWFItem(getWorkflowGroup(), excelFile.getFilename());

            boolean processed = processWithVhsService(excelFile, workflow)
                    || processWithOeifService(excelFile, workflow)
                    || processAsEams(excelFile, workflow)
                    || processAsEamsStandalone(excelFile, workflow);

            if (!processed) {
                handleInvalidExcelFile(excelFile, workflow);
            }
        }

        for (FileDetails csvFile : csvFiles) {
            WWorkflow workflow = WFsFileImportService.startWFWithWFItem(getWorkflowGroup(), csvFile.getFilename());

            boolean processed = processAsEams(csvFile, workflow)
                    || processAsEamsStandalone(csvFile, workflow)
                    || processAsTeilnehmer(csvFile, workflow);

            if (!processed) {
                handleInvalidFile(csvFile, workflow);
            }
        }
    }

    private boolean processWithVhsService(FileDetails file, WWorkflow workflow) {
        try {
            vhsService.readFile(file, Optional.empty());
            finalizeProcessing(file, workflow);
            return true;
        } catch (ParserException ex) {
            log.info("File {} is not a VHS file", file.getFilename());
            return false;
        }
    }

    private boolean processWithOeifService(FileDetails file, WWorkflow workflow) {
        try {
            oeifService.readFile(file);
            finalizeProcessing(file, workflow);
            return true;
        } catch (ParserException ex) {
            log.warn("File {} is not a valid OEIF file", file.getFilename());
            return false;
        }
    }

    private void handleInvalidExcelFile(FileDetails file, WWorkflow workflow) {
        String[] mailAdminJURecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.ADMIN_PR.getValue()).toArray(new String[0]);
        sharePointService.moveFile(file, sharePointProperties.getError());
        mailService.sendEmail(
                "file-import-service.error.excel.invalid-format",
                "german",
                null,
                mailAdminJURecipients,
                toObjectArray(getTimeWithZoneNow()),
                toObjectArray(file.getFilename())
        );
        log.warn("Malformed excel file {} uploaded that could not be parsed", file.getFilename());
        WFsFileImportService.setWWItemToError(workflow);
    }

    private boolean processAsEams(FileDetails file, WWorkflow workflow) {
        try {
            csvServiceImpl.setSource(TeilnehmerSource.EAMS);
            csvServiceImpl.readFile(file);
            finalizeProcessing(file, workflow);
            return true;
        } catch (ParserException ex) {
            log.info("File {} is not an eAms file", file.getFilename());
            return false;
        }
    }

    private boolean processAsEamsStandalone(FileDetails file, WWorkflow workflow) {
        try {
            eamsStandaloneService.readFile(file);
            finalizeProcessing(file, workflow);
            return true;
        } catch (ParserException ex) {
            log.info("File {} is not a eAMS Standalone file with Seminar ID column", file.getFilename());
            return false;
        }
    }

    private boolean processAsTeilnehmer(FileDetails file, WWorkflow workflow) {
        if (!teilnehmerCsvService.isCsvTeilnehmerFile(file)) return false;
        try {
            teilnehmerCsvService.parseCsvTeilnehmerFileAndValidateHeaders(file);
            finalizeProcessing(file, workflow);
            return true;
        } catch (ParserException ex) {
            log.info("File {} is not teilnehmer csv", file.getFilename());
            return false;
        }
    }

    private void finalizeProcessing(FileDetails file, WWorkflow workflow) {
        deleteLocalFile(file.getFile().getPath());
        setWorkflowPayload(new WorkflowPayload(workflow.getId(), file.getFilename()));
        WFsFileImportService.closeWWItem(workflow, getWorkflowPayload().toString());
        // Need to handle exception and response code since both could indicate an error in the chain
        // Refactoring is currently not an easy task since the method validateImportedParticipants
        // is used at multiple call sites
        try {
            var response = validatorService.validateImportedParticipants(getWorkflowPayload());
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error(
                        "Validation service returned status code {} for file {}",
                        response.getStatusCode(),
                        file.getFilename()
                );
                sharePointService.moveFile(file, sharePointProperties.getError());
                return;
            }
        } catch (Exception ex) {
            log.error("Exception occurred while validating imported participant in file {}", file.getFilename(), ex);
            sharePointService.moveFile(file, sharePointProperties.getError());
            return;
        }
        sharePointService.moveFile(file, sharePointProperties.getSuccessful());
    }

    private void handleInvalidFile(FileDetails file, WWorkflow workflow) {
        String[] mailAdminJURecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.ADMIN_PR.getValue()).toArray(new String[0]);
        sharePointService.moveFile(file, sharePointProperties.getError());
        mailService.sendEmail(
                "file-import-service.error.csv.invalid-format",
                "german",
                null,
                mailAdminJURecipients,
                toObjectArray(getTimeWithZoneNow()),
                toObjectArray(file.getFilename())
        );
        log.warn("Malformed csv file {} uploaded that could not be parsed", file.getFilename());
        WFsFileImportService.setWWItemToError(workflow);
    }


    private void handleWrongExtensions(List<FileDetails> files, List<FileDetails> excelFiles, List<FileDetails> csvFiles) {
        String[] mailAdminJURecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.ADMIN_PR.getValue()).toArray(new String[0]);
        List<FileDetails> falseFiles = new ArrayList<>(files);
        falseFiles.removeAll(excelFiles);
        falseFiles.removeAll(csvFiles);
        falseFiles.forEach(file -> {
            log.info("Handling wrong extensions.");
            sharePointService.moveFile(file, sharePointProperties.getError());
            mailService.sendEmail("file-import-service.error.file.wrong-type", "german", null, mailAdminJURecipients, toObjectArray(getTimeWithZoneNow()), toObjectArray(file.getFilename()));
            log.warn("Wrong file type: moved file {} to error folder", file.getFilename());
            deleteLocalFile(file.getFile().getPath());
        });
    }

}
