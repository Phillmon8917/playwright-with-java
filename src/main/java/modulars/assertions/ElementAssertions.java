package modulars.assertions;

import java.util.regex.Pattern;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import utils.env.EnvUtil;
import utils.logger.LoggingUtil;

public class ElementAssertions {

    static final double ACTION_TIMEOUT = Double.parseDouble(
            EnvUtil.get("ACTION_TIMEOUT").isEmpty() ? "30000" : EnvUtil.get("ACTION_TIMEOUT"));

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
                case "visible" ->
                    assertThat(locator).isVisible();
                case "hidden" ->
                    assertThat(locator).isHidden();
                case "enabled" ->
                    assertThat(locator).isEnabled();
                case "disabled" ->
                    assertThat(locator).isDisabled();
                default ->
                    throw new IllegalArgumentException("Unknown state: " + state);
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
                case "text" ->
                    assertThat(locator).hasText(expected.toString());
                case "value" ->
                    assertThat(locator).hasValue(expected.toString());
                case "attribute" -> {
                    if (attrName == null) {
                        throw new IllegalArgumentException("attrName must be provided for attribute assertion");
                    }
                    assertThat(locator).hasAttribute(attrName, expected.toString());
                }
                case "class" ->
                    assertThat(locator).hasClass(Pattern.compile(expected.toString()));
                case "count" ->
                    assertThat(locator).hasCount((int) expected);
                default ->
                    throw new IllegalArgumentException("Unknown type: " + type);
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
     * Polls using Playwright's retry engine until the element's text content
     * parses as an integer greater than the threshold, or
     * {@code ACTION_TIMEOUT} expires.
     *
     * @param locator the Playwright locator for the target element
     * @param threshold the threshold the parsed numeric value must exceed
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void pollUntilGreaterThan(Locator locator, int threshold,
            String locatorName, String methodName) {
        try {
            assertThat(locator).hasText(
                    Pattern.compile("\\d+"),
                    new LocatorAssertions.HasTextOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));

            String text = locator.textContent();
            int value = Integer.parseInt(text == null ? "" : text.trim());

            if (value <= threshold) {
                throw new RuntimeException(locatorName + " value (" + value
                        + ") is not greater than " + threshold);
            }

            LoggingUtil.info(methodName + " - " + locatorName
                    + " value (" + value + ") exceeded " + threshold);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName
                    + " failed to exceed " + threshold + ": " + err.getMessage());
            throw err;
        }
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
     * Polls using Playwright's retry engine until the element's text or input
     * value becomes non-empty, or {@code ACTION_TIMEOUT} expires.
     *
     * @param locator the Playwright locator for the target element
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @param type the content type to poll; {@code "value"} for input value,
     * any other string for text content
     */
    public void pollUntilNotEmpty(Locator locator, String locatorName,
            String methodName, String type) {
        try {
            Pattern nonEmpty = Pattern.compile(".+", Pattern.DOTALL);
            if ("value".equals(type)) {
                assertThat(locator).hasValue(
                        nonEmpty,
                        new LocatorAssertions.HasValueOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
            } else {
                assertThat(locator).hasText(
                        nonEmpty,
                        new LocatorAssertions.HasTextOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
            }
            LoggingUtil.info(methodName + " - " + locatorName + " " + type + " became non-empty");
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName
                    + " " + type + " polling failed: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Polls using Playwright's retry engine until the element's text content
     * becomes non-empty, or {@code ACTION_TIMEOUT} expires.
     *
     * @param locator the Playwright locator for the target element
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void pollUntilNotEmpty(Locator locator, String locatorName, String methodName) {
        pollUntilNotEmpty(locator, locatorName, methodName, "text");
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

    /**
     * Asserts that the element contains the given string value.
     *
     * @param locator the Playwright locator for the target element
     * @param type the content type to assert; {@code "class"}, {@code "text"},
     *                    {@code "value"}, {@code "placeholder"}, or {@code "attribute"}
     * @param expected the expected value
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     * @param attrName the name of the attribute to assert if type is
     * "attribute"
     */
    public void assertElementContains(Locator locator, String type, String expected,
            String locatorName, String methodName, String attrName) {
        try {
            Pattern contains = Pattern.compile(".*" + Pattern.quote(expected) + ".*", Pattern.DOTALL);
            switch (type) {
                case "class" ->
                    assertThat(locator).hasAttribute("class", contains,
                            new LocatorAssertions.HasAttributeOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "text" ->
                    assertThat(locator).containsText(expected,
                            new LocatorAssertions.ContainsTextOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "value" ->
                    assertThat(locator).hasValue(contains,
                            new LocatorAssertions.HasValueOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "placeholder" ->
                    assertThat(locator).hasAttribute("placeholder", contains,
                            new LocatorAssertions.HasAttributeOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "attribute" -> {
                    if (attrName == null) {
                        throw new IllegalArgumentException("attrName must be provided for attribute type");
                    }
                    assertThat(locator).hasAttribute(attrName, contains,
                            new LocatorAssertions.HasAttributeOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                }
                default ->
                    throw new IllegalArgumentException("Unknown type: " + type);
            }
            String attrSuffix = attrName != null ? " [attr=\"" + attrName + "\"]" : "";
            LoggingUtil.info(methodName + " - " + locatorName + " " + type + attrSuffix
                    + " contains expected: \"" + expected + "\"");
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName + " " + type
                    + " does not contain \"" + expected + "\": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Asserts that a specific property of the provided element contains the
     * expected substring. Convenience overload for types that do not require an
     * attribute name.
     *
     * @param locator the Playwright locator for the target element
     * @param type the property type to check
     * @param expected the substring expected to be present within the resolved
     * property value
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void assertElementContains(Locator locator, String type, String expected,
            String locatorName, String methodName) {
        assertElementContains(locator, type, expected, locatorName, methodName, null);
    }

    /**
     * Asserts that a specific property of the provided element does not contain
     * the unexpected substring. Convenience overload for types that do not
     * require an attribute name.
     *
     * @param locator the Playwright locator for the target element
     * @param type the property type to check
     * @param unexpected the substring expected not to be present within the
     * resolved property value
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void assertElementNotContains(Locator locator, String type, String unexpected,
            String locatorName, String methodName, String attrName) {
        try {
            Pattern notContains = Pattern.compile("^(?!.*" + Pattern.quote(unexpected) + ").*$", Pattern.DOTALL);
            switch (type) {
                case "class" ->
                    assertThat(locator).hasAttribute("class", notContains,
                            new LocatorAssertions.HasAttributeOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "text" ->
                    assertThat(locator).not().containsText(unexpected,
                            new LocatorAssertions.ContainsTextOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "value" ->
                    assertThat(locator).hasValue(notContains,
                            new LocatorAssertions.HasValueOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "placeholder" ->
                    assertThat(locator).hasAttribute("placeholder", notContains,
                            new LocatorAssertions.HasAttributeOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                case "attribute" -> {
                    if (attrName == null) {
                        throw new IllegalArgumentException("attrName must be provided for attribute type");
                    }
                    assertThat(locator).hasAttribute(attrName, notContains,
                            new LocatorAssertions.HasAttributeOptions().setTimeout(ElementAssertions.ACTION_TIMEOUT));
                }
                default ->
                    throw new IllegalArgumentException("Unknown type: " + type);
            }
            String attrSuffix = attrName != null ? " [attr=\"" + attrName + "\"]" : "";
            LoggingUtil.info(methodName + " - " + locatorName + " " + type + attrSuffix
                    + " does not contain: \"" + unexpected + "\"");
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - " + locatorName + " " + type
                    + " contains unexpected value \"" + unexpected + "\": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Asserts that a specific property of the provided element does NOT contain
     * the expected substring. Convenience overload for types that do not
     * require an attribute name.
     *
     * @param locator the Playwright locator for the target element
     * @param type the property type to check
     * @param unexpected the substring expected to be absent from the resolved
     * property value
     * @param locatorName the friendly name of the locator for logging
     * @param methodName the calling method name for logging
     */
    public void assertElementNotContains(Locator locator, String type, String unexpected,
            String locatorName, String methodName) {
        assertElementNotContains(locator, type, unexpected, locatorName, methodName, null);
    }
}
