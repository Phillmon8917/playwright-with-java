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
     * Viewport width used for local (non-CI) runs. Kept comfortably below a
     * standard 1920-wide monitor so the browser window does not overflow the
     * screen. The pipeline is unaffected — it keeps 1920x1080.
     */
    private static final int LOCAL_VIEWPORT_WIDTH = 1440;
    private static final int LOCAL_VIEWPORT_HEIGHT = 900;

    /**
     * Returns the configured action timeout and doubles it when the tests run
     * in CI.
     *
     * @return the action timeout in milliseconds
     */
    public static int getActionTimeout() {
        String env = EnvUtil.get("ACTION_TIMEOUT");
        int base = (!env.isEmpty()) ? Integer.parseInt(env) : 80_000;
        return IS_CI ? base * 2 : base;
    }

    /**
     * Returns the configured navigation timeout and doubles it when the tests
     * run in CI.
     *
     * @return the navigation timeout in milliseconds
     */
    public static int getNavigationTimeout() {
        String env = EnvUtil.get("NAVIGATION_TIMEOUT");
        int base = (!env.isEmpty()) ? Integer.parseInt(env) : 120_000;
        return IS_CI ? base * 2 : base;
    }

    /**
     * Returns the configured test timeout and doubles it when the tests run in
     * CI.
     *
     * @return the test timeout in milliseconds
     */
    public static int getTestTimeout() {
        String env = EnvUtil.get("TEST_TIMEOUT");
        int base = (!env.isEmpty()) ? Integer.parseInt(env) : 80_000;
        return IS_CI ? base * 2 : base;
    }

    /**
     * Returns the base URL of the application under test.
     *
     * @return the configured base URL, or the default fallback when none is set
     */
    public static String getBaseUrl() {
        String env = EnvUtil.get("BASE_URL");
        return !env.isEmpty() ? env : "https://phptravels.net/";
    }

    /**
     * Returns the default element/action timeout. Used by
     * {@code InlineLoginHelper} and other framework utilities that need a
     * timeout value outside of a Page context.
     *
     * @return the action timeout in milliseconds
     */
    public static int getDefaultTimeout() {
        return getActionTimeout();
    }

    /**
     * Launches the Chromium browser using the framework defaults for the
     * current environment.
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
     * Creates a browser context configured with the base URL, viewport size,
     * and video recording.
     *
     * <p>
     * The viewport is {@code 1920x1080} in CI and {@code 1440x900} locally so
     * that the browser window does not overflow a developer's screen. Video
     * recording dimensions are always fixed at {@code 1280x720} regardless of
     * environment.
     *
     * @param browser the browser used to create the context
     * @param videoDir the directory where recorded videos should be stored; may
     * be {@code null} to skip video recording
     * @return the configured browser context
     */
    public static BrowserContext createContext(Browser browser, Path videoDir) {
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setBaseURL(getBaseUrl())
                .setViewportSize(viewportWidth(), viewportHeight());

        if (videoDir != null) {
            options.setRecordVideoDir(videoDir)
                    .setRecordVideoSize(new RecordVideoSize(1280, 720));
        }

        return browser.newContext(options);
    }

    /**
     * Creates a browser context that restores a saved storage state and records
     * video for the session.
     *
     * <p>
     * The viewport follows the same CI vs local rules as
     * {@link #createContext}.
     *
     * @param browser the browser used to create the context
     * @param videoDir the directory where recorded videos should be stored; may
     * be {@code null} to skip video recording
     * @param storagePath the path to the storage state file to load into the
     * context
     * @return the configured browser context
     */
    public static BrowserContext createContextWithStorage(Browser browser, Path videoDir, Path storagePath) {
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setBaseURL(getBaseUrl())
                .setViewportSize(viewportWidth(), viewportHeight())
                .setStorageStatePath(storagePath);

        if (videoDir != null) {
            options.setRecordVideoDir(videoDir)
                    .setRecordVideoSize(new RecordVideoSize(1280, 720));
        }

        return browser.newContext(options);
    }

    /**
     * Creates a new page in the provided context and applies the configured
     * action and navigation timeouts.
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

    /**
     * Returns the viewport width appropriate for the current environment. CI
     * uses 1920; local runs use {@value #LOCAL_VIEWPORT_WIDTH}.
     */
    private static int viewportWidth() {
        return IS_CI ? 1920 : LOCAL_VIEWPORT_WIDTH;
    }

    /**
     * Returns the viewport height appropriate for the current environment. CI
     * uses 1080; local runs use {@value #LOCAL_VIEWPORT_HEIGHT}.
     */
    private static int viewportHeight() {
        return IS_CI ? 1080 : LOCAL_VIEWPORT_HEIGHT;
    }
}
