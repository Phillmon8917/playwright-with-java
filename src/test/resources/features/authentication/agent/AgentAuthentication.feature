@authentication @agent @regression
Feature: Agent Authentication
    As an agent user
    I want to be able to log in to the system
    So that I can access agent functionality and save my session

    Background:
        Given the storage directory exists
        And the agent storage state is cleared

    @sanity
    Scenario: Successful agent login and session storage
        Given the home page is loaded
        When I navigate to the login page
        And I log in with agent credentials
        Then the agent session should be saved to storage
        And the login should be successful
