package com.ibosng.fileimportservice.services.impl;

import com.ibosng.fileimportservice.services.FileParserService;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileImportSchedulerServiceImpl {

    private final SharePointService sharePointService;
    private final RedissonClient redissonClient;
    private final SharePointProperties sharePointProperties;
    private final FileParserService fileParserService;


    public void checkIncomingFiles() {
        RLock lock = redissonClient.getLock("fileImportService:CheckIncomingFilesLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    DriveItemCollectionPage items = sharePointService.getContentsOfFolder(Optional.of(sharePointProperties.getSource()));
                    fileParserService.manageFiles(items);
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
