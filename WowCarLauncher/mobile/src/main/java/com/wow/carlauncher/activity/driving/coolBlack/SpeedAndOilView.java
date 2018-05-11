package com.wow.carlauncher.activity.driving.coolBlack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by 10124 on 2018/4/26.
 */

public class SpeedAndOilView extends RelativeLayout {
    private final static int RATE = 100;
    private final static int MAX_SPEED = 200 * RATE;

    private final static int MAX_OIL = 100;
    @ViewInject(R.id.iv_cursor)
    private ImageView iv_cursor;

    @ViewInject(R.id.iv_oil)
    private ImageView iv_oil;

    @ViewInject(R.id.tv_speed)
    private TextView tv_speed;

    @ViewInject(R.id.tv_oil)
    private TextView tv_oil;

    private boolean show = false;

    private int currentValue = 0;
    private int tagerValue = 0;
    private int revChangeValue = 1;//转速变化的区间

    public SpeedAndOilView(Context context) {
        super(context);
        initView();
    }

    public SpeedAndOilView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        View amapView = View.inflate(getContext(), R.layout.content_speed_and_oil, null);
        this.addView(amapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        x.view().inject(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        show = true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        iv_cursor.setPivotX(getMeasuredWidth() / 2);
        iv_cursor.setPivotY(getMeasuredHeight() / 2);//支点在图片中心

        iv_oil.setPivotX(getMeasuredWidth() / 2);
        iv_oil.setPivotY(getMeasuredHeight() / 2);//支点在图片中心
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        show = false;
    }

    public void setOil(int oil) {
        if (show) {
            if (oil > MAX_OIL) {
                oil = MAX_OIL;
            } else if (oil < 0) {
                oil = 0;
            }
            iv_oil.setRotation(-(float) (oil * 90) / (float) MAX_OIL);

            tv_oil.setText("剩余油量:" + oil + "%");
        }
    }

    public void setSpeed(int speed) {
        if (show) {
            tv_speed.setText(speed + "");

            speed = speed * RATE;
            if (speed > MAX_SPEED) {
                speed = MAX_SPEED;
            } else if (speed < 0) {
                speed = 0;
            }
            tagerValue = speed;
            revChangeValue = Math.abs(tagerValue - currentValue) / 100;
            if (revChangeValue < 1) {
                revChangeValue = 1;
            }
            postValue();
        }
    }

    private void postValue() {
        if (revChangeValue + currentValue < tagerValue) {
            currentValue = currentValue + revChangeValue;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_cursor.setRotation((float) (currentValue * 270) / (float) MAX_SPEED);
                    postValue();
                }
            }, 1);
        } else if (revChangeValue + currentValue > tagerValue) {
            currentValue = currentValue - revChangeValue;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_cursor.setRotation((float) (currentValue * 270) / (float) MAX_SPEED);
                    postValue();
                }
            }, 1);
        }
    }
}
