package com.wow.carlauncher.view.activity.driving.blue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.view.activity.driving.DrivingView;

import butterknife.BindView;

public class BlueView extends DrivingView {

    public BlueView(@NonNull Context context) {
        super(context);
    }

    public BlueView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setFront(boolean front) {

    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_blue;
    }

    @BindView(R.id.iv_center)
    ImageView iv_center;

    @BindView(R.id.iv_vss_cursor)
    ImageView iv_vss_cursor;

    @BindView(R.id.iv_fuel_cursor)
    ImageView iv_fuel_cursor;

    @BindView(R.id.iv_fuel_mask)
    ImageView iv_fuel_mask;

    @BindView(R.id.iv_rpm_mask)
    ImageView iv_rpm_mask;

    @BindView(R.id.iv_rpm_cursor)
    ImageView iv_rpm_cursor;

    private int max = 49;
    private int start = 1;

    @Override
    protected void initView() {
        super.initView();
        TaskExecutor.self().run(() -> {
            for (int i = start; i <= max; i++) {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int index = i;
                TaskExecutor.self().autoPost(() -> {
                    iv_center.setImageResource(getContext().getResources().getIdentifier("driving_blue_center_gif_" + index, "mipmap", getContext().getPackageName()));
                    if (index == max) {
                        iv_vss_cursor.setVisibility(VISIBLE);
                        iv_fuel_cursor.setVisibility(VISIBLE);
                        iv_fuel_mask.setVisibility(VISIBLE);
                        iv_rpm_mask.setVisibility(VISIBLE);
                        iv_rpm_cursor.setVisibility(VISIBLE);
                    }
                });
            }
        }, 1000);
    }
}
