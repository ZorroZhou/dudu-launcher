package com.wow.carlauncher.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseActivity;

import org.xutils.view.annotation.ViewInject;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.open_lanncher)
    private Button open_lanncher;

    @ViewInject(R.id.set)
    private Button set;

    @ViewInject(R.id.sys_set)
    private Button sys_set;

    @ViewInject(R.id.test)
    private Button test;

    @Override
    public void init() {
        setContent(R.layout.activity_main);
    }

    @Override
    public void initView() {
        setTitle("车载启动器");
        open_lanncher.setOnClickListener(this);
        set.setOnClickListener(this);
        sys_set.setOnClickListener(this);
        test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set: {
                startActivity(new Intent(this, SetActivity.class));
                break;
            }
            case R.id.sys_set: {
                startActivity(new Intent(this, SystemSetActivity.class));
                break;
            }
            case R.id.test: {
                startActivity(new Intent(this, Launcher2Activity.class));
                break;
            }
        }
    }
}
