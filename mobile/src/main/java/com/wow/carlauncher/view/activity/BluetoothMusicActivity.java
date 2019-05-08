package com.wow.carlauncher.view.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseActivity;

import butterknife.OnLongClick;

public class BluetoothMusicActivity extends BaseActivity {
    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContent(R.layout.activity_bluetooth_music);
    }

    private boolean run = false;

    @OnLongClick(value = {R.id.btn_prev, R.id.btn_next, R.id.btn_play})
    public boolean longClickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_play: {
                if (!run) {
                    run = true;
                    sendBroadcast(new Intent("com.bt.ACTION_BT_MUSIC_PLAY"));
                } else {
                    run = false;
                    sendBroadcast(new Intent("com.bt.ACTION_BT_MUSIC_PAUSE"));
                }
                break;
            }
        }
        return false;
    }
}
