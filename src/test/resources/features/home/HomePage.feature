@regression @guest @shard1
Feature: Home Page
    As a guest user
    I want to interact with the home page
    So that I can navigate to various sections of the platform

    Background:
        Given the home page is loaded

    @sanity
    Scenario: Home page loads successfully
        Then the home page should be fully loaded

    @sanity
    Scenario: Header navigation links are present
        Then all header navigation links should be visible

    @sanity
    Scenario: Navigate to visa booking page
        When I click on the visa booking section
        Then I should be navigated to the visa booking page

    @sanity
    Scenario: Navigate to tours booking page
        When I click on the tours booking section
        Then I should be navigated to the tours booking page

    @sanity
    Scenario: Navigate to cars booking page
        When I click on the cars booking section
        Then I should be navigated to the cars booking page

    @sanity
    Scenario: Navigate to flight booking page
        When I click on the flights booking section
        Then I should be navigated to the flight booking page

    Scenario: Navigate to stays booking page
        When I click on the stays booking section
        Then I should be navigated to the stays booking page

    Scenario: Navigate to contact us page
        When I click on the contact us link
        Then I should be navigated to the contact us page

    Scenario: Navigate to about us page
        When I click on the about us link
        Then I should be navigated to the about us page

    Scenario: Navigate to cookies policy page
        When I click on the cookies policy link
        Then I should be navigated to the cookies policy page

    Scenario: Navigate to privacy policy page
        When I click on the privacy policy link
        Then I should be navigated to the privacy policy page

    Scenario: Navigate to become a supplier page
        When I click on the become a supplier link
        Then I should be navigated to the become a supplier page

    Scenario: Navigate to terms of use page
        When I click on the terms of use link
        Then I should be navigated to the terms of use page

    Scenario: Navigate to blogs page
        When I click on the blogs link
        Then I should be navigated to the blogs page

    @sanity
    Scenario: Change language to Russian
        When I change the language to "Russian"
        Then the page language should be changed successfully

    Scenario: Change currency to NGN
        When I change the currency to "NGN"
        Then the page currency should be changed successfully

    Scenario: Navigate to login page
        When I click on the login link
        Then I should be navigated to the login page

    Scenario: Navigate to customer signup page
        When I click on the customer signup link
        Then I should be navigated to the customer signup page

    Scenario: Navigate to agent signup page
        When I click on the agent signup link
        Then I should be navigated to the agent signup page

    Scenario: All quick search tabs are visible
        Then all quick search tabs should be visible

    @sanity
    Scenario: Download app section is displayed
        Then the download app section should be visible and functional

    Scenario: Navigate to affiliate program page
        When I click on the affiliate program link
        Then I should be navigated to the affiliate program page

    Scenario: Navigate to investors page
        When I click on the investors link
        Then I should be navigated to the investors page

    Scenario: Navigate to careers page
        When I click on the careers link
        Then I should be navigated to the careers page

    Scenario: Navigate to how to book page
        When I click on the how to book link
        Then I should be navigated to the how to book page

    Scenario: Navigate to file a claim page
        When I click on the file a claim link
        Then I should be navigated to the file a claim page

    Scenario: Navigate to refund policy page
        When I click on the refund policy link
        Then I should be navigated to the refund policy page

    Scenario: Navigate to best travel deals page
        When I click on the best travel deals link
        Then I should be navigated to the best travel deals page

    Scenario: Navigate to travel documents page
        When I click on the travel documents link
        Then I should be navigated to the travel documents page

    Scenario: Navigate to travel insurance page
        When I click on the travel insurance link
        Then I should be navigated to the travel insurance page

    Scenario: Navigate to disruptions page
        When I click on the disruptions link
        Then I should be navigated to the disruptions page

    Scenario: Navigate to FAQ page
        When I click on the FAQ link
        Then I should be navigated to the FAQ page

    Scenario: Navigate to accessibility page
        When I click on the accessibility link
        Then I should be navigated to the accessibility page
