package com.wow.carlauncher.view.activity.driving;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.view.base.BaseView;

public abstract class DrivingView extends BaseView {
    public DrivingView(@NonNull Context context) {
        super(context);
    }

    public DrivingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void setFront(boolean front);
}
