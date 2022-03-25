package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.GregorianCalendar;

public class TimeUtils {
    /**
     * Returns the current time in milliseconds.
     */
    protected static long getCurrTimeInMs() {
        GregorianCalendar currCalendar = new GregorianCalendar();
        return currCalendar.getTimeInMillis();
    }

    /**
     * Returns the current time as a string.
     */
    protected static String getCurrTimeInString() {
        GregorianCalendar currCalendar = new GregorianCalendar();
        return currCalendar.getTime().toString();
    }

    /**
     * Converts a given time represented in long as a string.
     */
    protected static String longTimeToString(long timestamp) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime().toString();
    }
}
