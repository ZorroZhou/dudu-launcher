package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseView;

public class LPage1View extends BaseView {
    public LPage1View(@NonNull Context context) {
        super(context);
        initView();
    }

    public LPage1View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        addContent(R.layout.content_l_page1);
    }

}
