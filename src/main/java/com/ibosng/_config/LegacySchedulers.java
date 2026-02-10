package com.ibosng._config;

import com.ibosng.aiservice.services.AISchedulerService;
import com.ibosng.fileimportservice.services.impl.FileImportSchedulerServiceImpl;
import com.ibosng.lhrservice.services.impl.LhrSchedulerServiceImpl;
import com.ibosng.moxisservice.services.impl.MoxisSchedulerServiceImpl;
import com.ibosng.usercreationservice.service.impl.UserCreationSchedulerServiceImpl;
import com.ibosng.validationservice.services.impl.ValidationSchedulerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "jobs", name = "legacySchedulersEnabled", havingValue = "true")
@RequiredArgsConstructor
public class LegacySchedulers {

    private static final String CRON_EVERY_30_MINUTES = "0 0/30 * * * ?";
    private static final String CRON_EVERY_HOUR = "0 0 * * * ?";
    private static final String CRON_1_MINUTE = "0 * * * * *";
    private static final String CRON_EOM = "0 0 0 L * ?";
    private static final String CRON_6_AM = "0 0 6 * * ?";


    private final AISchedulerService aiSchedulerService;
    private final ValidationSchedulerServiceImpl validationSchedulerService;
    private final UserCreationSchedulerServiceImpl userCreationSchedulerService;
    private final FileImportSchedulerServiceImpl fileImportSchedulerService;
    private final MoxisSchedulerServiceImpl moxisSchedulerService;
    private final LhrSchedulerServiceImpl lhrSchedulerService;


    @Scheduled(cron = CRON_EVERY_30_MINUTES)
    public void checkIncomingFilesScheduledValidationService() {
        validationSchedulerService.checkIncomingFiles();
    }

    @Scheduled(cron = "${cronExpressionImportUEBASeminars:0 0 23 31 12 ?}")
    public void importUEBASeminarsScheduled() {
        validationSchedulerService.importUEBASeminars();
    }

    @Scheduled(cron = "${cronExpressionImportFutureAbwesenheiten:0 0 23 31 12 ?}")
    public void sendFutureAbwesenheitenScheduled() {
        validationSchedulerService.sendFutureAbwesenheiten();
    }

    @Scheduled(cron = "${cronReplaceIbosRefenceWithBenutzer:0 0 23 31 12 ?}")
    public void replaceIbosRefenceWithBenutzerScheduled() {
        validationSchedulerService.replaceIbosRefenceWithBenutzer();
    }

    @Scheduled(cron = "${cronExpressionImportMAData:0 0 23 31 12 ?}")
    public void updateMADataScheduled() {
        validationSchedulerService.updateMAData();
    }

    @Scheduled(cron = CRON_1_MINUTE)
    public void checkIncomingFilesScheduledUserCreationService() {
        userCreationSchedulerService.checkIncomingFiles();
    }

    @Scheduled(cron = CRON_1_MINUTE)
    public void checkIncomingFilesScheduledFileImportService() {
        fileImportSchedulerService.checkIncomingFiles();
    }

    @Scheduled(cron = CRON_1_MINUTE)
    public void checkSignedDocumentsScheduled() {
        moxisSchedulerService.checkSignedDocuments();
    }

    @Scheduled(cron = CRON_1_MINUTE)
    public void checkNewJobsScheduled() {
        moxisSchedulerService.checkNewJobs();
    }

    @Scheduled(cron = CRON_EVERY_HOUR)
    public void updateChatSourcesScheduled() {
        aiSchedulerService.updateChatSources();
    }

    @Scheduled(cron = CRON_1_MINUTE)
    public void checkIncomingJobsScheduled() {
        lhrSchedulerService.checkIncomingJobs();
    }

    @Scheduled(cron = CRON_1_MINUTE)
    public void checkIncomingZeiterfassungTransferScheduled() {
        lhrSchedulerService.checkIncomingZeiterfassungTransfer();
    }

    @Scheduled(cron = CRON_1_MINUTE)
    public void checkPendingAuszahlungsantraegeScheduled() {
        lhrSchedulerService.checkPendingAuszahlungsantraege();
    }

    @Scheduled(cron = CRON_EOM)
    public void checkAuszahlbareUeberstundenScheduled() {
        lhrSchedulerService.checkAuszahlbareUeberstunden();
    }

    @Scheduled(cron = "${cronExpressionCheckClosedMonaten:0 1 0 * * *}")
    public void closeMonatenScheduled() {
        lhrSchedulerService.closeMonaten();
    }

    @Scheduled(cron = "${cronExpressionSyncLhrDocuments:0 0 18 * * ?}")
    public void syncLhrDocumentsScheduled() {
        lhrSchedulerService.syncLhrDocuments();
    }

    @Scheduled(cron = CRON_6_AM)
    public void syncMAAbwesenheitenDataScheduled() {
        lhrSchedulerService.syncMAAbwesenheitenData();
    }

    @Scheduled(cron = "${cronResyncLeistungserfassungData:0 0 * * * *}")
    public void resyncLeistungserfassungDataScheduled() {
        lhrSchedulerService.resyncLeistungserfassungData();
    }

    @Scheduled(initialDelay = 100)
    public void abwesenheitenCheckScheduled() {
        lhrSchedulerService.abwesenheitenCheck();
    }







}
