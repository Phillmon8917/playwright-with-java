package tests.signup.base;

import base.BaseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.subPages.home.HomePage;
import pages.subPages.signup.SignupPage;
import report.MonthlyReporter;

@ExtendWith(MonthlyReporter.class)
public abstract class SignupBaseTest extends BaseTest {

    protected HomePage homePage;
    protected SignupPage signupPage;

    @Override
    protected void initPages() {
        homePage = new HomePage(page);
        signupPage = new SignupPage(page);
    }
}