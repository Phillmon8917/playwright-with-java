# Playwright Automation Framework - Java

<!-- cspell:ignore Dskip Dexec Dtest Dcucumber -->

End-to-end test automation framework built with Playwright for Java, Cucumber, JUnit Platform, Maven, and Allure.

The framework targets browser-based regression, sanity, authentication, home page, and signup flows. It uses a layered structure with page objects, reusable interaction modules, Cucumber step definitions, environment-driven configuration, generated test data, screenshots, videos, logs, and report artifacts.

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Tags](#tags)
- [Test Data](#test-data)
- [Reporting](#reporting)
- [CI/CD](#cicd)

## Overview

This project is a modular Playwright test framework for PHPTravels-style web flows. Tests are written as Cucumber feature files under `src/test/resources/features`, with Java step definitions under `src/test/java/steps`.

The framework starts Playwright from Cucumber hooks, creates a fresh browser context for each scenario, attaches screenshots to Allure, records videos for failed scenarios, and supports parallel scenario execution.

## Key Features

- Playwright for Java browser automation
- Cucumber feature files and step definitions
- JUnit Platform suite runners
- Page Object Model with reusable page modules
- Environment configuration through `.env` and system environment variables
- Role-based authentication scenarios for admin, agent, and customer users
- Synthetic signup data through JavaFaker
- Allure reporting with screenshots, logs, and failure details
- Cucumber HTML and JSON reports
- Parallel execution through JUnit and Cucumber configuration
- GitHub Actions workflows for sanity, regression, and branch rules

## Project Structure

```text
src/
+-- main/java/
|   +-- annotations/            # Shared test tag constants
|   +-- config/                 # Playwright browser, context, and timeout setup
|   +-- extensions/             # JUnit extensions and failure/reporting helpers
|   +-- lookups/                # Lookup data helpers
|   +-- modulars/               # Reusable browser, element, assertion, network, and UI actions
|   +-- pages/                  # Page Object Model classes
|   +-- report/                 # Monthly and GitHub report generation
|   +-- utils/                  # Env, faker, credentials, logging, storage, timeout, and validation helpers
|
+-- test/java/
|   +-- context/                # Shared Cucumber scenario context
|   +-- hooks/                  # Cucumber setup, teardown, and error capture
|   +-- runner/                 # JUnit Platform Cucumber runners
|   +-- steps/                  # Cucumber step definitions
|
+-- test/resources/
    +-- features/               # Cucumber feature files
    +-- cucumber.properties
    +-- junit-platform.properties

data/                           # JSON data files
storage/                        # Local auth storage state files
target/                         # Test output, reports, and Allure results
```

## Technology Stack

| Component | Version |
| --- | --- |
| Java | 21 |
| Maven | 3.6+ |
| Playwright | 1.59.0 |
| JUnit Jupiter | 5.10.2 |
| JUnit Platform Suite | 1.10.2 |
| Cucumber | 7.14.0 |
| Allure JUnit 5 | 2.27.0 |
| Allure Maven | 2.12.0 |
| Log4j2 | 2.23.1 |
| JavaFaker | 1.0.2 |
| Jackson Databind | 2.19.2 |
| RestAssured | 5.4.0 |
| dotenv-java | 3.0.0 |
| AspectJ Weaver | 1.9.22 |

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Git
- Allure CLI, if you want to serve reports locally
- Node.js, only if you use the local Husky/cspell tooling

## Setup

Clone the repository:

```bash
git clone https://github.com/Phillmon8917/playwright-with-java.git
cd playwright-with-java
```

Install Maven dependencies:

```bash
mvn clean install -DskipTests
```

Install Playwright browsers:

```bash
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"
```

Create a local `.env` file from `.env.example` and fill in the required values:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

## Configuration

The framework reads configuration from `.env` first, then from system environment variables.

Common values:

```env
BASE_URL=https://phptravels.net/
ADMIN_USERNAME=your admin username
ADMIN_PASSWORD=your admin password
CUSTOMER_USERNAME=your customer username
CUSTOMER_PASSWORD=your customer password
AGENT_USERNAME=your agent username
AGENT_PASSWORD=your agent password
ACTION_TIMEOUT=80000
NAVIGATION_TIMEOUT=120000
TEST_TIMEOUT=80000
CI=false
DATA_REPO_TOKEN=your report storage token
```

Browser behavior:

- The framework currently launches Chromium.
- Headless mode is enabled when `CI` is present in the environment.
- Browser contexts default to `BASE_URL`, or `https://phptravels.net/` when `BASE_URL` is not set.
- Each scenario gets a fresh context and a `1920x1080` viewport.

## Running Tests

Run the full configured suite:

```bash
mvn test
```

Run only authentication scenarios:

```bash
mvn test -Dtest=AuthRunner
```

Run the main non-authentication scenarios:

```bash
mvn test -Dtest=CucumberRunner
```

Run scenarios by Cucumber tag:

```bash
mvn test -Dcucumber.filter.tags="@sanity"
mvn test -Dcucumber.filter.tags="@regression and not @authentication"
mvn test -Dcucumber.filter.tags="@guest and @shard1"
```

Skip automatic Allure serving for local runs:

```bash
mvn test -DskipAllureOpen=true
```

Useful output locations:

```text
target/allure-results/
target/cucumber-reports/report.html
target/cucumber-reports/report.json
target/cucumber-reports/auth-report.html
target/cucumber-reports/auth-report.json
target/surefire-reports/
```

## Tags

Shared tag constants are defined in `src/main/java/annotations/TestTags.java`.

Current tag groups:

- `@authentication`
- `@sanity`
- `@regression`
- `@guest`
- `@admin`
- `@agent`
- `@customer`
- `@shard1`
- `@shard2`
- `@shard3`
- `@wip`

Runner defaults:

- `AuthRunner` selects `features/authentication` and filters `@authentication`.
- `CucumberRunner` selects all features and filters `not @wip and not @authentication`.

## Test Data

Dynamic signup data is generated by `src/main/java/utils/faker/FakerHelper.java`.

Available helpers:

```java
FakerHelper.generateFirstName();
FakerHelper.generateLastName();
FakerHelper.generateEmail();
FakerHelper.generatePassword();
FakerHelper.generatePassword(8);
FakerHelper.generateCustomerSignupData();
```

`generateEmail()` appends a random number to the local part of the generated email address, which helps reduce collisions during signup tests.

`generateCustomerSignupData()` returns a map with:

```text
firstName
lastName
email
password
```

Static JSON data lives under `data/`.

## Reporting

The framework produces both Cucumber and Allure artifacts.

Allure attachments include:

- Full-page screenshots
- Console logs collected per scenario thread
- Video attachments for failed scenarios
- Failure details captured from Cucumber step errors

Serve the Allure report:

```bash
allure serve target/allure-results
```

Generate a report without opening a server:

```bash
mvn allure:report
```

## CI/CD

GitHub Actions workflows live in `.github/workflows`.

CI guidance:

- Set `CI=true` so the framework uses CI-oriented behavior.
- Provide required credentials as environment secrets.
- Publish `target/allure-results`, `target/cucumber-reports`, and `target/surefire-reports` as artifacts.
- Use tag filters such as `@sanity`, `@regression`, and shard tags for focused runs.

## Contributing

When adding or changing tests:

1. Add or update a feature file under `src/test/resources/features`.
2. Put Java step definitions under `src/test/java/steps`.
3. Reuse existing page objects and modular actions before adding new helpers.
4. Tag scenarios with the relevant role, suite, and shard tags.
5. Use `FakerHelper` for signup data instead of hardcoded user details.
6. Keep secrets in `.env` or CI secrets, never in committed test code.

## License

ISC

## Support

Repository: <https://github.com/Phillmon8917/playwright-with-java>

Last updated: May 2026
