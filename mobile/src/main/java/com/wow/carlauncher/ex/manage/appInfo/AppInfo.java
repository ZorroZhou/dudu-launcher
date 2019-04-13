package com.wow.carlauncher.ex.manage.appInfo;

/**
 * Created by 10124 on 2018/5/11.
 */

public class AppInfo {
    public final static String INTERNAL_APP_DRIVING = "com.wow.carlauncher.driving";
    public final static String INTERNAL_APP_SETTING = "com.wow.carlauncher.setting";

    public final static int MARK_OTHER_APP = 1;
    public final static int MARK_INTERNAL_APP = 2;

    public AppInfo(CharSequence name, String clazz, int appMark) {
        this.appMark = appMark;
        this.name = name;
        this.clazz = clazz;
    }

    public int appMark = MARK_OTHER_APP;//app的标记,标记是自己的APP还是其他的
    public String clazz;
    public CharSequence name;
}
