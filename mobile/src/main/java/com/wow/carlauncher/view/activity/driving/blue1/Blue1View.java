package com.wow.carlauncher.view.activity.driving.blue1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseEBusView;

/**
 * Created by 10124 on 2018/5/11.
 */

public class Blue1View extends BaseEBusView {
    public Blue1View(@NonNull Context context) {
        super(context);
    }

    public Blue1View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_blue1;
    }
}

