package extensions;

public class FailureTracker {

    private static final ThreadLocal<Boolean> failed = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<String> errorMessage = ThreadLocal.withInitial(() -> "Unknown error");

    /**
     * Marks the current thread's test execution as failed and stores the associated error message.
     *
     * @param message the failure message to retain for later retrieval
     */
    public static void markFailed(String message) {
        failed.set(true);
        errorMessage.set(message);
    }

    /**
     * Indicates whether the current thread has recorded a failure.
     *
     * @return {@code true} if a failure has been recorded for the current thread; otherwise {@code false}
     */
    public static boolean hasFailed() {
        return failed.get();
    }

    /**
     * Returns the stored failure message for the current thread.
     *
     * @return the recorded error message, or the default message when none has been set
     */
    public static String getError() {
        return errorMessage.get();
    }

    /**
     * Clears the failure state and error message stored for the current thread.
     */
    public static void clear() {
        failed.remove();
        errorMessage.remove();
    }
}
