package utils.date;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

    /**
     * Formats a given date into the specified pattern.
     * Supported tokens:
     * - yyyy : full year
     * - MM   : month number (01–12)
     * - MMM  : short month name (Jan–Dec)
     * - MMMM : full month name (January–December)
     * - dd   : day of month (01–31)
     */
    public static String getFormattedDate() {
        return getFormattedDate(LocalDate.now(), "dd-MM-yyyy");
    }

    /**
     * Formats the current date using the supplied pattern.
     *
     * @param format the output format pattern to apply
     * @return the formatted current date
     */
    public static String getFormattedDate(String format) {
        return getFormattedDate(LocalDate.now(), format);
    }

    /**
     * Formats the supplied date using the provided pattern tokens.
     *
     * @param date the date to format
     * @param format the output format pattern to apply
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
}
