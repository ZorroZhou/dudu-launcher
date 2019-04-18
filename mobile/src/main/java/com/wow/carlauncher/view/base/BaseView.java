package com.wow.carlauncher.view.base;

import android.content.Context;
import android.os.Bundle;
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
        startInit(null);
    }

    public BaseView(@NonNull Context context, Bundle savedInstanceState) {
        super(context);
        startInit(savedInstanceState);
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        startInit(null);
    }

    private void startInit(Bundle savedInstanceState) {
        View amapView = View.inflate(getContext(), getContent(), null);
        this.addView(amapView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        x.view().inject(this);
        initView(savedInstanceState);
    }

    protected abstract int getContent();

    protected void initView(Bundle savedInstanceState) {
    }

    public void onSaveInstanceState(Bundle outState) {
    }
}
