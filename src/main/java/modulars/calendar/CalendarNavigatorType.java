package modulars.calendar;

/**
 * Represents a date option for calendar selection.
 * month should be the full month name e.g. "January", "February".
 */
public record CalendarNavigatorType(int day, String month, int year) {}