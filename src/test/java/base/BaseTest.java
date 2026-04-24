package base;

import config.PlaywrightConfig;
import extensions.DynamicTimeoutExtension;
import extensions.FailureTracker;
import extensions.ReportingExtension;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import utils.logger.AllureLogAppender;
import utils.logger.LoggingUtil;
import utils.storage.StorageStateManager;

import java.nio.file.Path;

@ExtendWith({DynamicTimeoutExtension.class, ReportingExtension.class})
public abstract class BaseTest {

    private static final ThreadLocal<Playwright> TL_PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> TL_BROWSER = new ThreadLocal<>();

    protected BrowserContext context;
    protected Page page;
    private Path videoDir;
    private long testThreadId;

    /**
     * Returns the storage role whose saved authentication state should be loaded for the test.
     *
     * @return the storage role name, or {@code null} when no stored state should be used
     */
    protected String storageRole() {
        return null;
    }

    /**
     * Creates the Playwright context and page for the current test and initializes page objects.
     *
     * @param testInfo metadata about the test being executed
     * @throws Exception if the browser context or page cannot be created
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
     * Cleans up the Playwright context, attachments, and failure tracking after each test execution.
     *
     * @param testInfo metadata about the test that completed
     * @throws Exception if test artifacts cannot be finalized correctly
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
     * Closes the shared browser and Playwright instances after all tests in the class have run.
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
     * Initializes page object instances after the Playwright page has been created.
     */
    protected void initPages() {}

    /**
     * Returns the browser instance associated with the current test thread.
     *
     * @return the active browser instance for the current thread
     */
    protected static Browser getBrowser() {
        return TL_BROWSER.get();
    }
}
