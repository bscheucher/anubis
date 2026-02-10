package com.ibosng.moxisservice.services.impl;

import com.ibosng.dbservice.dtos.moxis.JobDescriptionDto;
import com.ibosng.dbservice.dtos.moxis.MoxisJobDto;
import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisReducedJobStateDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.moxis.MoxisJob;
import com.ibosng.dbservice.entities.moxis.MoxisJobStatus;
import com.ibosng.dbservice.entities.moxis.SigningJobType;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.moxis.MoxisJobService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.moxisservice.clients.MoxisClient;
import com.ibosng.moxisservice.exceptions.MoxisException;
import com.ibosng.moxisservice.services.MoxisJobRestService;
import com.ibosng.moxisservice.services.MoxisMapperService;
import com.ibosng.moxisservice.services.MoxisUpdateJobsService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Mappers.getSubdirectoryForDocument;
import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;
import static com.ibosng.moxisservice.services.impl.MoxisMapperServiceImpl.deleteLocalFile;
import static com.ibosng.moxisservice.utils.Constants.DIENSTVERTRAG;
import static com.ibosng.moxisservice.utils.Helpers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoxisJobRestServiceImpl implements MoxisJobRestService {
    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    @Getter
    @Value("${moxisConstituent:#{null}}")
    private String moxisConstituent;

    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    private static final String UNSIGNED = "Nicht unterschrieben";

    private final MoxisClient moxisClient;
    private final MoxisMapperService moxisMapperService;
    private final MoxisJobService jobService;
    private final ManageWFItemsService manageWFItemsService;
    private final ManageWFsService manageWFsService;
    private final MailService mailService;
    private final FileShareService fileShareService;
    private final PersonalnummerService personalnummerService;
    private MoxisUpdateJobsService moxisUpdateJobsService;
    private final StammdatenService stammdatenService;
    private final AzureSSOService azureSSOService;
    private final WWorkflowService wWorkflowService;

    public MoxisUpdateJobsService getMoxisUpdateJobsService() {
        return moxisUpdateJobsService;
    }

    @Autowired
    public void setMoxisUpdateJobsService(@Lazy MoxisUpdateJobsService moxisUpdateJobsService) {
        this.moxisUpdateJobsService = moxisUpdateJobsService;
    }

    private ResponseEntity<String> startVereinbarung(MoxisJobDto jobDto) {

        log.info("Starting Vereinbarung job for personalnummer {}", jobDto.getPersonalNummer());
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(jobDto.getPersonalNummer());
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(jobDto.getPersonalNummer());
        if (personalnummerEntity == null) {
            log.error("No Personalnummer found for: " + jobDto.getPersonalNummer());
            return null;
        }
        if (stammdaten == null) {
            log.error("No Stammdaten found for: " + jobDto.getPersonalNummer());
            return null;
        }

        String directoryPath = fileShareService.getVereinbarungenDirectory(jobDto.getPersonalNummer(), stammdaten);
        directoryPath += "/" + UNSIGNED;
        Optional<WWorkflow> workflowOptional = wWorkflowService.findById(jobDto.getWorkflow());
        if (workflowOptional.isEmpty()) {
            log.error("No Workflow found for Moxis Job: " + jobDto);
        }
        WWorkflow workflow = workflowOptional.get();
        String firma = personalnummerEntity.getFirma().getName();
        JobDescriptionDto descriptionDto = moxisMapperService.getJobDto(jobDto);

        String filename = fileShareService.getFullFileName(getFileSharePersonalunterlagen(), firma, jobDto.getPersonalNummer(), directoryPath, jobDto.getDescription(), jobDto.getPersonalNummer());
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        File jobFile = null;
        try {
            jobFile = moxisMapperService.createJsonFileFromDto(descriptionDto, filename);
            log.info("Downloading file '{}' from directory '{}'", filename, directoryPath);
            ByteArrayResource documentResource = convertInputStreamToByteArrayResource(
                    fileShareService.downloadFromFileShare(
                            getFileSharePersonalunterlagen(),
                            directoryPath,
                            filename),
                    String.valueOf(jobDto.getPersonalNummer()));
            builder.part("documentToSign", documentResource, MediaType.APPLICATION_PDF);
            builder.part("jobDescription", new FileSystemResource(jobFile), MediaType.APPLICATION_JSON);
        } catch (IOException ex) {
            log.error("Exception during downloading file {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Error processing job: %s", ex.getMessage()));
        }
        final MoxisJob job = moxisMapperService.mapAndSaveJob(descriptionDto, jobDto);
        setMoxisJobStatus(job, MoxisJobStatus.PENDING);
        try {
            log.info("Sending request to moxis for personalnummer {}", jobDto.getPersonalNummer());
            ResponseEntity<String> response = moxisClient.startProcess(builder.build(), moxisMapperService.isHandySignaturExtern());
            log.info("Response received from moxis for personalnummer {}, with status code {}", jobDto.getPersonalNummer(), response.getStatusCode());
            Integer instanceId = parseStringToInteger(response.getBody());
            if (instanceId != null) {
                job.setInstanceId(instanceId);
                setMoxisJobStatus(job, MoxisJobStatus.IN_PROGRESS);
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.COMPLETED, jobDto.getChangedBy());
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.IN_PROGRESS, jobDto.getChangedBy());
            }
            return response;
        } catch (MoxisException ex) {
            if (HttpStatus.CONFLICT.equals(ex.getHttpStatus())) {
                log.warn("Exception of conflict caught during the start of the process: {}", ex.getMessage());
            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.ERROR, job.getChangedBy());
                manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF, WWorkflowStatus.ERROR, job.getChangedBy());
                log.error("Exception caught during the start of the process: {}", ex.getMessage());
            }

            mailService.sendEmail("moxis-service.hr.neuer-ma-moxis-unterschriftsprozess-fehlgeschlagen", "german",
                    List.of(jobFile),
                    azureSSOService.getGroupMemberEmailsByName(IbosRole.HR.getValue()).toArray(new String[0]),
                    toObjectArray(), toObjectArray(getDateAndTimeInEmailFormat(getLocalDateNow())));
            setMoxisJobStatus(job, MoxisJobStatus.ERROR);
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(ex.getHttpStatus(), ex.getMessage() + " Reason: " + ex.getBody())).build();
        } finally {
            deleteLocalFile(jobFile.getPath());
        }
    }

    private ResponseEntity<String> startZusatz(MoxisJobDto jobDto) {
        log.info("Starting zusatz job for personalnummer {}", jobDto.getPersonalNummer());
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(jobDto.getPersonalNummer());
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(jobDto.getPersonalNummer(), SWorkflows.MA_VD_UNTERSCHRIFTENLAUF);

        String firma = personalnummerEntity.getFirma().getName();
        String subdirectory = getSubdirectoryForDocument(DIENSTVERTRAG);
        String directoryPath = firma + "/" + jobDto.getPersonalNummer() + "/" + subdirectory;
        JobDescriptionDto descriptionDto = moxisMapperService.getJobDto(jobDto);
        final MoxisJob job = moxisMapperService.mapAndSaveJob(descriptionDto, jobDto);

        String filename = fileShareService.getFullFileName(getFileShareTemp(), firma, jobDto.getPersonalNummer(), directoryPath, null, jobDto.getPersonalNummer());
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        File jobFile = null;
        try {
            jobFile = moxisMapperService.createJsonFileFromDto(descriptionDto, filename);
            log.info("Downloading file '{}' from directory '{}'", filename, directoryPath);
            ByteArrayResource documentResource = convertInputStreamToByteArrayResource(
                    fileShareService.downloadFromFileShare(
                            getFileShareTemp(),
                            directoryPath,
                            filename),
                    String.valueOf(jobDto.getPersonalNummer()));
            builder.part("documentToSign", documentResource, MediaType.APPLICATION_PDF);
            builder.part("jobDescription", new FileSystemResource(jobFile), MediaType.APPLICATION_JSON);
        } catch (IOException ex) {
            log.error("Exception during downloading file {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Error processing job: %s", ex.getMessage()));
        }
        setMoxisJobStatus(job, MoxisJobStatus.PENDING);
        try {
            log.info("Sending request to moxis for personalnummer {}", jobDto.getPersonalNummer());
            ResponseEntity<String> response = moxisClient.startProcess(builder.build(), moxisMapperService.isHandySignaturExtern());
            log.info("Response received from moxis for personalnummer {}, with status code {}", jobDto.getPersonalNummer(), response.getStatusCode());
            Integer instanceId = parseStringToInteger(response.getBody());
            if (instanceId != null) {
                job.setInstanceId(instanceId);
                setMoxisJobStatus(job, MoxisJobStatus.IN_PROGRESS);
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.COMPLETED, jobDto.getChangedBy());
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.IN_PROGRESS, jobDto.getChangedBy());
            }
            return response;
        } catch (MoxisException ex) {
            if (HttpStatus.CONFLICT.equals(ex.getHttpStatus())) {
                log.warn("Exception of conflict caught during the start of the process: {}", ex.getMessage());
            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.ERROR, job.getChangedBy());
                log.error("Exception caught during the start of the process: {}", ex.getMessage());
            }
            sendMailFehlgeschlagen(personalnummerEntity.getPersonalnummer(), jobFile);
            setMoxisJobStatus(job, MoxisJobStatus.ERROR);
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(ex.getHttpStatus(), ex.getMessage() + " Reason: " + ex.getBody())).build();
        } finally {
            deleteLocalFile(jobFile.getPath());
        }
    }

    private ResponseEntity<String> startMaOnboarding(MoxisJobDto jobDto, MoxisJob job, boolean isRestCall) {
        log.info("Starting process for personalnummer {}", jobDto.getPersonalNummer());
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(jobDto.getPersonalNummer());
        File jobFile = null;
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(jobDto.getPersonalNummer(), SWorkflows.UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG);
        try {

            String firma = personalnummerEntity.getFirma().getName();
            String subdirectory = getSubdirectoryForDocument(DIENSTVERTRAG);
            String directoryPath = firma + "/" + jobDto.getPersonalNummer() + "/" + subdirectory;

            JobDescriptionDto descriptionDto = moxisMapperService.getJobDto(jobDto);
            if (isRestCall) {
                job = moxisMapperService.mapAndSaveJob(descriptionDto, jobDto);
            }

            String filename = fileShareService.getFullFileName(getFileShareTemp(), firma, jobDto.getPersonalNummer(), directoryPath, null, jobDto.getPersonalNummer());
            jobFile = moxisMapperService.createJsonFileFromDto(descriptionDto, filename);
            log.info("Downloading file '{}' from directory '{}'", filename, directoryPath);

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            ByteArrayResource documentResource = convertInputStreamToByteArrayResource(
                    fileShareService.downloadFromFileShare(
                            getFileShareTemp(),
                            directoryPath,
                            filename),
                    String.valueOf(jobDto.getPersonalNummer()));

            builder.part("documentToSign", documentResource, MediaType.APPLICATION_PDF);
            builder.part("jobDescription", new FileSystemResource(jobFile), MediaType.APPLICATION_JSON);
            setMoxisJobStatus(job, MoxisJobStatus.PENDING);
            log.info("Sending request to moxis for personalnummer {}", jobDto.getPersonalNummer());
            ResponseEntity<String> response = moxisClient.startProcess(builder.build(), moxisMapperService.isHandySignaturExtern());
            log.info("Response received from moxis for personalnummer {}, with status code {}", jobDto.getPersonalNummer(), response.getStatusCode());
            Integer instanceId = parseStringToInteger(response.getBody());
            if (instanceId != null) {
                job.setInstanceId(instanceId);
                setMoxisJobStatus(job, MoxisJobStatus.IN_PROGRESS);
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.COMPLETED, jobDto.getChangedBy());
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.IN_PROGRESS, jobDto.getChangedBy());
            }
            return response;
        } catch (MoxisException e) {
            if (e.getHttpStatus() != null && e.getHttpStatus().equals(HttpStatus.CONFLICT)) {
                log.warn("Exception of conflict caught during the start of the process: {}", e.getMessage());
            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.ERROR, job.getChangedBy());
                log.error("Exception caught during the start of the process: {}", e.getMessage());
            }
            if (jobFile != null) {
                sendMailFehlgeschlagen(personalnummerEntity.getPersonalnummer(), jobFile);
            }
            setMoxisJobStatus(job, MoxisJobStatus.ERROR);
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage() + " Reason: " + e.getBody())).build();
        } catch (IOException e) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.ERROR, job.getChangedBy());
            log.error("IO exception caught during the start of the process: {}", e.getMessage());
            if (jobFile != null) {
                sendMailFehlgeschlagen(personalnummerEntity.getPersonalnummer(), jobFile);
            }
            setMoxisJobStatus(job, MoxisJobStatus.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Error processing job: %s", e.getMessage()));
        } finally {
            if (jobFile != null) {
                deleteLocalFile(jobFile.getPath());
            }
        }
    }

    @Override
    public ResponseEntity<String> startProcess(MoxisJobDto jobDto, MoxisJob job, boolean isRestCall) {
        if ((jobDto != null && SigningJobType.CONTRACT.equals(jobDto.getSigningJobType())) || (job != null && SigningJobType.CONTRACT.equals(job.getSigningJobType()))) {
            return startMaOnboarding(jobDto, job, isRestCall);
        }
        if ((jobDto != null && SigningJobType.ZUSATZ.equals(jobDto.getSigningJobType())) || (job != null && SigningJobType.ZUSATZ.equals(job.getSigningJobType()))) {
            return startZusatz(jobDto);
        }
        if ((jobDto != null && SigningJobType.VEREINBARUNG.equals(jobDto.getSigningJobType())) || (job != null && SigningJobType.VEREINBARUNG.equals(job.getSigningJobType()))) {
            return startVereinbarung(jobDto);
        }
        log.warn("Proccess wasn`t triggered for {}", (jobDto == null) ? job : jobDto);
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<MoxisJobStateDto> getJobState(String processInstanceId, String nameClassifier) {
        try {
            return moxisClient.getJobState(processInstanceId, nameClassifier);
        } catch (MoxisException e) {
            log.error("Exception caught during acquiring of the process: {}", e.getMessage());
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage())).build();
        }
    }

    @Override
    public ResponseEntity<String> cancelJob(String personalnummer, String userEmail) {
        try {
            MoxisJob moxisJob = jobService.findAllByReferenceId(personalnummer);
            ResponseEntity<String> result = moxisClient.cancelJob(String.valueOf(moxisJob.getInstanceId()), moxisMapperService.getMoxisUserDto(getMoxisConstituent(), true));
            MoxisJob job = jobService.findAllByReferenceId(personalnummer);
            if (job != null) {
                moxisUpdateJobsService.updateJobAndWW(job);
            }
            return result;
        } catch (MoxisException e) {
            log.error("Exception caught during the cancellation of the process: {}", e.getMessage());
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage())).build();
        }
    }

    @Override
    public ResponseEntity<List<MoxisReducedJobStateDto>> getStatesForJobs(List<Integer> jobIds) {
        try {
            return moxisClient.getJobStates(jobIds);
        } catch (MoxisException e) {
            log.error("Exception caught during getting job status for the jobs: {}", e.getMessage());
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage())).build();
        }
    }

    private void setMoxisJobStatus(MoxisJob job, MoxisJobStatus status) {
        if (job != null) {
            job.setStatus(status);
            jobService.save(job);
        }
    }

    private void sendMailFehlgeschlagen(String personalnummer, File jobFile) {
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
        String name = String.join(" ", stammdaten.getVorname(), stammdaten.getNachname());
        mailService.sendEmail("moxis-service.hr.neuer-ma-moxis-unterschriftsprozess-fehlgeschlagen",
                "german", List.of(jobFile), azureSSOService
                        .getGroupMemberEmailsByName(IbosRole.HR.getValue())
                        .toArray(new String[0]), toObjectArray(),
                toObjectArray(getDateAndTimeInEmailFormat(getLocalDateNow()),
                        name));
    }
}
