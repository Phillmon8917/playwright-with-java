package modulars.keyboard;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.logger.LoggingUtil;

public class KeyboardActions {

    private final Page page;

    public KeyboardActions(Page page) {
        this.page = page;
    }

    /** Core press method — all specific key methods delegate here. */
    private void press(Locator locator, String key, String methodName) {
        try {
            locator.press(key);
            LoggingUtil.info((methodName != null ? methodName : "Keyboard.press")
                    + " - Pressed key \"" + key + "\" on locator");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Keyboard.press")
                    + " - Failed to press key \"" + key + "\" on locator: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Presses the Enter key on the supplied locator.
     *
     * @param locator the locator that receives the key press
     * @param methodName the method name to include in log messages
     */
    public void pressEnter(Locator locator, String methodName) {
        press(locator, "Enter", methodName != null ? methodName : "Keyboard.pressEnter");
    }

    /**
     * Presses the Tab key on the supplied locator.
     *
     * @param locator the locator that receives the key press
     * @param methodName the method name to include in log messages
     */
    public void pressTab(Locator locator, String methodName) {
        press(locator, "Tab", methodName != null ? methodName : "Keyboard.pressTab");
    }

    /**
     * Presses the Escape key on the supplied locator.
     *
     * @param locator the locator that receives the key press
     * @param methodName the method name to include in log messages
     */
    public void pressEscape(Locator locator, String methodName) {
        press(locator, "Escape", methodName != null ? methodName : "Keyboard.pressEscape");
    }

    /**
     * Presses the Control+A shortcut on the supplied locator.
     *
     * @param locator the locator that receives the key press
     * @param methodName the method name to include in log messages
     */
    public void pressCtrlA(Locator locator, String methodName) {
        press(locator, "Control+A", methodName != null ? methodName : "Keyboard.pressCtrlA");
    }

    /**
     * Presses the Control+C shortcut on the supplied locator.
     *
     * @param locator the locator that receives the key press
     * @param methodName the method name to include in log messages
     */
    public void pressCtrlC(Locator locator, String methodName) {
        press(locator, "Control+C", methodName != null ? methodName : "Keyboard.pressCtrlC");
    }

    /**
     * Presses the Control+V shortcut on the supplied locator.
     *
     * @param locator the locator that receives the key press
     * @param methodName the method name to include in log messages
     */
    public void pressCtrlV(Locator locator, String methodName) {
        press(locator, "Control+V", methodName != null ? methodName : "Keyboard.pressCtrlV");
    }

    /**
     * Presses the Control+X shortcut on the supplied locator.
     *
     * @param locator the locator that receives the key press
     * @param methodName the method name to include in log messages
     */
    public void pressCtrlX(Locator locator, String methodName) {
        press(locator, "Control+X", methodName != null ? methodName : "Keyboard.pressCtrlX");
    }

    /** Presses a key directly on the page (not on a locator). */
    public void pressKey(String key, String methodName) {
        try {
            page.keyboard().press(key);
            LoggingUtil.info((methodName != null ? methodName : "Keyboard.pressKey")
                    + " - Pressed key \"" + key + "\" on page");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Keyboard.pressKey")
                    + " - Failed to press key \"" + key + "\" on page: " + err.getMessage());
            throw err;
        }
    }
}
