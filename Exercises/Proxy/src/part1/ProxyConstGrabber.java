package part1;

import java.util.*;

public class ProxyConstGrabber implements ConstGrabber {
    private List<Integer> allowedDays;
    private List<Integer> allowedTimeRange;
    private RealConstGrabber realConstGrabber;

    public ProxyConstGrabber(RealConstGrabber realConstGrabber) {
        this.allowedDays = Arrays.asList(0, 1, 2, 3, 4);   // Valid days
        this.allowedTimeRange = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ,10, 11, 12, 13, 14);    // Valid hours
        this.realConstGrabber = realConstGrabber;
    }

    private void checkDateTime() throws Exception {
        Calendar calendar = new GregorianCalendar();
        int dayOfWeek = getCurDay(calendar);
        int hourOfDay = getCurTime(calendar);

        if (!allowedDays.contains(dayOfWeek) || !allowedTimeRange.contains(hourOfDay)) {      // Invalid date time
            throw new Exception("Invalid date time");
        }
    }

    private int getCurDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private int getCurTime(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public double getPi() throws Exception {
        checkDateTime();
        return realConstGrabber.getPi();
    }

    @Override
    public double getE() throws Exception {
        checkDateTime();
        return realConstGrabber.getE();
    }
}
