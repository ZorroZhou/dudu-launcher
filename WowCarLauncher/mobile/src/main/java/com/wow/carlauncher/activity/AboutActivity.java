package com.wow.carlauncher.activity;

import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2017/10/26.
 */

public class AboutActivity extends BaseActivity {
    @ViewInject(R.id.about)
    private TextView about;

    @Override
    public void init() {
        setContent(R.layout.activity_about);
    }

    @Override
    public void initView() {
        setTitle("系统设置");
        about.setText("技术支持：QQ860913526.\nQQ交流群：109799213.\n如有问题请反馈至群或者qq邮箱");
    }
}
