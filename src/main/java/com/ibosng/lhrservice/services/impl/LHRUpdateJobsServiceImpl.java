package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.lhr.LhrJobStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.moxis.SigningJobType;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.lhr.LhrJobService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.lhrservice.services.LHRDienstnehmerService;
import com.ibosng.lhrservice.services.LHRUpdateJobsService;
import com.ibosng.lhrservice.services.LHRZeiterfassungService;
import com.ibosng.lhrservice.services.RestService;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.usercreationservice.service.UserCreationAnlageIbisAcamService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Constants.LHR_SERVICE;
import static com.ibosng.lhrservice.utils.Helpers.getDateAndTimeInEmailFormat;
import static com.ibosng.lhrservice.utils.Helpers.removeLeadingZeros;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Service
@Slf4j
@RequiredArgsConstructor
public class LHRUpdateJobsServiceImpl implements LHRUpdateJobsService {

    private final AzureSSOService azureSSOService;
    private final LHRDienstnehmerService lhrDienstnehmerService;
    private final ManageWFsService manageWFsService;
    private final ManageWFItemsService manageWFItemsService;
    private final LhrJobService lhrJobService;
    private final MailService mailService;
    private final StammdatenService stammdatenService;
    private final VertragsdatenService vertragsdatenService;
    private final LHRZeiterfassungService lhrZeiterfassungService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final PersonalnummerService personalnummerService;
    private final UserCreationAnlageIbisAcamService userAnlageIbisAcamService;


    @Override
    public void executeLhrJobs(List<LhrJob> jobs) {
        for (LhrJob job : jobs) {
           try {
               WWorkflow workflow = null;
               SWorkflows lhrDatenUebergebenSWF = null;
               SWorkflowItems datenAnLhrUebergebenSWI = null;
               boolean isMitarbeiter = job.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
               if (isMitarbeiter) {
                   if (SigningJobType.ZUSATZ.equals(job.getJobType())) {
                       lhrDatenUebergebenSWF = SWorkflows.MA_VD_ENDE;
                       workflow = manageWFsService.getWorkflowFromDataAndWFType(job.getPersonalnummer().getPersonalnummer(), SWorkflows.MA_VD_ENDE);
                       datenAnLhrUebergebenSWI = SWorkflowItems.MA_VD_DATEN_AN_LHR_UEBERMITTELN;
                   } else if (SigningJobType.CONTRACT.equals(job.getJobType())) {
                       lhrDatenUebergebenSWF = SWorkflows.LHR_DATEN_UEBERGEBEN;
                       workflow = manageWFsService.getWorkflowFromDataAndWFType(job.getPersonalnummer().getPersonalnummer(), lhrDatenUebergebenSWF);
                       datenAnLhrUebergebenSWI = SWorkflowItems.DATEN_AN_LHR_UEBERGEBEN;
                   }
               } else {
                   lhrDatenUebergebenSWF = SWorkflows.TN_ONBOARDING_LHR_DATEN_UEBERGEBEN;
                   workflow = manageWFsService.getWorkflowFromDataAndWFType(job.getPersonalnummer().getPersonalnummer(), lhrDatenUebergebenSWF);
                   datenAnLhrUebergebenSWI = SWorkflowItems.TN_ONBOARDING_DATEN_AN_LHR_UEBERGEBEN;
               }

               Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(job.getPersonalnummer().getPersonalnummer());
               String name = String.join(" ", stammdaten.getVorname(), stammdaten.getNachname());
               String[] mailHRRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.HR.getValue()).toArray(new String[0]);
               try {
                   log.info("Sending data to LHR for personalnummer {}", job.getPersonalnummer().getPersonalnummer());
                   lhrDienstnehmerService.createOrUpdateLHRDienstnehmerstamm(job.getPersonalnummer().getId());
                   job.setStatus(LhrJobStatus.COMPLETED);
                   lhrJobService.save(job);
                   log.info("Successfully sent data to LHR for personalnummer {}", job.getPersonalnummer().getPersonalnummer());
                   manageWFItemsService.setWFItemStatus(workflow, datenAnLhrUebergebenSWI, WWorkflowStatus.COMPLETED, LHR_SERVICE);
                   manageWFsService.setWFStatus(workflow.getWorkflowGroup(), lhrDatenUebergebenSWF, WWorkflowStatus.COMPLETED, LHR_SERVICE);
                   if (isMitarbeiter) {
                       if (SigningJobType.CONTRACT.equals(job.getJobType())) {
                           callUserCreationService(job.getPersonalnummer().getPersonalnummer());
                           log.info("Successfully sent data to the User-Creation service for personalnummer {}", job.getPersonalnummer().getPersonalnummer());
                       }

                       mailService.sendEmail("lhr-service.hr-neuer-ma-lhr-uebermittlung-erfolgreich",
                               "german",
                               null,
                               mailHRRecipients,
                               toObjectArray(name),
                               toObjectArray(name, getDateAndTimeInEmailFormat(LocalDateTime.now())));
                   } else {
                       manageWFItemstForTN(job.getPersonalnummer().getId(), workflow);
                       Personalnummer personalnummer = job.getPersonalnummer();
                       personalnummer.setOnboardedOn(getLocalDateNow());
                       personalnummerService.save(personalnummer);
                       mailService.sendEmail("lhr-service.tn-anabwesenheit-mitarbeiter-anlegen-erfolgreich",
                               "german",
                               null,
                               mailHRRecipients,
                               toObjectArray(name),
                               toObjectArray(name, getDateAndTimeInEmailFormat(LocalDateTime.now())));
                   }

               } catch (Exception ex) {
                   log.error("Job exception: {}", ex);
                   job.setStatus(LhrJobStatus.ERROR);
                   lhrJobService.save(job);
                   manageWFItemsService.setWFItemStatus(workflow, datenAnLhrUebergebenSWI, WWorkflowStatus.ERROR, LHR_SERVICE);
                   manageWFsService.setWFStatus(workflow.getWorkflowGroup(), lhrDatenUebergebenSWF, WWorkflowStatus.ERROR, LHR_SERVICE);
                   List<File> files = getUploadFiles(job.getPersonalnummer().getPersonalnummer());

                   switch (job.getJobType()) {
                       case CONTRACT -> {
                           if (isMitarbeiter) {
                               mailService.sendEmail(
                                       "lhr-service.hr.neuer-ma-lhr-uebermittlung-fehlgeschlagen",
                                       "german",
                                       !files.isEmpty() ? files : null,
                                       mailHRRecipients,
                                       toObjectArray(),
                                       toObjectArray(getDateAndTimeInEmailFormat(LocalDateTime.now()))
                               );
                           } else {
                               mailService.sendEmail(
                                       "lhr-service.tn-anabwesenheit-uebergabe-fehlgeschlagen",
                                       "german",
                                       !files.isEmpty() ? files : null,
                                       mailHRRecipients,
                                       toObjectArray(name),
                                       toObjectArray(name, getDateAndTimeInEmailFormat(LocalDateTime.now()))
                               );
                           }
                       }

                       case ZUSATZ -> mailService.sendEmail("lhr-service.ma-verwaltung-fehlgeschlagen", "german",
                               !files.isEmpty() ? files : null,
                               mailHRRecipients,
                               toObjectArray(name),
                               toObjectArray(name, toObjectArray(getDateAndTimeInEmailFormat(LocalDateTime.now())), ex.getMessage()));

                   }

                   log.error("Exception caught while sending data to LHR: {}", ex.getMessage());
               } finally {
                   // Ensure the temporary file is deleted
                   File tempFile = new File(System.getProperty("java.io.tmpdir"), "requestBody_" + removeLeadingZeros(job.getPersonalnummer().getPersonalnummer()) + ".json");
                   if (tempFile.exists() && !tempFile.delete()) {
                       log.error("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
                   } else {
                       log.info("Temporary file deleted: {}", tempFile.getAbsolutePath());
                   }
               }
           } catch (Exception ex) {
               log.warn("Exception caught while sending data to LHR: {}", ex.getMessage());
           }
        }
    }

    private void manageWFItemstForTN(Integer personalnummerId, WWorkflow workflow) {
        workflow = manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.TN_ONBOARDING_STAKEHOLDER, WWorkflowStatus.IN_PROGRESS, LHR_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_STAKEHOLDER_INFORMIEREN, WWorkflowStatus.IN_PROGRESS, LHR_SERVICE);
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerId(personalnummerId);
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummerId, List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (stammdaten != null && !isNullOrBlank(stammdaten.getVorname()) && !isNullOrBlank(stammdaten.getNachname()) && vertragsdaten != null && vertragsdaten.getEintritt() != null) {
            String[] mailADMINJU = azureSSOService.getGroupMemberEmailsByName(IbosRole.ADMIN_JU.getValue()).toArray(new String[0]);
            String[] mailLHRRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);
            mailService.sendEmail(
                    "lhr-service.tn-onboarding-fertig",
                    "german",
                    null,
                    ArrayUtils.addAll(mailADMINJU, mailLHRRecipients),
                    toObjectArray(stammdaten.getVorname(), stammdaten.getNachname()),
                    toObjectArray(stammdaten.getVorname(), stammdaten.getNachname(), vertragsdaten.getEintritt()));
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_STAKEHOLDER_INFORMIEREN, WWorkflowStatus.COMPLETED, LHR_SERVICE);
            manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.TN_ONBOARDING_STAKEHOLDER, WWorkflowStatus.COMPLETED, LHR_SERVICE);
        } else {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_ONBOARDING_STAKEHOLDER_INFORMIEREN, WWorkflowStatus.ERROR, LHR_SERVICE);
            manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.TN_ONBOARDING_STAKEHOLDER, WWorkflowStatus.ERROR, LHR_SERVICE);
            log.warn("Vorname, nachname or eintrittsdatum were empty for personalnummerId {}, email could not be send.", personalnummerId);
        }

    }

    public List<File> getUploadFiles(String personalnummer) {
        List<File> files = new ArrayList<>();
        File request = new File(System.getProperty("java.io.tmpdir"), "requestBody_" + removeLeadingZeros(personalnummer) + ".json");
        File error = new File(System.getProperty("java.io.tmpdir"), "errorResponse_" + removeLeadingZeros(personalnummer) + ".json");
        if (request.exists()) {
            files.add(request);
        }
        if (error.exists()) {
            files.add(error);
        }
        return files;
    }

    private ResponseEntity<String> callUserCreationService(String personalnummer) {
        log.info("User-Creation for personalnummer: {}", personalnummer);

        boolean success = userAnlageIbisAcamService.createMitarbeiterFile(personalnummer);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public void executeZeiterfassungTransfers(List<ZeiterfassungTransfer> jobs) {
        log.info("Starting ZeiterfassungTransfers for jobs");
        for (ZeiterfassungTransfer job : jobs) {
            log.info("Starting ZeiterfassungTransfers for job {}", job.getId());
            ResponseEntity<ZeiterfassungTransferDto> response = lhrZeiterfassungService.sendZeiterfassungTransfer(String.valueOf(job.getId()), job.getCreatedBy());
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                WWorkflow wWorkflow = manageWFsService.getWorkflowFromDataAndWFType(String.valueOf(job.getId()), SWorkflows.TN_AN_ABWESENHEITEN_TRANSFER_LHR);
                lockBookingPerion(wWorkflow, response.getBody(), job.getCreatedBy());
            } else {
                log.error("LHR Response was: {}", response.getStatusCode());
                String[] mailADMINJU = azureSSOService.getGroupMemberEmailsByName(IbosRole.ADMIN_JU.getValue()).toArray(new String[0]);
                String[] mailLHRRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);
                mailService.sendEmail("gateway-service.tn-anabweseneheit.anabweseneheit-transfer-error",
                        "german",
                        null,
                        ArrayUtils.addAll(mailADMINJU, mailLHRRecipients),
                        toObjectArray(job.getDatumVon(), job.getDatumBis()),
                        toObjectArray(job.getDatumVon(), job.getDatumBis()));
            }
        }
    }

    private void lockBookingPerion(WWorkflow wWorkflow, ZeiterfassungTransferDto zeiterfassungDto, String createdBy) {
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_ZEITBUCHUNGSPERIODE_SPERREN, WWorkflowStatus.IN_PROGRESS, createdBy);
        //TODO: Lock the booking period
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_ZEITBUCHUNGSPERIODE_SPERREN, WWorkflowStatus.COMPLETED, createdBy);
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_BETEILIGTEN_INFORMIEREN, WWorkflowStatus.IN_PROGRESS, createdBy);

        String[] mailADMINJU = azureSSOService.getGroupMemberEmailsByName(IbosRole.ADMIN_JU.getValue()).toArray(new String[0]);
        String[] mailLHRRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);
        mailService.sendEmail("gateway-service.tn-anabweseneheit.anabweseneheit-transfer-success",
                "german",
                null,
                ArrayUtils.addAll(mailADMINJU, mailLHRRecipients),
                toObjectArray(zeiterfassungDto.getDatumVon(), zeiterfassungDto.getDatumBis()),
                toObjectArray(zeiterfassungDto.getDatumVon(), zeiterfassungDto.getDatumBis()));
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_BETEILIGTEN_INFORMIEREN, WWorkflowStatus.COMPLETED, createdBy);
        WWorkflowGroup wWorkflowGroup = wWorkflow.getWorkflowGroup();
        manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.TN_AN_ABWESENHEITEN_TRANSFER_LHR, WWorkflowStatus.COMPLETED, createdBy);
        wWorkflowGroup.setStatus(WWorkflowStatus.COMPLETED);
        wWorkflowGroupService.save(wWorkflowGroup);
    }
}
