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
        icon();
    }

    public static void icon() {
        //QQ音乐
        putIcon("com.tencent.qqmusiccar", R.mipmap.app_icon_qqmusic);

        putIcon("com.nwd.android.phone", R.mipmap.app_icon_phone);
        putIcon("com.nwd.android.music.ui", R.mipmap.app_icon_music);
        putIcon("com.nwd.radio", R.mipmap.app_icon_radio);
        putIcon("com.nwd.bt.music", R.mipmap.app_icon_bt_music);

        putIcon("com.android.music", R.mipmap.app_icon_new_music);
        putIcon("com.android.settings", R.mipmap.app_icon_new_set);
    }

    public static void icon1() {
        putIcon("com.tencent.qqmusiccar", R.mipmap.app1_icon_qqmusic);
        putIcon("com.acloud.stub.localradio", R.mipmap.app1_icon_radio);
        putIcon("com.nwd.android.phone", R.mipmap.app1_icon_phone);
        putIcon("com.nwd.android.music.ui", R.mipmap.app1_icon_music);
        putIcon("com.nwd.radio", R.mipmap.app1_icon_radio);
        putIcon("com.nwd.bt.music", R.mipmap.app1_icon_bt_music);
        putIcon("com.android.music", R.mipmap.app1_icon_new_music);
        putIcon("com.android.settings", R.mipmap.app1_icon_new_set);
        putIcon("com.autonavi.amapauto", R.mipmap.app1_icon_new_nav);
        putIcon("com.fourtech.settings", R.mipmap.app1_icon_setting);
        putIcon("com.android.browser", R.mipmap.app1_icon_browser);
        putIcon("com.autochips.bluetooth", R.mipmap.app1_icon_bt_music);
        putIcon("com.acloud.stub.localmusic", R.mipmap.app1_icon_music);
        putIcon("com.android.settings", R.mipmap.app1_icon_setting);
        putIcon("cn.kuwo.kwmusiccar", R.mipmap.app1_icon_kuwo);
        putIcon("com.android.providers.telephony", R.mipmap.app1_icon_phone);
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
