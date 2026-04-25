package pages.subPages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import pages.basePage.BasePage;

public class LoginBasePage extends BasePage {

    protected final Locator emailInput;
    protected final Locator passwordInput;
    protected final Locator rememberMeCheckbox;
    protected final Locator loginButton;
    protected final Locator dashBoardHeader;
    protected final Locator loggedInUserButton;
    protected final Locator logoutButton;

    public LoginBasePage(Page page) {
        super(page);

        this.emailInput = page.locator("input[placeholder='Email Address']");
        this.passwordInput = page.locator("input[placeholder='Password']");
        this.rememberMeCheckbox = page.locator("div.checkbox-custom span");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
        this.dashBoardHeader = page.locator("div h1:text('Dashboard')");
        this.loggedInUserButton = page.locator("button span:has-text('account_circle')");
        this.logoutButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout"));
    }
}