package com.wow.carlauncher.ex.manage.appInfo;

import android.app.Activity;
import android.graphics.drawable.Drawable;

/**
 * Created by 10124 on 2018/5/11.
 */

public class InternalAppInfo extends AppInfo {
    public InternalAppInfo(Drawable icon, String name, String packageName, int appMark, Class<? extends Activity> LoadClazz) {
        super(icon, name, packageName, appMark);
        this.loadClazz = LoadClazz;
    }

    public Class<? extends Activity> loadClazz;
}
