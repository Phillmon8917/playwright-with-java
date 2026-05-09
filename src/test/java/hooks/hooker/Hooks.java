package hooks.hooker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import config.PlaywrightConfig;
import context.TestContext;
import hooks.errors.ErrorStore;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import pages.subPages.home.HomePage;
import pages.subPages.login.LoginPage;
import pages.subPages.signup.SignupPage;
import utils.logger.AllureLogAppender;
import utils.logger.LoggingUtil;

public class Hooks {

    private static final ThreadLocal<Playwright> TL_PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> TL_BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<Path> TL_VIDEO_DIR = new ThreadLocal<>();

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    /**
     * Sets up the test environment before each scenario. Initializes
     * Playwright, browser, context, page, and page objects. Registers logging
     * and clears error store.
     *
     * @param scenario the Cucumber scenario being executed
     */
    @Before
    public void setUp(Scenario scenario) {
        long threadId = Thread.currentThread().getId();
        AllureLogAppender.registerThread(threadId);
        ErrorStore.clear();

        LoggingUtil.info("Starting scenario: " + scenario.getName());

        if (TL_PLAYWRIGHT.get() == null) {
            Playwright playwright = Playwright.create();
            Browser browser = PlaywrightConfig.launchBrowser(playwright);
            TL_PLAYWRIGHT.set(playwright);
            TL_BROWSER.set(browser);
        }

        Path videoDir;
        try {
            videoDir = Files.createTempDirectory("pw-video-");
        } catch (Exception e) {
            videoDir = Paths.get("target/videos");
        }
        TL_VIDEO_DIR.set(videoDir);

        BrowserContext browserContext = PlaywrightConfig.createContext(TL_BROWSER.get(), videoDir);
        Page page = PlaywrightConfig.createPage(browserContext);

        context.setPage(page);
        context.setHomePage(new HomePage(page));
        context.setLoginPage(new LoginPage(page));
        context.setSignupPage(new SignupPage(page));
    }

    /**
     * Tears down the test environment after each scenario. Handles failed
     * scenarios, attaches screenshots/videos/logs, and cleans up resources.
     *
     * @param scenario the Cucumber scenario that finished
     */
    @After
    public void tearDown(Scenario scenario) {
        long threadId = Thread.currentThread().getId();
        boolean failed = scenario.isFailed();

        if (failed) {
            enforceRedFailure(scenario);
        }

        attachScreenshot(scenario.getName());

        try {
            Page page = context.getPage();
            if (page != null) {
                page.context().close();
            }
        } catch (Exception e) {
            LoggingUtil.info("Context close warning: " + e.getMessage());
        }

        Path videoDir = TL_VIDEO_DIR.get();
        if (failed && videoDir != null) {
            attachVideo(videoDir);
        }

        deleteDirectory(videoDir);
        TL_VIDEO_DIR.remove();
        ErrorStore.clear();

        attachLogs(threadId);

        LoggingUtil.info("Scenario finished: " + scenario.getName() + " | Status: " + scenario.getStatus());
    }

    /**
     * Enforces a red (failed) status in Allure for scenarios that failed but
     * weren't marked as such. Builds and sets error details in the test result.
     *
     * @param scenario the failed scenario
     */
    private void enforceRedFailure(Scenario scenario) {
        try {
            Allure.getLifecycle().updateTestCase(testResult -> {
                if (testResult.getStatus() != Status.FAILED) {
                    String errorMessage = buildErrorMessage(scenario);
                    LoggingUtil.error(errorMessage);
                    testResult.setStatus(Status.FAILED);
                    testResult.setStatusDetails(
                            new StatusDetails()
                                    .setMessage(errorMessage)
                                    .setTrace(errorMessage)
                    );
                }
            });
        } catch (Exception e) {
            LoggingUtil.error("Could not enforce red failure status: " + e.getMessage());
        }
    }

    /**
     * Builds a detailed error message string for a failed scenario, including
     * scenario details, tags, status, and error information from ErrorStore.
     *
     * @param scenario the failed scenario
     * @return a formatted error message string
     */
    private String buildErrorMessage(Scenario scenario) {
        StringBuilder sb = new StringBuilder();
        sb.append("SCENARIO FAILED: ").append(scenario.getName()).append("\n");
        sb.append("Tags          : ").append(scenario.getSourceTagNames()).append("\n");
        sb.append("Status        : ").append(scenario.getStatus()).append("\n");

        Throwable error = ErrorStore.get();
        if (error != null) {
            sb.append("Error Type    : ").append(error.getClass().getSimpleName()).append("\n");
            sb.append("Message       : ").append(error.getMessage() != null ? error.getMessage() : "No message").append("\n");

            if (error.getCause() != null) {
                sb.append("Caused By     : ").append(error.getCause().getMessage()).append("\n");
            }

            StackTraceElement[] stack = error.getStackTrace();
            if (stack.length > 0) {
                sb.append("Location      : ").append(stack[0].toString()).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Captures a full-page screenshot of the current page and attaches it to
     * Allure.
     *
     * @param scenarioName the name of the scenario for the attachment
     */
    private void attachScreenshot(String scenarioName) {
        try {
            Page page = context.getPage();
            if (page != null) {
                byte[] screenshot = page.screenshot(
                        new Page.ScreenshotOptions().setFullPage(true));
                Allure.getLifecycle().addAttachment(
                        "Screenshot", "image/png", "png", screenshot);
            }
        } catch (Exception e) {
            LoggingUtil.info("Failed to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Finds and attaches the first .webm video file from the video directory to
     * Allure.
     *
     * @param videoDir the directory containing video files
     */
    private void attachVideo(Path videoDir) {
        try {
            if (videoDir == null || !Files.exists(videoDir)) {
                return;
            }
            Files.walk(videoDir)
                    .filter(f -> f.toString().endsWith(".webm"))
                    .findFirst()
                    .ifPresent(src -> {
                        try {
                            byte[] videoBytes = Files.readAllBytes(src);
                            Allure.getLifecycle().addAttachment(
                                    "Video", "video/webm", "webm", videoBytes);
                        } catch (Exception ignored) {
                        }
                    });
        } catch (Exception ignored) {
        }
    }

    /**
     * Drains and attaches console logs for the given thread to Allure.
     *
     * @param threadId the thread ID to get logs for
     */
    private void attachLogs(long threadId) {
        try {
            String logs = AllureLogAppender.drainLogs(threadId);
            if (logs != null && !logs.isBlank()) {
                Allure.getLifecycle().addAttachment(
                        "Console Logs", "text/plain", "txt", logs.getBytes());
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Recursively deletes the specified directory and all its contents.
     *
     * @param dir the directory to delete
     */
    private void deleteDirectory(Path dir) {
        try {
            if (dir == null || !Files.exists(dir)) {
                return;
            }
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (Exception ignored) {
                        }
                    });
        } catch (Exception ignored) {
        }
    }
}
