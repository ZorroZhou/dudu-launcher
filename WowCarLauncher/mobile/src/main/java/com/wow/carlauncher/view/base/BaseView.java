package com.wow.carlauncher.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.x;

import java.lang.reflect.Method;

/**
 * Created by 10124 on 2018/4/22.
 */

public class BaseView extends FrameLayout {
    public BaseView(@NonNull Context context) {
        super(context);
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected void addContent(int r) {
        View amapView = View.inflate(getContext(), r, null);
        this.addView(amapView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        x.view().inject(this);
    }
}
