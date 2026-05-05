package steps.signup;

import context.TestContext;
import io.cucumber.java.en.When;
import pages.subPages.signup.SignupOptions;
import utils.faker.FakerHelper;

public class CustomerSignupSteps {

    private final TestContext context;

    public CustomerSignupSteps(TestContext context) {
        this.context = context;
    }

    @When("I navigate to the customer signup page")
    public void iNavigateToTheCustomerSignupPage() {
        context.getHomePage().verifyNavigationToCustomerSignupPage();
    }

    @When("I fill the customer signup form with valid details")
    public void iFillTheCustomerSignupFormWithValidDetails() {
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

    @When("I submit the customer signup form with an empty first name")
    public void iSubmitTheCustomerSignupFormWithAnEmptyFirstName() {
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

    @When("I submit the customer signup form with an empty last name")
    public void iSubmitTheCustomerSignupFormWithAnEmptyLastName() {
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

    @When("I submit the customer signup form with an empty email")
    public void iSubmitTheCustomerSignupFormWithAnEmptyEmail() {
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

    @When("I submit the customer signup form with an empty password")
    public void iSubmitTheCustomerSignupFormWithAnEmptyPassword() {
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

    @When("I submit the customer signup form with an empty confirm password")
    public void iSubmitTheCustomerSignupFormWithAnEmptyConfirmPassword() {
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
