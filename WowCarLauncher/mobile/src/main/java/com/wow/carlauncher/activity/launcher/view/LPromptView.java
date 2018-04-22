package com.wow.carlauncher.activity.launcher.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wow.carlauncher.R;

/**
 * Created by 10124 on 2018/4/22.
 */

public class LPromptView extends FrameLayout {
    public LPromptView(@NonNull Context context) {
        super(context);
        initView();
    }

    public LPromptView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        RelativeLayout amapView = (RelativeLayout) View.inflate(getContext(), R.layout.content_l_prompt, null);
        this.addView(amapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }
}
