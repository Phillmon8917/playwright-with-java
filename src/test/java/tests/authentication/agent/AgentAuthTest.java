package tests.authentication.agent;

import java.nio.file.Paths;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import utils.credentials.Credentials;
import utils.credentials.CredentialsOptions;
import utils.credentials.CredentialsProvider;
import utils.logger.LoggingUtil;

@Order(1)
public class AgentAuthTest extends AgentAuthBaseTest {

    @Test
    public void agentLoginTest() {
        homePage.loadThePage();
        homePage.verifyNavigationToLoginPage();

        Credentials credentials = CredentialsProvider.getCredentials(CredentialsOptions.AGENT);

        loginPage.login(credentials.getUsername(), credentials.getPassword(), true);

        String storagePath = "storage/agent.json";
        page.context().storageState(
                new com.microsoft.playwright.BrowserContext.StorageStateOptions()
                        .setPath(Paths.get(storagePath))
        );

        LoggingUtil.info("Agent login test passed + storage state saved");
    }
}
