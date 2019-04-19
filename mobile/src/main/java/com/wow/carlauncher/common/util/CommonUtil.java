package com.wow.carlauncher.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static boolean isNull(Object object) {
        if (null == object) {
            return true;
        }
        if (object instanceof String) {
            return object.toString().trim().length() == 0 || object.toString().trim().equals("") || object.toString().trim() == "null";
        }
        if (object instanceof CharSequence) {
            return object.toString().trim().length() == 0 || object.toString().trim().equals("") || object.toString().trim() == "null";
        }
        if (object instanceof Iterable) {
            return !((Iterable<?>) object).iterator().hasNext();
        }
        if (object.getClass().isArray()) {
            if (Array.getLength(object) == 0) {
                return true;
            } else {
                for (int i = 0; i < Array.getLength(object); i++) {
                    if (!isNull(Array.get(object, 1))) {
                        return false;
                    }
                }
                return true;
            }
        }
        if (object instanceof Number) {
            return ((Number) object).doubleValue() == 0;
        }
        if (object instanceof Boolean) {
            return !((Boolean) object).booleanValue();
        }
        if (object instanceof Character) {
            return object == "\0";
        }
        return false;
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    public static Map<String, String> url2Map(String url) {
        url = url.substring(url.lastIndexOf("?") + 1);
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            map.put(param.split("=")[0], param.split("=")[1]);
        }
        return map;
    }

    // 获取随机�?
    public static String getRand(int num) {
        String sRand = "";
        Random random = new Random();
        // 取随机产生的认证�?(4位数�?)
        for (int i = 0; i < num; i++) {
            String rand = "";
            // 随机生成数字或�?�字�?
            if (random.nextInt(10) > 5) {
                rand = String.valueOf((char) (random.nextInt(10) + 48));
            } else {
                rand = String.valueOf((char) (random.nextInt(26) + 65));
            }
            sRand += rand;
        }
        return sRand;
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

    public final static int getVerCode(Context context, String packageName) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return verCode;
    }

    public final static String convertFristToUpperCase(String temp) {
        String frist = temp.substring(0, 1);
        String other = temp.substring(1);
        return frist.toUpperCase(Locale.getDefault()) + other;
    }


    public static String toUTF8(String str) {
        String u = str;
        try {
            u = java.net.URLEncoder.encode(u, "UTF-8");
        } catch (Exception e) {

        }
        return u;
    }

    // 根据随机数来获取不同的布�?
    public static int random(int s) {
        Random r = new Random();
        int i = r.nextInt(s);
        return i;
    }

    // 判断邮箱的函�?
    public static boolean checkEmail(String email) {
        if (email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
            return true;
        } else {
            return false;
        }
    }

    // 判断手机号码
    public static boolean checkphone(String phonenumber) {
        String phone = "\\d{11}";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    public static String obj2Str(Object obj) {
        if (obj != null) {
            return String.valueOf(obj);
        } else {
            return null;
        }
    }

    public static boolean comparisonInt(Integer x1, Integer x2) {
        if (x1 == null && x2 == null) {
            return true;
        }
        if (x1 == null || x2 == null) {
            return false;
        }
        return x1.intValue() == x2.intValue();
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，4,7，其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(email)) return false;
        else return email.matches(telRegex);
    }


    /**
     * 弹出软键盘
     *
     * @param ct
     * @param seconds 延迟时间
     */
    public static void toggleSoftInputInDialog(final Context ct, int seconds) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) ct.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, seconds);
    }

    /**
     * 检验选择日期是都有效
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean checkDate(int year, int month, int day) {
        boolean rYear = false;
        List m2 = new ArrayList();
        m2.add(4);
        m2.add(6);
        m2.add(9);
        m2.add(11);
        if (year % 4 == 0) {
            rYear = true;
        }
        if (month == 2) {
            if (rYear) {
                if (day > 29) {
                    return false;
                }
            } else {
                if (day > 28) {
                    return false;
                }
            }
        } else if (m2.contains(month)) {
            if (day > 30) {
                return false;
            }
        }
        return true;
    }
}
