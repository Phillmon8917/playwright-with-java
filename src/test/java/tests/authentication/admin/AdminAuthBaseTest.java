package tests.authentication.admin;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import pages.subPages.home.HomePage;
import pages.subPages.login.LoginPage;
import utils.storage.StorageStateManager;

@Tag("authentication")
@Tag("admin")
public abstract class AdminAuthBaseTest extends BaseTest {

    protected HomePage homePage;
    protected LoginPage loginPage;

    @Override
    protected String storageRole() {
        return null;
    }

    @BeforeEach
    void prepareStorage() {
        StorageStateManager.ensureStorageDirectory();
        StorageStateManager.deleteStorage("admin");
    }

    @Override
    protected void initPages() {
        homePage = new HomePage(page);
        loginPage = new LoginPage(page);
    }
}