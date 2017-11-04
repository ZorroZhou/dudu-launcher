package com.wow.carlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.plugin.PluginManage;
import com.wow.carlauncher.plugin.music.controllers.NeteaseCloudMusicPlugin;
import com.wow.carlauncher.plugin.music.controllers.NeteaseCloudMusicPlugin2;
import com.wow.carlauncher.popupWindow.PopupWin;

import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

import static com.wow.carlauncher.common.CommonData.IDATA_PACKAGE_NAME;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK3;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK4;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK5;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK1_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK2_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK3_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK4_CLASS;
import static com.wow.carlauncher.common.CommonData.SDATA_DOCK5_CLASS;

/**
 * Created by 10124 on 2017/10/26.
 */
public class Lanncher2Activity extends BaseActivity {
    private static final String TAG = "LanncherActivity";

    private PackageManager pm;

    private NeteaseCloudMusicPlugin2 neteaseCloudMusicPlugin;

    @ViewInject(R.id.item_1)
    private LinearLayout item_1;

    @Override
    public void init() {
        setContent(R.layout.activity_lanncher2);
        pm = getPackageManager();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkPermission();

        neteaseCloudMusicPlugin = new NeteaseCloudMusicPlugin2(getApplication());
    }

    @Override
    public void initView() {
        hideTitle();
        item_1.addView(neteaseCloudMusicPlugin.getLauncherView(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        Button button2 = new Button(this);
//        button2.setText("Button2");
//        gl_main.addView(button2, getLayoutParams());
//
//        Button button3 = new Button(this);
//        button3.setText("Button3");
//        gl_main.addView(button3, getLayoutParams());
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            new AlertDialog.Builder(mContext).setTitle("系统提示")
                    .setMessage("APP需要弹出窗口权限！取消后可在APP设置调整！")
                    .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(intent);
                        }
                    }).setNegativeButton("不在提示", null).show();
        }
    }
}
