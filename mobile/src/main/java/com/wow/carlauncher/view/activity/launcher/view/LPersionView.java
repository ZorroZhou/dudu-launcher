package com.wow.carlauncher.view.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;
import com.wow.carlauncher.view.activity.launcher.BaseThemeView;

/**
 * Created by 10124 on 2018/4/20.
 */

public class LPersionView extends BaseThemeView {

    public LPersionView(@NonNull Context context) {
        super(context);
    }

    public LPersionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_l_person;
    }

}
