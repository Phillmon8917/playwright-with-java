package base;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import config.PlaywrightConfig;
import extensions.AuthExtension;
import extensions.DynamicTimeoutExtension;
import extensions.FailureTracker;
import extensions.ReportingExtension;
import utils.logger.AllureLogAppender;
import utils.logger.LoggingUtil;
import utils.storage.StorageStateManager;

@ExtendWith({AuthExtension.class, DynamicTimeoutExtension.class, ReportingExtension.class})
public abstract class BaseTest {

    private static final ThreadLocal<Playwright> TL_PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> TL_BROWSER = new ThreadLocal<>();

    protected BrowserContext context;
    protected Page page;
    private Path videoDir;
    private long testThreadId;

    /**
     * Provides the storage role to use for the test context.
     *
     * @return the storage role name, or null if no storage should be used
     */
    protected String storageRole() {
        return null;
    }

    /**
     * Sets up the browser context and page before each test.
     *
     * @param testInfo JUnit test information
     * @throws Exception if setup fails
     */
    @BeforeEach
    final void setUpContextAndPage(TestInfo testInfo) throws Exception {
        if (TL_PLAYWRIGHT.get() == null) {
            Playwright pw = Playwright.create();
            Browser br = PlaywrightConfig.launchBrowser(pw);
            TL_PLAYWRIGHT.set(pw);
            TL_BROWSER.set(br);
        }

        testThreadId = Thread.currentThread().getId();
        AllureLogAppender.registerThread(testThreadId);

        LoggingUtil.info("Starting: " + testInfo.getDisplayName());

        videoDir = ReportingExtension.onBeforeEach();

        String role = storageRole();
        if (role != null && StorageStateManager.storageExists(role)) {
            context = PlaywrightConfig.createContextWithStorage(
                    TL_BROWSER.get(), videoDir, StorageStateManager.getStoragePath(role));
        } else {
            context = PlaywrightConfig.createContext(TL_BROWSER.get(), videoDir);
        }

        page = PlaywrightConfig.createPage(context);
        initPages();
    }

    /**
     * Tears down the browser context and page after each test.
     *
     * @param testInfo JUnit test information
     * @throws Exception if teardown fails
     */
    @AfterEach
    final void tearDownContextAndPage(TestInfo testInfo) throws Exception {
        boolean failed = FailureTracker.hasFailed();
        ReportingExtension.onAfterEach(context, page, failed, videoDir, testThreadId);
        FailureTracker.clear();
        context = null;
        page = null;
        videoDir = null;
    }

    /**
     * Closes the browser and cleans up resources after all tests.
     */
    @AfterAll
    static void closeBrowser() {
        Browser br = TL_BROWSER.get();
        if (br != null) {
            br.close();
            TL_BROWSER.remove();
        }

        Playwright pw = TL_PLAYWRIGHT.get();
        if (pw != null) {
            pw.close();
            TL_PLAYWRIGHT.remove();
        }
    }

    /**
     * Initializes pages used by test subclasses.
     * Override to initialize test-specific pages.
     */
    protected void initPages() {
    }

    /**
     * Gets the browser instance for the current test thread.
     *
     * @return the Browser instance
     */
    protected static Browser getBrowser() {
        return TL_BROWSER.get();
    }
}
