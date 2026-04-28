package pages.subPages.dashboard.userView;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import pages.basePage.BasePage;

public class UserDashboardBasePage extends BasePage {

    protected final Locator dashboardSideBar;
    protected final Locator myBookingsSideBar;
    protected  final Locator allBookingsSideBar;
    protected final Locator staysSideBar;
    protected final Locator carsSideBar;
    protected final Locator toursSideBar;
    protected final Locator visaSideBar;
    protected final Locator flightsSideBar;
    protected final Locator supportSideBar;
    protected final Locator supportTicketsSideBar;
    protected final Locator favouritesSideBar;
    protected final Locator accountSideBar;
    protected final Locator profileSideBar;
    protected final Locator logoutSideBar;
    protected final Locator quickActionsHeader;
    protected final Locator editProfileSideBar;
    protected final Locator accountSummaryHeader;
    protected final Locator memberSinceText;
    protected final Locator totalTripsText;
    protected final Locator loyaltyPointsText;
    protected final Locator financeText;
    protected final Locator walletBalanceText;

    public UserDashboardBasePage(Page page) {
        super(page);

        this.dashboardSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("dashboard Dashboard"));
        this.financeText = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Finance"));
        this.walletBalanceText = page.getByText("Wallet Balance");
        this.myBookingsSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("My Bookings"));
        this.allBookingsSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("list_alt All Bookings"));
        this.staysSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("hotel Stays")).first();
        this.flightsSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("flight_takeoff Flights")).first();
        this.toursSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("tour Tours")).first();
        this.carsSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("directions_car Cars")).first();
        this.visaSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("description Visa")).first();
        this.supportSideBar = page.locator("div span:text('Support')");
        this.supportTicketsSideBar = page.locator("a span:text('Support Tickets')");
        this.favouritesSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("favorite Favourites"));
        this.accountSideBar = page.locator("div div span:text('Account')");
        this.profileSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("person Profile"));
        this.logoutSideBar = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("logout Logout"));
        this.quickActionsHeader =  page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Quick Actions"));
        this.editProfileSideBar =  page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("person Edit Profile"));
        this.accountSummaryHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Account Summary"));
        this.memberSinceText =  page.getByText("Member Since");
        this.totalTripsText =  page.getByText("Total Trips");
        this.loyaltyPointsText =  page.getByText("Loyalty Points");
    }
}