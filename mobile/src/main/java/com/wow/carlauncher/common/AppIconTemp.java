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
        //音乐类
        //QQ音乐
        putIcon("com.tencent.qqmusiccar", R.drawable.app_icon_qqmusic);
        //NWD音乐
        putIcon("com.nwd.android.music.ui", R.drawable.app_icon_music);
        //安卓原生音乐
        putIcon("com.android.music", R.drawable.app_icon_music);
        //一个奇怪的音乐播放器
        putIcon("com.acloud.stub.localmusic", R.drawable.app_icon_music);
        //NWD蓝牙音乐
        putIcon("com.nwd.bt.music", R.drawable.app_icon_bt_music);
        //autochips蓝牙音乐
        putIcon("com.autochips.bluetooth", R.drawable.app_icon_bt_music);
        //酷我
        putIcon("cn.kuwo.kwmusiccar", R.drawable.app_icon_kuwo);

        //电话应用
        //NWD的电话应用
        putIcon("com.nwd.android.phone", R.drawable.app_icon_phone);
        //原生电话应用
        putIcon("com.android.providers.telephony", R.drawable.app_icon_phone);

        //收音机
        //NWD收音机
        putIcon("com.nwd.radio", R.drawable.app_icon_radio);

        //高德导航
        putIcon("com.autonavi.amapauto", R.drawable.app_icon_amap);

        //安卓设置
        putIcon("com.android.settings", R.drawable.app_icon_set);
        //浏览器
        putIcon("com.android.browser", R.drawable.app_icon_browser);

        //ES浏览器
        putIcon("com.estrongs.android.pop", R.drawable.app_icon_esfile);

        putIcon("ecarx.camera.dvr", R.drawable.app_icon_dvr);

        putIcon("com.acloud.stub.localradio", R.drawable.app_icon_radio);
    }

    static void putIcon(String pname, int r) {
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
