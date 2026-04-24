package modulars.assertions;

import com.microsoft.playwright.Locator;
import utils.logger.LoggingUtil;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ElementAssertions {

    /**
     * Asserts that the provided element matches the expected state.
     *
     * @param locator the Playwright locator for the target element
     * @param state the expected state to verify
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void assertElementState(Locator locator, String state,
                                   String locatorName, String methodName) {
        try {
            switch (state) {
                case "visible"  -> assertThat(locator).isVisible();
                case "hidden"   -> assertThat(locator).isHidden();
                case "enabled"  -> assertThat(locator).isEnabled();
                case "disabled" -> assertThat(locator).isDisabled();
                default -> throw new IllegalArgumentException("Unknown state: " + state);
            }
            LoggingUtil.info(methodName + " - " + locatorName + " is " + state);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName + " is not " + state + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Asserts that the provided element property matches the expected value.
     *
     * @param locator the Playwright locator for the target element
     * @param type the property type to assert
     * @param expected the expected value of the property
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @param attrName the attribute name when performing an attribute assertion
     */
    public void assertElementProperty(Locator locator, String type, Object expected,
                                      String locatorName, String methodName, String attrName) {
        try {
            switch (type) {
                case "text"  -> assertThat(locator).hasText(expected.toString());
                case "value" -> assertThat(locator).hasValue(expected.toString());
                case "attribute" -> {
                    if (attrName == null)
                        throw new IllegalArgumentException("attrName must be provided for attribute assertion");
                    assertThat(locator).hasAttribute(attrName, expected.toString());
                }
                case "class" -> assertThat(locator).hasClass(Pattern.compile(expected.toString()));
                case "count" -> assertThat(locator).hasCount((int) expected);
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            }
            String attrSuffix = attrName != null ? " for attribute \"" + attrName + "\"" : "";
            LoggingUtil.info(methodName + " - " + locatorName + " " + type
                    + " matches expected: " + expected + attrSuffix);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName + " " + type
                    + " did not match expected: " + expected + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Asserts that the provided element property matches the expected value.
     *
     * @param locator the Playwright locator for the target element
     * @param type the property type to assert
     * @param expected the expected value of the property
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void assertElementProperty(Locator locator, String type, Object expected,
                                      String locatorName, String methodName) {
        assertElementProperty(locator, type, expected, locatorName, methodName, null);
    }

    /**
     * Asserts that the element text matches or contains the expected value.
     *
     * @param locator the Playwright locator for the target element
     * @param expected the expected text value
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @param type the text assertion mode, such as exact or partial
     */
    public void assertElementText(Locator locator, String expected, String locatorName,
                                  String methodName, String type) {
        try {
            if ("exact".equals(type)) {
                assertThat(locator).hasText(expected);
            } else {
                assertThat(locator).containsText(expected);
            }
            LoggingUtil.info(methodName + " - " + locatorName + " text assertion passed: " + expected);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName + " text assertion failed: "
                    + expected + " - " + err.getMessage());
            throw err;
        }
    }

    /**
     * Asserts that the element text exactly matches the expected value.
     *
     * @param locator the Playwright locator for the target element
     * @param expected the expected text value
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void assertElementText(Locator locator, String expected,
                                  String locatorName, String methodName) {
        assertElementText(locator, expected, locatorName, methodName, "exact");
    }


    /**
     * Asserts that an integer value is greater than the provided threshold.
     *
     * @param actual the actual numeric value
     * @param threshold the minimum threshold the value must exceed
     * @param valueName the friendly name of the value for logging
     * @param methodName the calling method name for logging
     */
    public void assertGreaterThan(int actual, int threshold, String valueName, String methodName) {
        try {
            org.junit.jupiter.api.Assertions.assertTrue(actual > threshold,
                    valueName + " (" + actual + ") is not greater than " + threshold);
            LoggingUtil.info(methodName + " - " + valueName + " (" + actual + ") is greater than " + threshold);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + valueName + " (" + actual
                    + ") is not greater than " + threshold + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Polls the element text until its numeric value becomes greater than the threshold.
     *
     * @param locator the Playwright locator for the target element
     * @param threshold the threshold the parsed numeric value must exceed
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @param timeoutMs the maximum polling time in milliseconds
     * @param intervalMs the delay between polling attempts in milliseconds
     * @throws InterruptedException if the polling sleep is interrupted
     */
    public void pollUntilGreaterThan(Locator locator, int threshold, String locatorName,
                                     String methodName, int timeoutMs, int intervalMs)
            throws InterruptedException {
        long start = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - start < timeoutMs) {
                String text = locator.textContent();
                try {
                    int value = Integer.parseInt(text == null ? "" : text.trim());
                    if (value > threshold) {
                        LoggingUtil.info(methodName + " - " + locatorName
                                + " value (" + value + ") exceeded " + threshold);
                        return;
                    }
                } catch (NumberFormatException ignored) {}
                Thread.sleep(intervalMs);
            }
            throw new RuntimeException(locatorName + " did not exceed " + threshold
                    + " within " + timeoutMs + "ms");
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName
                    + " failed to exceed " + threshold + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Polls the element text until its numeric value becomes greater than the threshold.
     *
     * @param locator the Playwright locator for the target element
     * @param threshold the threshold the parsed numeric value must exceed
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @throws InterruptedException if the polling sleep is interrupted
     */
    public void pollUntilGreaterThan(Locator locator, int threshold,
                                     String locatorName, String methodName) throws InterruptedException {
        pollUntilGreaterThan(locator, threshold, locatorName, methodName, 30_000, 250);
    }


    /**
     * Asserts that the provided string value is not null or empty.
     *
     * @param value the string value to validate
     * @param valueName the friendly name of the value for logging
     * @param methodName the calling method name for logging
     */
    public void assertNotNullOrEmptyVar(String value, String valueName, String methodName) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                LoggingUtil.info(methodName + " - " + valueName + " is not null or empty: \"" + value + "\"");
            } else {
                throw new RuntimeException(valueName + " is null or empty");
            }
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + valueName + " assertion failed: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Polls the element until its text or value content becomes non-empty.
     *
     * @param locator the Playwright locator for the target element
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @param type the content type to poll, such as text or value
     * @param timeoutMs the maximum polling time in milliseconds
     * @param intervalMs the delay between polling attempts in milliseconds
     * @throws InterruptedException if the polling sleep is interrupted
     */
    public void pollUntilNotEmpty(Locator locator, String locatorName, String methodName,
                                  String type, int timeoutMs, int intervalMs)
            throws InterruptedException {
        long start = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - start < timeoutMs) {
                String content = "value".equals(type)
                        ? locator.inputValue()
                        : locator.textContent();
                if (content != null && !content.trim().isEmpty()) {
                    LoggingUtil.info(methodName + " - " + locatorName
                            + " " + type + " became non-empty: \"" + content + "\"");
                    return;
                }
                Thread.sleep(intervalMs);
            }
            throw new RuntimeException(locatorName + " " + type
                    + " did not become non-empty within " + timeoutMs + "ms");
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName
                    + " " + type + " polling failed: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Polls the element until its text content becomes non-empty.
     *
     * @param locator the Playwright locator for the target element
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @throws InterruptedException if the polling sleep is interrupted
     */
    public void pollUntilNotEmpty(Locator locator, String locatorName,
                                  String methodName) throws InterruptedException {
        pollUntilNotEmpty(locator, locatorName, methodName, "text", 30_000, 250);
    }

    /**
     * Asserts that two values are equal.
     *
     * @param actual the actual value
     * @param expected the expected value
     * @param valueName the friendly name of the value for logging
     * @param methodName the calling method name for logging
     * @param <T> the value type being compared
     */
    public <T> void assertValuesEqual(T actual, T expected, String valueName, String methodName) {
        try {
            if (java.util.Objects.equals(actual, expected)) {
                LoggingUtil.info(methodName + " - " + valueName
                        + " values match: actual=\"" + actual + "\", expected=\"" + expected + "\"");
            } else {
                throw new RuntimeException(valueName + " values do not match: actual=\""
                        + actual + "\", expected=\"" + expected + "\"");
            }
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + valueName + " comparison failed: " + err.getMessage());
            throw err;
        }
    }
}
