package steps.authentication;

import java.nio.file.Paths;

import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.credentials.Credentials;
import utils.credentials.CredentialsOptions;
import utils.credentials.CredentialsProvider;
import utils.logger.LoggingUtil;
import utils.storage.StorageStateManager;

public class AdminAuthSteps {

    private final TestContext context;

    public AdminAuthSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Ensures that the storage directory for session states exists.
     */
    @Given("the storage directory exists")
    public void theStorageDirectoryExists() {
        StorageStateManager.ensureStorageDirectory();
    }

    /**
     * Clears the admin storage state file.
     */
    @When("the admin storage state is cleared")
    public void theAdminStorageStateIsCleared() {
        StorageStateManager.deleteStorage("admin");
    }

    /**
     * Navigates to the login page from the home page.
     */
    @When("I navigate to the login page")
    public void iNavigateToTheLoginPage() {
        context.getHomePage().verifyNavigationToLoginPage();
    }

    /**
     * Logs in using admin credentials retrieved from the credentials provider.
     */
    @When("I log in with admin credentials")
    public void iLogInWithAdminCredentials() {
        Credentials credentials = CredentialsProvider.getCredentials(CredentialsOptions.ADMIN);
        context.getLoginPage().login(credentials.getUsername(), credentials.getPassword(), true);
    }

    /**
     * Saves the current browser context's storage state to the admin storage
     * file.
     */
    @Then("the admin session should be saved to storage")
    public void theAdminSessionShouldBeSavedToStorage() {
        String storagePath = "storage/admin.json";
        context.getPage().context().storageState(
                new com.microsoft.playwright.BrowserContext.StorageStateOptions()
                        .setPath(Paths.get(storagePath))
        );
        LoggingUtil.info("Admin session saved to: " + storagePath);
    }
}
