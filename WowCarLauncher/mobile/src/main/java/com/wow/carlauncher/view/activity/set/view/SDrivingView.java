package com.wow.carlauncher.view.activity.set.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseView;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SDrivingView extends BaseView {
    public SDrivingView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SDrivingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        addContent(R.layout.content_set_obd);
    }
}
