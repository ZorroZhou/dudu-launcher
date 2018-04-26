package com.wow.carlauncher.activity;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.base.BaseActivity;

/**
 * Created by 10124 on 2018/4/25.
 */

public class DrivingActivity extends BaseActivity {
    @Override
    public void init() {
        setContent(R.layout.activity_driving);
    }

    @Override
    public void initView() {
        hideTitle();
    }
}
