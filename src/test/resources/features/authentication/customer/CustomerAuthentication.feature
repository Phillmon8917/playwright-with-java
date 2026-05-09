@customer @regression
Feature: Customer Authentication
  As a customer user
  I want to be able to log in to the system
  So that I can access customer functionality and save my session

  Background:
    Given the storage directory exists
    And the customer storage state is cleared

  @authentication
  Scenario: Successful customer login and session storage
    Given the home page is loaded
    When I navigate to the login page
    And I log in with customer credentials
    Then the customer session should be saved to storage
    And the login should be successful
