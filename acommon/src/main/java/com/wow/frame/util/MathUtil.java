package com.wow.frame.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MathUtil {
    public static int float2int(float s) {
        return (int) (s + 0.5f);
    }

    /**
     * double精确到两位小数
     *
     * @param s
     * @return
     */
    public static double roundedToTwoDecimals(double s) {
        BigDecimal b = new BigDecimal(s);
        //保留2位小数
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * double转String 保留两位小数
     *
     * @param s
     * @return
     */
    public static String doubleToString(double s) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(roundedToTwoDecimals(s));
    }
}
