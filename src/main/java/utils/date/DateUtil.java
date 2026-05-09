package utils.date;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

    /**
     * Returns the current date formatted as "dd-MM-yyyy".
     *
     * @return the formatted current date string
     */
    public static String getFormattedDate() {
        return getFormattedDate(LocalDate.now(), "dd-MM-yyyy");
    }

    /**
     * Returns the current date formatted according to the specified format
     * string. Supports placeholders: MMMM (full month), MMM (short month), MM
     * (month number), dd (day), yyyy (year).
     *
     * @param format the format string to use
     * @return the formatted current date string
     */
    public static String getFormattedDate(String format) {
        return getFormattedDate(LocalDate.now(), format);
    }

    /**
     * Returns the specified date formatted according to the format string.
     * Supports placeholders: MMMM (full month), MMM (short month), MM (month
     * number), dd (day), yyyy (year).
     *
     * @param date the LocalDate to format
     * @param format the format string to use
     * @return the formatted date string
     */
    public static String getFormattedDate(LocalDate date, String format) {
        String day = String.format("%02d", date.getDayOfMonth());
        String month = String.format("%02d", date.getMonthValue());
        String year = String.valueOf(date.getYear());
        String monthShort = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String monthFull = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return format
                .replace("MMMM", monthFull)
                .replace("MMM", monthShort)
                .replace("MM", month)
                .replace("dd", day)
                .replace("yyyy", year);
    }

    /**
     * Returns a date relative to today by the specified number of days,
     * formatted as "dd-MM-yyyy".
     *
     * @param offset the number of days to add (positive for future, negative
     * for past)
     * @return the formatted relative date string
     */
    public static String getRelativeDate(int offset) {
        return getFormattedDate(LocalDate.now().plusDays(offset), "dd-MM-yyyy");
    }

    /**
     * Returns a date relative to today by the specified number of days,
     * formatted according to the specified format string. Supports
     * placeholders: MMMM (full month), MMM (short month), MM (month number), dd
     * (day), yyyy (year).
     *
     * @param offset the number of days to add (positive for future, negative
     * for past)
     * @param format the format string to use
     * @return the formatted relative date string
     */
    public static String getRelativeDate(int offset, String format) {
        return getFormattedDate(LocalDate.now().plusDays(offset), format);
    }
}
