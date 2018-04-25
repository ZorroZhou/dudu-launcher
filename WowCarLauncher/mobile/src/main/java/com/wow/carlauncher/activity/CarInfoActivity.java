package com.wow.carlauncher.activity;

import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by 10124 on 2018/4/25.
 */

public class CarInfoActivity  extends BaseActivity {
    @ViewInject(R.id.about)
    private TextView about;

    @Override
    public void init() {
        setContent(R.layout.activity_about);
    }

}
