package tests.authentication.customer;

import org.junit.jupiter.api.Test;
import utils.credentials.Credentials;
import utils.credentials.CredentialsOptions;
import utils.credentials.CredentialsProvider;
import utils.logger.LoggingUtil;

import java.nio.file.Paths;

public class CustomerAuthTest extends CustomerAuthBaseTest {

    @Test
    public void customerLoginTest() {
        homePage.loadThePage();
        homePage.verifyNavigationToLoginPage();

        Credentials credentials = CredentialsProvider.getCredentials(CredentialsOptions.CUSTOMER);

        loginPage.login(credentials.getUsername(), credentials.getPassword(), true);

        String storagePath = "storage/customer.json";
        page.context().storageState(
                new com.microsoft.playwright.BrowserContext.StorageStateOptions()
                        .setPath(Paths.get(storagePath))
        );

        LoggingUtil.info("Customer login test passed + storage state saved");
    }
}