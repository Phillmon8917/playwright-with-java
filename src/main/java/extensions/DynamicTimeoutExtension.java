package extensions;

import config.PlaywrightConfig;
import org.junit.jupiter.api.extension.*;
import org.opentest4j.AssertionFailedError;
import utils.logger.AllureLogAppender;

import java.lang.reflect.Method;
import java.util.concurrent.*;

public class DynamicTimeoutExtension implements InvocationInterceptor {

    private static final ThreadLocal<Long> ownerThreadId = new ThreadLocal<>();

    /**
     * Stores the owning thread identifier for the current execution context.
     *
     * @param id the thread identifier to associate with the current thread
     */
    public static void setOwnerThreadId(long id) {
        ownerThreadId.set(id);
    }

    /**
     * Executes the test method inside a worker thread and enforces the configured timeout.
     *
     * @param invocation the wrapped invocation for the intercepted test method
     * @param invocationContext the reflective context describing the intercepted method call
     * @param extensionContext the JUnit extension context for the current test execution
     * @throws Throwable if the test execution fails or exceeds the configured timeout
     */
    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {

        int timeoutMs = PlaywrightConfig.getTestTimeout();
        long owner = Thread.currentThread().getId();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        BlockingQueue<Long> workerIdQueue = new LinkedBlockingQueue<>();

        Future<Void> future = executor.submit(() -> {
            long workerId = Thread.currentThread().getId();
            workerIdQueue.put(workerId);
            AllureLogAppender.registerWorkerForOwner(workerId, owner);
            try {
                invocation.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                AllureLogAppender.unregisterWorker(workerId);
            }
            return null;
        });

        workerIdQueue.poll(5, TimeUnit.SECONDS);

        try {
            future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            FailureTracker.markFailed(cause != null ? cause.getMessage() : "Unknown error");
            throw cause != null ? cause : e;
        } catch (TimeoutException e) {
            future.cancel(true);
            FailureTracker.markFailed("Test exceeded timeout of " + timeoutMs + "ms");
            throw new AssertionFailedError("Test exceeded timeout of " + timeoutMs + "ms");
        } finally {
            executor.shutdownNow();
        }
    }
}
