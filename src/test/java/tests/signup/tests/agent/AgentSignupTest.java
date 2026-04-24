package tests.signup.tests.agent;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.subPages.signup.SignupOptions;
import tests.signup.base.SignupBaseTest;
import utils.faker.FakerHelper;
import utils.logger.LoggingUtil;
import utils.math.SecurityQuestionSolver;

@Tag("regression")
@Tag("guest")
@Tag("shard2")
public class AgentSignupTest extends SignupBaseTest
{

    @Test
    @Tag("sanity")
    void verifySuccessfulAgentSignup() throws Exception {
        homePage.loadThePage();
        homePage.verifyNavigationToAgentSignupPage();

        String securityCheckQuestion = signupPage.extractSecurityCheckQuestion();
        LoggingUtil.info("Security check question is: " + securityCheckQuestion);

        String securityCheckAnswer = SecurityQuestionSolver.solve(securityCheckQuestion);
        LoggingUtil.info("Security check answer is: " + securityCheckAnswer);

        String generatedPassword = FakerHelper.generatePassword(8);
        signupPage.fillTheSignUpForm(new SignupOptions(
                FakerHelper.generateFirstName(),
                FakerHelper.generateLastName(),
                FakerHelper.generateEmail(),
                generatedPassword,
                generatedPassword,
                securityCheckAnswer,
                true,
                false,
                ""
        ));

        signupPage.verifyBackendValidationErrorsAreHidden();
        LoggingUtil.info("Assertion Passed - Agent signup is successful");
    }

    @Test
    @Tag("sanity")
    void verifyFirstNameInputValidation() {
        homePage.loadThePage();
        homePage.verifyNavigationToAgentSignupPage();

        signupPage.fillTheSignUpForm(new SignupOptions(
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
        LoggingUtil.info("Assertion Passed - First name input validation is successful");
    }

    @Test
    void verifyLastNameInputValidation() {
        homePage.loadThePage();
        homePage.verifyNavigationToAgentSignupPage();

        signupPage.fillTheSignUpForm(new SignupOptions(
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
        LoggingUtil.info("Assertion Passed - Last name input validation is successful");
    }

    @Test
    void verifyEmailInputValidation() {
        homePage.loadThePage();
        homePage.verifyNavigationToAgentSignupPage();

        signupPage.fillTheSignUpForm(new SignupOptions(
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
        LoggingUtil.info("Assertion Passed - Email input validation is successful");
    }

    @Test
    @Tag("sanity")
    void verifyPasswordInputValidation() {
        homePage.loadThePage();
        homePage.verifyNavigationToAgentSignupPage();

        signupPage.fillTheSignUpForm(new SignupOptions(
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
        LoggingUtil.info("Assertion Passed - Password input validation is successful");
    }

    @Test
    @Tag("sanity")
    void verifyConfirmPasswordInputValidation() {
        homePage.loadThePage();
        homePage.verifyNavigationToAgentSignupPage();

        signupPage.fillTheSignUpForm(new SignupOptions(
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
        LoggingUtil.info("Assertion Passed - Confirm password input validation is successful");
    }
}