package tests.home;

import base.BaseTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.subPages.home.HomePage;
import report.MonthlyReporter;

@ExtendWith(MonthlyReporter.class)
@Tag("shard1")
public abstract class HomeBaseTest extends BaseTest {

    protected HomePage homePage;

    @Override
    protected void initPages() {
        homePage = new HomePage(page);
    }
}