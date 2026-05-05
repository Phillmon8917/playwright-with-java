package context;

import com.microsoft.playwright.Page;

import pages.subPages.home.HomePage;
import pages.subPages.login.LoginPage;
import pages.subPages.signup.SignupPage;

public class TestContext {

    private Page page;
    private HomePage homePage;
    private LoginPage loginPage;
    private SignupPage signupPage;

    private String securityCheckAnswer;

    /**
     * Returns the Playwright page instance.
     *
     * @return the page instance
     */
    public Page getPage() {
        return page;
    }

    /**
     * Sets the Playwright page instance.
     *
     * @param page the page instance to set
     */
    public void setPage(Page page) {
        this.page = page;
    }

    /**
     * Returns the HomePage instance.
     *
     * @return the homePage instance
     */
    public HomePage getHomePage() {
        return homePage;
    }

    /**
     * Sets the HomePage instance.
     *
     * @param homePage the homePage instance to set
     */
    public void setHomePage(HomePage homePage) {
        this.homePage = homePage;
    }

    /**
     * Returns the LoginPage instance.
     *
     * @return the loginPage instance
     */
    public LoginPage getLoginPage() {
        return loginPage;
    }

    /**
     * Sets the LoginPage instance.
     *
     * @param loginPage the loginPage instance to set
     */
    public void setLoginPage(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    /**
     * Returns the SignupPage instance.
     *
     * @return the signupPage instance
     */
    public SignupPage getSignupPage() {
        return signupPage;
    }

    /**
     * Sets the SignupPage instance.
     *
     * @param signupPage the signupPage instance to set
     */
    public void setSignupPage(SignupPage signupPage) {
        this.signupPage = signupPage;
    }

    /**
     * Returns the security check answer.
     *
     * @return the securityCheckAnswer
     */
    public String getSecurityCheckAnswer() {
        return securityCheckAnswer;
    }

    /**
     * Sets the security check answer.
     *
     * @param securityCheckAnswer the securityCheckAnswer to set
     */
    public void setSecurityCheckAnswer(String securityCheckAnswer) {
        this.securityCheckAnswer = securityCheckAnswer;
    }
}
