package com.wow.carlauncher.plugin.controller;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.activity.LockActivity;
import com.wow.carlauncher.common.util.AppUtil;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.PluginManage;

/**
 * Created by 10124 on 2017/11/4.
 */

public class ControllerPlugin implements IPlugin, View.OnClickListener {
    public final static String TAG = "ControllerPlugin";
    private PluginManage pluginManage;
    private Context context;
    private LinearLayout launcherView;
    private AudioManager audioManager;
    private int oldV = 0;

    private TextView launcherWifi;

    public ControllerPlugin(Context context, PluginManage pluginManage) {
        this.pluginManage = pluginManage;
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);


        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, mFilter);
    }

    private void initLauncherView(LinearLayout launcherView) {
        launcherView.findViewById(R.id.btn_vu).setOnClickListener(this);
        launcherView.findViewById(R.id.btn_vd).setOnClickListener(this);
        launcherView.findViewById(R.id.btn_close_screen).setOnClickListener(this);
        launcherView.findViewById(R.id.btn_jy).setOnClickListener(this);
        launcherWifi = launcherView.findViewById(R.id.tv_wifi);
        Log.e(TAG, "initLauncherView: " + launcherWifi);
        refreshWifi();
    }

    @Override
    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = (LinearLayout) View.inflate(context, R.layout.plugin_controller_launcher, null);
            initLauncherView(launcherView);
        }
        return launcherView;
    }

    @Override
    public View getPopupView() {
        return null;
    }

    @Override
    public void destroy() {
        if (launcherView.getParent() != null) {
            ((ViewGroup) launcherView.getParent()).removeView(launcherView);
        }

        context.unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close_screen: {
                pluginManage.getCurrentActivity().startActivity(new Intent(pluginManage.getCurrentActivity(), LockActivity.class));
                break;
            }
            case R.id.btn_vu: {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                break;
            }
            case R.id.btn_vd: {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                break;
            }
            case R.id.btn_jy: {
                if (oldV == 0) {
                    oldV = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.STREAM_MUSIC);
                } else {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, oldV, AudioManager.STREAM_MUSIC);
                    oldV = 0;
                }
                break;
            }
        }
    }

    private void refreshWifi() {
        AppUtil.NetWorkState netWorkState = AppUtil.getNetWorkState(context);
        if (launcherWifi != null) {
            switch (netWorkState) {
                case NETWORKSTATE_NONE: {
                    launcherWifi.setText("无线连接：无网络");
                    break;
                }
                case NETWORKSTATE_MOBILE: {
                    launcherWifi.setText("无线连接：移动网络");
                    break;
                }
                case NETWORKSTATE_WIFI: {
                    launcherWifi.setText("无线连接：" + AppUtil.getConnectWifiSsid(context));
                    break;
                }
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                refreshWifi();
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        break;
                }
            }
        }
    };
}
