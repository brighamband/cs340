package edu.byu.cs.tweeter.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
        System.out.println("datetime " + datetime);
        try {
            DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date date = inputFormat.parse(datetime);

            System.out.println("timestamp in ms " + date.getTime());
            System.out.println("timestamp in sec " + millisToSec(date.getTime()));
            return millisToSec(date.getTime());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Converts a given time represented in long as a string.
     */
    public static String longTimeToString(long timestamp) {
        long millisTimestamp = secToMillis(timestamp);
        Date date = new Date(millisTimestamp);
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

    private static long secToMillis(long sec) {
        return sec * 1000;
    }
}
