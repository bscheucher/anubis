package com.ibosng._config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Configuration
@EnableAspectJAutoProxy
public class GlobalAppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * The source of truth for standard Web Requests.
     * Managed by Spring to live only for the duration of one HTTP request.
     */
    @Bean
    @RequestScope
    public GlobalUserHolder requestScopedUserHolder() {
        return new GlobalUserHolder();
    }

    /**
     * The bridge for Async threads.
     * Used to store user context when a background thread is spawned.
     */
    @Bean
    public ThreadLocal<GlobalUserHolder> threadLocalUserHolder() {
        return ThreadLocal.withInitial(GlobalUserHolder::new);
    }

    /**
     * UNIFIED USER HOLDER (The "Smart Bean")
     * <p>
     * This bean is marked as @Primary so it is the one injected into services.
     * It provides TRANSPARENCY: Services don't need to know if they are running
     * in a web thread or an async thread.
     * <p>
     * HOW IT WORKS:
     * 1. TRY-CATCH LOGIC: It first attempts to access the 'requestScopedUserHolder'.
     * - In a Web Request: Success.
     * - In an Async Thread: The RequestScope is inactive, Spring throws an exception.
     * 2. FALLBACK: The catch block redirects the call to 'threadLocalUserHolder',
     * ensuring the data is retrieved even if the original HTTP request has already finished.
     * <p>
     * No Signature Changes: We don't have to change any public method signatures. The service just asks the GlobalUserHolder for data, and it "just works."
     * Safety: The "finally" block in the decorator ensures that user "A"'s data never accidentally appears when the same thread is later reused to process a request for user "B".
     * Scope Independence: Background tasks (like generating heavy reports or syncing data) can continue running even if the user closes their browser and the HTTP request technically "dies."
     */
    @Bean
    @Primary
    public GlobalUserHolder globalUserHolder(GlobalUserHolder requestScopedUserHolder, ThreadLocal<GlobalUserHolder> threadLocalUserHolder) {
        return new GlobalUserHolder() {
            @Override
            public String getUsername() {
                try {
                    return requestScopedUserHolder.getUsername();
                } catch (Exception e) {
                    return threadLocalUserHolder.get().getUsername();
                }
            }

            @Override
            public Integer getUserId() {
                try {
                    return requestScopedUserHolder.getUserId();
                } catch (Exception e) {
                    return threadLocalUserHolder.get().getUserId();
                }
            }

            @Override
            public void setUsername(String username) {
                try {
                    requestScopedUserHolder.setUsername(username);
                } catch (Exception e) {
                    threadLocalUserHolder.get().setUsername(username);
                }
            }

            @Override
            public void setUserId(Integer userId) {
                try {
                    requestScopedUserHolder.setUserId(userId);
                } catch (Exception e) {
                    threadLocalUserHolder.get().setUserId(userId);
                }
            }
        };
    }

    /**
     * TASK DECORATOR (The Context Carrier)
     * <p>
     * This is the "glue" that makes the Smart Bean work across threads.
     * When an async task is submitted to the Executor:
     * 1. CAPTURE: It reads the user data from the RequestScope (while still in the main thread).
     * 2. HANDOFF: It wraps the task and, when the background thread starts, injects
     * the captured data into the ThreadLocal.
     * 3. CLEANUP: In the 'finally' block, it clears the ThreadLocal to prevent identity
     * leaks between tasks in the thread pool.
     */
    @Bean
    public TaskDecorator contextCopyingDecorator(GlobalUserHolder requestScopedUserHolder, ThreadLocal<GlobalUserHolder> threadLocalUserHolder) {
        return runnable -> {
            // 1. Capture requestAttributes from the REQUEST thread
            String username = requestScopedUserHolder.getUsername();
            Integer userId = requestScopedUserHolder.getUserId();
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            return () -> {
                try {
                    // 2. Apply requestAttributes to the ASYNC thread
                    if (requestAttributes != null) RequestContextHolder.setRequestAttributes(requestAttributes);

                    GlobalUserHolder asyncHolder = threadLocalUserHolder.get();
                    asyncHolder.setUsername(username);
                    asyncHolder.setUserId(userId);

                    runnable.run();
                } finally {
                    // 3. Clean up to prevent the passing user identity across threads.
                    // Thread pools reuse threads. Without this, the next thread might "inherit" the previous user's identity,
                    // causing severe security bugs or data corruption.next task that runs on this thread might "inherit" the previous user's identity, leading to severe security bugs or data corruption.
                    RequestContextHolder.resetRequestAttributes();
                    threadLocalUserHolder.remove();
                }
            };
        };
    }
}