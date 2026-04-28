package tests.authentication.admin;

import java.nio.file.Paths;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import utils.credentials.Credentials;
import utils.credentials.CredentialsOptions;
import utils.credentials.CredentialsProvider;
import utils.logger.LoggingUtil;

@Order(1)
public class AdminAuthTest extends AdminAuthBaseTest {

    @Test
    public void adminLoginTest() {

        homePage.loadThePage();
        homePage.verifyNavigationToLoginPage();

        Credentials credentials = CredentialsProvider.getCredentials(CredentialsOptions.ADMIN);

        loginPage.login(credentials.getUsername(), credentials.getPassword(), true);

        String storagePath = "storage/admin.json";
        page.context().storageState(
                new com.microsoft.playwright.BrowserContext.StorageStateOptions()
                        .setPath(Paths.get(storagePath))
        );

        LoggingUtil.info("Admin login test passed + storage state saved");
    }
}
