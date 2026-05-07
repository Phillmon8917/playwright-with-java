package utils.validation;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import utils.logger.LoggingUtil;
import utils.timeout.TimeoutUtil;

public class ValidationHelper {

    /**
     * Retrieves the validation message for the given field. If the field has a
     * native validation message, it will be returned. If the field does not
     * have a native validation message, it will search for an error tooltip or
     * error message element adjacent to the field element and return its text
     * content. If no error element is found, an empty string will be returned.
     *
     * @param page - The page object.
     * @param fieldId - The id of the field element.
     * @returns The validation message string.
     */
    public static String getValidationMessage(Page page, String fieldId) {
        try {
            page.waitForFunction(
                    "fieldId => {"
                    + "  const el = document.querySelector('#' + fieldId);"
                    + "  return el && el.validationMessage && el.validationMessage.trim().length > 0;"
                    + "}",
                    fieldId,
                    new Page.WaitForFunctionOptions().setTimeout(TimeoutUtil.of(8000))
            );

            String nativeMessage = (String) page.evalOnSelector(
                    "#" + fieldId,
                    "el => el.validationMessage"
            );

            if (nativeMessage != null && !nativeMessage.trim().isEmpty()) {
                LoggingUtil.info("getValidationMessage - Native validation message found for field: " + fieldId);
                return nativeMessage.trim();
            }

            Locator errorLocator = page.locator(
                    "#" + fieldId + " ~ .error-tooltip, #" + fieldId + " ~ .error-message"
            );

            if (errorLocator.count() > 0) {
                String errorText = errorLocator.first().innerText().trim();
                LoggingUtil.info("getValidationMessage - Error element message found for field: " + fieldId);
                return errorText;
            }

            LoggingUtil.info("getValidationMessage - No validation message found for field: " + fieldId);
            return "";
        } catch (Exception err) {
            try {
                Locator errorLocator = page.locator(
                        "#" + fieldId + " ~ .error-tooltip, #" + fieldId + " ~ .error-message"
                );
                if (errorLocator.count() > 0) {
                    return errorLocator.first().innerText().trim();
                }
            } catch (Exception ignored) {
            }

            LoggingUtil.error("getValidationMessage - Failed to get validation message for field: "
                    + fieldId + ": " + err.getMessage());
            return "";
        }
    }
}
