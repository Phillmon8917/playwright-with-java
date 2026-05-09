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

public class CustomerAuthSteps {

    private final TestContext context;

    public CustomerAuthSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Clears the customer storage state file.
     */
    @When("the customer storage state is cleared")
    public void theCustomerStorageStateIsCleared() {
        StorageStateManager.deleteStorage("customer");
    }

    /**
     * Logs in using customer credentials retrieved from the credentials
     * provider.
     */
    @When("I log in with customer credentials")
    public void iLogInWithCustomerCredentials() {
        Credentials credentials = CredentialsProvider.getCredentials(CredentialsOptions.CUSTOMER);
        context.getLoginPage().login(credentials.getUsername(), credentials.getPassword(), true);
    }

    /**
     * Saves the current browser context's storage state to the customer storage
     * file.
     */
    @Then("the customer session should be saved to storage")
    public void theCustomerSessionShouldBeSavedToStorage() {
        String storagePath = "storage/customer.json";
        context.getPage().context().storageState(
                new com.microsoft.playwright.BrowserContext.StorageStateOptions()
                        .setPath(Paths.get(storagePath))
        );
        LoggingUtil.info("Customer session saved to: " + storagePath);
    }
}
