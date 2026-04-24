package modulars.iframe;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import utils.logger.LoggingUtil;

public class IframeActions {

    /**
     * Fills the given selector inside the iframe with the given text.
     */
    public void fillTheElement(Locator frameLocator, String selector,
                               String text, String methodName) {
        try {
            Frame frame = frameLocator.elementHandle().contentFrame();
            if (frame == null) throw new RuntimeException("Could not get frame from the provided locator");
            frame.fill(selector, text);
            LoggingUtil.info((methodName != null ? methodName : "Iframe.fill")
                    + " - Filled \"" + selector + "\" in iframe with text: " + text);
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Iframe.fill")
                    + " - Failed to fill \"" + selector + "\" in iframe: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Clicks the given selector inside the iframe.
     */
    public void clickTheElement(Locator frameLocator, String selector, String methodName) {
        try {
            Frame frame = frameLocator.elementHandle().contentFrame();
            if (frame == null) throw new RuntimeException("Could not get frame from the provided locator");
            frame.click(selector);
            LoggingUtil.info((methodName != null ? methodName : "Iframe.click")
                    + " - Clicked \"" + selector + "\" inside iframe");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Iframe.click")
                    + " - Failed to click \"" + selector + "\" inside iframe: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Returns the text content of the given selector inside the iframe.
     */
    public String getText(Locator frameLocator, String selector, String methodName) {
        try {
            Frame frame = frameLocator.elementHandle().contentFrame();
            if (frame == null) throw new RuntimeException("Could not get frame from the provided locator");
            String text = frame.textContent(selector);
            LoggingUtil.info((methodName != null ? methodName : "Iframe.getText")
                    + " - Got text from \"" + selector + "\" inside iframe: " + text);
            return text;
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Iframe.getText")
                    + " - Failed to get text from \"" + selector + "\" inside iframe: " + err.getMessage());
            throw err;
        }
    }
}