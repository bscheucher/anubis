package com.ibosng.lhrservice.services.impl;

import com.ibosng._service.AsyncService;
import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.lhr.LhrJobStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeiterfassung.Auszahlungsantrag;
import com.ibosng.dbservice.entities.zeiterfassung.AuszahlungsantragStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.lhr.LhrJobService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeiterfassung.AuszahlungsantragService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungTransferService;
import com.ibosng.lhrservice.dtos.zeitdaten.AnfrageSuccessDto;
import com.ibosng.lhrservice.enums.LhrDocuments;
import com.ibosng.lhrservice.services.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Constants.*;

@Service
@Slf4j
public class LhrSchedulerServiceImpl implements SchedulerService {
    @Getter
    @Value("${lhrPreEintrittDataSubmissionPeriod:#{null}}")
    private Integer preEintrittDataSubmissionPeriod;

    @Getter
    @Value("${lhrMinusDaysLastSyncOfDocuments:#{null}}")
    private Integer lhrMinusDaysLastSyncOfDocuments;

    @Getter
    @Value("${lhrMinusMontsCloseMonths:#{null}}")
    private Integer lhrMinusMontsCloseMonths;


    private final LHRUpdateJobsService lhrUpdateJobsService;
    private final LhrJobService lhrJobService;
    private final LHRZeitdatenService lhrZeitdatenService;
    private final AuszahlungsantragService auszahlungsantragService;
    private final LHRAuszahlungsService lhrAuszahlungsService;
    private final LHRUrlaubService lhrUrlaubService;
    private final ZeiterfassungTransferService zeiterfassungTransferService;
    private final LHRDokumenteService lhrDokumenteService;
    private final PersonalnummerService personalnummerService;
    private final StammdatenService stammdatenService;
    private final RedissonClient redissonClient;
    private final LeistungserfassungService leistungserfassungService;
    private final AsyncService asyncService;

    public LhrSchedulerServiceImpl(LHRUpdateJobsService lhrUpdateJobsService,
                                   LhrJobService lhrJobService,
                                   LHRZeitdatenService lhrZeitdatenService,
                                   AuszahlungsantragService auszahlungsantragService,
                                   LHRAuszahlungsService lhrAuszahlungsService,
                                   LHRUrlaubService lhrUrlaubService,
                                   ZeiterfassungTransferService zeiterfassungTransferService,
                                   LHRDokumenteService lhrDokumenteService,
                                   PersonalnummerService personalnummerService,
                                   StammdatenService stammdatenService,
                                   RedissonClient redissonClient,
                                   LeistungserfassungService leistungserfassungService,
                                   AsyncService asyncService) {
        this.lhrUpdateJobsService = lhrUpdateJobsService;
        this.lhrJobService = lhrJobService;
        this.lhrZeitdatenService = lhrZeitdatenService;
        this.auszahlungsantragService = auszahlungsantragService;
        this.lhrAuszahlungsService = lhrAuszahlungsService;
        this.lhrUrlaubService = lhrUrlaubService;
        this.zeiterfassungTransferService = zeiterfassungTransferService;
        this.lhrDokumenteService = lhrDokumenteService;
        this.personalnummerService = personalnummerService;
        this.stammdatenService = stammdatenService;
        this.redissonClient = redissonClient;
        this.leistungserfassungService = leistungserfassungService;
        this.asyncService = asyncService;
    }

    public void checkIncomingJobs() {
        RLock lock = redissonClient.getLock("lhrService:CheckIncomingJobsLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Getting pending jobs for LHR");
                    List<LhrJob> jobs = lhrJobService.findAllByStatusAndEintrittLessThanEqual(LhrJobStatus.PENDING, LocalDate.now().plusDays(getPreEintrittDataSubmissionPeriod()));
                    lhrUpdateJobsService.executeLhrJobs(jobs);
                } catch (Exception ex) {
                    log.error("Error occurred while getting pending jobs", ex);
                } finally {
                    lock.unlock();  // Always release the lock in the finally block
                }
            } else {
                log.info("Another instance is already processing this task.");
            }
        } catch (InterruptedException e) {
            log.error("Failed to acquire lock", e);
            Thread.currentThread().interrupt();  // Restore interrupted state
        }
    }


    public void checkIncomingZeiterfassungTransfer() {
        RLock lock = redissonClient.getLock("lhrService:CheckIncomingZeiterfassungTransferLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Getting pending zeittransfer for LHR");
                    List<ZeiterfassungTransfer> zeiterfassungTransfers = zeiterfassungTransferService.findAllByStatus(ZeiterfassungStatus.VALID);
                    lhrUpdateJobsService.executeZeiterfassungTransfers(zeiterfassungTransfers);
                } catch (Exception ex) {
                    log.error("Error occurred while getting pending zeittransfer for LHR", ex);
                } finally {
                    lock.unlock();  // Always release the lock in the finally block
                }
            } else {
                log.info("Another instance is already processing this task.");
            }
        } catch (InterruptedException e) {
            log.error("Failed to acquire lock", e);
            Thread.currentThread().interrupt();  // Restore interrupted state
        }
    }

    public void checkPendingAuszahlungsantraege() {
        RLock lock = redissonClient.getLock("lhrService:CheckPendingAuszahlungsantraegeLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Updating WAITING Auszahlungsantraege");
                    List<Auszahlungsantrag> waitingAntraege = auszahlungsantragService.findByStatus(AuszahlungsantragStatus.WAITING);
                    for (Auszahlungsantrag auszahlungsantrag : waitingAntraege) {
                        ResponseEntity<AnfrageSuccessDto> fetchedAntrag = lhrAuszahlungsService.getAuszahlungsanfrage(auszahlungsantrag.getPersonalnummer().getPersonalnummer(), auszahlungsantrag.getAnfrageNr());
                        if (fetchedAntrag != null && fetchedAntrag.getBody() != null && !isNullOrBlank(fetchedAntrag.getBody().getStatus())) {
                            auszahlungsantrag.setStatus(AuszahlungsantragStatus.fromValue(fetchedAntrag.getBody().getStatus()));
                            auszahlungsantragService.save(auszahlungsantrag);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Error occurred while updating WAITING Auszahlungsantraege {}", ex.getMessage());
                } finally {
                    lock.unlock();  // Always release the lock in the finally block
                }
            } else {
                log.info("Another instance is already processing this task.");
            }
        } catch (InterruptedException e) {
            log.error("Failed to acquire lock", e);
            Thread.currentThread().interrupt();  // Restore interrupted state
        }
    }


    public void checkAuszahlbareUeberstunden() {
        RLock lock = redissonClient.getLock("lhrService:CheckAuszahlbareUeberstundenLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Checking for Auszahlbarestunden");
                    lhrZeitdatenService.checkForAuszahlbareStunden();
                } catch (Exception ex) {
                    log.error("Error while checking for Auszahlbarestunden", ex);
                } finally {
                    lock.unlock();  // Always release the lock in the finally block
                }
            } else {
                log.info("Another instance is already processing this task.");
            }
        } catch (InterruptedException e) {
            log.error("Failed to acquire lock", e);
            Thread.currentThread().interrupt();  // Restore interrupted state
        }
    }


    @Override
    public void closeMonaten() {
        RLock lock = redissonClient.getLock("lhrService:CloseMonatenLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    LocalDate lastSyncMonth = LocalDate.now().minusMonths(getLhrMinusMontsCloseMonths());
                    lhrZeitdatenService.closeMonaten(lastSyncMonth);
                } catch (Exception ex) {
                    log.error("Error occured while closing monat {}", ex.getMessage());
                } finally {
                    lock.unlock();  // Always release the lock in the finally block
                }
            } else {
                log.info("Another instance is already processing this task.");
            }
        } catch (InterruptedException e) {
            log.error("Failed to acquire lock", e);
            Thread.currentThread().interrupt();  // Restore interrupted state
        }
    }

    @Override
    public void syncLhrDocuments() {
        LocalDate lastSyncOfDocuments;
        List<Personalnummer> personalnummerList = personalnummerService.findAllByMitarbeiterType(MitarbeiterType.MITARBEITER);
        for (Personalnummer personalnummer : personalnummerList) {
            if (personalnummer.getOnboardedOn() != null && LocalDateTime.now().withDayOfMonth(1).isBefore(personalnummer.getOnboardedOn())) {
                lastSyncOfDocuments = LocalDate.of(2025, 1, 1);
            } else {
                lastSyncOfDocuments = LocalDate.now().minusDays(lhrMinusDaysLastSyncOfDocuments).withDayOfMonth(1);
            }
            log.info("Syncing documents as of {}", lastSyncOfDocuments);
            RLock lock = redissonClient.getLock("lhrService:SyncLhrDocumentsLock:" + personalnummer.getPersonalnummer());

            try {
                if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Try to acquire the lock for 10 minutes
                    try {
                        log.info("Sync documents for personalnummer: {}", personalnummer.getPersonalnummer());
                        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer.getPersonalnummer());
                        final String identifier = getIdentifier(stammdaten);

                        lhrDokumenteService.processAndUploadFiles(REGEX_L16, "L16", LhrDocuments.L16, identifier, personalnummer.getPersonalnummer(), lastSyncOfDocuments);
                        lhrDokumenteService.processAndUploadFiles(REGEX_ELDA, "ELDA", LhrDocuments.ELDA, identifier, personalnummer.getPersonalnummer(), lastSyncOfDocuments);
                        lhrDokumenteService.processAndUploadFiles(REGEX_GEHALTSZETTEL, "Gehaltszettel", LhrDocuments.NETTOZETTEL, identifier, personalnummer.getPersonalnummer(), lastSyncOfDocuments);
                    } catch (Exception ex) {
                        log.error("Error occured while syncLhrDocuments {}", ex.getMessage());
                    } finally {
                        lock.unlock();  // Ensure the lock is always released
                    }
                } else {
                    log.info("Another instance is already processing sync for personalnummer: {}", personalnummer.getPersonalnummer());
                }
            } catch (Exception ex) {
                log.error("Error occurred while syncing LHR documents for personalnummer: {}", personalnummer.getPersonalnummer(), ex);
            }
        }
    }

    @NotNull
    private static String getIdentifier(Stammdaten stammdaten) {
        if (stammdaten != null) {
            return stammdaten.getVorname().toUpperCase().replace(" ", "_") +
                    "_" + stammdaten.getNachname().toUpperCase().replace(" ", "_");
        }
        return "null";
    }

    @Override
    public void syncMAAbwesenheitenData() {
        RLock lock = redissonClient.getLock("lhrService:SyncMAAbwesenheitenDataLock");  // Distributed lock for MA-Abwesenheiten

        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Try to acquire the lock for up to 10 minutes
                try {
                    log.info("Syncing MA-Abwesenheiten data");
                    lhrUrlaubService.calculateAbwesenheiten();
                } catch (Exception ex) {
                    log.error("Error occured while syncMAAbwesenheitenData {}", ex.getMessage());
                } finally {
                    lock.unlock();  // Always release the lock
                }
            } else {
                log.info("Another instance is already processing MA-Abwesenheiten data.");
            }
        } catch (Exception ex) {
            log.error("Failed to acquire lock", ex);
        }
    }

    @Override
    public void resyncLeistungserfassungData() {
        final String originalName = Thread.currentThread().getName();
        Thread.currentThread().setName("leistung-sync");

        RLock lock = redissonClient.getLock("lhrService:syncLeistungserfassung");

        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Try to acquire the lock for up to 10 minutes
                List<CompletableFuture<ResponseEntity<?>>> futures = new ArrayList<>();
                List<Leistungserfassung> leistungserfassungs = leistungserfassungService.findAllNotSyncedWithLhr();
                try {
                    log.info("Syncing Leistungserfassung with LHR");
                    for (Leistungserfassung leistungserfassung : leistungserfassungs) {
                        if (futures.size() >= 25) {
                            log.info("Waiting for threads to be finished");
                            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); //avoid overloading thread pool executor
                            futures.clear();
                            log.info("Threads finished");
                        }

                        futures.add(asyncService.asyncExecutor(() -> lhrUrlaubService.sendLeistungsdatumToLhr(
                                leistungserfassung.getPersonalnummer().getId(),
                                leistungserfassung.getLeistungsdatum().format(DateTimeFormatter.ISO_LOCAL_DATE)))
                        );
                    }
                } catch (Exception ex) {
                    log.error("Error occured while Leistungserfassung-sync {}", ex.getMessage());
                } finally {
                    lock.unlock();  // Always release the lock
                    log.info("Waiting for threads to be finished");
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); //avoid overloading thread pool executor
                    futures.clear();
                    log.info("Leistungserfassung sync with LHR, finished for {} entities", leistungserfassungs.size());
                }
            } else {
                log.info("Another instance is already processing Leistungserfassung data.");
            }
        } catch (Exception ex) {
            log.error("Failed to acquire lock", ex);
        }
        Thread.currentThread().setName(originalName);
    }

    public void abwesenheitenCheck() {
        final String originalName = Thread.currentThread().getName();
        Thread.currentThread().setName("abw-comparing");
        RLock lock = redissonClient.getLock("lhrService:abwesenheitenComparingCron");

        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {
                lhrUrlaubService.compareAndUpdateErroneousUrlaube();
                //todo add compareAndUpdateErroneousZeitausgleich0
            } else {
                log.info("Another instance is already processing abwesenheiten data.");
            }
        } catch (Exception ex) {
            log.error("Failed to acquire lock", ex);
        } finally {
            log.info("Abwesenheit processing finished");
            lock.unlock();
        }
        Thread.currentThread().setName(originalName);
    }
}
