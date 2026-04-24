package pages.subPages.signup;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import pages.basePage.BasePage;

public abstract class SignupBasePage extends BasePage {

    protected final Locator firstNameInput;
    protected final Locator lastNameInput;
    protected final Locator emailInput;
    protected final Locator passwordInput;
    protected final Locator confirmPasswordInput;
    protected final Locator securityCheckQuestion;
    protected final Locator securityCheckAnswerInput;
    protected final Locator agreeToTermsCheckbox;
    protected final Locator createAccountButton;
    protected final Locator backendErrorMessage;

    protected SignupBasePage(Page page) {
        super(page);

        this.firstNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name *"));
        this.lastNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name *"));
        this.emailInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email Address *"));
        this.passwordInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *").setExact(true));
        this.confirmPasswordInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Confirm Password *"));
        this.securityCheckQuestion = page.locator("label[for='captcha_answer']");
        this.securityCheckAnswerInput = page.getByLabel(java.util.regex.Pattern.compile("Security Check:"));
        this.agreeToTermsCheckbox = page.locator("input[name='terms']");
        this.createAccountButton = page.locator("button[type='submit']");
        this.backendErrorMessage = page.locator(".alert-error");
    }
}