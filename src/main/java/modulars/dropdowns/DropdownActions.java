package modulars.dropdowns;

import com.microsoft.playwright.Locator;
import utils.logger.LoggingUtil;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DropdownActions {

    /**
     * Selects a dropdown option by visible text.
     * Falls back to clicking the dropdown then the option if selectOption fails.
     */
    public void selectByText(Locator locator, String text, String methodName) {
        try {
            locator.selectOption(new com.microsoft.playwright.options.SelectOption().setLabel(text));
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.selectByText")
                    + " - Selected option by text: " + text);
        } catch (Exception ignored) {
            locator.click();
            locator.page().locator("text=" + text).click();
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.selectByText")
                    + " - Fallback selected custom option by text: " + text);
        }
    }

    /**
     * Selects a dropdown option by value attribute.
     */
    public void selectByValue(Locator locator, String value, String methodName) {
        try {
            locator.selectOption(new com.microsoft.playwright.options.SelectOption().setValue(value));
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.selectByValue")
                    + " - Selected option by value: " + value);
        } catch (Exception ignored) {
            locator.click();
            locator.page().locator("[data-value=\"" + value + "\"]").click();
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.selectByValue")
                    + " - Fallback selected custom option by value: " + value);
        }
    }

    /**
     * Selects a dropdown option by index.
     */
    public void selectByIndex(Locator locator, int index, String methodName) {
        try {
            List<Locator> options = locator.locator("option").all();
            if (index < 0 || index >= options.size())
                throw new IndexOutOfBoundsException("Index " + index + " out of range");
            String value = options.get(index).getAttribute("value");
            locator.selectOption(new com.microsoft.playwright.options.SelectOption().setValue(value != null ? value : ""));
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.selectByIndex")
                    + " - Selected option at index: " + index);
        } catch (Exception ignored) {
            locator.click();
            locator.page().locator("li, div[role='option']").nth(index).click();
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.selectByIndex")
                    + " - Fallback selected custom option at index: " + index);
        }
    }

    /**
     * Returns all option texts from the dropdown.
     */
    public List<String> getOptions(Locator locator, String methodName) {
        try {
            List<String> options = locator.locator("option").allTextContents();
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.getOptions")
                    + " - Retrieved " + options.size() + " options");
            return options;
        } catch (Exception ignored) {
            locator.click();
            List<String> options = locator.page().locator("li, div[role='option']").allTextContents();
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.getOptions")
                    + " - Fallback retrieved " + options.size() + " custom options");
            return options;
        }
    }

    /**
     * Asserts the dropdown contains the given option text.
     */
    public void hasOption(Locator locator, String expected, String methodName) {
        List<String> options = getOptions(locator, methodName);
        org.junit.jupiter.api.Assertions.assertTrue(options.contains(expected),
                "Dropdown does not contain option: " + expected);
        LoggingUtil.info((methodName != null ? methodName : "Dropdown.hasOption")
                + " - Verified dropdown contains option: " + expected);
    }

    /**
     * Asserts the dropdown has the given value selected.
     * Falls back to checking aria-selected for custom dropdowns.
     */
    public void hasSelectedOption(Locator locator, String expected, String methodName) {
        try {
            assertThat(locator).hasValue(expected);
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.hasSelectedOption")
                    + " - Verified selected option: " + expected);
        } catch (Exception ignored) {
            Locator selected = locator.page().locator(".selected, [aria-selected='true']");
            assertThat(selected).hasText(expected);
            LoggingUtil.info((methodName != null ? methodName : "Dropdown.hasSelectedOption")
                    + " - Verified custom selected option: " + expected);
        }
    }
}