package com.wow.carlauncher.common.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SunRiseSetUtil {
    private static int[] days_of_month_1 = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static int[] days_of_month_2 = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private final static double h = -0.833;//日出日落时太阳的位置

    private final static double UTo = 180.0;//上次计算的日落日出时间，初始迭代值180.0

    //输入日期
    //输入经纬度
    //判断是否为闰年：若为闰年，返回1；若不是闰年,返回0
    private static boolean leap_year(int year) {
        return ((year % 400 == 0) || (year % 100 != 0) && (year % 4 == 0));
    }

    //求从格林威治时间公元2000年1月1日到计算日天数days
    private static int days(int year, int month, int date) {
        int i, a = 0;
        for (i = 2000; i < year; i++) {
            if (leap_year(i)) a = a + 366;
            else a = a + 365;
        }
        if (leap_year(year)) {
            for (i = 0; i < month - 1; i++) {
                a = a + days_of_month_2[i];
            }
        } else {
            for (i = 0; i < month - 1; i++) {
                a = a + days_of_month_1[i];
            }
        }
        a = a + date;
        return a;
    }

    //求格林威治时间公元2000年1月1日到计算日的世纪数t
    private static double t_century(int days, double UTo) {
        return ((double) days + UTo / 360) / 36525;
    }

    //求太阳的平黄径
    private static double L_sun(double t_century) {
        return (280.460 + 36000.770 * t_century);
    }

    //求太阳的平近点角
    private static double G_sun(double t_century) {
        return (357.528 + 35999.050 * t_century);
    }

    //求黄道经度
    private static double ecliptic_longitude(double L_sun, double G_sun) {
        return (L_sun + 1.915 * Math.sin(G_sun * Math.PI / 180) + 0.02 * Math.sin(2 * G_sun * Math.PI / 180));
    }

    //求地球倾角
    private static double earth_tilt(double t_century) {
        return (23.4393 - 0.0130 * t_century);
    }

    //求太阳偏差
    private static double sun_deviation(double earth_tilt, double ecliptic_longitude) {
        return (180 / Math.PI * Math.asin(Math.sin(Math.PI / 180 * earth_tilt) * Math.sin(Math.PI / 180 * ecliptic_longitude)));
    }

    //求格林威治时间的太阳时间角GHA
    private static double GHA(double UTo, double G_sun, double ecliptic_longitude) {
        return (UTo - 180 - 1.915 * Math.sin(G_sun * Math.PI / 180) - 0.02 * Math.sin(2 * G_sun * Math.PI / 180) + 2.466 * Math.sin(2 * ecliptic_longitude * Math.PI / 180) - 0.053 * Math.sin(4 * ecliptic_longitude * Math.PI / 180));
    }

    //求修正值e
    private static double e(double h, double glat, double sun_deviation) {
        return 180 / Math.PI * Math.acos((Math.sin(h * Math.PI / 180) - Math.sin(glat * Math.PI / 180) * Math.sin(sun_deviation * Math.PI / 180)) / (Math.cos(glat * Math.PI / 180) * Math.cos(sun_deviation * Math.PI / 180)));
    }

    //求日出时间
    private static double UT_rise(double UTo, double GHA, double glong, double e) {
        return (UTo - (GHA + glong + e));
    }

    //求日落时间
    private static double UT_set(double UTo, double GHA, double glong, double e) {
        return (UTo - (GHA + glong - e));
    }

    //判断并返回结果（日出）
    private static double result_rise(double UT, double UTo, double glong, double glat, int year, int month, int date) {
        double d;
        if (UT >= UTo) d = UT - UTo;
        else d = UTo - UT;
        if (d >= 0.1) {
            UTo = UT;
            UT = UT_rise(UTo, GHA(UTo, G_sun(t_century(days(year, month, date), UTo)), ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)), G_sun(t_century(days(year, month, date), UTo)))), glong, e(h, glat, sun_deviation(earth_tilt(t_century(days(year, month, date), UTo)), ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)), G_sun(t_century(days(year, month, date), UTo))))));
            result_rise(UT, UTo, glong, glat, year, month, date);
        }
        return UT;
    }

    //判断并返回结果（日落）
    private static double result_set(double UT, double UTo, double glong, double glat, int year, int month, int date) {
        double d;
        if (UT >= UTo) d = UT - UTo;
        else d = UTo - UT;
        if (d >= 0.1) {
            UTo = UT;
            UT = UT_set(UTo,
                    GHA(UTo, G_sun(t_century(days(year, month, date), UTo)),
                            ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)),
                                    G_sun(t_century(days(year, month, date), UTo)))),
                    glong,
                    e(h, glat, sun_deviation(earth_tilt(t_century(days(year, month, date), UTo)),
                            ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)),
                                    G_sun(t_century(days(year, month, date), UTo))))));
            result_set(UT, UTo, glong, glat, year, month, date);
        }
        return UT;
    }

    //求时区
    public static int Zone(double glong) {
        if (glong >= 0) return ((int) (glong / 15.0) + 1);
        else return ((int) (glong / 15.0) - 1);
    }

    public static String getSunrise(double longitude, double latitude, Date sunTime) {
        if (sunTime != null) {
            double sunrise;
            int year, month, date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateTime = sdf.format(sunTime);
            String[] rq = dateTime.split("-");
            String y = rq[0];
            String m = rq[1];
            String d = rq[2];
            year = Integer.parseInt(y);
            if (m != null && !m.equals("") && !m.contains("0")) {
                m = m.replaceAll("0", "");
            }
            month = Integer.parseInt(m);
            date = Integer.parseInt(d);
            sunrise = result_rise(UT_rise(UTo,
                    GHA(UTo, G_sun(t_century(days(year, month, date), UTo)),
                            ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)),
                                    G_sun(t_century(days(year, month, date), UTo)))),
                    longitude,
                    e(h, latitude, sun_deviation(earth_tilt(t_century(days(year, month, date), UTo)),
                            ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)),
                                    G_sun(t_century(days(year, month, date), UTo)))))), UTo, longitude, latitude, year, month, date);
            return (int) (sunrise / 15 + 8) + ":" + (int) (60 * (sunrise / 15 + 8 - (int) (sunrise / 15 + 8)));
        }
        return null;
    }

    public static String getSunset(double longitude, double latitude, Date sunTime) {
        if (sunTime != null) {
            double sunset;
            int year, month, date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateTime = sdf.format(sunTime);
            String[] rq = dateTime.split("-");
            String y = rq[0];
            String m = rq[1];
            String d = rq[2];
            year = Integer.parseInt(y);
            if (m != null && !m.equals("") && !m.contains("0")) {
                m = m.replaceAll("0", "");
            }
            month = Integer.parseInt(m);
            date = Integer.parseInt(d);
            sunset = result_set(UT_set(UTo,
                    GHA(UTo, G_sun(t_century(days(year, month, date), UTo)),
                            ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)),
                                    G_sun(t_century(days(year, month, date), UTo)))),
                    longitude,
                    e(h, latitude, sun_deviation(earth_tilt(t_century(days(year, month, date), UTo)),
                            ecliptic_longitude(L_sun(t_century(days(year, month, date), UTo)),
                                    G_sun(t_century(days(year, month, date), UTo)))))), UTo, longitude, latitude, year, month, date);
            return (int) (sunset / 15 + 8) + ":" + (int) (60 * (sunset / 15 + 8 - (int) (sunset / 15 + 8)));
        }
        return null;
    }

    public static boolean isNight(double longitude, double latitude, Date time) {
        try {
            String[] zao = getSunrise(longitude, latitude, time).split(":");
            String[] wan = getSunset(longitude, latitude, time).split(":");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String[] dateTime = sdf.format(time).split(":");

            int ch = Integer.parseInt(dateTime[0]);
            int cm = Integer.parseInt(dateTime[1]);

            int zh = Integer.parseInt(zao[0]);
            int zm = Integer.parseInt(zao[0]);

            int wh = Integer.parseInt(wan[0]);
            int wm = Integer.parseInt(wan[0]);

            boolean zaoshang = false;
            if (ch > zh || (ch == zh && cm > zm)) {
                zaoshang = true;
            }
            boolean wanshang = false;

            if (ch < wh || (ch == zh && cm < wm)) {
                wanshang = true;
            }
            return !(zaoshang && wanshang);
        } catch (Exception ignored) {
        }
        return false;
    }
}
