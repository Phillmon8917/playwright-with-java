package extensions.failure;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.opentest4j.AssertionFailedError;

import io.qameta.allure.Allure;
import utils.logger.LoggingUtil;

public class FailureHandlerExtension implements TestExecutionExceptionHandler {

    /**
     * Handles test execution exceptions by logging the error details and
     * reporting to Allure. For assertion failures, logs specifically as
     * assertion failed. For other exceptions, builds a detailed error message
     * and throws an AssertionError.
     *
     * @param context the extension context containing test information
     * @param throwable the exception that occurred during test execution
     * @throws Throwable re-throws the original exception or a new
     * AssertionError
     */
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        String testName = context.getDisplayName();
        String errorType = throwable.getClass().getSimpleName();
        String errorMessage = throwable.getMessage() != null ? throwable.getMessage() : "No message available";

        if (throwable instanceof AssertionFailedError || throwable instanceof AssertionError) {
            LoggingUtil.error("ASSERTION FAILED | Test: " + testName + " | " + errorMessage);
            Allure.step("Assertion Failed: " + errorMessage, io.qameta.allure.model.Status.FAILED);
            throw throwable;
        }

        String fullMessage = buildErrorMessage(testName, errorType, errorMessage, throwable);

        LoggingUtil.error(fullMessage);
        Allure.step(fullMessage, io.qameta.allure.model.Status.FAILED);

        throw new AssertionError(fullMessage, throwable);
    }

    /**
     * Builds a detailed error message string containing test name, error type,
     * message, cause if present, and stack trace location.
     *
     * @param testName the display name of the failed test
     * @param errorType the simple class name of the exception
     * @param errorMessage the error message from the exception
     * @param throwable the exception that occurred
     * @return a formatted string containing all error details
     */
    private String buildErrorMessage(String testName, String errorType, String errorMessage, Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("TEST FAILED | ").append(testName).append("\n");
        sb.append("Error Type  : ").append(errorType).append("\n");
        sb.append("Message     : ").append(errorMessage).append("\n");

        if (throwable.getCause() != null) {
            sb.append("Caused By   : ").append(throwable.getCause().getMessage()).append("\n");
        }

        StackTraceElement[] stack = throwable.getStackTrace();
        if (stack.length > 0) {
            sb.append("Location    : ").append(stack[0].toString());
        }

        return sb.toString();
    }
}
