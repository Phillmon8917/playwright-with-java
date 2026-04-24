package modulars.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.logger.LoggingUtil;

public class ElementActions {

    /**
     * Clears the given element by selecting all text and deleting it.
     */
    public void clearElement(Locator locator, String elementName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.click(new Locator.ClickOptions().setForce(true));
            locator.press("Control+A");
            locator.press("Delete");
            LoggingUtil.info(methodName + " - Successfully cleared " + elementName);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to clear " + elementName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Fills the given element with the given value.
     * Set encrypt=true to mask the value in logs.
     */
    public void fillTheElement(Locator locator, String value, String locatorName,
                               String methodName, boolean encrypt) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.click(new Locator.ClickOptions().setForce(true));
            locator.fill(value);

            String logValue = encrypt ? "*".repeat(value.length()) : value;
            LoggingUtil.info(methodName + " - Successfully filled " + locatorName + " with value: " + logValue);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to fill " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /** Overload without encryption flag — defaults to false. */
    public void fillTheElement(Locator locator, String value, String locatorName, String methodName) {
        fillTheElement(locator, value, locatorName, methodName, false);
    }

    /**
     * Types text into the element sequentially with a delay between characters.
     */
    public void typeSequentially(Locator locator, String text, String locatorName,
                                 String methodName, int delayMs) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.click(new Locator.ClickOptions().setForce(true));
            locator.pressSequentially(text, new Locator.PressSequentiallyOptions().setDelay(delayMs));
            LoggingUtil.info(methodName + " - Successfully typed sequentially into " + locatorName);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to type sequentially into " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /** Overload with default 100ms delay. */
    public void typeSequentially(Locator locator, String text, String locatorName, String methodName) {
        typeSequentially(locator, text, locatorName, methodName, 100);
    }

    /**
     * Scrolls the given element into view.
     */
    public void scrollIntoView(Locator locator, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.scrollIntoViewIfNeeded();
            LoggingUtil.info(methodName + " - Successfully scrolled " + locatorName + " into view");
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to scroll " + locatorName + " into view: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Clicks on the given element.
     * clickType: null = left click (force), "double" = double click, "right" = right click.
     */
    public void clickElement(Locator locator, String locatorName,
                             String methodName, String clickType) {
        try {
            locator.scrollIntoViewIfNeeded();
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            if (clickType == null) {
                locator.click(new Locator.ClickOptions().setForce(true));
                LoggingUtil.info(methodName + " - Successfully clicked " + locatorName);
            } else if (clickType.equals("double")) {
                locator.dblclick();
                LoggingUtil.info(methodName + " - Successfully double-clicked " + locatorName);
            } else if (clickType.equals("right")) {
                locator.click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
                LoggingUtil.info(methodName + " - Successfully right-clicked " + locatorName);
            }
        } catch (Exception err) {
            String prefix = clickType != null ? "Failed to " + clickType + "-click " : "Failed to click ";
            LoggingUtil.error(methodName + " - " + prefix + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /** Overload with no clickType — defaults to standard left click. */
    public void clickElement(Locator locator, String locatorName, String methodName) {
        clickElement(locator, locatorName, methodName, null);
    }

    /**
     * Hovers over the given element.
     */
    public void hoverElement(Locator locator, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.hover();
            LoggingUtil.info(methodName + " - Successfully hovered over " + locatorName);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to hover over " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Focuses the given element.
     */
    public void focusElement(Locator locator, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.focus();
            LoggingUtil.info(methodName + " - Successfully focused on " + locatorName);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to focus on " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Retrieves the text content of the given element.
     */
    public String getText(Locator locator, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String text = locator.textContent();
            LoggingUtil.info(methodName + " - Successfully retrieved text from " + locatorName);
            return text;
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to get text from " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Retrieves the value of the given attribute from the element.
     */
    public String getAttribute(Locator locator, String attr, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String value = locator.getAttribute(attr);
            LoggingUtil.info(methodName + " - Successfully retrieved attribute \"" + attr + "\" from " + locatorName);
            return value;
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to get attribute \"" + attr + "\" from " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Returns the count of elements matching the given locator.
     */
    public int getCount(Locator locator, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            int count = locator.count();
            LoggingUtil.info(methodName + " - Successfully counted " + count + " elements for " + locatorName);
            return count;
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to count elements for " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Clicks the element using JavaScript evaluate — bypasses overlays.
     */
    public void jsClick(Locator locator, String locatorName, String methodName) {
        try {
            locator.scrollIntoViewIfNeeded();
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.evaluate("el => el.click()");
            LoggingUtil.info(methodName + " - Successfully JS-clicked " + locatorName);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to JS-click " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Checks the given checkbox element.
     */
    public void checkElement(Locator locator, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.check(new Locator.CheckOptions().setForce(true));
            org.junit.jupiter.api.Assertions.assertTrue(locator.isChecked(),
                    locatorName + " was not checked after check()");
            LoggingUtil.info(methodName + " - Successfully checked " + locatorName);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to check " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Unchecks the given checkbox element.
     */
    public void uncheckElement(Locator locator, String locatorName, String methodName) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            locator.uncheck(new Locator.UncheckOptions().setForce(true));
            org.junit.jupiter.api.Assertions.assertFalse(locator.isChecked(),
                    locatorName + " was still checked after uncheck()");
            LoggingUtil.info(methodName + " - Successfully unchecked " + locatorName);
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Failed to uncheck " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }
}