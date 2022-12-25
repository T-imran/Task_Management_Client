package com.example.application.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtility {


    public static final String FORMAT_DATE_TEMPLATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_TIME_TEMPLATE = "yyyy-MM-dd HH:mm:ss";


    public static final ThreadLocal<DateFormat> FORMAT_DATE_TIME = ThreadLocal.withInitial(() ->
            new SimpleDateFormat(FORMAT_DATE_TIME_TEMPLATE));
    public static final ThreadLocal<DateFormat> FORMAT_DATE = ThreadLocal.withInitial(() ->
            new SimpleDateFormat(FORMAT_DATE_TEMPLATE));


    /**
     * Parse a date string to local date time format.
     */
    public static LocalDateTime getLocalDateTime(String pointDate) {

        try {
            Calendar calendar = stringToCalendar(pointDate);

            // Getting the timezone
            TimeZone tz = calendar.getTimeZone();

            // Getting zone id
            ZoneId zoneId = tz.toZoneId();

            // conversion and return
            return LocalDateTime.ofInstant(calendar.toInstant(), zoneId);
        } catch (Exception e) {
            System.err.println("Failed to parse local date time with date string format {} due to {}" + pointDate
                    + e.getMessage());
            return null;
        }
    }

    public static Calendar stringToCalendar(String dateText) {

        if (dateText.contains(".")) {
            dateText = dateText.substring(0, dateText.indexOf("."));
        }

        Calendar pointDate = new GregorianCalendar();
        if (dateText.length() != FORMAT_DATE_TIME_TEMPLATE.length() && dateText.length()
                != FORMAT_DATE_TEMPLATE.length()) {
            return null;
        }
        Date date = dateText.length() == FORMAT_DATE_TIME_TEMPLATE.length()
                ? DateUtility.parseDateTime(dateText)
                : DateUtility.parseDate(dateText);
        pointDate.setTime(date);
        return pointDate;
    }

    /**
     * Parse a date string to a date with date time format.
     *
     * @param dateStr - date string
     * @return Date
     */
    public static Date parseDateTime(String dateStr) {
        try {
            return FORMAT_DATE_TIME.get().parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Failed to parse date with date time format {} due to {}" + dateStr + e.getMessage());
        }
        return null;
    }

    /**
     * Parse a date string to a date format.
     *
     * @param dateStr - date string
     * @return Date
     */
    public static Date parseDate(String dateStr) {
        try {
            return FORMAT_DATE.get().parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Failed to parse date with date format {} due to {}" + dateStr + e.getMessage());
        }
        return null;
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME_TEMPLATE);
        return localDateTime.format(dateTimeFormatter);
    }

}
