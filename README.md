# Playwright Automation Framework - Java

A comprehensive end-to-end automation testing framework built with **Playwright for Java**, **JUnit 5**, and **Maven**. This framework provides a scalable, maintainable architecture for testing web applications with advanced features like role-based authentication, dynamic timeouts, detailed reporting, and multi-threaded test execution.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Setup & Installation](#setup--installation)
- [Running Tests](#running-tests)
- [Test Tags & Categorization](#test-tags--categorization)
- [Project Modules](#project-modules)
- [Reporting](#reporting)

---

## 🎯 Overview

This project is a **modular, enterprise-grade test automation framework** designed for testing complex web applications. It leverages Playwright's cross-browser capabilities with Java for type-safe, maintainable test code. The framework supports multiple user roles (admin, agent, customer, guest), provides persistent authentication state management, and includes comprehensive reporting and logging.

---

## ✨ Key Features

- **Cross-Browser Testing**: Chrome, Firefox, and WebKit support via Playwright
- **Role-Based Test Execution**: Built-in support for multiple user roles with persistent session storage
- **Page Object Model**: Well-organized page objects with inheritance and modular components
- **Dynamic Timeout Management**: Configurable timeouts with CI environment adaptability
- **Comprehensive Logging**: Log4j2 integration with Allure reporting support
- **Video Recording**: Automatic video capture for test runs
- **Screenshot & Artifacts**: Automatic capture on test failures
- **Parallel Test Execution**: Thread-safe design supporting multiple concurrent test runs
- **Custom Test Tags**: Annotation-driven test categorization (sanity, regression, role-based, shard-based)
- **Failure Tracking**: Extension-based failure detection and reporting
- **Environment Configuration**: .env file support for environment-specific settings
- **REST API Testing**: Integration with RestAssured for API test support
- **Data Generation**: JavaFaker integration for synthetic test data

---

## 🏗️ Architecture

The framework follows a **layered architecture** with clear separation of concerns:

```
┌──────────────────────────────────────┐
│        Test Classes                   │
│    (authentication, home, signup)     │
└──────────────────────────────────────┘
           ↓
┌──────────────────────────────────────┐
│      Page Objects & Modulars          │
│    (UI Components, Reusable Logic)    │
└──────────────────────────────────────┘
           ↓
┌──────────────────────────────────────┐
│    Extensions & Configuration         │
│  (Setup/Teardown, Reporting, Auth)   │
└──────────────────────────────────────┘
           ↓
┌──────────────────────────────────────┐
│    Utilities & Helpers                │
│  (Logging, Storage, Validation, etc)  │
└──────────────────────────────────────┘
           ↓
┌──────────────────────────────────────┐
│      Playwright Core                  │
│   (Browser, Page, Context)            │
└──────────────────────────────────────┘
```

---

## 📁 Project Structure

```
src/
├── main/java/
│   ├── annotations/
│   │   └── TestTags.java           # Test categorization constants
│   ├── config/
│   │   └── PlaywrightConfig.java   # Browser, context, page configuration
│   ├── extensions/
│   │   ├── AuthExtension.java      # Role-based auth verification
│   │   ├── DynamicTimeoutExtension.java  # Timeout management
│   │   ├── FailureTracker.java     # Test failure detection
│   │   └── ReportingExtension.java # Allure reporting integration
│   ├── lookups/
│   │   └── numbers/                # Test data lookups
│   ├── modulars/                   # Reusable UI component modules
│   │   ├── assertions/             # Custom assertion helpers
│   │   ├── browser/                # Browser interaction utilities
│   │   ├── calendar/               # Calendar component interactions
│   │   ├── dropdowns/              # Dropdown handling
│   │   ├── elements/               # Element interaction utilities
│   │   ├── iframe/                 # IFrame handling
│   │   ├── keyboard/               # Keyboard input utilities
│   │   ├── network/                # Network interaction utilities
│   │   ├── toggle/                 # Toggle/switch components
│   ├── pages/
│   │   ├── basePage/               # Base page object with common methods
│   │   └── subPages/               # Specific page objects
│   ├── report/                     # Reporting utilities
│   └── utils/
│       ├── credentials/            # Credential management
│       ├── date/                   # Date/time utilities
│       ├── env/                    # Environment configuration
│       ├── faker/                  # Test data generation
│       ├── logger/                 # Logging utilities
│       ├── math/                   # Mathematical utilities
│       ├── storage/                # Session storage management
│       ├── timeout/                # Timeout handling
│       └── validation/             # Input validation helpers
│
├── test/java/
│   ├── base/
│   │   └── BaseTest.java           # Abstract base test class
│   └── tests/
│       ├── authentication/         # Auth-related tests
│       ├── home/                   # Home page tests
│       └── signup/                 # Sign-up flow tests
│
└── resources/
    └── junit-platform.properties   # JUnit 5 configuration

data/                              # Test data files (JSON)
storage/                           # Persisted authentication state
target/
├── allure-results/                # Allure report artifacts
└── surefire-reports/              # JUnit test reports
```

---

## 🛠️ Technology Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Playwright | 1.59.0 |
| JUnit 5 | 5.10.2 |
| Maven | 3.x |
| Log4j2 | 2.23.1 |
| Allure | 2.27.0 |
| Jackson | 2.19.2 |
| RestAssured | 5.4.0 |
| JavaFaker | 1.0.2 |
| dotenv-java | 3.0.0 |
| AspectJ | 1.9.22 |

---

## 📦 Setup & Installation

### Prerequisites
- **Java 21** or higher
- **Maven 3.6+**
- **Playwright browsers** (auto-installed on first run)
- **Git** (for version control)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/Phillmon8917/playwright-with-java.git
   cd playwright-with-java
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure environment variables**
   Create a `.env` file in the project root:
   ```env
   # Timeouts (in milliseconds)
   ACTION_TIMEOUT=80000
   NAVIGATION_TIMEOUT=120000
   TEST_TIMEOUT=80000

   # Browser configuration
   BROWSER=chromium
   HEADLESS=true

   # CI environment flag (set by CI/CD pipelines)
   CI=false
   ```

4. **Install Playwright browsers**
   ```bash
   mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"
   ```

---

## 🧪 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=AdminAuthTest
```

### Run Tests by Tag
```bash
# Run sanity tests
mvn clean test -Dgroups=sanity

# Run regression tests
mvn clean test -Dgroups=regression

# Run customer role tests
mvn clean test -Dgroups=customer
```

### Run Tests in Headless/Headed Mode
```bash
# Headed mode (browser visible)
mvn clean test -DHEADLESS=false

# Headless mode (default)
mvn clean test -DHEADLESS=true
```

### Parallel Test Execution
```bash
mvn clean test -DthreadCount=4
```

---

## 🏷️ Test Tags & Categorization

Test tags are defined in `TestTags.java` and provide classification for test organization and filtering:

### Role-Based Tags
- `@Tag(TestTags.GUEST)` - Guest/unauthenticated user tests
- `@Tag(TestTags.CUSTOMER)` - Customer role tests
- `@Tag(TestTags.AGENT)` - Agent role tests
- `@Tag(TestTags.ADMIN)` - Admin role tests

### Test Type Tags
- `@Tag(TestTags.SANITY)` - Sanity/smoke tests
- `@Tag(TestTags.REGRESSION)` - Comprehensive regression tests
- `@Tag(TestTags.AUTHENTICATION)` - Authentication flow tests

### Execution Sharding Tags
- `@Tag(TestTags.SHARD1)` - First test shard (for parallel CI execution)
- `@Tag(TestTags.SHARD2)` - Second test shard
- `@Tag(TestTags.SHARD3)` - Third test shard

### Example Test Class
```java
@ExtendWith({AuthExtension.class})
@Tag(TestTags.ADMIN)
@Tag(TestTags.AUTHENTICATION)
class AdminAuthTest extends BaseTest {
    
    @Override
    protected String storageRole() {
        return "admin";
    }
    
    @Test
    @Tag(TestTags.SANITY)
    void shouldLoginAsAdmin() {
        // Test implementation
    }
}
```

---

## 📦 Project Modules

### **Config Module** (`config/`)
Manages browser configuration, context creation, and timeout settings.
- **PlaywrightConfig**: Centralized configuration for browser launch, context creation, and page setup
- Timeout management with CI environment adaptation

### **Extensions Module** (`extensions/`)
JUnit 5 extensions for cross-cutting concerns:
- **AuthExtension**: Validates role-based test prerequisites and storage state
- **DynamicTimeoutExtension**: Manages and adjusts timeouts dynamically
- **FailureTracker**: Detects and tracks test failures
- **ReportingExtension**: Integrates with Allure for test artifacts and reporting

### **Modular Components** (`modulars/`)
Reusable UI interaction components:
- **Assertions**: Custom assertion helpers for common patterns
- **Browser**: Browser-level interactions (navigate, wait, etc.)
- **Calendar**: Date picker and calendar interactions
- **Dropdowns**: Select/dropdown element handling
- **Elements**: Generic element utilities
- **IFrame**: IFrame navigation and interaction
- **Keyboard**: Keyboard input and shortcuts
- **Network**: Network request/response handling
- **Toggle**: Toggle and switch components

### **Pages Module** (`pages/`)
Page Object Model implementation:
- **BasePage**: Abstract base with common methods for all pages
- **SubPages**: Concrete page objects for specific application pages

### **Utilities Module** (`utils/`)
Helper utilities for common tasks:
- **Credentials**: Credential storage and retrieval
- **Date**: Date/time operations and formatting
- **Env**: Environment variable loading
- **Faker**: Synthetic test data generation
- **Logger**: Structured logging with Allure integration
- **Math**: Mathematical operations
- **Storage**: Session storage state management
- **Timeout**: Timeout configuration and helpers
- **Validation**: Input validation and verification helpers

---

## 📊 Reporting

### Allure Integration

Tests automatically generate Allure reports with:
- **Screenshots** on test failures
- **Video recordings** of test execution
- **Structured logs** with detailed execution flow
- **Test metadata** (duration, status, tags, parameters)
- **Failure analysis** with stack traces and artifacts

### Generate Allure Report
```bash
# Run tests
mvn clean test

# Generate and serve Allure report (auto-launched)
allure serve target/allure-results
```

### JUnit Test Reports
XML-formatted test reports are generated in `target/surefire-reports/`

---

## 🔐 Security & Best Practices

- **Credentials**: Stored in `storage/` directory (excluded from git)
- **Environment Secrets**: Use `.env` file for sensitive configuration
- **No Hardcoded Data**: Leverage JavaFaker for dynamic test data
- **Logs**: Sensitive information should be masked in reports
- **Storage State**: Role-based authentication state is cached and reused

---

## 🚀 CI/CD Integration

The framework is designed for CI/CD pipelines:
- Set `CI=true` environment variable for CI execution
- Timeouts automatically double in CI environments for reliability
- Parallel test execution supported via sharding tags
- Allure reports generated as artifacts
- Video recordings available for failed tests

---

## 📝 Contributing

When adding new tests:
1. Extend `BaseTest` class
2. Use appropriate test tags for classification
3. Leverage existing page objects and modulars
4. Add proper logging via `LoggingUtil`
5. Follow Page Object Model patterns
6. Document authentication requirements via `storageRole()`

---

## 📄 License

ISC

---

## 🤝 Support

For issues, questions, or contributions, please refer to the GitHub repository:
https://github.com/Phillmon8917/playwright-with-java

---

**Last Updated**: April 2026
