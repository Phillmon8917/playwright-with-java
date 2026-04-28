package tests.dashboard.userView;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import auth.Auth;
import auth.Role;
import base.BaseTest;
import pages.subPages.dashboard.userView.UserDashboardPage;
import pages.subPages.home.HomePage;
import report.MonthlyReporter;

@Auth(role = Role.CUSTOMER)
@ExtendWith(MonthlyReporter.class)
@Tag("regression")
@Tag("userDashboard")
@Tag("shard4")
public class UserDashboardBaseTest extends BaseTest {

    protected HomePage homePage;
    protected UserDashboardPage userDashboardPage;

    @Override
    protected String storageRole() {
        return "customer";
    }

    @Override
    protected void initPages() {
        homePage = new HomePage(page);
        userDashboardPage = new UserDashboardPage(page);
    }
}