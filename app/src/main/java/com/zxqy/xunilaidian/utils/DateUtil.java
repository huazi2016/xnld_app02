package com.zxqy.xunilaidian.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    //获取时间戳中还有多少天
    public static long getDayValue(long value) {
        return value / (24 * 60 * 60 * 1000);
    }

    // 获取时间戳中还剩多少秒
    public static long getSecondValue(long value) {
        return value / 1000 - getDayValue(value) * 24 * 60 * 60 - getHourValue(value) * 60 * 60 - getMinValue(value) * 60;
    }

    //获取时间戳中的小时数
    public static long getHourValue(long value) {
        return value / (60 * 60 * 1000) - getDayValue(value) * 24;
    }

    //获取时间戳中 还有多少分钟
    public static long getMinValue(long value) {
//        return value / (60 * 1000) - getDayValue(value) * 24 * 60 - getHourValue(value) * 60;
        return value / (1000 * 60);
    }

    //获取当天凌晨时间
    public static long getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long time = calendar.getTime().getTime();
        return time;
    }

    public static long getHourTime(int time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long hour = calendar.getTimeInMillis();
        return hour;
    }

    public static long getHourTimeTest(int time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long hour = calendar.getTimeInMillis();
        Log.d("测试时间", DateUtil.getDate8(hour, DateUtil.PATTERN_YMDHMS));
        return hour;
    }


    public static long getTodayEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    public static String miunteForHour(int minute) {
        if (minute > 60) {
            return minute / 60 + "小时" + minute % 60 + "分钟";
        } else {
            return minute + "分钟";
        }

    }


    public static boolean sameday(Date date1, Date date2) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String time1 = dateFormat1.format(date1);
        String time2 = dateFormat2.format(date2);
        if (time1.equals(time2)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean sameMonth(Date date1, Date date2) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM");
        dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");
        dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String time1 = dateFormat1.format(date1);
        String time2 = dateFormat2.format(date2);
        if (time1.equals(time2)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDate_Minute(long time) {
        SimpleDateFormat format;
        format = new SimpleDateFormat(PATTERN_MINUTE, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String currentDateTimeString = format.format(time);
        String date_sub = currentDateTimeString.substring(0, currentDateTimeString.length() - 2);
        if (Integer.valueOf(date_sub) >= 60) {
            format = new SimpleDateFormat(PATTERN_HMS_CH, Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            return format.format(time);
        }
        return currentDateTimeString;
    }

    public static String getDate(long time, String pattern) {
        SimpleDateFormat format;
        format = new SimpleDateFormat(pattern, Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String currentDateTimeString = format.format(time);
        return currentDateTimeString;
    }

    public static String getDate8(long time, String pattern) {
        SimpleDateFormat format;
        format = new SimpleDateFormat(pattern, Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String currentDateTimeString = format.format(time);
        return currentDateTimeString;
    }

    public static Date yesterday(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();//这是昨天

//        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd " );
//
//        String str = sdf.format(yesterday);
        return yesterday;
    }

    public static Date diffDate(Date date, int diff) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -diff);
        Date yesterday = c.getTime();//这是昨天

//        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd " );
//
//        String str = sdf.format(yesterday);
        return yesterday;
    }

    public static Date beforeMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return m;
    }

    public static Date lastMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        Date m = c.getTime();
        return m;
    }

    public static Date stringToDate(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");//日期格式
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static Date tomorrow(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = c.getTime();//这是明天
        return tomorrow;
    }


    /**
     * 日期格式
     **/
    public static final String PATTERN_DISPMDY = "yy'年'MMMd'日'";

    public static final String PATTERN_DISPMDYHM = "yy'年'MMMd'日' HH'时'mm'分'";

    public static final String PATTERN_MDYHMS = "MM/dd/yyyy HH:mm:ss";

    public static final String PATTERN_FYMDHMS = "yyyy/MM/dd";

    public static final String PATTERN_YMDHMS = "yyyy/MM/dd HH:mm:ss";

    public static final String PATTERN_HHMMSS = "HH:mm:ss";

    public static final String PATTERN_YMDHM = "yyyy/MM/dd HH:mm";

    public static final String PATTERN_HMS = "H:m:s";

    public static final String PATTERN_HM = "HH:mm";
    public static final String PATTERN_MNUMBER = "m";
    public static final String PATTERN_MDY = "MM/dd/yyyy";

    public static final String PATTERN_MD = "M.dd";
    public static final String PATTERN_MD_DAY = "MM月dd日";
    public static final String PATTERN_YMD = "yyyy-MM-dd";
    public static final String PATTERN_YM = "yyyy-MM";

    public static final String PATTERN_HMS_CH = "H小时m分钟";
    public static final String PATTERN_MINUTE = "m分钟";
    public static final String PATTERN_M = "ss";
    public static final String PATTERN_YEAR = "yyyy年MM月dd日";
    public static final String PATTERN_STUDY = "MM-dd HH:mm";
    // 时分秒
    private final static SimpleDateFormat dateFormater_hms = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    // 没日期
    private final static SimpleDateFormat dateFormater_nohms = new SimpleDateFormat(
            "yyyy-MM-dd");
    // 年月
    private final static SimpleDateFormat dateFormater_YM = new SimpleDateFormat(
            "yyyy-MM");
    // 美式格式
    private final static SimpleDateFormat dateFormater_us = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss z yyyy", Locale.US);

    /**
     * Date格式为字符串
     *
     * @param date
     * @param pattern 转化的格式如 "yyyy-MM-dd HH:mm:ss" 具体DateUtil见定义的格式常量
     * @return
     */
    public static String getString(Date date, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("data", e.toString());
            return "";
        }
    }

    public static Date stringToDate(String dateString, String pattern) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public static String getString(long time, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
            Date date = new Date();
            date.setTime(time * 1000L);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("data", e.toString());
            return "";
        }
    }

    public static String getString(long time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(PATTERN_YMDHM, Locale.CHINA);
            Date date = new Date();
            date.setTime(time);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("data", e.toString());
            return "";
        }
    }


    public static String getStringToLong(long time,String PATTERN) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(PATTERN_YMDHM, Locale.CHINA);
            Date date = new Date();
            date.setTime(time * 1000L);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("data", e.toString());
            return "";
        }
    }

    public static String get_Am_Pm(long time) {
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        //apm=0 表示上午，apm=1表示下午来。
        int apm = mCalendar.get(Calendar.AM_PM);
        if (apm == 0) {
            if (minute < 10) {
                return "上午" + hour + ":" + "0" + minute;
            } else {
                return "上午" + hour + ":" + minute;
            }

        } else {
            if (minute < 10) {
                return "下午 " + hour + ":" + "0" + minute;
            } else {
                return "下午" + hour + ":" + minute;
            }
        }
    }

    public static String get_day_apm(long time) {
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        //apm=0 表示上午，apm=1表示下午来。
        int apm = mCalendar.get(Calendar.AM_PM);
        String str = "0";
        if (apm == 0) {
            if (minute < 10) {
                str = "上午" + hour + ":" + "0" + minute;
            } else {
                str = "上午" + hour + ":" + minute;
            }

        } else {
            if (minute < 10) {
                str = "下午 " + hour + ":" + "0" + minute;
            } else {
                str = "下午" + hour + ":" + minute;
            }
        }
        String day = getDate(time, PATTERN_MD_DAY);
        return day + " " + str;
    }

    public static String getString(Date date) {
        return getString(date, PATTERN_YMDHMS);
    }

    public static String getYMString(Date date) {
        return getString(date, PATTERN_YM);
    }

    public static String getDateString(Date date) {
        return getString(date, PATTERN_YMD);
    }

    public static Date toDate(String sdate) {
        return toDate(sdate, PATTERN_YMD);
    }

    public static Date toDate(String sdate, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
            return format.parse(sdate);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static long getToDayTimeForHourMin(String sourse) {
        Date date = toDate(DateUtil.getString(new Date(), "yyyy-MM-dd") + " " + sourse, "yyyy-MM-dd HH:mm");
        return date.getTime();
    }


    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;


    /**
     * @return yyyy-mm-dd 2012-12-25
     */
    public static String getDate() {
        return getYear() + "-" + getMonth() + "-" + getDay();
    }

    /**
     * 与当前时间相加获得新的时间
     */
    public static String getTimeForCurrentTime(int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int current_min = calendar.get(Calendar.MINUTE);
        int new_min = current_min + min;
        int new_hour = 0;
        if (new_min > 60) {
            new_min = new_min - 60;
            new_hour++;
        }
        new_hour = current_hour + hour + new_hour;
        if (new_hour < 12) {
            return "上午" + new_hour + ":" + new_min;
        } else if (new_hour == 12) {
            return "中午" + new_hour + ":" + new_min;
        } else if (new_hour < 18) {
            return "下午" + new_hour + ":" + new_min;
        } else {
            return "晚上" + new_hour + ":" + new_min;
        }
    }

    /**
     * 距离今天多久
     *
     * @param date
     * @return
     */
    public static String getCreateAt(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_MINUTE)
            return "刚刚";
        else if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时前";
        else if (ago <= ONE_DAY * 2)
            return "昨天";
        else if (ago <= ONE_DAY * 3)
            return "前天";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "个月前";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0
            // so month+1
            return year + "年前";
        }

    }

    /**
     * 距离截止日期还有多长时间
     *
     * @param date
     * @return
     */
    public static String fromDeadline(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long interval = deadline - now;
        if (interval <= ONE_HOUR)
            return "只剩下" + interval / ONE_MINUTE + "分钟";
        else if (interval <= ONE_DAY)
            return "只剩下" + interval / ONE_HOUR + "小时"
                    + (interval % ONE_HOUR / ONE_MINUTE) + "分钟";
        else {
            long day = interval / ONE_DAY;
            long hour = interval % ONE_DAY / ONE_HOUR;
            long minute = interval % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }

    }


    /**
     * 获取当前日期
     *
     * @param @return
     * @return String
     */
    public static String getCurrentTime() {
        Date now = new Date();
        return dateFormater_hms.format(now);
    }

    /**
     * 获取当前日期
     *
     * @param @return
     * @return String
     */
    public static String getCurrentDay() {
        Date now = new Date();
        return dateFormater_nohms.format(now);
    }


    /**
     * 获取当前年月
     *
     * @param @return
     * @return String
     */
    public static String getCurrentDateYM() {
        Date now = new Date();
        return dateFormater_YM.format(now);
    }

    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
                    % ONE_HOUR / ONE_MINUTE + "分";
        else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
                    + "分";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "天前" + hour + "点" + minute + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "个月" + day + "天" + hour + "点" + minute + "分前";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "年前" + month + "月" + day + "天";
        }
    }

    public static boolean isOneDay(long lastTime) {
        boolean is = false;
        long now = (new Date().getTime()) / 1000;
        long ago = now - lastTime;
        if (ago >= ONE_DAY) {
            is = true;
        }

        return is;
    }

    public static String getYear() {
        return Calendar.getInstance().get(Calendar.YEAR) + "";
    }

    public static String getMonth() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return month + "";
    }

    public static String getDay() {
        return Calendar.getInstance().get(Calendar.DATE) + "";
    }

    /**
     * 获取几天前的日期
     *
     * @param day 几天前
     * @return yyyy-mm-dd
     */
    public static String getSimpleDateToDayTime(int day) {
        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -day);
        return sj.format(calendar.getTime());
    }

    public static String get24Hour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "";
    }

    public static String getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE) + "";
    }

    public static String getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND) + "";
    }

    /*获取星期几*/
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();


    /*
     * int型星期几转为字符串
     * */
    public static String getWeek(int week) {
        String wee = null;
        switch (week) {
            case 1:
                wee = "周日";
                break;
            case 2:
                wee = "周一";
                break;
            case 3:
                wee = "周二";
                break;
            case 4:
                wee = "周三";
                break;
            case 5:
                wee = "周四";
                break;
            case 6:
                wee = "周五";
                break;
            case 7:
                wee = "周六";
                break;
        }
        return wee;
    }


    /**
     * 将特定的日期转为指定的格式
     *
     * @param //           本来的日期格式
     * @param toDateFormat 要转的日期格式
     * @param sourDate     日期
     * @return
     */
    public static String getDateFormat(String fromDateFormat, String toDateFormat, String sourDate) {
        try {
            if (TextUtils.isEmpty(sourDate)) {
                return "";
            }
            // 分析从 ParsePosition 给定的索引处开始的文本。如果分析成功，则将 ParsePosition 的索引更新为所用最后一个字符后面的索引
            SimpleDateFormat nowFormat = new SimpleDateFormat(fromDateFormat, Locale.CHINA);
            SimpleDateFormat toFormat = new SimpleDateFormat(toDateFormat, Locale.CHINA);
            ParsePosition position = new ParsePosition(0);
            Date dateValue = nowFormat.parse(sourDate, position);
            return toFormat.format(dateValue);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static long getHour(long time) {
        return (time / (60 * 60 * 1000) - getDay(time) * 24);
    }

    public static long getMin(long time) {
        return ((time / (60 * 1000)) - getDay(time) * 24 * 60 - getHour(time) * 60);
    }

    public static long getDay(long time) {
        return time / (24 * 60 * 60 * 1000);
    }

    public static long getSecond(long time) {
        return (time / 1000 - getDay(time) * 24 * 60 * 60 - getHour(time) * 60 * 60 - getMin(time) * 60);
    }

    /**
     * 获取指定日期的时间戳
     * 秒级
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param min
     * @param secend
     * @return
     */
    public static long getTimeInMillis(int year, int month, int day, int hour, int min, int secend) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min, secend);
        return cal.getTimeInMillis();
    }

    public static String getNowData() {
        return getString(new Date(), "yyyy-MM-dd");
    }

    /**
     * 将毫秒数转化为时间
     */
    public static String getTimeMillisTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi)  / ss;
        // long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟" + second + "秒";
        } else if (hour > 0) {
            return hour + "小时" + minute + "分钟" + second + "秒";
        } else if (minute > 0) {
            return minute + "分钟" + second + "秒";
        } else if (second > 0) {
            return second + "秒";
        } else {
            return "0秒";
        }
    }


    /**
     * 将特定的日期转为指定的格式
     *
     * @param //           本来的日期格式
     * @param toDateFormat 要转的日期格式
     * @param sourDate     日期
     * @return
     */
    public static String getDateFormat(String toDateFormat, String sourDate) {
        try {
            if (TextUtils.isEmpty(sourDate)) {
                return "";
            }
            // 分析从 ParsePosition 给定的索引处开始的文本。如果分析成功，则将 ParsePosition 的索引更新为所用最后一个字符后面的索引
            SimpleDateFormat nowFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            SimpleDateFormat toFormat = new SimpleDateFormat(toDateFormat, Locale.CHINA);
            ParsePosition position = new ParsePosition(0);
            Date dateValue = nowFormat.parse(sourDate, position);
            return toFormat.format(dateValue);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取指定格式的date
     *
     * @param pattern
     * @return
     */
    public static Date getNowSimpleDate(String pattern) {
        String source = getString(new Date(), pattern);
        return toDate(source, pattern);
    }
}
