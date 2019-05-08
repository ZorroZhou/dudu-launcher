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
    //尝试接收以下通知
    //com.bt.ACTION_BT_MUSIC_PLAY
    //com.bt.ACTION_BT_MUSIC_PAUSE
    // if ("com.bt.ACTION_AVRCP_MUSIC_ID3".equals(paramAnonymousContext))
    //          {
    //            paramAnonymousContext = paramAnonymousIntent.getStringExtra("extra_avrcp_id3_title");
    //            paramAnonymousIntent = paramAnonymousIntent.getStringExtra("extra_avrcp_id3_artist");
    //            BtMusicInterface.this.notifyBtMusicId3Info(paramAnonymousContext, paramAnonymousIntent);
    //            return;
    //          }
    //if ("com.nwd.action.ACTION_KEY_VALUE".equals(paramAnonymousContext)) {}



    //蓝牙状态
    //localIntentFilter.addAction("com.bt.ACTION_BT_HFP_ESTABLISHED");
    //    localIntentFilter.addAction("com.bt.ACTION_BT_END_CALL");
    //    localIntentFilter.addAction("com.bt.ACTION_BT_HFP_RELEASE");
    //    localIntentFilter.addAction("com.bt.ACTION_BT_BEGIN_CALL_ONLINE");
    //    localIntentFilter.addAction("com.bt.ACTION_BT_INCOMING_CALL");
    //    localIntentFilter.addAction("com.bt.ACTION_BT_OUTGOING_CALL");
    //    localIntentFilter.addAction("com.bt.ACTION_BT_INCOMING_NUMBER");
    //    localIntentFilter.addAction("com.bt.ACTION_BT_OUTGOING_NUMBER");
}
