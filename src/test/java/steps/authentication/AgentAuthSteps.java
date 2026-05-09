package steps.authentication;

import java.nio.file.Paths;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.credentials.Credentials;
import utils.credentials.CredentialsOptions;
import utils.credentials.CredentialsProvider;
import utils.logger.LoggingUtil;
import utils.storage.StorageStateManager;

public class AgentAuthSteps {

    private final TestContext context;

    public AgentAuthSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Clears the agent storage state file.
     */
    @When("the agent storage state is cleared")
    public void theAgentStorageStateIsCleared() {
        StorageStateManager.deleteStorage("agent");
    }

    /**
     * Logs in using agent credentials retrieved from the credentials provider.
     */
    @When("I log in with agent credentials")
    public void iLogInWithAgentCredentials() {
        Credentials credentials = CredentialsProvider.getCredentials(CredentialsOptions.AGENT);
        context.getLoginPage().login(credentials.getUsername(), credentials.getPassword(), true);
    }

    /**
     * Saves the current browser context's storage state to the agent storage
     * file.
     */
    @Then("the agent session should be saved to storage")
    public void theAgentSessionShouldBeSavedToStorage() {
        String storagePath = "storage/agent.json";
        context.getPage().context().storageState(
                new com.microsoft.playwright.BrowserContext.StorageStateOptions()
                        .setPath(Paths.get(storagePath))
        );
        LoggingUtil.info("Agent session saved to: " + storagePath);
    }
}
