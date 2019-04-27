package com.wow.carlauncher.view.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.adapter.SelectAppAdapter;
import com.wow.carlauncher.view.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK1;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK2;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK3;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_APP_TO_DOCK4;

public class BluetoothMusicActivity extends BaseActivity {
    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContent(R.layout.activity_select_app);
    }

    private boolean run = false;

    @Event(value = {R.id.btn_prev, R.id.btn_next, R.id.btn_play})
    private boolean longClickEvent(View view) {
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
            case R.id.ll_dock2: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK2);
                break;
            }
            case R.id.ll_dock3: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK3);
                break;
            }
            case R.id.ll_dock4: {
                startActivityForResult(new Intent(this, AppSelectActivity.class), REQUEST_SELECT_APP_TO_DOCK4);
                break;
            }
        }
        return false;
    }
}
