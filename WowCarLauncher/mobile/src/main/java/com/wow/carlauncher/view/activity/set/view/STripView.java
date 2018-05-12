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

public class STripView extends BaseView {
    public STripView(@NonNull Context context) {
        super(context);
        initView();
    }

    public STripView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        addContent(R.layout.content_set_trip);
    }
}
