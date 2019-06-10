package com.wow.carlauncher.view.activity.driving;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wow.carlauncher.view.base.BaseView;

public abstract class DrivingBaseView extends BaseView {
    public DrivingBaseView(@NonNull Context context) {
        super(context);
    }

    public DrivingBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private DrivingActivity drivingActivity;

    public DrivingActivity getActivity() {
        return drivingActivity;
    }

    public void setDrivingActivity(DrivingActivity drivingActivity) {
        this.drivingActivity = drivingActivity;
    }

    private boolean showing;

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public boolean showing() {
        return showing;
    }
}
