@regression @guest @shard3
Feature: Customer Signup
    As a guest user
    I want to sign up as a customer
    So that I can access customer features on the platform

    Background:
        Given the home page is loaded
        And I navigate to the customer signup page

    @sanity
    Scenario: Successful customer signup
        Given a security check question is displayed
        When I solve the security check question
        And I fill the customer signup form with valid details
        Then the signup should be successful with no backend validation errors

    Scenario: First name field validation on customer signup
        When I submit the customer signup form with an empty first name
        Then a validation error should be shown for the first name field

    Scenario: Last name field validation on customer signup
        When I submit the customer signup form with an empty last name
        Then a validation error should be shown for the last name field

    @sanity
    Scenario: Email field validation on customer signup
        When I submit the customer signup form with an empty email
        Then a validation error should be shown for the email field

    Scenario: Password field validation on customer signup
        When I submit the customer signup form with an empty password
        Then a validation error should be shown for the password field

    Scenario: Confirm password field validation on customer signup
        When I submit the customer signup form with an empty confirm password
        Then a validation error should be shown for the confirm password field
