package com.ibosng.moxisservice.services.impl;

import com.ibosng.moxisservice.services.MoxisUpdateJobsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoxisSchedulerServiceImpl {

    private final MoxisUpdateJobsService moxisUpdateJobsService;
    private final RedissonClient redissonClient;


    public void checkSignedDocuments() {
        RLock lock = redissonClient.getLock("moxisService:CheckSignedDocumentsLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Checking status of jobs");
                    moxisUpdateJobsService.updateActiveMoxisJobsSeparately();
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


    public void checkNewJobs() {
        RLock lock = redissonClient.getLock("moxisService:CheckNewJobsLock");  // Distributed lock with Redis
        try {
            if (lock.tryLock(0, 10, TimeUnit.MINUTES)) {  // Attempt to acquire the lock
                try {
                    log.info("Checking for new jobs");
                    moxisUpdateJobsService.processMoxisRetryJob();
                } catch (Exception ex) {
                    log.error("Error occurred while checking for new jobs", ex);
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
