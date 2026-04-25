package config;

import java.nio.file.Path;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RecordVideoSize;

import utils.env.EnvUtil;

public class PlaywrightConfig {

    private static final boolean IS_CI = !EnvUtil.get("CI").isEmpty();

    /**
     * Returns the configured action timeout and doubles it when the tests run in CI.
     *
     * @return the action timeout in milliseconds
     */
    public static int getActionTimeout() {
        String env = EnvUtil.get("ACTION_TIMEOUT");
        int base = (!env.isEmpty()) ? Integer.parseInt(env) : 80_000;
        return IS_CI ? base * 2 : base;
    }

    /**
     * Returns the configured navigation timeout and doubles it when the tests run in CI.
     *
     * @return the navigation timeout in milliseconds
     */
    public static int getNavigationTimeout() {
        String env = EnvUtil.get("NAVIGATION_TIMEOUT");
        int base = (!env.isEmpty()) ? Integer.parseInt(env) : 120_000;
        return IS_CI ? base * 2 : base;
    }

    /**
     * Returns the configured test timeout and doubles it when the tests run in CI.
     *
     * @return the test timeout in milliseconds
     */
    public static int getTestTimeout() {
        String env = EnvUtil.get("TEST_TIMEOUT");
        int base = (!env.isEmpty()) ? Integer.parseInt(env) : 80_000;
        return IS_CI ? base * 2 : base;
    }

    /**
     * Launches the Chromium browser using the framework defaults for the current environment.
     *
     * @param playwright the Playwright instance used to launch the browser
     * @return the launched browser instance
     */
    public static Browser launchBrowser(Playwright playwright) {
        return playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(IS_CI));
    }

    /**
     * Creates a browser context configured with the base URL, viewport size, and video recording.
     *
     * @param browser the browser used to create the context
     * @param videoDir the directory where recorded videos should be stored
     * @return the configured browser context
     */
    public static BrowserContext createContext(Browser browser, Path videoDir) {
        String baseUrl = EnvUtil.get("BASE_URL");

        return browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(!baseUrl.isEmpty() ? baseUrl : "https://phptravels.net/")
                .setViewportSize(1920, 1080)
                .setRecordVideoDir(videoDir)
                .setRecordVideoSize(new RecordVideoSize(1280, 720)));
    }

    /**
     * Creates a browser context that restores a saved storage state and records video for the session.
     *
     * @param browser the browser used to create the context
     * @param videoDir the directory where recorded videos should be stored
     * @param storagePath the path to the storage state file to load into the context
     * @return the configured browser context
     */
    public static BrowserContext createContextWithStorage(Browser browser, Path videoDir, Path storagePath) {
        String baseUrl = EnvUtil.get("BASE_URL");

        return browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(!baseUrl.isEmpty() ? baseUrl : "https://phptravels.net/")
                .setViewportSize(1920, 1080)
                .setStorageStatePath(storagePath)
                .setRecordVideoDir(videoDir)
                .setRecordVideoSize(new RecordVideoSize(1280, 720)));
    }

    /**
     * Creates a new page in the provided context and applies the configured action and navigation timeouts.
     *
     * @param context the browser context used to create the page
     * @return the configured page instance
     */
    public static Page createPage(BrowserContext context) {
        Page page = context.newPage();
        page.setDefaultTimeout(getActionTimeout());
        page.setDefaultNavigationTimeout(getNavigationTimeout());
        return page;
    }
}
