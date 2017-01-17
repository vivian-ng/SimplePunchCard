package com.maplerain.simplepunchcard;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vivian on 1/5/17.
 *
 * Helper class for date and time functions.
 */

public class TimeHelper {
    private String dateString;
    private String timeString;
    private String yearMthString;

    public TimeHelper() {
        Calendar dateTimeNow = Calendar.getInstance();
        timeString = new SimpleDateFormat("HH:mm").format(dateTimeNow.getTime());
        dateString = new SimpleDateFormat("yyyy/MM/dd").format(dateTimeNow.getTime());
        yearMthString = new SimpleDateFormat("yyyy-MM").format(dateTimeNow.getTime());

    }

    public String getDate() {
        return dateString;
    }

    public String getTime() {
        return timeString;
    }

    public String getYearMth() {
        return yearMthString;
    }

}
