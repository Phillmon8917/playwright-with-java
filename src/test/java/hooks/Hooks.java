package hooks;

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
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
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
     * Sets up the test environment before each scenario execution. Initializes
     * Playwright, browser, and page objects.
     *
     * @param scenario the Cucumber scenario being executed
     */
    @Before
    public void setUp(Scenario scenario) {
        long threadId = Thread.currentThread().getId();
        AllureLogAppender.registerThread(threadId);

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
     * Cleans up the test environment after each scenario execution. Attaches
     * screenshots, videos, and logs if the scenario failed.
     *
     * @param scenario the Cucumber scenario that completed
     */
    @After
    public void tearDown(Scenario scenario) {
        long threadId = Thread.currentThread().getId();
        boolean failed = scenario.isFailed();

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

        attachLogs(threadId);

        LoggingUtil.info("Scenario finished: " + scenario.getName() + " | Status: " + scenario.getStatus());
    }

    /**
     * Attaches a screenshot to the Allure report for the given scenario.
     *
     * @param scenarioName the name of the scenario
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
     * Attaches a video recording to the Allure report if available.
     *
     * @param videoDir the directory containing the video files
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
     * Attaches console logs to the Allure report for the given thread.
     *
     * @param threadId the ID of the thread whose logs to attach
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
     * Deletes the specified directory and all its contents.
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
