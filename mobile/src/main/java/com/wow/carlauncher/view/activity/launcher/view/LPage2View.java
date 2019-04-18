package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.base.BaseView;

public class LPage2View extends BaseView {
    public LPage2View(@NonNull Context context) {
        super(context);
    }

    public LPage2View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_page2;
    }
}
