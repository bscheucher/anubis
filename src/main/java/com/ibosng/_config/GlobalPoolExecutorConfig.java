package com.ibosng._config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class GlobalPoolExecutorConfig {

    /**
     * taskExecutor have always <b>CorePoolSize</b> threads </br>
     * when <b>CorePoolSize</b> is reached, thread will be in the in Queue</br>
     * when <b>QueueCapacity</b> is reached, additional threads will be created until <b>MaxPoolSize</b> is reached</br>
     *
     *
     */
    @Bean
    public Executor executorWithTaskDecorator(TaskDecorator contextCopyingDecorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(150); //always will keep alive even in idle
        executor.setQueueCapacity(200);
        executor.setMaxPoolSize(200);
        executor.setThreadNamePrefix("Async task - ");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setTaskDecorator(contextCopyingDecorator); // Apply the copied requestAttributes from original http request
        executor.initialize();
        return executor;
    }
}
