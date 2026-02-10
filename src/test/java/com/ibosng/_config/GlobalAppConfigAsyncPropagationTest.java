package com.ibosng._config;

import com.ibosng._service.AsyncService;
import com.ibosng._service.impl.AsyncServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Purpose
 * -------
 * This test suite documents and verifies the runtime behavior of the primary {@link GlobalUserHolder}
 * bean and the asynchronous execution infrastructure configured by {@link GlobalPoolExecutorConfig}.
 * <p>
 * What we verify
 * --------------
 * 1) Request-scope propagation to async tasks: The configured TaskDecorator must capture the
 * request-scoped user context and make it visible in the async thread when executing via the
 * application-provided {@link AsyncService}.
 * 2) ThreadLocal fallback: When no HTTP request is active (no RequestScope), the primary
 * {@link GlobalUserHolder} bean should transparently write to and read from a ThreadLocal-backed
 * fallback.
 * 3) Backend default: When neither RequestScope nor ThreadLocal provides a username, the value must
 * fall back to the backend constant {@link GlobalUserHolder#IBOSNG_BACKEND}.
 * <p>
 * Why this matters
 * ----------------
 * - Background async tasks (e.g., @Async methods) frequently run outside of a web request thread.
 * Without explicit propagation, request-scoped attributes are lost. These tests ensure the
 * configured TaskDecorator captures and propagates the relevant context.
 * - Some code paths (e.g., scheduled jobs) execute with no request context at all. The ThreadLocal
 * fallback and default constant preserve predictable behavior in those environments.
 */
@EnableAsync
@SpringBootTest(classes = {
        GlobalAppConfig.class,
        GlobalPoolExecutorConfig.class,
        AsyncServiceImpl.class
})
@Import(GlobalAppConfig.class)
class GlobalAppConfigAsyncPropagationTest {

    @Autowired
    private GlobalUserHolder globalUserHolder;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private ThreadLocal<GlobalUserHolder> threadLocalUserHolder;

    // Mock collaborators required by AsyncServiceImpl constructor
    @MockBean
    private com.ibosng.validationservice.services.Validation2LHRService validation2LHRService;
    @MockBean
    private com.ibosng.dbservice.services.lhr.AbwesenheitService abwesenheitService;
    @MockBean
    private com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService zeitbuchungService;
    @MockBean
    private com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService leistungserfassungService;
    @MockBean
    private com.ibosng.dbservice.services.ZeitausgleichService zeitausgleichService;
    @MockBean
    private com.ibosng.validationservice.services.ValidatorService validatorService;

    /**
     * Ensure isolation between tests by clearing both the Spring RequestContext and the
     * ThreadLocal-based user holder.
     */
    @AfterEach
    void cleanupRequestContext() {
        RequestContextHolder.resetRequestAttributes();
        threadLocalUserHolder.remove();
    }

    /**
     * Scenario: An HTTP request is active and sets username/userId into the primary GlobalUserHolder.
     * Expectation: When invoking async work via {@link AsyncService#asyncExecutor},
     * the async thread sees the same values because the TaskDecorator captured the request attributes.
     */
    @Test
    void asyncTaskGetsUserFromRequestScopeViaTaskDecorator() throws ExecutionException, InterruptedException {
        // Arrange: simulate an HTTP request so that RequestScope is active
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);

        // Set user context in the request-scoped user holder via the primary bean
        String expectedUsername = "alice";
        Integer expectedUserId = 42;
        globalUserHolder.setUsername(expectedUsername);
        globalUserHolder.setUserId(expectedUserId);

        // Act: execute on the configured async executor. The TaskDecorator should propagate
        // the RequestAttributes so that GlobalUserHolder resolves to the same request scope.
        CompletableFuture<String> usernameFuture = asyncService.asyncExecutor(globalUserHolder::getUsername);
        CompletableFuture<Integer> userIdFuture = asyncService.asyncExecutor(globalUserHolder::getUserId);

        // Assert: values seen in async thread match what was set in the request thread
        assertThat(usernameFuture.get()).isEqualTo(expectedUsername);
        assertThat(userIdFuture.get()).isEqualTo(expectedUserId);
    }

    /**
     * Scenario: Submit an async task while an HTTP request is active, then immediately reset
     * RequestContext in the main thread before the async task completes. Expectation: the async
     * task still sees the original request-scoped values because the TaskDecorator captured the
     * RequestAttributes at submission time.
     */
    @Test
    void asyncTaskStillSeesCapturedRequestAttributes_whenMainThreadResetsAfterSubmission() throws Exception {
        // Arrange: simulate an HTTP request so that RequestScope is active
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);

        String expectedUsername = "carol";
        Integer expectedUserId = 73;
        globalUserHolder.setUsername(expectedUsername);
        globalUserHolder.setUserId(expectedUserId);

        // Use a latch to delay the async supplier, giving the main thread time to reset context
        CountDownLatch proceedLatch = new CountDownLatch(1);

        CompletableFuture<String> usernameFuture = asyncService.asyncExecutor(() -> {
            try {
                // Wait until the main thread resets RequestContext and signals us to proceed
                boolean released = proceedLatch.await(5, TimeUnit.SECONDS);
                if (!released) {
                    throw new IllegalStateException("Timed out waiting for proceedLatch");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            return globalUserHolder.getUsername();
        });

        // Act: immediately reset RequestContext in the main thread before the async supplier runs
        RequestContextHolder.resetRequestAttributes();
        // Allow async task to proceed now that context has been cleared in the caller thread
        proceedLatch.countDown();

        // Assert: async task still reads the values captured at submission time
        assertThat(usernameFuture.get()).isEqualTo(expectedUsername);
    }

    /**
     * Scenario: Two sequential tasks on the same executor. First task runs under a request with a user
     * set. Second task is submitted with no request/user provided (caller has no RequestAttributes).
     * Expectation: The TaskDecorator does not leak the first task's context into the second task,
     * so the second resolves to the backend default username.
     */
    @Test
    void sequentialTasks_sameExecutor_secondTaskSeesBackendDefault_whenNoUserProvided() throws Exception {
        // First submission: with request/user
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        ServletRequestAttributes attrs1 = new ServletRequestAttributes(request1);
        RequestContextHolder.setRequestAttributes(attrs1);

        String firstUser = "dave";
        globalUserHolder.setUsername(firstUser);
        globalUserHolder.setUserId(101);

        String firstSeen = asyncService.asyncExecutor(globalUserHolder::getUsername).get();
        assertThat(firstSeen).isEqualTo(firstUser);

        // Prepare for second submission: caller provides NO request/user this time
        // (We clear the caller thread RequestAttributes so the TaskDecorator captures none.)
        RequestContextHolder.resetRequestAttributes();

        String secondSeen = asyncService.asyncExecutor(globalUserHolder::getUsername).get();
        assertThat(secondSeen).isEqualTo(GlobalUserHolder.IBOSNG_BACKEND);
    }

    /**
     * Scenario: Two sequential tasks on the same executor. First task runs with user "erin".
     * Second task runs under a different request with user "frank". Expectation: The second task
     * sees only its own request context ("frank") and not the first task's values.
     */
    @Test
    void sequentialTasks_sameExecutor_secondTaskSeesItsOwnDifferentUser() throws Exception {
        // First submission: request with user "erin"
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        ServletRequestAttributes attrs1 = new ServletRequestAttributes(request1);
        RequestContextHolder.setRequestAttributes(attrs1);
        globalUserHolder.setUsername("erin");
        globalUserHolder.setUserId(201);

        String firstSeen = asyncService.asyncExecutor(globalUserHolder::getUsername).get();
        assertThat(firstSeen).isEqualTo("erin");

        // Second submission: different request with user "frank"
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        ServletRequestAttributes attrs2 = new ServletRequestAttributes(request2);
        RequestContextHolder.setRequestAttributes(attrs2);
        globalUserHolder.setUsername("frank");
        globalUserHolder.setUserId(202);

        String secondSeen = asyncService.asyncExecutor(globalUserHolder::getUsername).get();
        assertThat(secondSeen).isEqualTo("frank");
    }

    /**
     * Scenario: No HTTP request is active (e.g., non-web thread). When we set values via the primary
     * GlobalUserHolder, it should transparently use the ThreadLocal fallback. Subsequent reads from
     * either the ThreadLocal or the primary bean should return the same values.
     */
    @Test
    void globalUserHolderFallsBackToThreadLocalWhenNoRequestScope() {
        // Ensure no request attributes are present
        RequestContextHolder.resetRequestAttributes();

        // When RequestScope is inactive, the primary bean should write/read to the ThreadLocal fallback
        String expectedUsername = "bob";
        Integer expectedUserId = 7;

        globalUserHolder.setUsername(expectedUsername);
        globalUserHolder.setUserId(expectedUserId);

        // Verify it actually stored the values in the ThreadLocal holder
        GlobalUserHolder tl = threadLocalUserHolder.get();
        assertThat(tl.getUsername()).isEqualTo(expectedUsername);
        assertThat(tl.getUserId()).isEqualTo(expectedUserId);

        // And the primary bean reads them back correctly
        assertThat(globalUserHolder.getUsername()).isEqualTo(expectedUsername);
        assertThat(globalUserHolder.getUserId()).isEqualTo(expectedUserId);
    }

    /**
     * Scenario: In the current thread there is no request context and nothing has been written into
     * the ThreadLocal. The username should fall back to the constant {@code IBOSNG_BACKEND}.
     */
    @Test
    void whenNoRequestScope_getUsernameFallsBackToBackendConstant_inCurrentThread() {
        // Simulate a non-HTTP context (like a @Scheduled job): no RequestAttributes available
        RequestContextHolder.resetRequestAttributes();

        String username = globalUserHolder.getUsername();

        assertThat(username).isEqualTo(GlobalUserHolder.IBOSNG_BACKEND);
    }

    /**
     * Scenario: Execute on a plain background thread with no request context and no ThreadLocal value.
     * Expectation: The username resolves to the backend default constant in that thread as well.
     */
    @Test
    void whenRunningOnPlainThread_getUsernameFallsBackToBackendConstant() throws InterruptedException {
        // Ensure no request attributes leak from other tests
        RequestContextHolder.resetRequestAttributes();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> result = new AtomicReference<>();

        Thread t = new Thread(() -> {
            try {
                result.set(globalUserHolder.getUsername());
            } finally {
                latch.countDown();
            }
        }, "plain-scheduled-like-thread");
        t.start();

        // Await thread completion to make assertions deterministic
        boolean finished = latch.await(5, TimeUnit.SECONDS);
        assertThat(finished).as("thread finished").isTrue();
        assertThat(result.get()).isEqualTo(GlobalUserHolder.IBOSNG_BACKEND);
    }
}
