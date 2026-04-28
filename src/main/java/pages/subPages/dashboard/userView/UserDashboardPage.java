package pages.subPages.dashboard.userView;

import com.microsoft.playwright.Page;

import utils.logger.LoggingUtil;

public class UserDashboardPage extends UserDashboardBasePage {

    public UserDashboardPage(Page page) {
        super(page);
    }

    /**
     * Verifies that the user dashboard is properly loaded by asserting the current URL matches the expected dashboard URL.
     */
    public void verifyNavigationToUserDashboard() {
        this.modulars.browser.assertUrl(this.page, "https://phptravels.net/dashboard", "verifyNavigationToUserDashboard");
    }

    /**
     * Clicks the dashboard side bar to toggle or interact with it.
     */
    public void clickDashboardSideBar() {
        this.modulars.elements.jsClick(this.dashboardSideBar, "Dashboard Side Bar", "clickDashboardSideBar");
    }

    /**
     * Clicks the My Bookings side bar to open or close it based on the specified side bar option.
     * If the option is OPEN, ensures the side bar is expanded; if CLOSE, ensures it is collapsed.
     *
     * @param sideBarOption the action to perform on the side bar (OPEN or CLOSE)
     */
    private void clickMyBookingsSideBar(SideBarOption sideBarOption) {
        String myBookingsClassAttribute = this.modulars.elements.getAttribute(this.myBookingsSideBar, "class", "My Bookings Side Bar", "clickMyBookingsSideBar");
        LoggingUtil.info("My Bookings Side Bar Class Attribute: " + myBookingsClassAttribute);
        if (sideBarOption == SideBarOption.OPEN) {
            if (!myBookingsClassAttribute.contains("rotate-180")) {
                this.modulars.elements.jsClick(this.myBookingsSideBar, "My Bookings Side Bar", "clickMyBookingsSideBar");
            }

            this.modulars.assertions.assertElementContains(this.myBookingsSideBar, "class", "rotate-180", "My Bookings Side Bar", "clickMyBookingsSideBar");
        } else {
            if (myBookingsClassAttribute.contains("rotate-180")) {
                this.modulars.elements.jsClick(this.myBookingsSideBar, "My Bookings Side Bar", "clickMyBookingsSideBar");
            }

            this.modulars.assertions.assertElementNotContains(this.myBookingsSideBar, "class", "rotate-180", "My Bookings Side Bar", "clickMyBookingsSideBar");
        }
    }

    public void navigateToAllBookingsPage() {
        this.clickMyBookingsSideBar(SideBarOption.OPEN);
        this.modulars.network.assertNetworkRequest(
                "https://phptravels.net/bookings",
                200,
                () -> this.modulars.elements.jsClick(this.allBookingsSideBar, "All Bookings Side Bar", "navigateToAllBookingsPage"),
                "GET",
                "All Bookings Page",
                "navigateToAllBookingsPage"
        );

        this.modulars.browser.assertUrl(page, "https://phptravels.net/bookings", "navigateToAllBookingsPage");
        this.modulars.browser.assertTitle(page, "My Bookings | PHPTRAVELS", "navigateToAllBookingsPage");
    }
}
