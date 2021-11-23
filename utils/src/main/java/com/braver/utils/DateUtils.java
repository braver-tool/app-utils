/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class DateUtils {

    /*API and Local Storage Formats*/
    public static SimpleDateFormat common_date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat ymdhm_date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static SimpleDateFormat mdy_date_format = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
    public static SimpleDateFormat ymd_date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat railway_time_format = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static SimpleDateFormat emdy_date_format = new SimpleDateFormat("EEEE MM/dd/yyyy", Locale.getDefault());
    public static SimpleDateFormat mdy_date_format_1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    public static SimpleDateFormat emdyhma_date_format = new SimpleDateFormat("EEEE MM/dd/yyyy hh:mm a", Locale.getDefault());
    public static SimpleDateFormat normal_time_format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static SimpleDateFormat mdy_date_format_2 = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    public static SimpleDateFormat mdyhma_date_format_2 = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
    public static SimpleDateFormat hmamd_date_format_2 = new SimpleDateFormat("h:mm a, MMM dd", Locale.getDefault());
    public static SimpleDateFormat mdy_date_format_3 = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
    public static SimpleDateFormat dmy_date_format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static SimpleDateFormat md_date_format = new SimpleDateFormat("MMM dd", Locale.getDefault());
    public static SimpleDateFormat md_date_year_time_format1 = new SimpleDateFormat("MMM d, yyyy - h:mm a", Locale.getDefault());
    public static SimpleDateFormat md_date_format1 = new SimpleDateFormat("M/dd", Locale.getDefault());
    public static SimpleDateFormat utc_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    public static SimpleDateFormat mdyhmsa_date_format = new SimpleDateFormat("M/d/yyyy hh:mm:ss a", Locale.getDefault());
    public static SimpleDateFormat mdyhmsa_date_format1 = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", Locale.getDefault());
    public static SimpleDateFormat mdyhma_date_format = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
    public static SimpleDateFormat mdyhma_date_format1 = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault());
    public static SimpleDateFormat mdy_format = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
    public static SimpleDateFormat mdy_format1 = new SimpleDateFormat("MMM d yyyy", Locale.getDefault());
    public static SimpleDateFormat mdy_date_time_format = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", Locale.getDefault());
    public static SimpleDateFormat mdhma_date_format = new SimpleDateFormat("MMM d h:mm a", Locale.getDefault());
    public static SimpleDateFormat emdy_date_format_3 = new SimpleDateFormat("EEEE MM/dd/yy", Locale.getDefault());
    public static SimpleDateFormat mdyhmsa_date_format2 = new SimpleDateFormat("M/dd/yyyy hh:mm a", Locale.getDefault());
    public static SimpleDateFormat mdy_date_format_9 = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    public static SimpleDateFormat mdyhmsa_date_format4 = new SimpleDateFormat("MMMM d, yyyy - h:mm:ss a", Locale.getDefault());
    public static SimpleDateFormat yyyy_format = new SimpleDateFormat("yyyy", Locale.getDefault());
    public static SimpleDateFormat normal_time_format_1 = new SimpleDateFormat("h:mm a", Locale.getDefault());

    /**
     * Method used to get Current date
     *
     * @return current date
     */
    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        return DateUtils.common_date_format.format(c);
    }

    /**
     * Method used to get Current date with UTC
     *
     * @return current date
     */
    public static String getCurrentDateInUTCFormat() {
        utc_format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return utc_format.format(Calendar.getInstance().getTime());
    }

    /*
     * Method used to get current time of the device
     * @return currentTime value
     */
    public static String getCurrentRailwayTime() {
        return railway_time_format.format(Calendar.getInstance().getTime());

    }

    /*
     * Method used to get current utc time of the device
     * @return currentTime value
     */
    public static String getCurrentNormalTime() {
        return normal_time_format.format(Calendar.getInstance().getTime());
    }

    /**
     * Method used to get given date format into change format
     *
     * @param actualDate   - date string
     * @param actualFormat - current date format
     * @param neededFormat - changed date format
     * @return changed date format
     */
    public static String convertDateFormat(String actualDate, SimpleDateFormat actualFormat, SimpleDateFormat neededFormat) {
        if (actualDate == null || actualDate.isEmpty() || actualFormat == null) {
            actualDate = getCurrentDate();
            actualFormat = common_date_format;
        }
        Date resultDate;
        try {
            resultDate = actualFormat.parse(actualDate);
            return neededFormat.format(resultDate);
        } catch (ParseException e) {
            if (android.os.Build.MODEL.toLowerCase().contains("moto") && (actualDate.toLowerCase().contains("am") || actualDate.toLowerCase().contains("pm"))) {
                try {
                    actualDate = actualDate.toLowerCase().replace("am", "a.m.").replace("pm", "p.m.");
                    resultDate = actualFormat.parse(actualDate);
                    if (resultDate != null) {
                        return neededFormat.format(resultDate);
                    }
                } catch (ParseException e1) {
                    Log.d("##convertDateFormat", "-------->" + e1.getMessage());
                    if (actualFormat.equals(normal_time_format_1) && actualDate.length() == 7) {
                        actualDate = "0".concat(actualDate);
                    } else {
                        actualDate = actualDate.charAt(0) == '0' ? actualDate.substring(1) : actualDate;
                    }
                }
            }
            return actualDate;
        }
    }


    /**
     * Method used to add 10 minutes for given date
     *
     * @param selectedDate - date string
     * @return ten minutes add date string
     */
    public static String addMinutesOfTheDate(String selectedDate, int addMinutes) {
        String currentDate;
        try {
            Date date = common_date_format.parse(selectedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + (addMinutes));
            Date currentDay = calendar.getTime();
            currentDate = common_date_format.format(currentDay);
            return currentDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return getCurrentDate();
        }
    }

    /*
     * Method used to get minus hours of the device
     * @return minus hour value
     */
    public static String addHourOfTheDate(String selectedDate, int addHour) {
        String currentDate;
        Date date = null;
        try {
            date = common_date_format.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + (addHour));
        Date currentDay = calendar.getTime();
        currentDate = common_date_format.format(currentDay);
        return currentDate;
    }

    /**
     * Method used to get given date format into change Utc format
     *
     * @param dateString - date string
     * @return changed date format
     */
    public static String convertDateToUTCFormat(String dateString) {
        String utcDate = "";
        if (!dateString.isEmpty()) {
            Date date = null;
            try {
                date = common_date_format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            formatterIST.setTimeZone(TimeZone.getTimeZone("UTC"));
            utcDate = formatterIST.format(date);
        }
        return utcDate;
    }


    /**
     * Method used to get given date format into change normal format
     *
     * @param dateString - date string
     * @return changed date format
     */
    public static String convertDateToNormalFormatFromUTC(String dateString) {
        String utcDate;
        SimpleDateFormat format;
        SimpleDateFormat dateFormat = null;
        Date formatedDate = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            formatedDate = dateFormat.parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (formatedDate == null) {
            try {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                formatedDate = dateFormat.parse(dateString);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        if (formatedDate == null) {
            try {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                formatedDate = dateFormat.parse(dateString);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }

        if (formatedDate == null) {
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        } else {
            format = dateFormat;
        }
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if (date != null) {
            utcDate = formatterIST.format(date);
        } else {
            utcDate = "";
        }
        return utcDate;
    }

    /**
     * Method used to get given date format into change twenty four hour format
     *
     * @param dateString - date string
     * @return changed date format
     */
    public static String convertNormalTimeToRailwayTime(String dateString) {
        String result = "";
        if (!dateString.isEmpty()) {
            Date date = null;
            try {
                date = normal_time_format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date == null) {
                result = motoTimeTo24Hr(dateString);
            } else {
                result = railway_time_format.format(date);
            }
        }
        return result;
    }

    /**
     * Method used to get given date format into change hours format
     *
     * @param dateString - date string
     * @return changed date format
     */
    public static String convertrailwayTimeToNormalTime(String dateString) {
        String result = "";
        Date date = null;
        try {
            date = railway_time_format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String reminderTime = normal_time_format.format(date);
        if (reminderTime.length() > 8) {
            result = reminderTime.replace("a.m.", "am").replace("p.m.", "pm");
        } else {
            result = reminderTime;
        }
        return result;
    }

    /**
     * Method used to get given date format into change twenty four hour format for moto mobile
     *
     * @param dateString - date string
     * @return changed date format
     */
    private static String motoTimeTo24Hr(String dateString) {
        String result = "";
        if (!dateString.isEmpty()) {
            String format = dateString.substring(6, 8);
            if ((format.equals("PM") || (format.equals("pm")))) {
                int hour = Integer.parseInt(dateString.substring(0, 2));
                String hhh = String.valueOf(hour + 12);
                String minute = dateString.substring(3, 5);
                result = hhh.concat(":").concat(minute);
            } else {
                result = dateString.substring(0, 5);
            }
        }
        return result;
    }

    /**
     * Method used to get given date format into change text format
     *
     * @param dateString - date string
     * @return changed date format
     */
    public static int[] getPartsOfTheDate(String dateString) {
        Calendar calendar = Calendar.getInstance();
        if (!dateString.isEmpty()) {
            String cDate = convertDateFormat(dateString, common_date_format, dmy_date_format);
            String[] parts = cDate.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            calendar.set(YEAR, year);
            calendar.set(MONTH, (month - 1));
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        return new int[]{calendar.get(YEAR), calendar.get(MONTH), calendar.get(Calendar.DAY_OF_MONTH)};
    }


    /**
     * Method used to get calendar start and end date is valid or not
     *
     * @param startDate - date string
     * @param endDate   - date string
     * @return boolean value
     */

    public static boolean isValidEndDate(String startDate, String endDate) {
        boolean isTrue;
        Date sttDate = null;
        Date enddDt = null;
        try {
            sttDate = ymd_date_format.parse(startDate);
            enddDt = ymd_date_format.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(sttDate);
        endCalendar.setTime(enddDt);
        isTrue = endCalendar.getTime().after(startCalendar.getTime()) || startCalendar.getTime().equals(endCalendar.getTime());
        return isTrue;
    }


    /**
     * Method used to set calendar date validation for display dots in calendar home screen
     *
     * @param startDate - date string
     * @param endDate   - date string
     * @return array list value
     */
    public static List<String> getBetweenDates(String startDate, String endDate) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = ymd_date_format.parse(startDate);
            date2 = ymd_date_format.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> datesInRange = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date2);
        while (calendar.before(endCalendar) || calendar.equals(endCalendar)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String format = simpleDateFormat.format(calendar.getTime());
            datesInRange.add(format);
            calendar.add(DATE, 1);
        }
        return datesInRange;
    }

    /***
     * Method used for is current date in Text box
     * **/
    public static boolean isToday(String selectedDate) {
        boolean isCurrentDate;
        Date currentDay = new Date();
        String reportDay = ymd_date_format.format(currentDay);
        isCurrentDate = selectedDate.equals(reportDay);
        return isCurrentDate;
    }


    /**
     * This method is used to get call duration time between start time and end time
     */
    public static String getDifferenceTime(String startTime, String endTime) {
        String diffTime = "";
        long diffMinutes;
        Date d1, d2;
        try {
            d1 = common_date_format.parse(startTime);
            d2 = common_date_format.parse(endTime);
            //in milliseconds
            long time = d2.getTime() - d1.getTime();
            int h = (int) (time / 3600000);
            int m = (int) (time - h * 3600000) / 60000;
            int s = (int) (time - h * 3600000 - m * 60000) / 1000;
            if (h == 0) {
                diffTime = (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
            } else {
                diffTime = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffTime;
    }

    /**
     * This method is used to get call hold time seconds between start time and end time
     */
    public static int getDifferenceTimeAsSeconds(String startTime, String endTime) {
        int diffTimeInSeconds = 0;
        Date d1, d2;
        try {
            d1 = common_date_format.parse(startTime);
            d2 = common_date_format.parse(endTime);
            //in milliseconds
            long time = (d2.getTime() - d1.getTime()) / 1000;
            diffTimeInSeconds = (int) time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffTimeInSeconds;
    }


    /**
     * @param dateOfBirthFromApi -  String data
     * @return - Age as String
     * Method used to validate Patient's Age from their DOB
     */
    public static String getAgeFromDob(String dateOfBirthFromApi) {
        try {
            Calendar startCal = Calendar.getInstance();
            Date stDate = ymd_date_format.parse(dateOfBirthFromApi);
            startCal.setTime(stDate);
            Calendar endCal = Calendar.getInstance();
            int diff = endCal.get(YEAR) - startCal.get(YEAR);
            if (startCal.get(MONTH) > endCal.get(MONTH) || (startCal.get(MONTH) == endCal.get(MONTH) && startCal.get(DATE) > endCal.get(DATE))) {
                diff--;
            }
            String actAge = String.valueOf(diff);
            if (actAge.contains("-")) {
                actAge = "0";
            }
            return "(".concat(actAge).concat(" yrs)");
        } catch (Exception e) {
            return "(0 yrs)";
        }
    }

    /**
     * Method used for get reward date value validation
     *
     * @return string date value
     */
    public static String getSupScriptFormattedDate(String sourceDate) {
        Date date = null;
        SimpleDateFormat dateFormat;
        try {
            date = mdy_date_format_2.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayNumberSuffix = getDayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH));
        dateFormat = new SimpleDateFormat("MMM d'" + dayNumberSuffix + "',yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Method used for get concat date with text
     *
     * @return string value
     */

    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "<sup><small>th</small></sup>";
        }
        switch (day % 10) {
            case 1:
                return "<sup><small>st</small></sup>";
            case 2:
                return "<sup><small>nd</small></sup>";
            case 3:
                return "<sup><small>rd</small></sup>";
            default:
                return "<sup><small>th</small></sup>";
        }
    }

    /**
     * @param dayOfMonth - int data
     * @param month      - int data
     * @param year       - int data
     * @return - Boolean data
     * Method used to validate is Date is Correct or Not?
     */
    public static boolean isValidDate(int dayOfMonth, int month, int year) {
        boolean leapYear;
        boolean isValidDate = true;
        leapYear = checkLeap(year);
        if (dayOfMonth > 31) {
            isValidDate = false;
        } else if (month > 12) {
            isValidDate = false;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (dayOfMonth > 30) {
                isValidDate = false;
            }
        } else if (month == 2) {
            if (leapYear && dayOfMonth > 29) {
                isValidDate = false;
            } else if (!leapYear && dayOfMonth > 28) {
                isValidDate = false;
            }
        }
        return isValidDate;
    }

    /**
     * @param year - Selected Year
     * @return - Boolean
     * Method used to validate ---> Is given year is Leap or not?
     */
    private static boolean checkLeap(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }


    /**
     * This method is used to convert date for notification time
     */
    public static String convertTimeToMills(String notificationTimeDate) {
        Date date1 = null;
        try {
            date1 = common_date_format.parse(notificationTimeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTime(date1);
        return String.valueOf(alarmTime.getTimeInMillis());
    }

    /**
     * This method is used to convert date long into string formate
     */
    public static String getDateFromMillsWithTimeZone(long millisecond) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millisecond);
            common_date_format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return common_date_format.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to convert date long into string formate
     */
    public static String getDateFromMillsWithOutTimeZone(long millisecond) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millisecond);
            return common_date_format.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}