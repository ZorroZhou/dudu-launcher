package com.wow.carlauncher.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.R;

/**
 * Created by 10124 on 2018/4/25.
 */

public class LPersionView extends LBaseView {

    public LPersionView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LPersionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        addContent(R.layout.content_l_persion);
    }
}
