package com.yeket.music.bridge.infrastructure.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DETAILED_DATE_FORMAT = "yy-MM-dd HH:mm:ss Z";

    public static Date getDate(String stringDate){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            return formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String getString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }

    public static String getDetailedString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(DETAILED_DATE_FORMAT);
        return formatter.format(date);
    }

    public static Date getDetailedDate(String stringDate){
        SimpleDateFormat formatter = new SimpleDateFormat(DETAILED_DATE_FORMAT);
        try{
            return formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
