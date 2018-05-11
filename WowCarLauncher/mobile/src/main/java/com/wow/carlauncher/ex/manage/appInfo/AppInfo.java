package com.wow.carlauncher.ex.manage.appInfo;

import android.graphics.drawable.Drawable;

/**
 * Created by 10124 on 2018/5/11.
 */

public class AppInfo {
    public final static String INTERNAL_APP_DRIVING = "com.wow.test";


    public final static int MARK_OTHER_APP = 1;
    public final static int MARK_INTERNAL_APP = 2;

    public AppInfo(Drawable icon, CharSequence name, String packageName, int appMark) {
        this.icon = icon;
        this.appMark = appMark;
        this.name = name;
        this.packageName = packageName;
    }

    public int appMark = MARK_OTHER_APP;//app的标记,标记是自己的APP还是其他的
    public String packageName;
    public Drawable icon;
    public CharSequence name;

    @Override
    public String toString() {
        return "AppInfo{" +
                "packageName='" + packageName + '\'' +
                ", icon=" + icon +
                ", name='" + name + '\'' +
                '}';
    }
}
