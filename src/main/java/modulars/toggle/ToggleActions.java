package modulars.toggle;

import com.microsoft.playwright.Locator;
import utils.logger.LoggingUtil;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ToggleActions {

    /**
     * Ensures the toggle is checked. Checks it if not already checked.
     */
    public void ensureChecked(Locator locator, String methodName) {
        try {
            if (!locator.isChecked()) locator.check();
            assertThat(locator).isChecked();
            LoggingUtil.info((methodName != null ? methodName : "Toggle.ensureChecked")
                    + " - Toggle is checked");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Toggle.ensureChecked")
                    + " - Failed to ensure toggle is checked: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Ensures the toggle is unchecked. Unchecks it if currently checked.
     */
    public void ensureUnchecked(Locator locator, String methodName) {
        try {
            if (locator.isChecked()) locator.uncheck();
            assertThat(locator).not().isChecked();
            LoggingUtil.info((methodName != null ? methodName : "Toggle.ensureUnchecked")
                    + " - Toggle is unchecked");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Toggle.ensureUnchecked")
                    + " - Failed to ensure toggle is unchecked: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Asserts the toggle is currently checked.
     */
    public void assertChecked(Locator locator, String methodName) {
        try {
            assertThat(locator).isChecked();
            LoggingUtil.info((methodName != null ? methodName : "Toggle.assertChecked")
                    + " - Toggle is checked");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Toggle.assertChecked")
                    + " - Toggle check assertion failed: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Asserts the toggle is currently unchecked.
     */
    public void assertUnchecked(Locator locator, String methodName) {
        try {
            assertThat(locator).not().isChecked();
            LoggingUtil.info((methodName != null ? methodName : "Toggle.assertUnchecked")
                    + " - Toggle is unchecked");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Toggle.assertUnchecked")
                    + " - Toggle uncheck assertion failed: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Clicks the toggle element to flip its state.
     */
    public void toggle(Locator locator, String methodName) {
        try {
            locator.click();
            LoggingUtil.info((methodName != null ? methodName : "Toggle.toggle")
                    + " - Toggle clicked");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Toggle.toggle")
                    + " - Failed to toggle: " + err.getMessage());
            throw err;
        }
    }
}