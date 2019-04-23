package com.wow.carlauncher.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import org.xutils.x;

/**
 * Created by 10124 on 2018/4/22.
 */

public abstract class BaseView extends FrameLayout {
    public BaseView(@NonNull Context context) {
        super(context);
        startInit();
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        startInit();
    }

    private void startInit() {
        View view = View.inflate(getContext(), getContent(), null);
        this.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        x.view().inject(this, view);
        initView();
    }

    protected abstract int getContent();

    protected void initView() {
    }
}
