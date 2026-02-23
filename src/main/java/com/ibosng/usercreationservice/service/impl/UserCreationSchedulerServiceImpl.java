package com.ibosng.usercreationservice.service.impl;

import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.ibosng.usercreationservice.service.UserCreationAnlageIbosNGService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreationSchedulerServiceImpl {

    private final SharePointProperties sharePointProperties;
    private final SharePointService sharePointService;
    private final UserCreationAnlageIbosNGService userAnlageIbosNGService;
    private final RedissonClient redissonClient;

    public void checkIncomingFiles() {
        RLock lock = redissonClient.getLock("userCreationService:CheckIncomingFilesLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Checking incoming files");
                    userAnlageIbosNGService.proccessMitarbeiters(
                            sharePointService.getUploadedFiles(sharePointProperties.getAngelegteBenutzerNeu()));
                } catch (Exception ex) {
                    log.error("Error occurred while checking incoming files", ex);
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
}
