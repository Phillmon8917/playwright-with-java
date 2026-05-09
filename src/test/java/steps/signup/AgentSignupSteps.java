package steps.signup;

import context.TestContext;
import io.cucumber.java.en.When;
import pages.subPages.signup.SignupOptions;
import utils.faker.FakerHelper;

public class AgentSignupSteps {

    private final TestContext context;

    public AgentSignupSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Navigates to the agent signup page from the home page.
     */
    @When("I navigate to the agent signup page")
    public void iNavigateToTheAgentSignupPage() {
        context.getHomePage().verifyNavigationToAgentSignupPage();
    }

    /**
     * Solves the security check question (answer already resolved in context).
     */
    @When("I solve the security check question")
    public void iSolveTheSecurityCheckQuestion() {
        // Answer already resolved and stored in context by CommonSteps
    }

    /**
     * Fills the agent signup form with valid generated details.
     */
    @When("I fill the agent signup form with valid details")
    public void iFillTheAgentSignupFormWithValidDetails() {
        String generatedPassword = FakerHelper.generatePassword(8);
        context.getSignupPage().fillTheSignUpForm(new SignupOptions(
                FakerHelper.generateFirstName(),
                FakerHelper.generateLastName(),
                FakerHelper.generateEmail(),
                generatedPassword,
                generatedPassword,
                context.getSecurityCheckAnswer(),
                true,
                false,
                ""
        ));
    }

    /**
     * Submits the agent signup form with an empty first name to test
     * validation.
     */
    @When("I submit the agent signup form with an empty first name")
    public void iSubmitTheAgentSignupFormWithAnEmptyFirstName() {
        context.getSignupPage().fillTheSignUpForm(new SignupOptions(
                "",
                FakerHelper.generateLastName(),
                FakerHelper.generateEmail(),
                FakerHelper.generatePassword(8),
                FakerHelper.generatePassword(8),
                null,
                true,
                true,
                "first_name"
        ));
    }

    /**
     * Submits the agent signup form with an empty last name to test validation.
     */
    @When("I submit the agent signup form with an empty last name")
    public void iSubmitTheAgentSignupFormWithAnEmptyLastName() {
        context.getSignupPage().fillTheSignUpForm(new SignupOptions(
                FakerHelper.generateFirstName(),
                "",
                FakerHelper.generateEmail(),
                FakerHelper.generatePassword(8),
                FakerHelper.generatePassword(8),
                null,
                true,
                true,
                "last_name"
        ));
    }

    /**
     * Submits the agent signup form with an empty email to test validation.
     */
    @When("I submit the agent signup form with an empty email")
    public void iSubmitTheAgentSignupFormWithAnEmptyEmail() {
        context.getSignupPage().fillTheSignUpForm(new SignupOptions(
                FakerHelper.generateFirstName(),
                FakerHelper.generateLastName(),
                "",
                FakerHelper.generatePassword(8),
                FakerHelper.generatePassword(8),
                null,
                true,
                true,
                "email"
        ));
    }

    /**
     * Submits the agent signup form with an empty password to test validation.
     */
    @When("I submit the agent signup form with an empty password")
    public void iSubmitTheAgentSignupFormWithAnEmptyPassword() {
        context.getSignupPage().fillTheSignUpForm(new SignupOptions(
                FakerHelper.generateFirstName(),
                FakerHelper.generateLastName(),
                FakerHelper.generateEmail(),
                "",
                FakerHelper.generatePassword(8),
                null,
                true,
                true,
                "password"
        ));
    }

    /**
     * Submits the agent signup form with an empty confirm password to test
     * validation.
     */
    @When("I submit the agent signup form with an empty confirm password")
    public void iSubmitTheAgentSignupFormWithAnEmptyConfirmPassword() {
        context.getSignupPage().fillTheSignUpForm(new SignupOptions(
                FakerHelper.generateFirstName(),
                FakerHelper.generateLastName(),
                FakerHelper.generateEmail(),
                FakerHelper.generatePassword(8),
                "",
                null,
                true,
                true,
                "confirm_password"
        ));
    }
}
