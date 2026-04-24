package tests.home;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.logger.LoggingUtil;

@Tag("regression")
@Tag("guest")
public class HomePageTest extends HomeBaseTest {

    @Test
    @Tag("sanity")
    void verifyHomePageIsLoaded() {
        homePage.loadThePage();
        homePage.verifyPageLoaded();
        LoggingUtil.info("Assertion Passed - Home Page is loaded");
    }

    @Test
    @Tag("sanity")
    void verifyHeaderNavigationLinksArePresent() {
        homePage.loadThePage();
        homePage.verifyThatHeaderNavLinksAreVisible();
        LoggingUtil.info("Assertion Passed - Header navigation links are present");
    }

    @Test
    @Tag("sanity")
    void verifyNavigationToVisaBookingPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToVisaBookingPage();
        LoggingUtil.info("Assertion Passed - Navigation to visa booking page is successful");
    }

    @Test
    @Tag("sanity")
    void verifyNavigationToToursBookingPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToToursBooking();
        LoggingUtil.info("Assertion Passed - Navigation to tours booking page is successful");
    }

    @Test
    @Tag("sanity")
    void verifyNavigationToCarsBookingPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToCarsBooking();
        LoggingUtil.info("Assertion Passed - Navigation to cars booking page is successful");
    }

    @Test
    @Tag("sanity")
    void verifyNavigationToFlightBookingPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToFlightsBooking();
        LoggingUtil.info("Assertion Passed - Navigation to flight booking page is successful");
    }

    @Test
    void verifyNavigationToStaysBookingPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToStaysBooking();
        LoggingUtil.info("Assertion Passed - Navigation to stays booking page is successful");
    }

    @Test
    void verifyNavigationToContactUsPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToContactUsPage();
        LoggingUtil.info("Assertion Passed - Navigation to contact us page is successful");
    }

    @Test
    void verifyNavigationToAboutUsPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToAboutUsPage();
        LoggingUtil.info("Assertion Passed - Navigation to about us page is successful");
    }

    @Test
    void verifyNavigationToCookiesPolicyPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToCookiesPolicyPage();
        LoggingUtil.info("Assertion Passed - Navigation to cookies policy page is successful");
    }

    @Test
    void verifyNavigationToPrivacyPolicyPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToPrivacyPolicyPage();
        LoggingUtil.info("Assertion Passed - Navigation to privacy policy page is successful");
    }

    @Test
    void verifyNavigationToBecomeASupplierPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToBecomeASupplierPage();
        LoggingUtil.info("Assertion Passed - Navigation to become a supplier page is successful");
    }

    @Test
    void verifyNavigationToTermsOfUsePage() {
        homePage.loadThePage();
        homePage.verifyNavigationToTermsOfUsePage();
        LoggingUtil.info("Assertion Passed - Navigation to terms of use page is successful");
    }

    @Test
    void verifyNavigationToBlogsPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToBlogsPage();
        LoggingUtil.info("Assertion Passed - Navigation to blogs page is successful");
    }

    @Test
    @Tag("sanity")
    void verifyLanguageChange() {
        homePage.loadThePage();
        homePage.verifyLanguageChange("Russian");
        LoggingUtil.info("Assertion Passed - Language change is successful");
    }

    @Test
    void verifyCurrencyChange() {
        homePage.loadThePage();
        homePage.verifyCurrencyChange("NGN");
        LoggingUtil.info("Assertion Passed - Currency change is successful");
    }

    @Test
    void verifyNavigationToLoginPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToLoginPage();
        LoggingUtil.info("Assertion Passed - Navigation to login page is successful");
    }

    @Test
    void verifyNavigationToCustomerSignupPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToCustomerSignupPage();
        LoggingUtil.info("Assertion Passed - Navigation to customer signup page is successful");
    }

    @Test
    void verifyNavigationToAgentSignupPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToAgentSignupPage();
        LoggingUtil.info("Assertion Passed - Navigation to agent signup page is successful");
    }

    @Test
    void verifyAllQuickSearchTabsAreVisible() {
        homePage.loadThePage();
        homePage.verifyAllQuickSearchTabsAreVisible();
        LoggingUtil.info("Assertion Passed - All quick search tabs are visible");
    }

    @Test
    @Tag("sanity")
    void verifyDownloadAppSection() {
        homePage.loadThePage();
        homePage.verifyDownloadAppSection();
        LoggingUtil.info("Assertion Passed - Navigation to download app page is successful");
    }

    @Test
    void verifyNavigationToAffiliateProgramPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToAffiliateProgramPage();
        LoggingUtil.info("Assertion Passed - Navigation to affiliate program page is successful");
    }

    @Test
    void verifyNavigationToInvestorsPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToInvestorsPage();
        LoggingUtil.info("Assertion Passed - Navigation to investors page is successful");
    }

    @Test
    void verifyNavigationToCareersPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToCareersAndJobsPage();
        LoggingUtil.info("Assertion Passed - Navigation to careers page is successful");
    }

    @Test
    void verifyNavigationToHowToBookPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToHowToBookPage();
        LoggingUtil.info("Assertion Passed - Navigation to how to book page is successful");
    }

    @Test
    void verifyNavigationToFileAClaimPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToFileAClaimPage();
        LoggingUtil.info("Assertion Passed - Navigation to how to file a claim page is successful");
    }

    @Test
    void verifyNavigationToRefundPolicyPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToRefundPolicyPage();
        LoggingUtil.info("Assertion Passed - Navigation to refund policy page is successful");
    }

    @Test
    void verifyNavigationToBestTravelDealsPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToBestTravelDealsPage();
        LoggingUtil.info("Assertion Passed - Navigation to best travel deals page is successful");
    }

    @Test
    void verifyNavigationToTravelDocumentsPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToTravelDocumentsPage();
        LoggingUtil.info("Assertion Passed - Navigation to travel documents page is successful");
    }

    @Test
    void verifyNavigationToTravelInsurancePage() {
        homePage.loadThePage();
        homePage.verifyNavigationToTravelInsurancePage();
        LoggingUtil.info("Assertion Passed - Navigation to travel insurance page is successful");
    }

    @Test
    void verifyNavigationToDisruptionsPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToDisruptionsPage();
        LoggingUtil.info("Assertion Passed - Navigation to disruptions page is successful");
    }

    @Test
    void verifyNavigationToFAQPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToFAQPage();
        LoggingUtil.info("Assertion Passed - Navigation to FAQ page is successful");
    }

    @Test
    void verifyNavigationToAccessibilityPage() {
        homePage.loadThePage();
        homePage.verifyNavigationToAccessibilityPage();
        LoggingUtil.info("Assertion Passed - Navigation to accessibility page is successful");
    }
}