package pages.subPages.home;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import pages.basePage.BasePage;

import java.util.regex.Pattern;

public abstract class HomeBasePage extends BasePage {

    protected final Locator logo;

    protected final Locator servicesButton;
    protected final Locator visaBookingNavLink;
    protected final Locator toursBookingNavLink;
    protected final Locator carsBookingNavLink;
    protected final Locator flightsBookingNavLink;
    protected final Locator staysBookingNavLink;

    protected final Locator companyButton;
    protected final Locator contactUsNavLink;
    protected final Locator contactUsHeader;
    protected final Locator aboutUsNavLink;
    protected final Locator aboutUsHeader;
    protected final Locator cookiesPolicyNavLink;
    protected final Locator cookiesPolicyHeader;
    protected final Locator privacyPolicyNavLink;
    protected final Locator privacyPolicyHeader;
    protected final Locator becomeASupplierNavLink;
    protected final Locator becomeASupplierHeader;
    protected final Locator termsOfUseNavLink;
    protected final Locator termsOfUseHeader;

    protected final Locator blogsNavLink;
    protected final Locator blogsHeader;

    protected final Locator languageButton;
    protected final Locator currencyButton;

    protected final Locator loginButton;
    protected final Locator signInToYourAccountButton;

    protected final Locator signupButton;
    protected final Locator customerSignupButton;
    protected final Locator agentSignupButton;
    protected final Locator agentSignupLink;
    protected final Locator agentRegistrationHeader;

    protected final Locator affiliateProgramNavLink;
    protected final Locator investorsNavLink;
    protected final Locator careersAndJobsNavLink;
    protected final Locator howToBookNavLink;
    protected final Locator fileAClaimNavLink;
    protected final Locator refundPolicyNavLink;

    protected final Locator bestTravelDealsNavLink;
    protected final Locator travelDocumentsNavLink;
    protected final Locator travelInsuranceNavLink;
    protected final Locator disruptionNavLink;
    protected final Locator faqOrAnswersNavLink;
    protected final Locator accessibilityNavLink;

    protected final Locator facebookNavLink;
    protected final Locator twitterNavLink;
    protected final Locator instagramNavLink;
    protected final Locator youtubeNavLink;
    protected final Locator linkedinNavLink;

    protected final Locator footerPrivacyNavLink;
    protected final Locator footerTermsNavLink;
    protected final Locator footerCookiesNavLink;
    protected final Locator footerRefundNavLink;

    protected final Locator downloadAppNavigationLink;

    protected final Locator visaQuickSearchTab;
    protected final Locator checkVisaSearchButton;
    protected final Locator toursQuickSearchTab;
    protected final Locator toursSearchButton;
    protected final Locator carsQuickSearchTab;
    protected final Locator carsSearchButton;
    protected final Locator flightsQuickSearchTab;
    protected final Locator flightsSearchButton;
    protected final Locator staysQuickSearchTab;
    protected final Locator staysSearchButton;

    protected HomeBasePage(Page page) {
        super(page);

        this.logo = page.locator("a[href='https://phptravels.net/'] img[alt='PHPTARVELS']");

        this.servicesButton = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Services expand_more"));
        this.visaBookingNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Visa Booking"));
        this.toursBookingNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Tours Booking"));
        this.carsBookingNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Cars Booking"));
        this.flightsBookingNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Flights Booking"));
        this.staysBookingNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Stays Booking"));

        this.companyButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Company expand_more"));
        this.contactUsNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Contact us"));
        this.contactUsHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Contact us"));
        this.aboutUsNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right About us"));
        this.aboutUsHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("About us"));
        this.cookiesPolicyNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Cookies policy"));
        this.cookiesPolicyHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Cookies Policy"));
        this.privacyPolicyNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Privacy policy"));
        this.privacyPolicyHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Privacy Policy"));
        this.becomeASupplierNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Become a supplier"));
        this.becomeASupplierHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Become a Supplier"));
        this.termsOfUseNavLink = page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("chevron_right Terms of use"));
        this.termsOfUseHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Terms of Use"));

        this.blogsNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Blogs"));
        this.blogsHeader = page.locator("h1:text('Blogs')");

        this.languageButton = page.locator("div.group > button").nth(2);
        this.currencyButton = page.locator("div.group > button").nth(3);

        this.loginButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("login Login"));
        this.signInToYourAccountButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Sign In to your account", Pattern.CASE_INSENSITIVE)));

        this.signupButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("person_add Signup expand_more"));
        this.customerSignupButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("person Customer Signup"));
        this.agentSignupButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("business_center Agent Signup"));
        this.agentSignupLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Agent Signup"));
        this.agentRegistrationHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Agent Registration"));

        this.affiliateProgramNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Affiliate"));
        this.investorsNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Investors"));
        this.careersAndJobsNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Careers and Jobs"));
        this.howToBookNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right How to Book"));
        this.fileAClaimNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right File a Claim"));
        this.refundPolicyNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Refund Policy"));

        this.bestTravelDealsNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Best Travel"));
        this.travelDocumentsNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Travel Documents"));
        this.travelInsuranceNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Travel Insurance"));
        this.disruptionNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Disruption"));
        this.faqOrAnswersNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right FAQ / Answers"));
        this.accessibilityNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("chevron_right Accessibility"));

        this.facebookNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Facebook"));
        this.instagramNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Instagram"));
        this.twitterNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Twitter"));
        this.youtubeNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("YouTube"));
        this.linkedinNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("LinkedIn"));

        this.footerPrivacyNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("shield Privacy"));
        this.footerTermsNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("description Terms"));
        this.footerCookiesNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("cookie Cookies"));
        this.footerRefundNavLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("currency_exchange Refund"));

        this.downloadAppNavigationLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Google Play"));

        this.visaQuickSearchTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Visa"));
        this.checkVisaSearchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Check Visa|Searching", Pattern.CASE_INSENSITIVE)));
        this.toursQuickSearchTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("explore Tours"));
        this.toursSearchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Search Tours|Searching", Pattern.CASE_INSENSITIVE)));
        this.carsQuickSearchTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("directions_car Cars"));
        this.carsSearchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Search Cars|Searching", Pattern.CASE_INSENSITIVE)));
        this.flightsQuickSearchTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("flight_takeoff Flights"));
        this.flightsSearchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Search Flights|Searching", Pattern.CASE_INSENSITIVE)));
        this.staysQuickSearchTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("hotel Stays"));
        this.staysSearchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("Search Hotels|Searching", Pattern.CASE_INSENSITIVE)));
    }

    /**
     * Returns the language option locator matching the supplied choice text.
     *
     * @param choice the language label to locate
     * @return the locator for the requested language option
     */
    protected Locator languageOption(String choice) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(choice));
    }

    /**
     * Returns the currency option locator matching the supplied choice text.
     *
     * @param choice the currency label to locate
     * @return the locator for the requested currency option
     */
    protected Locator currencyOption(String choice) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(choice));
    }
}
