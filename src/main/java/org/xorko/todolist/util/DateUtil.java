package org.xorko.todolist.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    /** The date pattern used to format */
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    /** The date formatter */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Returns the given date as a formatted string
     * The format used is {@link DateUtil#DATE_PATTERN}.
     *
     * @param date
     *          The date to be returned as a string
     * @return
     *          The formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
     * to a {@link LocalDate} object.
     *
     * Returns null if the String could not be converted
     *
     * @param dateString
     *          The date as String
     * @return
     *          The date object or null if it could not be converted
     */
    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Checks the String whether it is a valid date
     * @param dateString
     *          The string to check
     * @return
     *          True if <code>dateString</code> is a valid date
     */
    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString) != null;
    }


}
