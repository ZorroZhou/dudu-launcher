package com.wow.carlauncher.common;

import com.wow.carlauncher.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10124 on 2018/4/25.
 */

public class AppIconUtil {
    private static Map<String, Integer> appiconMark;

    static {
        appiconMark = new HashMap<>();
        //QQ音乐
        appiconMark.put("com.tencent.qqmusiccar", R.mipmap.app_icon_qqmusic);

        appiconMark.put("com.nwd.android.phone", R.mipmap.app_icon_phone);
        appiconMark.put("com.nwd.android.music.ui", R.mipmap.app_icon_music);
        appiconMark.put("com.nwd.radio", R.mipmap.app_icon_radio);
        appiconMark.put("com.nwd.bt.music", R.mipmap.app_icon_bt_music);

    }

    public static int getIcon(String app) {
        if (appiconMark.containsKey(app)) {
            return appiconMark.get(app);
        }
        return 0;
    }
}
