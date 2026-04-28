package auth;

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

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public final class AuthManager {

    private static final Map<Role, ReentrantLock> LOCKS = new EnumMap<>(Role.class);
    private static final Map<Role, Boolean> COMPLETED = new EnumMap<>(Role.class);

    static {
        for (Role role : Role.values()) {
            LOCKS.put(role, new ReentrantLock());
            COMPLETED.put(role, false);
        }
        StorageStateManager.ensureStorageDirectory();
    }

    private AuthManager() {
    }

    /**
     * Ensures that the user is authenticated for the specified role. If the
     * role is GUEST, skips authentication. If authentication is already
     * completed for the role, skips it. Otherwise, performs authentication by
     * logging in with the appropriate credentials. Uses a lock to ensure
     * thread-safe authentication per role.
     *
     * @param role the role to authenticate for
     */
    public static void ensureAuthenticated(Role role) {
        if (role == Role.GUEST) {
            LoggingUtil.info("Guest test detected, skipping auth");
            return;
        }

        ReentrantLock lock = LOCKS.get(role);
        lock.lock();
        try {
            if (COMPLETED.get(role)) {
                LoggingUtil.info("Auth already exists for " + role + ", skipping");
                return;
            }

            LoggingUtil.info("Auth requested: " + role);
            LoggingUtil.info("Running " + role + " auth...");

            deleteAndAuthenticate(role);
            COMPLETED.put(role, true);

            LoggingUtil.info(role + " auth completed");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Resets the authentication state for all roles except GUEST. Clears the
     * completion status and deletes the stored authentication state for each
     * role. Each role's reset is protected by a lock to ensure thread safety.
     */
    public static void resetAll() {
        for (Role role : Role.values()) {
            if (role == Role.GUEST) {
                continue;
            }
            ReentrantLock lock = LOCKS.get(role);
            lock.lock();
            try {
                COMPLETED.put(role, false);
                StorageStateManager.deleteStorage(roleName(role));
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Deletes the existing stored authentication state and performs a fresh
     * authentication for the specified role. Launches a Playwright browser,
     * navigates to the home page, redirects to login, and authenticates using
     * credentials. Stores the resulting browser context state for future use.
     *
     * @param role the role to authenticate for
     */
    private static void deleteAndAuthenticate(Role role) {
        StorageStateManager.deleteStorage(roleName(role));

        try (Playwright playwright = Playwright.create()) {
            Browser browser = PlaywrightConfig.launchBrowser(playwright);
            try {
                BrowserContext context = PlaywrightConfig.createContext(browser, null);
                try {
                    Page page = PlaywrightConfig.createPage(context);

                    HomePage homePage = new HomePage(page);
                    LoginPage loginPage = new LoginPage(page);

                    Credentials credentials = CredentialsProvider.getCredentials(
                            credentialsOptionFor(role));

                    homePage.loadThePage();
                    homePage.verifyNavigationToLoginPage();
                    loginPage.login(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            true);

                    Path storagePath = StorageStateManager.getStoragePath(roleName(role));
                    context.storageState(
                            new BrowserContext.StorageStateOptions().setPath(storagePath));

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
        }
    }

    /**
     * Converts the given role to its lowercase string representation.
     *
     * @param role the role to convert
     * @return the role name in lowercase
     */
    private static String roleName(Role role) {
        return role.name().toLowerCase();
    }

    /**
     * Maps the specified role to the corresponding credentials option.
     *
     * @param role the role to get credentials option for
     * @return the credentials option for the role
     * @throws IllegalArgumentException if the role is GUEST, which has no
     * credentials
     */
    private static CredentialsOptions credentialsOptionFor(Role role) {
        return switch (role) {
            case CUSTOMER ->
                CredentialsOptions.CUSTOMER;
            case AGENT ->
                CredentialsOptions.AGENT;
            case ADMIN ->
                CredentialsOptions.ADMIN;
            case GUEST ->
                throw new IllegalArgumentException("GUEST has no credentials");
        };
    }
}
