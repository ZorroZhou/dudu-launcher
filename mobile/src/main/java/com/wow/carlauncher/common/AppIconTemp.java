package com.wow.carlauncher.common;

import com.wow.carlauncher.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10124 on 2018/4/25.
 */

public class AppIconTemp {
    private static Map<String, Integer> appiconMark = new HashMap<>();

    static {
        //QQ音乐
        putIcon("com.tencent.qqmusiccar", R.mipmap.app_icon_qqmusic);

        putIcon("com.nwd.android.phone", R.mipmap.app_icon_phone);
        putIcon("com.nwd.android.music.ui", R.mipmap.app_icon_music);
        putIcon("com.nwd.radio", R.mipmap.app_icon_radio);
        putIcon("com.nwd.bt.music", R.mipmap.app_icon_bt_music);

        putIcon("com.android.music", R.mipmap.app_icon_new_music);
        putIcon("com.android.settings", R.mipmap.app_icon_new_set);
    }

    public static void putIcon(String pname, int r) {
        appiconMark.put(pname, r);
    }

    public static int getIcon(String app) {
        Integer id = appiconMark.get(app);
        if (id != null) {
            return id;
        }
        return 0;
    }
}
