package pages.subPages.home;

import com.microsoft.playwright.Page;
import utils.logger.LoggingUtil;

public class HomePage extends HomeBasePage {

    public HomePage(Page page) {
        super(page);
    }

    /**
     * Opens the home page and waits for the page loader to disappear.
     */
    public void loadThePage() {
        modulars.browser.loadThePage(page);
        modulars.browser.waitForPageLoaderToDisappear(page, "loadThePage");
    }

    /**
     * Verifies that the home page has loaded by checking the site logo visibility.
     */
    public void verifyPageLoaded() {
        modulars.assertions.assertElementState(logo, "visible", "Logo", "verifyPageLoaded");
    }

    /**
     * Verifies that the main navigation links in the header are visible.
     */
    public void verifyThatHeaderNavLinksAreVisible() {
        modulars.assertions.assertElementState(servicesButton, "visible", "Services", "verifyThatHeaderNavLinksAreVisible");
        modulars.assertions.assertElementState(companyButton, "visible", "Company", "verifyThatHeaderNavLinksAreVisible");
        modulars.assertions.assertElementState(blogsNavLink, "visible", "Blogs", "verifyThatHeaderNavLinksAreVisible");
        modulars.assertions.assertElementState(languageButton, "visible", "Language", "verifyThatHeaderNavLinksAreVisible");
        modulars.assertions.assertElementState(currencyButton, "visible", "Currency", "verifyThatHeaderNavLinksAreVisible");
        modulars.assertions.assertElementState(loginButton, "visible", "Login", "verifyThatHeaderNavLinksAreVisible");
        modulars.assertions.assertElementState(signupButton, "visible", "Signup", "verifyThatHeaderNavLinksAreVisible");
    }

    /**
     * Verifies navigation from the Services menu to the Visa Booking page.
     */
    public void verifyNavigationToVisaBookingPage() {
        modulars.elements.clickElement(servicesButton, "Services", "verifyNavigationToVisaBookingPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/visa",
                200,
                () -> modulars.elements.clickElement(visaBookingNavLink, "Visa Booking", "verifyNavigationToVisaBookingPage"),
                "GET",
                "Visa Booking Page",
                "verifyNavigationToVisaBookingPage"
        );
    }

    /**
     * Verifies navigation from the Services menu to the Tours Booking page.
     */
    public void verifyNavigationToToursBooking() {
        modulars.elements.hoverElement(servicesButton, "Services", "verifyNavigationToToursBooking");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/tours",
                200,
                () -> modulars.elements.clickElement(toursBookingNavLink, "Tours", "verifyNavigationToToursBooking"),
                "GET",
                "Tours Page",
                "verifyNavigationToToursBooking"
        );
    }

    /**
     * Verifies navigation from the Services menu to the Cars Booking page.
     */
    public void verifyNavigationToCarsBooking() {
        modulars.elements.hoverElement(servicesButton, "Services", "verifyNavigationToCarsBooking");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/cars",
                200,
                () -> modulars.elements.clickElement(carsBookingNavLink, "Cars", "verifyNavigationToCarsBooking"),
                "GET",
                "Cars Page",
                "verifyNavigationToCarsBooking"
        );
    }

    /**
     * Verifies navigation from the Services menu to the Flights Booking page.
     */
    public void verifyNavigationToFlightsBooking() {
        modulars.elements.hoverElement(servicesButton, "Services", "verifyNavigationToFlightsBooking");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/flights",
                200,
                () -> modulars.elements.clickElement(flightsBookingNavLink, "Flights", "verifyNavigationToFlightsBooking"),
                "GET",
                "Flights Page",
                "verifyNavigationToFlightsBooking"
        );
    }

    /**
     * Verifies navigation from the Services menu to the Stays Booking page.
     */
    public void verifyNavigationToStaysBooking() {
        modulars.elements.hoverElement(servicesButton, "Services", "verifyNavigationToStaysBooking");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/stays",
                200,
                () -> modulars.elements.clickElement(staysBookingNavLink, "Stays", "verifyNavigationToStaysBooking"),
                "GET",
                "Stays Page",
                "verifyNavigationToStaysBooking"
        );
    }

    /**
     * Verifies navigation from the Company menu to the Contact Us page.
     */
    public void verifyNavigationToContactUsPage() {
        modulars.elements.hoverElement(companyButton, "Company", "verifyNavigationToContactUsPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/contact-us",
                200,
                () -> modulars.elements.clickElement(contactUsNavLink, "Contact Us", "verifyNavigationToContactUsPage"),
                "GET",
                "Contact Us Page",
                "verifyNavigationToContactUsPage"
        );
        modulars.assertions.assertElementState(contactUsHeader, "visible", "Contact Us Header", "verifyNavigationToContactUsPage");
        LoggingUtil.info("Contact Us Page is opened successfully");
    }

    /**
     * Verifies navigation from the Company menu to the About Us page.
     */
    public void verifyNavigationToAboutUsPage() {
        modulars.elements.hoverElement(companyButton, "Company", "verifyNavigationToAboutUsPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/about-us",
                200,
                () -> modulars.elements.clickElement(aboutUsNavLink, "About Us", "verifyNavigationToAboutUsPage"),
                "GET",
                "About Us Page",
                "verifyNavigationToAboutUsPage"
        );
        modulars.assertions.assertElementState(aboutUsHeader, "visible", "About Us Header", "verifyNavigationToAboutUsPage");
        LoggingUtil.info("About Us Page is opened successfully");
    }

    /**
     * Verifies navigation from the Company menu to the Cookies Policy page.
     */
    public void verifyNavigationToCookiesPolicyPage() {
        modulars.elements.hoverElement(companyButton, "Company", "verifyNavigationToCookiesPolicyPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/cookies-policy",
                200,
                () -> modulars.elements.clickElement(cookiesPolicyNavLink, "Cookies Policy", "verifyNavigationToCookiesPolicyPage"),
                "GET",
                "Cookies Policy Page",
                "verifyNavigationToCookiesPolicyPage"
        );
        modulars.assertions.assertElementState(cookiesPolicyHeader, "visible", "Cookies Policy Header", "verifyNavigationToCookiesPolicyPage");
        LoggingUtil.info("Cookies Policy Page is opened successfully");
    }

    /**
     * Verifies navigation from the Company menu to the Privacy Policy page.
     */
    public void verifyNavigationToPrivacyPolicyPage() {
        modulars.elements.hoverElement(companyButton, "Company", "verifyNavigationToPrivacyPolicyPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/privacy-policy",
                200,
                () -> modulars.elements.clickElement(privacyPolicyNavLink, "Privacy Policy", "verifyNavigationToPrivacyPolicyPage"),
                "GET",
                "Privacy Policy Page",
                "verifyNavigationToPrivacyPolicyPage"
        );
        modulars.assertions.assertElementState(privacyPolicyHeader, "visible", "Privacy Policy Header", "verifyNavigationToPrivacyPolicyPage");
        LoggingUtil.info("Privacy Policy Page is opened successfully");
    }

    /**
     * Verifies navigation from the Company menu to the Become a Supplier page.
     */
    public void verifyNavigationToBecomeASupplierPage() {
        modulars.elements.hoverElement(companyButton, "Company", "verifyNavigationToBecomeASupplierPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/become-a-supplier",
                200,
                () -> modulars.elements.clickElement(becomeASupplierNavLink, "Become A Supplier", "verifyNavigationToBecomeASupplierPage"),
                "GET",
                "Become A Supplier Page",
                "verifyNavigationToBecomeASupplierPage"
        );
        modulars.assertions.assertElementState(becomeASupplierHeader, "visible", "Become A Supplier Header", "verifyNavigationToBecomeASupplierPage");
        LoggingUtil.info("Become A Supplier Page is opened successfully");
    }

    /**
     * Verifies navigation from the Company menu to the Terms of Use page.
     */
    public void verifyNavigationToTermsOfUsePage() {
        modulars.elements.hoverElement(companyButton, "Company", "verifyNavigationToTermsOfUsePage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/terms-of-use",
                200,
                () -> modulars.elements.clickElement(termsOfUseNavLink, "Terms Of Use", "verifyNavigationToTermsOfUsePage"),
                "GET",
                "Terms Of Use Page",
                "verifyNavigationToTermsOfUsePage"
        );
        modulars.assertions.assertElementState(termsOfUseHeader, "visible", "Terms Of Use Header", "verifyNavigationToTermsOfUsePage");
        LoggingUtil.info("Terms Of Use Page is opened successfully");
    }

    /**
     * Verifies navigation to the Blogs page and checks that its heading is visible.
     */
    public void verifyNavigationToBlogsPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/blog",
                302,
                () -> modulars.elements.jsClick(blogsNavLink, "Blogs", "verifyNavigationToBlogsPage"),
                "GET",
                "Blogs Page",
                "verifyNavigationToBlogsPage"
        );
        modulars.assertions.assertElementState(blogsHeader, "visible", "Blogs Header", "verifyNavigationToBlogsPage");
        LoggingUtil.info("Blogs Page is opened successfully");
    }

    /**
     * Changes the selected site language and verifies the updated value is displayed.
     *
     * @param language the language value to select
     */
    public void verifyLanguageChange(String language) {
        modulars.elements.clickElement(languageButton, "Language", "verifyLanguageChange");
        modulars.elements.jsClick(languageOption(language), language, "verifyLanguageChange");
        modulars.assertions.assertElementText(languageButton, language, "Language Button", "verifyLanguageChange", "contains");
        LoggingUtil.info("Language is changed to " + language);
    }

    /**
     * Changes the selected site currency and verifies the updated value is displayed.
     *
     * @param currency the currency value to select
     */
    public void verifyCurrencyChange(String currency) {
        modulars.elements.clickElement(currencyButton, "Currency", "verifyCurrencyChange");
        modulars.elements.jsClick(currencyOption(currency), currency, "verifyCurrencyChange");
        modulars.assertions.assertElementText(currencyButton, currency, "Currency Button", "verifyCurrencyChange", "contains");
        LoggingUtil.info("Currency is changed to " + currency);
    }

    /**
     * Verifies navigation to the login page.
     */
    public void verifyNavigationToLoginPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/login",
                200,
                () -> modulars.elements.jsClick(loginButton, "Login", "verifyNavigationToLoginPage"),
                "GET",
                "Login Page",
                "verifyNavigationToLoginPage"
        );
        modulars.assertions.assertElementState(signInToYourAccountButton, "visible", "Sign In To Your Account Button", "verifyNavigationToLoginPage");
        LoggingUtil.info("Login Page is opened successfully");
    }

    /**
     * Verifies navigation to the customer signup page.
     */
    public void verifyNavigationToCustomerSignupPage() {
        modulars.elements.hoverElement(signupButton, "Signup", "verifyNavigationToCustomerSignupPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/signup",
                200,
                () -> modulars.elements.jsClick(customerSignupButton, "Customer Signup", "verifyNavigationToCustomerSignupPage"),
                "GET",
                "Customer Signup Page",
                "verifyNavigationToCustomerSignupPage"
        );
        modulars.assertions.assertElementState(agentRegistrationHeader, "hidden", "Agent Registration Header", "verifyNavigationToCustomerSignupPage");
        LoggingUtil.info("Customer Signup Page is opened successfully");
    }

    /**
     * Verifies navigation to the agent signup page and confirms the agent registration header is visible.
     */
    public void verifyNavigationToAgentSignupPage() {
        modulars.elements.hoverElement(signupButton, "Signup", "verifyNavigationToAgentSignupPage");
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/agent-signup",
                200,
                () -> modulars.elements.clickElement(agentSignupButton, "Agent Signup", "verifyNavigationToAgentSignupPage"),
                "GET",
                "Agent Signup Page",
                "verifyNavigationToAgentSignupPage"
        );
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/signup?type=agent",
                200,
                () -> modulars.elements.jsClick(agentSignupLink, "Agent Signup Link", "verifyNavigationToAgentSignupPage"),
                "GET",
                "Agent Signup Page",
                "verifyNavigationToAgentSignupPage"
        );
        modulars.assertions.assertElementState(agentRegistrationHeader, "visible", "Agent Registration Header", "verifyNavigationToAgentSignupPage");
        LoggingUtil.info("Agent Signup Page is opened successfully");
    }

    /**
     * Verifies that each quick search tab can be opened and displays its corresponding action button.
     */
    public void verifyAllQuickSearchTabsAreVisible() {
        modulars.elements.clickElement(visaQuickSearchTab, "Visa Quick Search Tab", "verifyAllQuickSearchTabsAreVisible");
        modulars.assertions.assertElementState(checkVisaSearchButton, "visible", "Check Visa Search Button", "verifyAllQuickSearchTabsAreVisible");

        modulars.elements.clickElement(toursQuickSearchTab, "Tours Quick Search Tab", "verifyAllQuickSearchTabsAreVisible");
        modulars.assertions.assertElementState(toursSearchButton, "visible", "Tours Search Button", "verifyAllQuickSearchTabsAreVisible");

        modulars.elements.clickElement(carsQuickSearchTab, "Cars Quick Search Tab", "verifyAllQuickSearchTabsAreVisible");
        modulars.assertions.assertElementState(carsSearchButton, "visible", "Cars Search Button", "verifyAllQuickSearchTabsAreVisible");

        modulars.elements.clickElement(flightsQuickSearchTab, "Flights Quick Search Tab", "verifyAllQuickSearchTabsAreVisible");
        modulars.assertions.assertElementState(flightsSearchButton, "visible", "Flights Search Button", "verifyAllQuickSearchTabsAreVisible");

        modulars.elements.clickElement(staysQuickSearchTab, "Stays Quick Search Tab", "verifyAllQuickSearchTabsAreVisible");
        modulars.assertions.assertElementState(staysSearchButton, "visible", "Stays Search Button", "verifyAllQuickSearchTabsAreVisible");
    }

    /**
     * Verifies that the app download link opens the expected Google Play page in a new tab.
     */
    public void verifyDownloadAppSection() {

        Page newTab = page.waitForPopup(() -> {
            modulars.elements.jsClick(
                    downloadAppNavigationLink,
                    "Download App Navigation Link",
                    "verifyDownloadAppSection"
            );
        });

        newTab.waitForLoadState();

        String url = newTab.url().trim();
        modulars.assertions.assertValuesEqual(
                url,
                "https://play.google.com/store/apps/details?id=com.phptravelsnative",
                "URL",
                "verifyDownloadAppSection"
        );

        String pageTitle = newTab.title();
        modulars.assertions.assertValuesEqual(
                pageTitle,
                "PHPTRAVELS - Apps on Google Play",
                "Page Title",
                "verifyDownloadAppSection"
        );
    }

    /**
     * Verifies navigation to the Affiliate Program page.
     */
    public void verifyNavigationToAffiliateProgramPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/affiliate-program",
                200,
                () -> modulars.elements.jsClick(affiliateProgramNavLink, "Affiliate Program Navigation Link", "verifyNavigationToAffiliateProgramPage"),
                "GET",
                "Affiliate Program Page",
                "verifyNavigationToAffiliateProgramPage"
        );
    }

    /**
     * Verifies navigation to the Investors page.
     */
    public void verifyNavigationToInvestorsPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/investors",
                200,
                () -> modulars.elements.jsClick(investorsNavLink, "Investors Navigation Link", "verifyNavigationToInvestorsPage"),
                "GET",
                "Investors Page",
                "verifyNavigationToInvestorsPage"
        );
    }

    /**
     * Verifies navigation to the Careers and Jobs page.
     */
    public void verifyNavigationToCareersAndJobsPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/careers-and-jobs",
                200,
                () -> modulars.elements.jsClick(careersAndJobsNavLink, "Careers And Jobs Navigation Link", "verifyNavigationToCareersAndJobsPage"),
                "GET",
                "Careers And Jobs Page",
                "verifyNavigationToCareersAndJobsPage"
        );
    }

    /**
     * Verifies navigation to the How to Book page.
     */
    public void verifyNavigationToHowToBookPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/how-to-book",
                200,
                () -> modulars.elements.jsClick(howToBookNavLink, "How To Book Navigation Link", "verifyNavigationToHowToBookPage"),
                "GET",
                "How To Book Page",
                "verifyNavigationToHowToBookPage"
        );
    }

    /**
     * Verifies navigation to the File a Claim page.
     */
    public void verifyNavigationToFileAClaimPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/file-a-claim",
                200,
                () -> modulars.elements.jsClick(fileAClaimNavLink, "File A Claim Navigation Link", "verifyNavigationToFileAClaimPage"),
                "GET",
                "File A Claim Page",
                "verifyNavigationToFileAClaimPage"
        );
    }

    /**
     * Verifies navigation to the Refund Policy page.
     */
    public void verifyNavigationToRefundPolicyPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/refund-policy",
                200,
                () -> modulars.elements.jsClick(refundPolicyNavLink, "Refund Policy Navigation Link", "verifyNavigationToRefundPolicyPage"),
                "GET",
                "Refund Policy Page",
                "verifyNavigationToRefundPolicyPage"
        );
    }

    /**
     * Verifies navigation to the Best Travel Deals page.
     */
    public void verifyNavigationToBestTravelDealsPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/best-travel-deals",
                200,
                () -> modulars.elements.jsClick(bestTravelDealsNavLink, "Best Travel Deals Navigation Link", "verifyNavigationToBestTravelDealsPage"),
                "GET",
                "Best Travel Deals Page",
                "verifyNavigationToBestTravelDealsPage"
        );
    }

    /**
     * Verifies navigation to the Travel Documents page.
     */
    public void verifyNavigationToTravelDocumentsPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/travel-documents",
                200,
                () -> modulars.elements.jsClick(travelDocumentsNavLink, "Travel Documents Navigation Link", "verifyNavigationToTravelDocumentsPage"),
                "GET",
                "Travel Documents Page",
                "verifyNavigationToTravelDocumentsPage"
        );
    }

    /**
     * Verifies navigation to the Travel Insurance page.
     */
    public void verifyNavigationToTravelInsurancePage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/travel-insurance",
                200,
                () -> modulars.elements.jsClick(travelInsuranceNavLink, "Travel Insurance Navigation Link", "verifyNavigationToTravelInsurancePage"),
                "GET",
                "Travel Insurance Page",
                "verifyNavigationToTravelInsurancePage"
        );
    }

    /**
     * Verifies navigation to the Disruptions page.
     */
    public void verifyNavigationToDisruptionsPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/disruption",
                200,
                () -> modulars.elements.jsClick(disruptionNavLink, "Disruptions Navigation Link", "verifyNavigationToDisruptionsPage"),
                "GET",
                "Disruptions Page",
                "verifyNavigationToDisruptionsPage"
        );
    }

    /**
     * Verifies navigation to the FAQ page.
     */
    public void verifyNavigationToFAQPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/frequently-asked-questions",
                200,
                () -> modulars.elements.jsClick(faqOrAnswersNavLink, "FAQ Navigation Link", "verifyNavigationToFAQPage"),
                "GET",
                "FAQ Page",
                "verifyNavigationToFAQPage"
        );
    }

    /**
     * Verifies navigation to the Accessibility page.
     */
    public void verifyNavigationToAccessibilityPage() {
        modulars.network.assertNetworkRequest(
                "https://phptravels.net/page/accessibility",
                200,
                () -> modulars.elements.jsClick(accessibilityNavLink, "Accessibility Navigation Link", "verifyNavigationToAccessibilityPage"),
                "GET",
                "Accessibility Page",
                "verifyNavigationToAccessibilityPage"
        );
    }

    /**
     * Verifies navigation to the Facebook page from the site footer or social links.
     */
    public void verifyNavigationToFacebookPage() {
        modulars.network.assertNetworkRequest(
                "https://www.facebook.com/phptravels",
                200,
                () -> modulars.elements.jsClick(facebookNavLink, "Facebook Navigation Link", "verifyNavigationToFacebookPage"),
                "GET",
                "Facebook Page",
                "verifyNavigationToFacebookPage"
        );
    }
}
