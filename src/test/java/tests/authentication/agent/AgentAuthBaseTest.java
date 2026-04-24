package tests.authentication.agent;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import pages.subPages.home.HomePage;
import pages.subPages.login.LoginPage;
import utils.storage.StorageStateManager;

@Tag("authentication")
@Tag("agent")
public abstract class AgentAuthBaseTest extends BaseTest {

    protected HomePage homePage;
    protected LoginPage loginPage;

    @Override
    protected String storageRole() {
        return null;
    }

    @BeforeEach
    void prepareStorage() {
        StorageStateManager.ensureStorageDirectory();
        StorageStateManager.deleteStorage("agent");
    }

    @Override
    protected void initPages() {
        homePage = new HomePage(page);
        loginPage = new LoginPage(page);
    }
}