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
//        GregorianCalendar currCalendar = new GregorianCalendar();
//        currCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return currCalendar.getTimeInMillis();
    }

    /**
     * Returns the current time as a string.
     */
    public static String getCurrTimeAsString() {
        Date currDate = getCurrDate();
        return currDate.toString();
//        GregorianCalendar currCalendar = new GregorianCalendar();
//        currCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return currCalendar.getTime().toString();
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
//        GregorianCalendar calendar = new GregorianCalendar();
//        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
//        calendar.setTimeInMillis(timestamp);
//        return calendar.getTime().toString();
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
