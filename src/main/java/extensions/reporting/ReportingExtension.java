package extensions.reporting;

import io.qameta.allure.Allure;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.extension.*;
import utils.logger.AllureLogAppender;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class ReportingExtension implements BeforeAllCallback, AfterAllCallback {

    /**
     * Runs before all tests in the current extension scope.
     *
     * @param context the JUnit extension context for the current test container
     */
    @Override
    public void beforeAll(ExtensionContext context) {
    }

    /**
     * Runs after all tests in the current extension scope have completed.
     *
     * @param context the JUnit extension context for the current test container
     */
    @Override
    public void afterAll(ExtensionContext context) {
    }

    /**
     * Creates a temporary directory used to store Playwright video recordings for a test run.
     *
     * @return the path to the created temporary video directory
     * @throws Exception if the directory cannot be created
     */
    public static Path onBeforeEach() throws Exception {
        return Files.createTempDirectory("pw-video-");
    }

    /**
     * Performs post-test reporting tasks including screenshot capture, video attachment, cleanup, and log attachment.
     *
     * @param bc the browser context associated with the completed test
     * @param page the page used during the completed test
     * @param failed indicates whether the test execution failed
     * @param videoDir the directory containing any recorded test video
     * @param threadId the thread identifier used to collect buffered logs
     * @throws Exception if cleanup of the video directory fails
     */
    public static void onAfterEach(BrowserContext bc, Page page, boolean failed, Path videoDir, long threadId) throws Exception {
        attachScreenshot(page);

        try {
            bc.close();
        } catch (Exception ignored) {}

        if (failed && videoDir != null) {
            attachVideo(videoDir);
        }

        if (videoDir != null) {
            deleteDirectory(videoDir);
        }

        attachLogs(threadId);
    }

    /**
     * Captures a full-page screenshot and attaches it to the Allure report.
     *
     * @param page the page to capture
     */
    private static void attachScreenshot(Page page) {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.getLifecycle().addAttachment(
                    "Screenshot",
                    "image/png",
                    "png",
                    screenshot
            );
        } catch (Exception ignored) {}
    }

    /**
     * Finds the recorded video file in the given directory and attaches it to the Allure report.
     *
     * @param vDir the directory that may contain the recorded video file
     */
    private static void attachVideo(Path vDir) {
        try {
            Files.walk(vDir)
                    .filter(f -> f.toString().endsWith(".webm"))
                    .findFirst()
                    .ifPresent(src -> {
                        try {
                            byte[] videoBytes = Files.readAllBytes(src);
                            Allure.getLifecycle().addAttachment(
                                    "Video",
                                    "video/webm",
                                    "webm",
                                    videoBytes
                            );
                        } catch (Exception ignored) {}
                    });
        } catch (Exception ignored) {}
    }

    /**
     * Drains any buffered logs for the specified thread and attaches them to the Allure report.
     *
     * @param threadId the owner thread identifier whose logs should be attached
     */
    private static void attachLogs(long threadId) {
        String logs = AllureLogAppender.drainLogs(threadId);
        if (!logs.isBlank()) {
            Allure.getLifecycle().addAttachment(
                    "Console Logs",
                    "text/plain",
                    "txt",
                    logs.getBytes()
            );
        }
    }

    /**
     * Deletes the supplied directory and all of its contents when it exists.
     *
     * @param dir the directory to delete recursively
     * @throws Exception if walking the directory structure fails
     */
    private static void deleteDirectory(Path dir) throws Exception {
        if (!Files.exists(dir)) return;
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try { Files.delete(p); } catch (Exception ignored) {}
                });
    }
}
