package edu.byu.cs.tweeter.server;

import java.util.GregorianCalendar;

public class TimeUtils {
    /**
     * Returns the current time in milliseconds.
     */
    public static long getCurrTimeInMs() {
        GregorianCalendar currCalendar = new GregorianCalendar();
        return currCalendar.getTimeInMillis();
    }

    /**
     * Returns the current time as a string.
     */
    public static String getCurrTimeInString() {
        GregorianCalendar currCalendar = new GregorianCalendar();
        return currCalendar.getTime().toString();
    }

    /**
     * Converts a given time represented in long as a string.
     */
    public static String longTimeToString(long timestamp) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime().toString();
    }
}
