package modulars.calendar;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.logger.LoggingUtil;

public class CalendarNavigator {

    private final Page page;
    private final Locator currentSwitch;

    public CalendarNavigator(Page page) {
        this.page = page;
        this.currentSwitch = page.locator(".datepicker-days .switch");
    }

    /**
     * Selects the given date in the calendar widget.
     * Navigates to the correct year, then month, then clicks the day.
     */
    public void selectDate(CalendarNavigatorType dateOption, String locatorName, String methodName) {
        try {
            String targetDay  = String.valueOf(dateOption.day());
            String targetYear = String.valueOf(dateOption.year());
            String targetMonth = dateOption.month();

            ensureCorrectYear(targetYear);
            ensureCorrectMonth(targetMonth);
            ensureCorrectDay(targetDay);

            LoggingUtil.info((methodName != null ? methodName : "Calendar.selectDate")
                    + " - Successfully selected " + dateOption.day()
                    + " " + dateOption.month() + " " + dateOption.year()
                    + " on " + locatorName);
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "Calendar.selectDate")
                    + " - Failed selecting " + dateOption.day()
                    + " " + dateOption.month() + " " + dateOption.year()
                    + " on " + locatorName + ": " + err.getMessage());
            throw err;
        }
    }

    /**
     * Switches the calendar view to the requested year when it is not already selected.
     *
     * @param year the target year to select
     */
    private void ensureCorrectYear(String year) {
        String currentText = currentSwitch.textContent();
        if (currentText == null || !currentText.contains(year)) {
            currentSwitch.click(new Locator.ClickOptions().setForce(true));
            currentSwitch.click(new Locator.ClickOptions().setForce(true));
            page.locator(".year:has-text(\"" + year + "\")").click(new Locator.ClickOptions().setForce(true));
        }
    }

    /**
     * Switches the calendar view to the requested month when it is not already selected.
     *
     * @param month the target month name to select
     */
    private void ensureCorrectMonth(String month) {
        Locator switchLocator = page.locator(".datepicker-days .switch");
        String currentText = switchLocator.textContent();
        String monthAbbr = month.substring(0, 3);
        if (currentText == null || !currentText.contains(monthAbbr)) {
            page.locator(".month:has-text(\"" + monthAbbr + "\")").click();
        }
    }

    /**
     * Selects the requested day within the currently displayed month.
     *
     * @param day the day of the month to select
     */
    private void ensureCorrectDay(String day) {
        Locator selectedDay = page.locator(".day.active:not(.old):not(.new):not(.disabled)");
        if (selectedDay.count() > 0) {
            String selectedText = selectedDay.first().textContent();
            if (selectedText != null && selectedText.trim().equals(day)) return;
        }
        page.locator(".day:not(.old):not(.new):not(.disabled)")
                .filter(new Locator.FilterOptions().setHasText(day))
                .first()
                .click();
    }
}
