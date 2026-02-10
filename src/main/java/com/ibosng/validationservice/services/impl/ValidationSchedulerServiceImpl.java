package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.validationservice.services.ValidationImportService;
import com.ibosng.workflowservice.dtos.WorkflowPayload;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationSchedulerServiceImpl {

    private final WWorkflowItemService workflowItemService;
    private final ValidatorServiceImpl validatorService;
    private final ValidationImportService validationImportService;
    private final RedissonClient redissonClient;

    public void checkIncomingFiles() {
        RLock lock = redissonClient.getLock("validationService:CheckIncomingFilesLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Checking incoming files...");
                    List<WWorkflowItem> workflowItems = workflowItemService
                            .findAllByNameAndStatus(SWorkflowItems.VALIDATING_TEILNEHMER.getValue(), WWorkflowStatus.NEW)
                            .stream()
                            .filter(wwi -> wwi.getPredecessor().getStatus().equals(WWorkflowStatus.COMPLETED))
                            .toList();

                    for (WWorkflowItem workflowItem : workflowItems) {
                        WorkflowPayload workflowPayload = new WorkflowPayload(workflowItem.getWorkflow().getId(), workflowItem.getData());
                        validatorService.validateImportedParticipants(workflowPayload);
                    }
                } catch (Exception ex) {
                    log.error("Error occured while checkIncomingFiles {}", ex.getMessage());
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

    public void importUEBASeminars() {
        String controlFlagKey = "validationService:importUEBASeminars:enabled";
        RBucket<Boolean> controlFlag = redissonClient.getBucket(controlFlagKey);  // Redis key for the control flag

        if (!Boolean.TRUE.equals(controlFlag.get())) {
            log.info("importUEBASeminars is currently disabled.");
            return;
        }
        RLock lock = redissonClient.getLock("validationService:ImportUEBASeminarsLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Importing UEBASeminars");
                    validationImportService.importUEBASeminars();
                } catch (Exception ex) {
                    log.error("Error occured while importUEBASeminars {}", ex.getMessage());
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

    public void sendFutureAbwesenheiten() {
        String controlFlagKey = "validationService:sendFutureAbwesenheiten:enabled";
        RBucket<Boolean> controlFlag = redissonClient.getBucket(controlFlagKey);  // Redis key for the control flag

        if (!Boolean.TRUE.equals(controlFlag.get())) {
            log.info("sendFutureAbwesenheiten is currently disabled.");
            return;
        }
        RLock lock = redissonClient.getLock("validationService:SendFutureAbwesenheitenLock");  // Distributed lock for MA-Abwesenheiten
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Try to acquire the lock for up to 10 minutes
                try {
                    log.info("sending Future Abwesenheiten");
                    validationImportService.importFutureAbwesenheiten();
                } catch (Exception ex) {
                    log.error("Error occured while syncMAAbwesenheitenData", ex);
                } finally {
                    lock.unlock();  // Always release the lock
                }
            } else {
                log.info("Another instance is already processing sendFutureAbwesenheiten data.");
            }
        } catch (Exception ex) {
            log.error("Failed to acquire lock", ex);
        }
    }

    public void replaceIbosRefenceWithBenutzer() {
        String controlFlagKey = "validationService:replaceIbosRefenceWithBenutzer:enabled";
        RBucket<Boolean> controlFlag = redissonClient.getBucket(controlFlagKey);  // Redis key for the control flag

        if (!Boolean.TRUE.equals(controlFlag.get())) {
            log.info("replaceIbosRefenceWithBenutzer is currently disabled.");
            return;
        }
        RLock lock = redissonClient.getLock("validationService:ReplaceIbosRefenceWithBenutzerLock");  // Distributed lock for MA-Abwesenheiten
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Try to acquire the lock for up to 10 minutes
                try {
                    log.info("replacing IbosRefenceWithBenutzer for Fuehrungskraft and Startcoach");
                    validationImportService.replaceIbosRefenceWithBenutzer();
                } catch (Exception ex) {
                    log.error("Error occured while replaceIbosRefenceWithBenutzer", ex);
                } finally {
                    lock.unlock();  // Always release the lock
                }
            } else {
                log.info("Another instance is already processing replaceIbosRefenceWithBenutzer data.");
            }
        } catch (Exception ex) {
            log.error("Failed to acquire lock", ex);
        }
    }

    public void updateMAData() {
        String controlFlagKey = "validationService:updateMAData:enabled";
        RBucket<Boolean> controlFlag = redissonClient.getBucket(controlFlagKey);  // Redis key for the control flag

        if (!Boolean.TRUE.equals(controlFlag.get())) { // Check if update is enabled via Redis flag
            log.info("updateMAData is currently disabled.");
            return;
        }

        RLock lock = redissonClient.getLock("validationService:UpdateMADataLock");  // Distributed lock for MA-Abwesenheiten
        boolean isLockAcquired = false;

        try {
            isLockAcquired = lock.tryLock(0, 10, TimeUnit.MINUTES);  // Try to acquire the lock for up to 10 minutes
            if (isLockAcquired) {
                log.info("Starting updateMAData process.");
                validationImportService.importDataFromIbos();
            } else {
                log.info("Another instance is already processing updateMAData.");
            }
        } catch (Exception ex) {
            log.error("Error occurred while updating MA data", ex);
        } finally {
            if (isLockAcquired) {
                try {
                    lock.unlock();
                } catch (IllegalMonitorStateException e) {
                    log.warn("Attempted to unlock without holding the lock", e);
                }
            }
        }
    }
}
