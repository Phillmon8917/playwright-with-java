@admin @regression
Feature: Admin Authentication
  As an admin user
  I want to be able to log in to the system
  So that I can access admin functionality and save my session

  Background:
    Given the storage directory exists
    And the admin storage state is cleared

  @authentication
  Scenario: Successful admin login and session storage
    Given the home page is loaded
    When I navigate to the login page
    And I log in with admin credentials
    Then the admin session should be saved to storage
    And the login should be successful
