package com.wow.carlauncher.view.activity;

import android.view.WindowManager;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.ex.manage.baiduVoice.BaiduVoiceAssistant;
import com.wow.carlauncher.ex.manage.baiduVoice.event.MVaNewWordFind;
import com.wow.carlauncher.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

public class VoiceAssistantActivity extends BaseActivity {
    @Override
    public void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContent(R.layout.activity_voice_assistant);
    }

    @ViewInject(R.id.tv_shuru)
    private TextView tv_shuru;

    @ViewInject(R.id.tv_check)
    private TextView tv_check;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MVaNewWordFind event) {
        if (event.isChecking()) {
            tv_check.setText(event.getWord());
        } else {
            tv_shuru.setText(event.getWord());
            tv_check.setText("请说话");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        BaiduVoiceAssistant.self().stopWakeUp();
        BaiduVoiceAssistant.self().startAsr();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        BaiduVoiceAssistant.self().stopAsr();
        BaiduVoiceAssistant.self().startWakeUp();
    }
}
