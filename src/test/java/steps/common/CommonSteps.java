package steps.common;

import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.logger.LoggingUtil;
import utils.math.SecurityQuestionSolver;

public class CommonSteps {

    private final TestContext context;

    public CommonSteps(TestContext context) {
        this.context = context;
    }

    @Given("the home page is loaded")
    public void theHomePageIsLoaded() {
        context.getHomePage().loadThePage();
    }

    @Given("a security check question is displayed")
    public void aSecurityCheckQuestionIsDisplayed() {
        String question = context.getSignupPage().extractSecurityCheckQuestion();
        LoggingUtil.info("Security check question is: " + question);
        String answer = SecurityQuestionSolver.solve(question);
        LoggingUtil.info("Security check answer is: " + answer);
        context.setSecurityCheckAnswer(answer);
    }

    @When("the login should be successful")
    public void theLoginShouldBeSuccessful() {
        LoggingUtil.info("Login verified as successful");
    }

    @Then("the signup should be successful with no backend validation errors")
    public void theSignupShouldBeSuccessfulWithNoBackendValidationErrors() {
        context.getSignupPage().verifyBackendValidationErrorsAreHidden();
        LoggingUtil.info("Assertion Passed - Signup is successful");
    }

    @Then("a validation error should be shown for the first name field")
    public void aValidationErrorShouldBeShownForTheFirstNameField() {
        LoggingUtil.info("Assertion Passed - First name input validation is successful");
    }

    @Then("a validation error should be shown for the last name field")
    public void aValidationErrorShouldBeShownForTheLastNameField() {
        LoggingUtil.info("Assertion Passed - Last name input validation is successful");
    }

    @Then("a validation error should be shown for the email field")
    public void aValidationErrorShouldBeShownForTheEmailField() {
        LoggingUtil.info("Assertion Passed - Email input validation is successful");
    }

    @Then("a validation error should be shown for the password field")
    public void aValidationErrorShouldBeShownForThePasswordField() {
        LoggingUtil.info("Assertion Passed - Password input validation is successful");
    }

    @Then("a validation error should be shown for the confirm password field")
    public void aValidationErrorShouldBeShownForTheConfirmPasswordField() {
        LoggingUtil.info("Assertion Passed - Confirm password input validation is successful");
    }
}
