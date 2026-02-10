package com.ibosng.moxisservice.services.impl;

import com.ibosng.dbservice.dtos.moxis.MoxisJobDto;
import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisReducedJobStateDto;
import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.lhr.LhrJobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.LvAcceptance;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsaenderung;
import com.ibosng.dbservice.entities.mitarbeiter.VertragsaenderungStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import com.ibosng.dbservice.entities.moxis.MoxisJob;
import com.ibosng.dbservice.entities.moxis.MoxisJobStatus;
import com.ibosng.dbservice.entities.moxis.SigningJobType;
import com.ibosng.dbservice.entities.moxis.UserClassifier;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.entities.zeitbuchung.MoxisStatus;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.lhr.LhrJobService;
import com.ibosng.dbservice.services.mitarbeiter.LvAcceptanceService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsaenderungService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.moxis.MoxisJobService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.moxisservice.services.MoxisDocumentService;
import com.ibosng.moxisservice.services.MoxisJobRestService;
import com.ibosng.moxisservice.services.MoxisUpdateJobsService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;
import static com.ibosng.moxisservice.utils.Constants.MOXIS_SERVICE;
import static com.ibosng.moxisservice.utils.Helpers.getDateAndTimeInEmailFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoxisUpdateJobsServiceImpl implements MoxisUpdateJobsService {
    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    private final MoxisJobService moxisJobService;
    private final MoxisJobRestService moxisJobRestService;
    private final MoxisDocumentService moxisDocumentService;
    private final ManageWFsService manageWFsService;
    private final ManageWFItemsService manageWFItemsService;
    private final VertragsdatenService vertragsdatenService;
    private final LhrJobService lhrJobService;
    private final FileShareService fileShareService;
    private final WFService wfService;
    private final WWorkflowItemService wWorkflowItemService;
    private final LvAcceptanceService lvAcceptanceService;
    private final VertragsaenderungService vertragsaenderungService;
    private final MailService mailService;
    private final StammdatenService stammdatenService;
    private final AzureSSOService azureSSOService;

    private final VereinbarungService vereinbarungService;


    @Override
    public void updateActiveMoxisJobs() {
        List<MoxisJob> activeJobs = moxisJobService.findAllByStatus(MoxisJobStatus.IN_PROGRESS);
        ResponseEntity<List<MoxisReducedJobStateDto>> responseStatutes = moxisJobRestService.getStatesForJobs(activeJobs.stream().map(MoxisJob::getInstanceId).toList());
        if (responseStatutes.getStatusCode().equals(HttpStatus.OK) && responseStatutes.getBody() != null) {
            List<MoxisReducedJobStateDto> updateMoxisStatuses = responseStatutes.getBody();
            for (MoxisReducedJobStateDto dto : updateMoxisStatuses) {
                for (MoxisJob job : activeJobs) {
                    MoxisJobStatus moxisStatus = MoxisJobStatus.mapFromMoxisStatus(MoxisStatus.findByMoxisName(dto.getState()));
                    if (dto.getProcessInstanceId().equals(String.valueOf(job.getInstanceId())) && moxisStatus != null && !job.getStatus().equals(moxisStatus)) {
                        job.setStatus(MoxisJobStatus.mapFromMoxisStatus(MoxisStatus.findByMoxisName(dto.getState())));
                        job = moxisJobService.save(job);
                        // If process in error or canceled, reset wfs until Stamm- und Vertragsdatenerfassen
                        if (moxisStatus.equals(MoxisJobStatus.ERROR) ||
                                moxisStatus.equals(MoxisJobStatus.CANCELLED) ||
                                moxisStatus.equals(MoxisJobStatus.SIGNATURE_DENIED) ||
                                moxisStatus.equals(MoxisJobStatus.TIMEOUT)) {
                            restartProcess(job);
                        } else {
                            updateWWIAndDownloadDocument(job);
                        }
                    }
                }
            }
        } else {
            log.error("Response returned with an error: {}", responseStatutes.getBody());
        }
    }

    private void restartProcess(MoxisJob job) {
        if (job != null && SigningJobType.CONTRACT.equals(job.getSigningJobType())) {
            restartOnboardingProccess(job);
            return;
        }
        if (job != null && SigningJobType.ZUSATZ.equals(job.getSigningJobType())) {
            restartZusatz(job);
            return;
        }
        if (job != null && SigningJobType.VEREINBARUNG.equals(job.getSigningJobType())) {
            restartVereinbarung(job);
            return;
        }
        log.warn("Restart proccess wasn`t triggered for job: {}", job);
    }

    private void restartVereinbarung(MoxisJob job) {
        Vereinbarung vereinbarung = vereinbarungService.findById(Integer.valueOf(job.getReferenceId())).orElse(null);
        if (vereinbarung == null) {
            log.error("No Vereinbarung found for MoxisJob: " + job.toString());
            return;
        }
        WWorkflow workflow = vereinbarung.getWorkflow();
        manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF, WWorkflowStatus.NEW, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.NEW, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.NEW, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_DOKUMENT_SPEICHERN, WWorkflowStatus.NEW, MOXIS_SERVICE);
    }

    private void restartZusatz(MoxisJob job) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(job.getReferenceId(), SWorkflows.MA_VD_UNTERSCHRIFTENLAUF);
        WWorkflowItem wwei = wWorkflowItemService.findAllByWorkflow(workflow).stream().filter(wwi -> wwi.getWorkflowItem().getName().equals(SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_DURCHFUEHREN.getValue())).findFirst().orElse(null);

        WWorkflow datenErfassenWf = workflow;
        // Navigate up until we reach Vertragsaenderung_starten and fetch the workflow
        while (!datenErfassenWf.getWorkflow().getName().equals(SWorkflows.MA_VD_VEREINBARUNG_STARTEN.getValue())) {
            datenErfassenWf = datenErfassenWf.getPredecessor();
        }
        // Reset wfs in chain to status NEW
        if (workflow != null && datenErfassenWf != null) {
            wfService.resetWfChainToStatus(workflow, datenErfassenWf, WWorkflowStatus.NEW, MOXIS_SERVICE);
        }
        if (wwei != null) {
            wwei.setData(job.getStatus().name());
            wwei.setStatus(WWorkflowStatus.ERROR);
            wWorkflowItemService.save(wwei);
        }

        WWorkflow stammdatenErfassen = manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.MA_VD_VEREINBARUNG_STARTEN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(stammdatenErfassen, SWorkflowItems.MA_VD_VERTRAGSAENDERUNG_VERVOLLSTAENDIGEN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
    }

    private void restartOnboardingProccess(MoxisJob job) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(job.getReferenceId(), SWorkflows.UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG);
        WWorkflowItem wwei = wWorkflowItemService.findAllByWorkflow(workflow).stream().filter(wwi -> wwi.getWorkflowItem().getName().equals(SWorkflowItems.UNTERSCHRIFTENLAUF_DURCHFUEHREN.getValue())).findFirst().orElse(null);

        WWorkflow datenErfassenWf = workflow;
        // Navigate up until we reach Stamm- und Vertragsdatenerfassen and fetch the workflow
        while (!datenErfassenWf.getWorkflow().getName().equals(SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN.getValue())) {
            datenErfassenWf = datenErfassenWf.getPredecessor();
        }
        // Reset wfs in chain to status NEW
        if (workflow != null && datenErfassenWf != null) {
            wfService.resetWfChainToStatus(workflow, datenErfassenWf, WWorkflowStatus.NEW, MOXIS_SERVICE);
        }
        if (wwei != null) {
            wwei.setData(job.getStatus().name());
            wwei.setStatus(WWorkflowStatus.ERROR);
            wWorkflowItemService.save(wwei);
        }
        List<LvAcceptance> lvAcceptanceList = lvAcceptanceService.findByPersonalnummer(job.getReferenceId()).stream().filter(lv -> lv.getStatus().equals(MitarbeiterStatus.ACTIVE)).toList();
        if (lvAcceptanceList.size() == 1) {
            LvAcceptance lvAcceptance = lvAcceptanceList.get(0);
            lvAcceptance.setEcard(false);
            lvAcceptance.setBankcard(false);
            lvAcceptance.setArbeitsgenehmigungDok(false);
            lvAcceptance.setGehaltEinstufung(false);
            lvAcceptanceService.save(lvAcceptance);
        }
        // Set Stamm- und Vertragsdatenerfassen to IN_PROGRESS
        WWorkflow stammdatenErfassen = manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(stammdatenErfassen, SWorkflowItems.STAMMDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(stammdatenErfassen, SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
    }

    @Override
    public void updateActiveMoxisJobsSeparately() {
        List<MoxisJob> activeJobs = moxisJobService.findAllByStatus(MoxisJobStatus.IN_PROGRESS);
        for (MoxisJob job : activeJobs) {
            updateJobAndWW(job);
        }
        moxisJobService.saveAll(activeJobs);
    }

    @Override
    public void updateJobAndWW(MoxisJob job) {
        ResponseEntity<MoxisJobStateDto> response = moxisJobRestService.getJobState(String.valueOf(job.getInstanceId()), UserClassifier.UPN.getType());
        if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
            MoxisJobStateDto dto = response.getBody();
            MoxisJobStatus moxisStatus = MoxisJobStatus.mapFromMoxisStatus(MoxisStatus.findByMoxisName(dto.getState()));
            if (moxisStatus != null && !job.getStatus().equals(moxisStatus)) {
                job.setStatus(MoxisJobStatus.mapFromMoxisStatus(MoxisStatus.findByMoxisName(dto.getState())));
                job = moxisJobService.save(job);
                if (moxisStatus.equals(MoxisJobStatus.ERROR) ||
                        moxisStatus.equals(MoxisJobStatus.CANCELLED) ||
                        moxisStatus.equals(MoxisJobStatus.SIGNATURE_DENIED) ||
                        moxisStatus.equals(MoxisJobStatus.TIMEOUT)) {
                    restartProcess(job);
                } else {
                    updateWWIAndDownloadDocument(job);
                }

                if (job != null && !isNullOrBlank(job.getReferenceId()) && MoxisJobStatus.CANCELLED.equals(moxisStatus)) {
                    sendMailCanceled(job.getReferenceId());
                }
            }
        } else {
            log.error("Response returned with an error: {}", response.getBody());
        }
    }

    private void updateWWIAndDownloadDocument(MoxisJob job) {
        if (job != null && SigningJobType.CONTRACT.equals(job.getSigningJobType())) {
            updateWWIforDokument(job);
            return;
        }
        if (job != null && SigningJobType.ZUSATZ.equals(job.getSigningJobType())) {
            updateWWIforZusatz(job);
            return;
        }
        if (job != null && SigningJobType.VEREINBARUNG.equals(job.getSigningJobType())) {
            // todo if succesful download document from moxis and store in az fs
            updateWWIforVereinbarung(job);
            return;
        }
        log.warn("Downloading process wasn`t triggered for job: {}", job);
    }

    private void updateWWIforVereinbarung(MoxisJob job) {

        WWorkflow workflow = job.getWorkflow();

        if (workflow != null || true) {
            if (job.getStatus().equals(MoxisJobStatus.SUCCESS)) {
                try {
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_DOKUMENT_SPEICHERN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);


                    moxisDocumentService.getAndUploadVereinbarung(String.valueOf(job.getInstanceId()), job.getReferenceId());
                    Vereinbarung vereinbarung = vereinbarungService.findVereinbarungByWorkflow_Id(workflow.getId());
                    vereinbarung.setStatus(VereinbarungStatus.COMPLETED);
                    vereinbarungService.save(vereinbarung);


                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_DOKUMENT_SPEICHERN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                } catch (Exception e) {
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_DOKUMENT_SPEICHERN, WWorkflowStatus.ERROR, MOXIS_SERVICE);
                    log.error("Exception caught while uploading Vereinbarung for Personalnummer {} with an exception: {}", job.getReferenceId(), e);
                }

            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.ERROR, MOXIS_SERVICE);
            }
        }
    }


    private void updateWWIforZusatz(MoxisJob job) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(job.getReferenceId(), SWorkflows.MA_VD_UNTERSCHRIFTENLAUF);

        if (workflow != null) {
            if (job.getStatus().equals(MoxisJobStatus.SUCCESS)) {
                try {
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIEBENES_DOKUMENT_SPEICHERN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);

                    fileShareService.renameAndMoveSignedDocumentsAndDirectories(job.getReferenceId());

                    moxisDocumentService.getAndUploadDocument(String.valueOf(job.getInstanceId()), job.getReferenceId());

                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIEBENES_DOKUMENT_SPEICHERN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.MA_VD_UNTERSCHRIFTENLAUF, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    //Creating job for the LHR service
                    createLhrJobForVertragsaenderung(job.getReferenceId(), workflow.getWorkflowGroup());
                } catch (Exception e) {
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIEBENES_DOKUMENT_SPEICHERN, WWorkflowStatus.ERROR, MOXIS_SERVICE);
                    log.error("Exception caught while uploading zusatz for personalnummer {} with an exception: {}", job.getReferenceId(), e);
                }

            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.ERROR, MOXIS_SERVICE);
                vertragsdatenService.findByPersonalnummerString(job.getReferenceId()).stream().findFirst()
                        .ifPresent(vd -> mailService.sendEmail("moxis-service.people-unterschriftenlauf-fehlgeschlagen",
                                "german",
                                null,
                                new String[]{vd.getFuehrungskraft().getEmail()},
                                toObjectArray(),
                                toObjectArray())
                        );
            }
        }
    }

    private void updateWWIforDokument(MoxisJob job) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(job.getReferenceId(), SWorkflows.UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG);

        if (workflow != null) {
            if (job.getStatus().equals(MoxisJobStatus.SUCCESS)) {
                try {
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIEBENE_DOKUMENTE_SPEICHERN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);

                    fileShareService.renameAndMoveSignedDocumentsAndDirectories(job.getReferenceId());

                    moxisDocumentService.getAndUploadDocument(String.valueOf(job.getInstanceId()), job.getReferenceId());

                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIEBENE_DOKUMENTE_SPEICHERN, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG, WWorkflowStatus.COMPLETED, MOXIS_SERVICE);
                    //Creating job for the LHR service
                    createLhrJobForContract(job.getReferenceId(), workflow.getWorkflowGroup());
                } catch (Exception e) {
                    manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIEBENE_DOKUMENTE_SPEICHERN, WWorkflowStatus.ERROR, MOXIS_SERVICE);
                    log.error("Exception caught while uploading vertrag for personalnummer {} with an exception: {}", job.getReferenceId(), e.getMessage());
                }

            } else {
                manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.UNTERSCHRIFTENLAUF_DURCHFUEHREN, WWorkflowStatus.ERROR, MOXIS_SERVICE);
            }
        }
    }

    private void createLhrJobForVertragsaenderung(String personalnummer, WWorkflowGroup workflowGroup) {
        Vertragsdaten vertragsdaten = vertragsaenderungService.findAllByPersonalnummerAndStatus(personalnummer, VertragsaenderungStatus.IN_PROGRESS)
                .stream().map(Vertragsaenderung::getSuccessor).findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.error("Vertragsdaten could not be found for personalnummer {}", personalnummer);
            return;
        }
        LhrJob lhrJob = new LhrJob();
        lhrJob.setCreatedBy(MOXIS_SERVICE);
        lhrJob.setCreatedOn(getLocalDateNow());
        lhrJob.setEintritt(vertragsdaten.getEintritt());
        lhrJob.setPersonalnummer(vertragsdaten.getPersonalnummer());
        lhrJob.setStatus(LhrJobStatus.PENDING);
        lhrJob.setJobType(SigningJobType.ZUSATZ);
        lhrJobService.save(lhrJob);
        WWorkflow workflow = manageWFsService.setWFStatus(workflowGroup, SWorkflows.MA_VD_ENDE, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.MA_VD_DATEN_AN_LHR_UEBERMITTELN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
    }

    private void createLhrJobForContract(String personalnummer, WWorkflowGroup workflowGroup) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(personalnummer).stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.error("Vertragsdaten could not be found for personalnummer {}", personalnummer);
            return;
        }
        LhrJob lhrJob = new LhrJob();
        lhrJob.setCreatedBy(MOXIS_SERVICE);
        lhrJob.setCreatedOn(getLocalDateNow());
        lhrJob.setEintritt(vertragsdaten.getEintritt());
        lhrJob.setPersonalnummer(vertragsdaten.getPersonalnummer());
        lhrJob.setStatus(LhrJobStatus.PENDING);
        lhrJob.setJobType(SigningJobType.CONTRACT);
        lhrJobService.save(lhrJob);
        WWorkflow workflow = manageWFsService.setWFStatus(workflowGroup, SWorkflows.LHR_DATEN_UEBERGEBEN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.DATEN_AN_LHR_UEBERGEBEN, WWorkflowStatus.IN_PROGRESS, MOXIS_SERVICE);
    }

    @Override
    public void processMoxisRetryJob() {
        List<MoxisJob> moxisJobList = moxisJobService.findAllByStatus(MoxisJobStatus.RETRY);
        for (MoxisJob moxisJob : moxisJobList) {
            MoxisJobDto moxisJobDto = mapMoxisJobDto(moxisJob);
            moxisJobRestService.startProcess(moxisJobDto, moxisJob, false);
        }
    }

    private MoxisJobDto mapMoxisJobDto(MoxisJob moxisJob) {
        MoxisJobDto moxisJobDto = new MoxisJobDto();
        moxisJobDto.setDescription(moxisJob.getDescription());
        moxisJobDto.setPersonalNummer(moxisJob.getReferenceId());
        moxisJobDto.setChangedBy(moxisJob.getConstituent());
        moxisJobDto.setWorkflow(moxisJob.getWorkflow().getId());
        moxisJobDto.setSigningJobType(moxisJob.getSigningJobType());
        return moxisJobDto;
    }

    private void sendMailCanceled(String personalnummer) {
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
        String name = String.join(" ", stammdaten.getVorname(), stammdaten.getNachname());
        mailService.sendEmail("moxis-service.hr.neuer-ma-moxis-unterschriftsprozess-fehlgeschlagen",
                "german", null, azureSSOService
                        .getGroupMemberEmailsByName(IbosRole.HR.getValue())
                        .toArray(new String[0]), toObjectArray(),
                toObjectArray(getDateAndTimeInEmailFormat(getLocalDateNow()),
                        name));

        mailService.sendEmail("moxis-service.lv.neuer-ma-moxis-unterschriftsprozess-fehlgeschlagen",
                "german", null, azureSSOService
                        .getGroupMemberEmailsByName(IbosRole.LV.getValue())
                        .toArray(new String[0]), toObjectArray(),
                toObjectArray(getDateAndTimeInEmailFormat(getLocalDateNow()),
                        name));
    }

}