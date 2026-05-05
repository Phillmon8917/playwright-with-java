package steps.home;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.logger.LoggingUtil;

public class HomePageSteps {

    private final TestContext context;

    public HomePageSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Verifies that the home page is fully loaded.
     */
    @Then("the home page should be fully loaded")
    public void theHomePageShouldBeFullyLoaded() {
        context.getHomePage().verifyPageLoaded();
        LoggingUtil.info("Assertion Passed - Home Page is loaded");
    }

    /**
     * Verifies that all header navigation links are visible.
     */
    @Then("all header navigation links should be visible")
    public void allHeaderNavigationLinksShouldBeVisible() {
        context.getHomePage().verifyThatHeaderNavLinksAreVisible();
        LoggingUtil.info("Assertion Passed - Header navigation links are present");
    }

    /**
     * Clicks on the visa booking section.
     */
    @When("I click on the visa booking section")
    public void iClickOnTheVisaBookingSection() {
        context.getHomePage().verifyNavigationToVisaBookingPage();
    }

    /**
     * Verifies navigation to the visa booking page.
     */
    @Then("I should be navigated to the visa booking page")
    public void iShouldBeNavigatedToTheVisaBookingPage() {
        LoggingUtil.info("Assertion Passed - Navigation to visa booking page is successful");
    }

    /**
     * Clicks on the tours booking section.
     */
    @When("I click on the tours booking section")
    public void iClickOnTheToursBookingSection() {
        context.getHomePage().verifyNavigationToToursBooking();
    }

    /**
     * Verifies navigation to the tours booking page.
     */
    @Then("I should be navigated to the tours booking page")
    public void iShouldBeNavigatedToTheToursBookingPage() {
        LoggingUtil.info("Assertion Passed - Navigation to tours booking page is successful");
    }

    /**
     * Clicks on the cars booking section.
     */
    @When("I click on the cars booking section")
    public void iClickOnTheCarsBookingSection() {
        context.getHomePage().verifyNavigationToCarsBooking();
    }

    /**
     * Verifies navigation to the cars booking page.
     */
    @Then("I should be navigated to the cars booking page")
    public void iShouldBeNavigatedToTheCarsBookingPage() {
        LoggingUtil.info("Assertion Passed - Navigation to cars booking page is successful");
    }

    /**
     * Clicks on the flights booking section.
     */
    @When("I click on the flights booking section")
    public void iClickOnTheFlightsBookingSection() {
        context.getHomePage().verifyNavigationToFlightsBooking();
    }

    /**
     * Verifies navigation to the flight booking page.
     */
    @Then("I should be navigated to the flight booking page")
    public void iShouldBeNavigatedToTheFlightBookingPage() {
        LoggingUtil.info("Assertion Passed - Navigation to flight booking page is successful");
    }

    /**
     * Clicks on the stays booking section.
     */
    @When("I click on the stays booking section")
    public void iClickOnTheStaysBookingSection() {
        context.getHomePage().verifyNavigationToStaysBooking();
    }

    /**
     * Verifies navigation to the stays booking page.
     */
    @Then("I should be navigated to the stays booking page")
    public void iShouldBeNavigatedToTheStaysBookingPage() {
        LoggingUtil.info("Assertion Passed - Navigation to stays booking page is successful");
    }

    /**
     * Clicks on the contact us link.
     */
    @When("I click on the contact us link")
    public void iClickOnTheContactUsLink() {
        context.getHomePage().verifyNavigationToContactUsPage();
    }

    /**
     * Verifies navigation to the contact us page.
     */
    @Then("I should be navigated to the contact us page")
    public void iShouldBeNavigatedToTheContactUsPage() {
        LoggingUtil.info("Assertion Passed - Navigation to contact us page is successful");
    }

    /**
     * Clicks on the about us link.
     */
    @When("I click on the about us link")
    public void iClickOnTheAboutUsLink() {
        context.getHomePage().verifyNavigationToAboutUsPage();
    }

    /**
     * Verifies navigation to the about us page.
     */
    @Then("I should be navigated to the about us page")
    public void iShouldBeNavigatedToTheAboutUsPage() {
        LoggingUtil.info("Assertion Passed - Navigation to about us page is successful");
    }

    /**
     * Clicks on the cookies policy link.
     */
    @When("I click on the cookies policy link")
    public void iClickOnTheCookiesPolicyLink() {
        context.getHomePage().verifyNavigationToCookiesPolicyPage();
    }

    /**
     * Verifies navigation to the cookies policy page.
     */
    @Then("I should be navigated to the cookies policy page")
    public void iShouldBeNavigatedToTheCookiesPolicyPage() {
        LoggingUtil.info("Assertion Passed - Navigation to cookies policy page is successful");
    }

    /**
     * Clicks on the privacy policy link.
     */
    @When("I click on the privacy policy link")
    public void iClickOnThePrivacyPolicyLink() {
        context.getHomePage().verifyNavigationToPrivacyPolicyPage();
    }

    /**
     * Verifies navigation to the privacy policy page.
     */
    @Then("I should be navigated to the privacy policy page")
    public void iShouldBeNavigatedToThePrivacyPolicyPage() {
        LoggingUtil.info("Assertion Passed - Navigation to privacy policy page is successful");
    }

    /**
     * Clicks on the become a supplier link.
     */
    @When("I click on the become a supplier link")
    public void iClickOnTheBecomeASupplierLink() {
        context.getHomePage().verifyNavigationToBecomeASupplierPage();
    }

    /**
     * Verifies navigation to the become a supplier page.
     */
    @Then("I should be navigated to the become a supplier page")
    public void iShouldBeNavigatedToTheBecomeASupplierPage() {
        LoggingUtil.info("Assertion Passed - Navigation to become a supplier page is successful");
    }

    /**
     * Clicks on the terms of use link.
     */
    @When("I click on the terms of use link")
    public void iClickOnTheTermsOfUseLink() {
        context.getHomePage().verifyNavigationToTermsOfUsePage();
    }

    /**
     * Verifies navigation to the terms of use page.
     */
    @Then("I should be navigated to the terms of use page")
    public void iShouldBeNavigatedToTheTermsOfUsePage() {
        LoggingUtil.info("Assertion Passed - Navigation to terms of use page is successful");
    }

    /**
     * Clicks on the blogs link.
     */
    @When("I click on the blogs link")
    public void iClickOnTheBlogsLink() {
        context.getHomePage().verifyNavigationToBlogsPage();
    }

    /**
     * Verifies navigation to the blogs page.
     */
    @Then("I should be navigated to the blogs page")
    public void iShouldBeNavigatedToTheBlogsPage() {
        LoggingUtil.info("Assertion Passed - Navigation to blogs page is successful");
    }

    /**
     * Changes the language to the specified value.
     *
     * @param language the language to change to
     */
    @When("I change the language to {string}")
    public void iChangeTheLanguageTo(String language) {
        context.getHomePage().verifyLanguageChange(language);
    }

    /**
     * Verifies that the page language has been changed successfully.
     */
    @Then("the page language should be changed successfully")
    public void thePageLanguageShouldBeChangedSuccessfully() {
        LoggingUtil.info("Assertion Passed - Language change is successful");
    }

    /**
     * Changes the currency to the specified value.
     *
     * @param currency the currency to change to
     */
    @When("I change the currency to {string}")
    public void iChangeTheCurrencyTo(String currency) {
        context.getHomePage().verifyCurrencyChange(currency);
    }

    /**
     * Verifies that the page currency has been changed successfully.
     */
    @Then("the page currency should be changed successfully")
    public void thePageCurrencyShouldBeChangedSuccessfully() {
        LoggingUtil.info("Assertion Passed - Currency change is successful");
    }

    /**
     * Clicks on the login link.
     */
    @When("I click on the login link")
    public void iClickOnTheLoginLink() {
        context.getHomePage().verifyNavigationToLoginPage();
    }

    /**
     * Verifies navigation to the login page.
     */
    @Then("I should be navigated to the login page")
    public void iShouldBeNavigatedToTheLoginPage() {
        LoggingUtil.info("Assertion Passed - Navigation to login page is successful");
    }

    /**
     * Clicks on the customer signup link.
     */
    @When("I click on the customer signup link")
    public void iClickOnTheCustomerSignupLink() {
        context.getHomePage().verifyNavigationToCustomerSignupPage();
    }

    /**
     * Verifies navigation to the customer signup page.
     */
    @Then("I should be navigated to the customer signup page")
    public void iShouldBeNavigatedToTheCustomerSignupPage() {
        LoggingUtil.info("Assertion Passed - Navigation to customer signup page is successful");
    }

    /**
     * Clicks on the agent signup link.
     */
    @When("I click on the agent signup link")
    public void iClickOnTheAgentSignupLink() {
        context.getHomePage().verifyNavigationToAgentSignupPage();
    }

    /**
     * Verifies navigation to the agent signup page.
     */
    @Then("I should be navigated to the agent signup page")
    public void iShouldBeNavigatedToTheAgentSignupPage() {
        LoggingUtil.info("Assertion Passed - Navigation to agent signup page is successful");
    }

    /**
     * Verifies that all quick search tabs are visible.
     */
    @Then("all quick search tabs should be visible")
    public void allQuickSearchTabsShouldBeVisible() {
        context.getHomePage().verifyAllQuickSearchTabsAreVisible();
        LoggingUtil.info("Assertion Passed - All quick search tabs are visible");
    }

    /**
     * Verifies that the download app section is visible and functional.
     */
    @Then("the download app section should be visible and functional")
    public void theDownloadAppSectionShouldBeVisibleAndFunctional() {
        context.getHomePage().verifyDownloadAppSection();
        LoggingUtil.info("Assertion Passed - Download app section verified");
    }

    /**
     * Clicks on the affiliate program link.
     */
    @When("I click on the affiliate program link")
    public void iClickOnTheAffiliateProgramLink() {
        context.getHomePage().verifyNavigationToAffiliateProgramPage();
    }

    /**
     * Verifies navigation to the affiliate program page.
     */
    @Then("I should be navigated to the affiliate program page")
    public void iShouldBeNavigatedToTheAffiliateProgramPage() {
        LoggingUtil.info("Assertion Passed - Navigation to affiliate program page is successful");
    }

    /**
     * Clicks on the investors link.
     */
    @When("I click on the investors link")
    public void iClickOnTheInvestorsLink() {
        context.getHomePage().verifyNavigationToInvestorsPage();
    }

    /**
     * Verifies navigation to the investors page.
     */
    @Then("I should be navigated to the investors page")
    public void iShouldBeNavigatedToTheInvestorsPage() {
        LoggingUtil.info("Assertion Passed - Navigation to investors page is successful");
    }

    /**
     * Clicks on the careers link.
     */
    @When("I click on the careers link")
    public void iClickOnTheCareersLink() {
        context.getHomePage().verifyNavigationToCareersAndJobsPage();
    }

    /**
     * Verifies navigation to the careers page.
     */
    @Then("I should be navigated to the careers page")
    public void iShouldBeNavigatedToTheCareersPage() {
        LoggingUtil.info("Assertion Passed - Navigation to careers page is successful");
    }

    /**
     * Clicks on the how to book link.
     */
    @When("I click on the how to book link")
    public void iClickOnTheHowToBookLink() {
        context.getHomePage().verifyNavigationToHowToBookPage();
    }

    /**
     * Verifies navigation to the how to book page.
     */
    @Then("I should be navigated to the how to book page")
    public void iShouldBeNavigatedToTheHowToBookPage() {
        LoggingUtil.info("Assertion Passed - Navigation to how to book page is successful");
    }

    /**
     * Clicks on the file a claim link.
     */
    @When("I click on the file a claim link")
    public void iClickOnTheFileAClaimLink() {
        context.getHomePage().verifyNavigationToFileAClaimPage();
    }

    /**
     * Verifies navigation to the file a claim page.
     */
    @Then("I should be navigated to the file a claim page")
    public void iShouldBeNavigatedToTheFileAClaimPage() {
        LoggingUtil.info("Assertion Passed - Navigation to file a claim page is successful");
    }

    /**
     * Clicks on the refund policy link.
     */
    @When("I click on the refund policy link")
    public void iClickOnTheRefundPolicyLink() {
        context.getHomePage().verifyNavigationToRefundPolicyPage();
    }

    /**
     * Verifies navigation to the refund policy page.
     */
    @Then("I should be navigated to the refund policy page")
    public void iShouldBeNavigatedToTheRefundPolicyPage() {
        LoggingUtil.info("Assertion Passed - Navigation to refund policy page is successful");
    }

    /**
     * Clicks on the best travel deals link.
     */
    @When("I click on the best travel deals link")
    public void iClickOnTheBestTravelDealsLink() {
        context.getHomePage().verifyNavigationToBestTravelDealsPage();
    }

    /**
     * Verifies navigation to the best travel deals page.
     */
    @Then("I should be navigated to the best travel deals page")
    public void iShouldBeNavigatedToTheBestTravelDealsPage() {
        LoggingUtil.info("Assertion Passed - Navigation to best travel deals page is successful");
    }

    /**
     * Clicks on the travel documents link.
     */
    @When("I click on the travel documents link")
    public void iClickOnTheTravelDocumentsLink() {
        context.getHomePage().verifyNavigationToTravelDocumentsPage();
    }

    /**
     * Verifies navigation to the travel documents page.
     */
    @Then("I should be navigated to the travel documents page")
    public void iShouldBeNavigatedToTheTravelDocumentsPage() {
        LoggingUtil.info("Assertion Passed - Navigation to travel documents page is successful");
    }

    /**
     * Clicks on the travel insurance link.
     */
    @When("I click on the travel insurance link")
    public void iClickOnTheTravelInsuranceLink() {
        context.getHomePage().verifyNavigationToTravelInsurancePage();
    }

    /**
     * Verifies navigation to the travel insurance page.
     */
    @Then("I should be navigated to the travel insurance page")
    public void iShouldBeNavigatedToTheTravelInsurancePage() {
        LoggingUtil.info("Assertion Passed - Navigation to travel insurance page is successful");
    }

    /**
     * Clicks on the disruptions link.
     */
    @When("I click on the disruptions link")
    public void iClickOnTheDisruptionsLink() {
        context.getHomePage().verifyNavigationToDisruptionsPage();
    }

    /**
     * Verifies navigation to the disruptions page.
     */
    @Then("I should be navigated to the disruptions page")
    public void iShouldBeNavigatedToTheDisruptionsPage() {
        LoggingUtil.info("Assertion Passed - Navigation to disruptions page is successful");
    }

    /**
     * Clicks on the FAQ link.
     */
    @When("I click on the FAQ link")
    public void iClickOnTheFAQLink() {
        context.getHomePage().verifyNavigationToFAQPage();
    }

    /**
     * Verifies navigation to the FAQ page.
     */
    @Then("I should be navigated to the FAQ page")
    public void iShouldBeNavigatedToTheFAQPage() {
        LoggingUtil.info("Assertion Passed - Navigation to FAQ page is successful");
    }

    /**
     * Clicks on the accessibility link.
     */
    @When("I click on the accessibility link")
    public void iClickOnTheAccessibilityLink() {
        context.getHomePage().verifyNavigationToAccessibilityPage();
    }

    /**
     * Verifies navigation to the accessibility page.
     */
    @Then("I should be navigated to the accessibility page")
    public void iShouldBeNavigatedToTheAccessibilityPage() {
        LoggingUtil.info("Assertion Passed - Navigation to accessibility page is successful");
    }
}
