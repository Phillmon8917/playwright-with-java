package tests.dashboard.userView;

import org.junit.jupiter.api.Test;

import utils.logger.LoggingUtil;

public class UserDashboardTest extends UserDashboardBaseTest {

    @Test
    public void verifyUserDashboardPageIsLoaded() {
        homePage.loadThePage();
        homePage.navigateToDashboardPage();
        userDashboardPage.verifyNavigationToUserDashboard();
        
        LoggingUtil.info("Assertion Passed: Users can open dashboard page successfully");
    }
}
