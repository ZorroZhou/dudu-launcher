package com.wow.carlauncher.common.util;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Che on 2016/11/23 0023.
 */
public class DateUtil {
    public static final long DAY_MILLIS = 86400000;

    /**
     * 获取当前年月日时分秒
     *
     * @return
     */
    public static String getNowTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(now);
        return date;
    }

    public final static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public final static Date stringToDate(String dateString, String format) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public final static String dateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 根据格式将字符串Date转化为标准格式的字符串
     *
     * @param dateString
     * @return
     */
    public final static String strDateTostrDate(String dateString, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date dateValue = null;
        try {
            dateValue = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(dateValue);
    }

    public static int getAge(String birth) {
        int age = 0;
        Date birthDate = stringToDate(birth);
        Date now = new Date();
        SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
        SimpleDateFormat format_M = new SimpleDateFormat("MM");
        String birth_year = format_y.format(birthDate);
        String this_year = format_y.format(now);
        String birth_month = format_M.format(birthDate);
        String this_month = format_M.format(now);
        age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
        if (this_month.compareTo(birth_month) < 0) age -= 1;
        if (age < 0) age = 0;
        return age;
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String date1 = sdf.format(new Date());
        return Integer.parseInt(date1);
    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String convertVoiceLength(String time) {
        int second = (int) (Float.parseFloat(time) / 1000);
        int minute = 0;
        int hour = 0;
        if (second >= 60) {
            second = second % 60;
            minute = second / 60;
            if (minute >= 60) {
                minute = minute % 60;
                hour = minute / 60;
            }
        }
        String res = "";
        if (hour < 10) {
            res = "0" + hour + ":";
        } else {
            res = hour + ":";
        }
        if (minute < 10) {
            res = res + "0" + minute + ":";
        } else {
            res = res + minute + ":";
        }
        if (second < 10) {
            res = res + "0" + second;
        } else {
            res = res + second;
        }
        return res;
    }


}
