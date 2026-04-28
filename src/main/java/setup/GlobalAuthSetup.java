package setup;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import config.PlaywrightConfig;
import pages.subPages.home.HomePage;
import pages.subPages.login.LoginPage;
import utils.credentials.Credentials;
import utils.credentials.CredentialsOptions;
import utils.credentials.CredentialsProvider;
import utils.logger.LoggingUtil;
import utils.storage.StorageStateManager;


public final class GlobalAuthSetup {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static volatile boolean hasRun = false;

    private static final List<RoleConfig> ROLES = List.of(
            new RoleConfig("admin", CredentialsOptions.ADMIN),
            new RoleConfig("agent", CredentialsOptions.AGENT),
            new RoleConfig("customer", CredentialsOptions.CUSTOMER)
    );

    private GlobalAuthSetup() {
    }

    /**
     * Ensures all role storage files exist. Safe to call multiple times —
     * skipped entirely after the first successful run within the same JVM
     * session.
     */
    public static void run() {
        if (hasRun) {
            return;
        }

        LOCK.lock();
        try {
            if (hasRun) {
                return;
            }

            StorageStateManager.ensureStorageDirectory();

            for (RoleConfig role : ROLES) {
                if (!StorageStateManager.storageExists(role.name())) {
                    LoggingUtil.info("GlobalAuthSetup: storage missing for '" + role.name() + "' — running auth.");
                    authenticate(role);
                } else {
                    LoggingUtil.info("GlobalAuthSetup: storage present for '" + role.name() + "' — skipping.");
                }
            }

            hasRun = true;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Performs a headless login for one role using the same page objects and
     * credential provider used by the dedicated auth tests.
     *
     * @param role the role configuration to authenticate
     */
    private static void authenticate(RoleConfig role) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = PlaywrightConfig.launchBrowser(playwright);
            try {
                BrowserContext context = PlaywrightConfig.createContext(browser, null);
                try {
                    Page page = PlaywrightConfig.createPage(context);

                    HomePage homePage = new HomePage(page);
                    LoginPage loginPage = new LoginPage(page);

                    Credentials credentials = CredentialsProvider.getCredentials(role.credentialsOption());

                    homePage.loadThePage();
                    homePage.verifyNavigationToLoginPage();
                    loginPage.login(credentials.getUsername(), credentials.getPassword(), true);

                    Path storagePath = StorageStateManager.getStoragePath(role.name());
                    context.storageState(
                            new BrowserContext.StorageStateOptions().setPath(storagePath)
                    );

                    LoggingUtil.info("GlobalAuthSetup: auth complete for '" + role.name() + "'. Saved to " + storagePath);
                } finally {
                    try {
                        context.close();
                    } catch (Exception ignored) {
                    }
                }
            } finally {
                try {
                    browser.close();
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(
                    "GlobalAuthSetup: auth failed for role '" + role.name() + "'. Cause: " + e.getMessage(), e);
        }
    }

    private record RoleConfig(String name, CredentialsOptions credentialsOption) {

    }
}
