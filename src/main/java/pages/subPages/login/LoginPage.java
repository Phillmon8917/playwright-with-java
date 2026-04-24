package pages.subPages.login;

import com.microsoft.playwright.Page;
import utils.logger.LoggingUtil;

public class LoginPage extends LoginBasePage {

    public LoginPage(Page page) {
        super(page);
    }

    /**
     * Fills the email input field on the login form.
     *
     * @param email the email address to enter
     */
    private void fillEmailInput(String email) {
        modulars.elements.fillTheElement(emailInput, email, "Email", "fillEmailInput", true);
    }

    /**
     * Fills the password input field on the login form.
     *
     * @param password the password to enter
     */
    private void fillPasswordInput(String password) {
        modulars.elements.fillTheElement(passwordInput, password, "Password", "fillPasswordInput", true);
    }

    /**
     * Toggles the remember me option when it should be enabled.
     *
     * @param remember indicates whether the remember me option should be selected
     */
    private void toggleRememberMe(boolean remember) {
        if (remember) {
            modulars.elements.jsClick(rememberMeCheckbox, "Toggle Remember Me", "toggleRememberMe");
        }
    }

    /**
     * Clicks the login button to submit the login form.
     */
    private void clickLoginButton() {
        modulars.elements.jsClick(loginButton, "Login", "clickLoginButton");
    }

    /**
     * Clicks the logout button for the currently signed-in user.
     */
    private void clickLogoutButton() {
        modulars.elements.jsClick(logoutButton, "Logout", "clickLogoutButton");
    }

    /**
     * Verifies that the dashboard header is visible after a successful login.
     */
    private void verifySuccessfulLogin() {
        modulars.assertions.assertElementState(
                dashBoardHeader,
                "visible",
                "Dashboard Header",
                "verifySuccessfulLogin"
        );
    }

    /**
     * ONLY responsible for UI login flow.
     */
    public void login(String email, String password, boolean remember) {
        fillEmailInput(email);
        fillPasswordInput(password);
        toggleRememberMe(remember);
        clickLoginButton();
        verifySuccessfulLogin();

        LoggingUtil.info("Login successful");
    }

    /**
     * Logs the current user out and verifies that the login button becomes visible again.
     */
    public void logout() {
        modulars.elements.hoverElement(loggedInUserButton, "Logged In User Button", "logout");
        clickLogoutButton();

        modulars.assertions.assertElementState(
                loginButton,
                "visible",
                "Login Button",
                "logout"
        );

        LoggingUtil.info("Logout successful");
    }
}
