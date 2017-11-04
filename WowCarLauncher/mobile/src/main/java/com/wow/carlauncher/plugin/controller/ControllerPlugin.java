package com.wow.carlauncher.plugin.controller;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.SystemBrightManager;
import com.wow.carlauncher.plugin.IPlugin;
import com.wow.carlauncher.plugin.PluginManage;

/**
 * Created by 10124 on 2017/11/4.
 */

public class ControllerPlugin implements IPlugin, View.OnClickListener {
    protected Context context;
    private LinearLayout launcherView;
    private AudioManager audioManager;
    private int oldV = 0;

    public ControllerPlugin(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private void initLauncherView(LinearLayout launcherView) {
        launcherView.findViewById(R.id.btn_vu).setOnClickListener(this);
        launcherView.findViewById(R.id.btn_vd).setOnClickListener(this);
        launcherView.findViewById(R.id.btn_lu).setOnClickListener(this);
        launcherView.findViewById(R.id.btn_ld).setOnClickListener(this);
        launcherView.findViewById(R.id.btn_jy).setOnClickListener(this);
    }

    @Override
    public View getLauncherView() {
        if (launcherView == null) {
            launcherView = (LinearLayout) View.inflate(context, R.layout.plugin_controller, null);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
}
