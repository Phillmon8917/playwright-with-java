package pages.subPages.signup;

import com.microsoft.playwright.Page;
import utils.validation.ValidationHelper;

public class SignupPage extends SignupBasePage {

    public SignupPage(Page page) {
        super(page);
    }

    /**
     * Fills the first name input field on the signup form.
     *
     * @param firstName the first name to enter
     */
    private void fillFirstNameInput(String firstName) {
        modulars.elements.fillTheElement(firstNameInput, firstName, "First Name", "fillFirstNameInput");
    }

    /**
     * Fills the last name input field on the signup form.
     *
     * @param lastName the last name to enter
     */
    private void fillLastNameInput(String lastName) {
        modulars.elements.fillTheElement(lastNameInput, lastName, "Last Name", "fillLastNameInput");
    }

    /**
     * Fills the email input field on the signup form.
     *
     * @param email the email address to enter
     */
    private void fillEmailInput(String email) {
        modulars.elements.fillTheElement(emailInput, email, "Email", "fillEmailInput", true);
    }

    /**
     * Fills the password input field on the signup form.
     *
     * @param password the password to enter
     */
    private void fillPasswordInput(String password) {
        modulars.elements.fillTheElement(passwordInput, password, "Password", "fillPasswordInput", true);
    }

    /**
     * Fills the confirm password input field on the signup form.
     *
     * @param password the confirmation password to enter
     */
    private void fillConfirmPasswordInput(String password) {
        modulars.elements.fillTheElement(confirmPasswordInput, password, "Confirm Password", "fillConfirmPasswordInput", true);
    }

    /**
     * Fills the security check answer field on the signup form.
     *
     * @param answer the answer to the displayed security question
     */
    private void fillSecurityCheckAnswerInput(String answer) {
        modulars.elements.fillTheElement(securityCheckAnswerInput, answer, "Security Check Answer", "fillSecurityCheckAnswerInput");
    }

    /**
     * Extracts the visible security question text from the signup form.
     *
     * @return the displayed security question, or an empty string when no text is available
     */
    public String extractSecurityCheckQuestion() {
        String question = modulars.elements.getText(securityCheckQuestion, "Security Check Question", "extractSecurityCheckQuestion");
        return question != null ? question : "";
    }

    /**
     * Selects the terms agreement checkbox when agreement is required.
     *
     * @param agree indicates whether the terms checkbox should be selected
     */
    private void agreeToTerms(boolean agree) {
        if (agree) {
            modulars.elements.jsClick(agreeToTermsCheckbox, "Agree to Terms", "agreeToTerms");
        }
    }

    /**
     * Clicks the create account button to submit the signup form.
     */
    private void clickCreateAccountButton() {
        modulars.elements.jsClick(createAccountButton, "Create Account", "clickCreateAccountButton");
    }

    /**
     * Fills the signup form using the supplied options and verifies either client-side validation or successful submission.
     *
     * @param option the signup data and expected behavior to apply to the form
     */
    public void fillTheSignUpForm(SignupOptions option) {
        if (option.firstName() != null && !option.firstName().isEmpty()) {
            fillFirstNameInput(option.firstName());
        }
        if (option.lastName() != null && !option.lastName().isEmpty()) {
            fillLastNameInput(option.lastName());
        }
        if (option.email() != null && !option.email().isEmpty()) {
            fillEmailInput(option.email());
        }
        if (option.password() != null && !option.password().isEmpty()) {
            fillPasswordInput(option.password());
        }
        if (option.confirmPassword() != null && !option.confirmPassword().isEmpty()) {
            fillConfirmPasswordInput(option.confirmPassword());
        }
        if (option.securityCheck() != null && !option.securityCheck().isEmpty()) {
            fillSecurityCheckAnswerInput(option.securityCheck());
        }
        agreeToTerms(option.agreeToTerms());

        if (option.expectValidationErrors()) {
            clickCreateAccountButton();
            String validationMessage = ValidationHelper.getValidationMessage(page, option.fieldId());
            modulars.assertions.assertNotNullOrEmptyVar(validationMessage, "Validation Message", "fillTheSignUpForm");
        } else {
            modulars.network.assertNetworkRequest(
                    "https://phptravels.net/login",
                    200,
                    this::clickCreateAccountButton,
                    "GET",
                    "Create Account",
                "fillTheSignUpForm"
            );
        }
    }

    /**
     * Verifies that backend validation error messaging is not visible on the signup page.
     */
    public void verifyBackendValidationErrorsAreHidden() {
        modulars.assertions.assertElementState(backendErrorMessage, "hidden", "Backend Error Message", "verifyBackendValidationErrorsAreHidden");
    }
}
