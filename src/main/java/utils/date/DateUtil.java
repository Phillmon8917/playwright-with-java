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

    public static String getFormattedDate(String format) {
        return getFormattedDate(LocalDate.now(), format);
    }

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