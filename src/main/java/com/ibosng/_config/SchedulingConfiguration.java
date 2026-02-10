package com.ibosng._config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulingConfiguration {

    @Value("${jobs.legacySchedulersEnabled}")
    private boolean legacySchedulersEnabled;

    @Value("${jobs.lhrOutboxSchedulerEnabled}")
    private boolean lhrOutboxSchedulerEnabled;

    @Value("${jobs.lhrOutboxSchedulerDelayInMilliseconds}")
    private String lhrOutboxSchedulerDelayInMilliseconds;

    @PostConstruct
    void logAfterStartup() {
        log.info("INFO: Running with legacy jobs {}",
                legacySchedulersEnabled ? "ENABLED" : "DISABLED");

        log.info("INFO: Running with LHR outbox scheduler {}",
                lhrOutboxSchedulerEnabled ? "ENABLED" : "DISABLED");

        if (lhrOutboxSchedulerEnabled) {
            log.info("INFO: FixedDelay of LHR outbox scheduler set to {} milliseconds",
                    lhrOutboxSchedulerDelayInMilliseconds);
        }
    }


    /**
     * Originally present in LHR service; setting poolSize to 20.
     * Other Scheduled Jobs did not use an increased poolSize.
     * Left globally active to avoid defining multiple ThreadPoolTaskScheduler beans
     * and referencing them on all Scheduled jobs.
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20); //can run simultaneously 20 crons
        scheduler.setThreadNamePrefix("cron-");
        scheduler.initialize();
        return scheduler;
    }
}
