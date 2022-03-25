package edu.byu.cs.tweeter.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    /**
     * Returns the current time in seconds, stored as a long.
     */
    public static long getCurrTimeAsLong() {
        Date currDate = getCurrDate();
        return millisToSec(currDate.getTime());
    }

    public static long stringTimeToLong(String datetime) {
        try {
            Date date = DateFormat.getDateInstance().parse(datetime);
            return date.getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    /**
     * Converts a given time represented in long as a string.
     */
    public static String longTimeToString(long timestamp) {
        Date date = new Date(timestamp);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return date.toString();
    }

    private static Date getCurrDate() {
        Date date = new Date();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return date;
    }

    /**
     * Converts a time stored as a long from milliseconds to seconds.
     */
    private static long millisToSec(long millis) {
        return millis / 1000;
    }
}
